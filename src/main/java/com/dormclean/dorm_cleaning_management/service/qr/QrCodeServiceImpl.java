package com.dormclean.dorm_cleaning_management.service.qr;

import com.dormclean.dorm_cleaning_management.dto.zipFile.QrGenerationData;
import com.dormclean.dorm_cleaning_management.dto.qr.QrRequestDto;
import com.dormclean.dorm_cleaning_management.dto.qr.QrResponseDto;
import com.dormclean.dorm_cleaning_management.dto.zipFile.ZipFileEntry;
import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.entity.QrCode;
import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.entity.enums.RoomStatus;
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
                .orElseThrow(() -> new RuntimeException("해당 생활관의 정보를 찾을 수 없습니다."));
        Room room = roomRepository.findByDormAndRoomNumber(dorm, dto.roomNumber())
                .orElseThrow(() -> new RuntimeException("해당 호실의 정보를 찾을 수 없습니다."));

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

        // QR 이미지 생성
        return generateQrCode(content, 250, 250, labelText);
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
            throw new RuntimeException("QR 코드 생성 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public byte[] generateZipForDorms(List<String> dormCodes) {
        // 데이터 준비 및 qrCode 업데이트/저장
        List<QrGenerationData> qrDataList = qrDataProcessor.prepareQrDataAndSaveBulk(dormCodes);

        // 순서가 섞이지 않도록 리스트 처리는 주의해야 하나, ZIP 내 파일명이 명확하므로 병렬 처리 결과 수집이 더 중요
        List<ZipFileEntry> zipEntries = qrDataList.parallelStream()
                .map(data -> {
                    byte[] imageBytes = generateQrCode(
                            data.content(),
                            250,
                            250,
                            data.labelText());
                    return new ZipFileEntry(data.fileName(), imageBytes);
                })
                .toList();

        return createZipFromEntries(zipEntries);
    }

    private byte[] createZipFromEntries(List<ZipFileEntry> entries) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream zip = new ZipOutputStream(baos)) {

            for (ZipFileEntry entry : entries) {
                ZipEntry zipEntry = new ZipEntry(entry.fileName());
                zip.putNextEntry(zipEntry);
                zip.write(entry.imageBytes());
                zip.closeEntry();
            }
            zip.finish();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("ZIP 생성 실패", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public QrResponseDto getQrData(String token) {
        // 찾은 정보를 DTO로 변환해서 반환
        return qrCodeRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 QR입니다."));
    }
}
