package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.dto.CleaningCodeDto;
import com.dormclean.dorm_cleaning_management.dto.GetCleaningCodeResponseDto;
import com.dormclean.dorm_cleaning_management.dto.RegistrationCleaningCodeRequestDto;
import com.dormclean.dorm_cleaning_management.entity.CleaningCode;
import com.dormclean.dorm_cleaning_management.repository.CleaningCodeRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CleaningCodeServiceImpl implements CleaningCodeService {
    private final CleaningCodeRepository cleaningCodeRepository;
    private final HttpSession session;

    @Override
    @Transactional
    public void registration(RegistrationCleaningCodeRequestDto dto) {
        CleaningCode existingCode = cleaningCodeRepository.findById(1L)
                .orElse(null);

        if (existingCode != null) {
            // 이미 있다면 덮어쓰기
            existingCode.updateCode(dto.cleaningCode());
        } else {
            // 없다면 새로 저장
            CleaningCode newCleaningCode = CleaningCode.builder()
                    .cleaningCode(dto.cleaningCode())
                    .build();
            cleaningCodeRepository.save(newCleaningCode);
        }
    }

    @Override
    public void useCleaningCode(CleaningCodeDto dto) {
        CleaningCode checkCode = cleaningCodeRepository.findByCleaningCode(dto.cleaningCode())
                .orElseThrow(() -> new IllegalArgumentException("코드가 유효하지 않습니다."));

        session.setAttribute("cleaningCode", checkCode.getCleaningCode());
    }

    @Override
    public GetCleaningCodeResponseDto getCleaningCode() {
        CleaningCode cleaningCode = cleaningCodeRepository.findById(1L)
                .orElse(null);

        if (cleaningCode == null) {
            return null;
        }

        return new GetCleaningCodeResponseDto(cleaningCode.getCleaningCode());
    }
}
