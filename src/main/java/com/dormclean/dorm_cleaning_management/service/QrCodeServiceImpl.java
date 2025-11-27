package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.dto.QrRequestDto;
import com.dormclean.dorm_cleaning_management.dto.QrResponseDto;
import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.entity.QrCode;
import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;
import com.dormclean.dorm_cleaning_management.repository.QrCodeRepository;
import com.dormclean.dorm_cleaning_management.repository.RoomRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {
    @Value("${app.host}")
    private String host;

    private final QrCodeRepository qrCodeRepository;
    private final RoomRepository roomRepository;
    private final DormRepository dormRepository;

    @Override
    @Transactional
    public byte[] createSecureQr(QrRequestDto dto) {
        Dorm dorm = dormRepository.findByDormName(dto.dormName()).orElseThrow(() -> new RuntimeException("해당 기숙사의 정보를 찾을 수 없습니다."));
        Room room = roomRepository.findByDormAndRoomNumber(dorm, dto.roomNumber()).orElseThrow(() -> new RuntimeException("해당 호실의 정보를 찾을 수 없습니다."));

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

        // QR 이미지 생성
        return generateQrCode(content, 250, 250);
    }

    @Override
    public byte[] generateQrCode(String content, int width, int height){
        try{
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);

            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                MatrixToImageWriter.writeToStream(bitMatrix, "png", byteArrayOutputStream);
                return byteArrayOutputStream.toByteArray();
            }
        } catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("QR 코드 생성 중 오류 발생", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public QrResponseDto getQrData(String token) {
        // UUID(토큰)로 DB에서 찾기
        QrCode qrCode = qrCodeRepository.findByUuid(token)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않거나 만료된 QR 코드입니다."));

        String dormName = qrCode.getRoom().getDorm().getDormName();
        String roomNumber = qrCode.getRoom().getRoomNumber();

        // 찾은 정보를 DTO로 변환해서 반환
        return new QrResponseDto(dormName, roomNumber);
    }
}
