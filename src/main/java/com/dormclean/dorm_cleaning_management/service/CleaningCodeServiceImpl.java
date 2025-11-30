package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.dto.CleaningCodeDto;
import com.dormclean.dorm_cleaning_management.entity.CleaningCode;
import com.dormclean.dorm_cleaning_management.entity.Dorm;
import com.dormclean.dorm_cleaning_management.repository.CleaningCodeRepository;
import com.dormclean.dorm_cleaning_management.repository.DormRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CleaningCodeServiceImpl implements  CleaningCodeService {
    private final CleaningCodeRepository cleaningCodeRepository;
    private final DormRepository dormRepository;
    private final HttpSession session;

    @Override
    @Transactional
    public void registration(String dormCode, String cleaningCode) {
        Dorm dorm = dormRepository.findByDormCode(dormCode).orElseThrow(() -> new IllegalArgumentException("해당 기숙사가 존재하지 않습니다."));

        Optional<CleaningCode> existingCode = cleaningCodeRepository.findByDorm(dorm);

        if (existingCode.isPresent()) {
            // 이미 있다면 덮어쓰기
            existingCode.get().updateCode(cleaningCode);
        } else {
            // 없다면 새로 저장
            CleaningCode newCleaningCode = CleaningCode.builder()
                    .dorm(dorm)
                    .code(cleaningCode)
                    .build();
            cleaningCodeRepository.save(newCleaningCode);
        }
    }

    @Override
    public void useCleaningCode(String cleaningCode, String dormCode) {
        System.out.println("dorm %s".formatted(dormCode));
        CleaningCode checkCode = cleaningCodeRepository.findByCodeAndDorm_DormCode(cleaningCode, dormCode)
                .orElseThrow(() -> new IllegalArgumentException("코드가 유효하지 않습니다."));

        session.setAttribute("cleaningCode", checkCode.getCode());
        session.setAttribute("dormCodeForCleaning", dormCode);
    }
}
