package com.dormclean.dorm_cleaning_management.service.qr;

import com.dormclean.dorm_cleaning_management.dto.zipFile.QrGenerationData;
import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.entity.QrCode;
import com.dormclean.dorm_cleaning_management.entity.Room;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;
import com.dormclean.dorm_cleaning_management.repository.QrCodeRepository;
import com.dormclean.dorm_cleaning_management.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component // 스프링 빈으로 등록
@RequiredArgsConstructor
public class QrDataProcessor {

    private final DormRepository dormRepository;
    private final RoomRepository roomRepository;
    private final QrCodeRepository qrCodeRepository;

    @Value("${app.host}") // application.yml의 설정값
    private String host;

    /**
     * 이 메소드는 '별도 클래스'에 있으므로, 호출 시 트랜잭션이 확실하게 적용됩니다.
     * 메소드가 끝날 때 변경된 QR 정보가 DB에 커밋(저장)됩니다.
     */
    @Transactional
    public List<QrGenerationData> prepareQrDataAndSaveBulk(List<String> dormCodes) {
        List<QrGenerationData> resultData = new ArrayList<>();
        List<QrCode> qrCodesToSave = new ArrayList<>();

        for (String dormCode : dormCodes) {
            Dorm dorm = dormRepository.findByDormCode(dormCode)
                    .orElseThrow(() -> new IllegalArgumentException("생활관 정보 없음: " + dormCode));

            List<Room> rooms = roomRepository.findByDorm(dorm);

            // 한 번에 QR 조회 (N+1 문제 해결)
            List<QrCode> existingQrCodes = qrCodeRepository.findByRoomIn(rooms);

            // 검색 속도를 위해 Map으로 변환
            Map<Long, QrCode> qrMap = existingQrCodes.stream()
                    .collect(Collectors.toMap(qr -> qr.getRoom().getId(), qr -> qr));

            String dormFolder = dormCode + "/";

            for (Room room : rooms) {
                QrCode qrCode = qrMap.get(room.getId());

                if (qrCode != null) {
                    qrCode.refreshUuid(); // 기존 QR은 UUID 갱신
                } else {
                    qrCode = QrCode.builder().room(room).build(); // 없으면 새로 생성
                }
                qrCodesToSave.add(qrCode);

                // 이미지 생성에 필요한 데이터 미리 가공
                String content = String.format("%s/check?token=%s", host, qrCode.getUuid());
                String labelText = String.format("%s동 %s호", dormCode, room.getRoomNumber());
                String fileName = dormFolder + "QR_" + dormCode + "동_" + room.getRoomNumber() + "호.png";

                resultData.add(new QrGenerationData(content, labelText, fileName));
            }
        }

        // 여기서 일괄 저장 (이 시점에 DB에 반영 준비 완료)
        qrCodeRepository.saveAll(qrCodesToSave);

        return resultData;
    }
}