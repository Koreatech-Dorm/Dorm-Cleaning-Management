package com.dormclean.dorm_cleaning_management.service.qr;

import com.dormclean.dorm_cleaning_management.dto.zipFile.QrGenerationData;
import com.dormclean.dorm_cleaning_management.entity.QrCode;
import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.repository.QrCodeRepository;
import com.dormclean.dorm_cleaning_management.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component // 스프링 빈으로 등록
@RequiredArgsConstructor
public class QrDataProcessor {
    private final RoomRepository roomRepository;
    private final QrCodeRepository qrCodeRepository;

    @Value("${app.host}")
    private String host;

    // ToDo: List로 받은 생활관들의 모든 방에 대해 qrCode UUID 저장
    @Transactional
    public List<QrGenerationData> prepareQrDataAndSaveBulk(List<String> dormCodes) {
        List<Room> allRooms = roomRepository.findAllRoomsWithDormAndQrByDormCodes(dormCodes);

        List<QrGenerationData> resultData = new ArrayList<>();
        List<QrCode> qrCodesToSave = new ArrayList<>();

        for (Room room : allRooms) {
            QrCode qrCode = room.getQrCode();

            if (qrCode != null) {
                qrCode.refreshUuid();
            } else {
                qrCode = QrCode.builder().room(room).build();
            }
            qrCodesToSave.add(qrCode);

            resultData.add(new QrGenerationData(
                    String.format("%s/check?token=%s", host, qrCode.getUuid()),
                    String.format("%s동 %s호", room.getDorm().getDormCode(), room.getRoomNumber()),
                    String.format("%s/QR_%s.png", room.getDorm().getDormCode(), room.getRoomNumber())
            ));
        }
        qrCodeRepository.saveAll(qrCodesToSave);
        return resultData;
    }
}