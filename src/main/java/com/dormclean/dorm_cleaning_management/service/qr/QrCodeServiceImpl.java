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
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
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
        String content = String.format("%s/check?token=%s", host, qrCode.getUuid());

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
            // QR Code BitMatrix 생성
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);

            // BitMatrix를 BufferedImage로 변환
            MatrixToImageConfig config = new MatrixToImageConfig(0xFF000000, 0xFFFFFFFF); // 흑백 설정
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, config);

            // 텍스트를 위한 추가 공간 설정 (하단에 50px 추가)
            int textHeight = 50;
            BufferedImage combinedImage = new BufferedImage(width, height + textHeight, BufferedImage.TYPE_INT_RGB);

            // 그래픽 객체 생성 (그리기 도구)
            Graphics2D g = combinedImage.createGraphics();

            // 텍스트 안티앨리어싱 설정 (LCD 화면에 최적화된 설정, 가장 선명하게 보임)
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            // 전체적인 렌더링 품질을 최우선으로 설정
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            // 일반적인 도형의 안티앨리어싱 설정 (글자 외의 요소도 부드럽게)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 전체 배경을 흰색으로 칠하기
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height + textHeight);

            // QR 이미지 그리기
            g.drawImage(qrImage, 0, 0, null);

            // 텍스트 설정 및 그리기
            g.setColor(Color.BLACK);
            g.setFont(new Font("Pretendard", Font.BOLD, 20)); // 폰트 설정 (시스템에 없는 경우 SansSerif 등으로 대체)

            // 텍스트를 중앙에 정렬하기 위한 좌표 계산
            FontMetrics fontMetrics = g.getFontMetrics();
            int textWidth = fontMetrics.stringWidth(labelText);
            int x = (width - textWidth) / 2; // 가로 중앙
            int y = height + (textHeight / 2) + (fontMetrics.getAscent() / 4); // 세로 중앙 (QR 아래)

            g.drawString(labelText, x, y);
            g.dispose(); // 그래픽 자원 해제

            // 이미지를 byte[]로 변환하여 반환
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

                // 네트워크 스트림 비우기 유도 (클라이언트가 끊기지 않게 함)
                outputStream.flush();
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
