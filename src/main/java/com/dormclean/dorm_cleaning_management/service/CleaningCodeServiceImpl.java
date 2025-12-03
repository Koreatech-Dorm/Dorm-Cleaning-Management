package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.entity.CleaningCode;
import com.dormclean.dorm_cleaning_management.repository.CleaningCodeRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CleaningCodeServiceImpl implements CleaningCodeService {
    private final CleaningCodeRepository cleaningCodeRepository;
    private final HttpSession session;

    @Override
    @Transactional
    public void registration(String cleaningCode) {
        CleaningCode existingCode = cleaningCodeRepository.findAll()
                .stream()
                .findFirst()
                .orElse(null);

        if (existingCode != null) {
            // 이미 있다면 덮어쓰기
            existingCode.updateCode(cleaningCode);
        } else {
            // 없다면 새로 저장
            CleaningCode newCleaningCode = CleaningCode.builder()
                    .cleaningCode(cleaningCode)
                    .build();
            cleaningCodeRepository.save(newCleaningCode);
        }
    }

    @Override
    public void useCleaningCode(String cleaningCode) {
        CleaningCode checkCode = cleaningCodeRepository.findByCleaningCode(cleaningCode)
                .orElseThrow(() -> new IllegalArgumentException("코드가 유효하지 않습니다."));

        session.setAttribute("cleaningCode", checkCode.getCleaningCode());
    }
}
