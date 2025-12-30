package com.dormclean.dorm_cleaning_management.service.qr;

import com.dormclean.dorm_cleaning_management.dto.zipFile.QrGenerationData;
import com.dormclean.dorm_cleaning_management.dto.qr.QrRequestDto;
import com.dormclean.dorm_cleaning_management.dto.qr.QrResponseDto;
import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.entity.QrCode;
import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.exception.dorm.DormNotFoundException;
import com.dormclean.dorm_cleaning_management.exception.qr.InvalidQrException;
import com.dormclean.dorm_cleaning_management.exception.qr.QrCreationFailedException;
import com.dormclean.dorm_cleaning_management.exception.qr.ZipCreationFailedException;
import com.dormclean.dorm_cleaning_management.exception.room.RoomNotFoundException;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;
import com.dormclean.dorm_cleaning_management.repository.QrCodeRepository;
import com.dormclean.dorm_cleaning_management.repository.RoomRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {
    @Value("${app.host}")
    private String host;

    private final QrCodeRepository qrCodeRepository;
    private final RoomRepository roomRepository;
    private final DormRepository dormRepository;
    private final QrDataProcessor qrDataProcessor;

    private static final QRCodeWriter QR_WRITER = new QRCodeWriter();
    private static final MatrixToImageConfig QR_CONFIG = new MatrixToImageConfig(0xFF000000, 0xFFFFFFFF);
    private static final Map<RenderingHints.Key, Object> HINTS;
    private static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 20);

    static {
        HINTS = new HashMap<>();
        HINTS.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        HINTS.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        HINTS.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ImageIO.setUseCache(false);
    }

    @Override
    @Transactional
    public byte[] createSecureQr(QrRequestDto dto) {
        Dorm dorm = dormRepository.findByDormCode(dto.dormCode())
                .orElseThrow(DormNotFoundException::new);
        Room room = roomRepository.findByDormAndRoomNumber(dorm, dto.roomNumber())
                .orElseThrow(RoomNotFoundException::new);

        QrCode qrCode = qrCodeRepository.findByRoom(room).orElse(null);

        if (qrCode != null) {
            // 이미 존재하면 -> UUID만 새로고침
            qrCode.refreshUuid();
        } else {
            // 없으면 -> 새로 생성해서 저장
            qrCode = QrCode.builder()
                    .room(room)
                    .build();

            qrCodeRepository.save(qrCode);
        }

        // URL 생성
        String content;
        if (host.contains("8080")) {
            content = String.format("%s/check?token=%s", host, qrCode.getUuid());
        } else {
            content = String.format("%s/?token=%s", host, qrCode.getUuid());
        }

        String labelText = String.format("%s동 %s호", dto.dormCode(), dto.roomNumber());

        try {
            // QR 이미지 생성
            return generateQrCode(content, 250, 250, labelText);
        } catch (Exception e) {
            throw new QrCreationFailedException();
        }
    }

    @Override
    public byte[] generateQrCode(String content, int width, int height, String labelText) {
        try {
            BitMatrix bitMatrix = QR_WRITER.encode(content, BarcodeFormat.QR_CODE, width, height);

            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, QR_CONFIG);

            int textHeight = 50;

            BufferedImage combinedImage = new BufferedImage(width, height + textHeight, BufferedImage.TYPE_BYTE_BINARY);

            Graphics2D g = combinedImage.createGraphics();

            try {
                g.setRenderingHints(HINTS);

                g.setColor(Color.WHITE);
                g.fillRect(0, 0, width, height + textHeight);
                g.drawImage(qrImage, 0, 0, null);

                g.setColor(Color.BLACK);
                g.setFont(LABEL_FONT);

                FontMetrics fontMetrics = g.getFontMetrics();
                int textWidth = fontMetrics.stringWidth(labelText);
                int x = (width - textWidth) / 2;
                int y = height + (textHeight / 2) + (fontMetrics.getAscent() / 4);

                g.drawString(labelText, x, y);
            } finally {
                g.dispose();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(combinedImage, "png", baos);
            return baos.toByteArray();

        } catch (Exception e) {
            throw new QrCreationFailedException();
        }
    }

    @Override
    public void generateZipForDormsToStream(List<String> dormCodes, OutputStream outputStream) {
        List<QrGenerationData> qrDataList = qrDataProcessor.prepareQrDataAndSaveBulk(dormCodes);

        // ZipOutputStream 생성
        try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {
            zos.setLevel(5);
            for (QrGenerationData data : qrDataList) {
                // QR 이미지 생성 (하나씩만 메모리에 올림)
                byte[] imageBytes = generateQrCode(
                        data.content(),
                        250,
                        250,
                        data.labelText());

                // 즉시 ZIP에 추가
                ZipEntry zipEntry = new ZipEntry(data.fileName());
                zos.putNextEntry(zipEntry);
                zos.write(imageBytes);
                zos.closeEntry();
            }
            zos.finish(); // 압축 마무리
        } catch (IOException e) {
            throw new ZipCreationFailedException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public QrResponseDto getQrData(String token) {
        // 찾은 정보를 DTO로 변환해서 반환
        return qrCodeRepository.findByToken(token)
                .orElseThrow(InvalidQrException::new);
    }
}
