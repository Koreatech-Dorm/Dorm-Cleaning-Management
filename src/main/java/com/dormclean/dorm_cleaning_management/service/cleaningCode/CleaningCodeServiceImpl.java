package com.dormclean.dorm_cleaning_management.service.cleaningCode;

import com.dormclean.dorm_cleaning_management.dto.cleaningCode.GetCleaningCodeResponseDto;
import com.dormclean.dorm_cleaning_management.dto.cleaningCode.RegistrationCleaningCodeRequestDto;
import com.dormclean.dorm_cleaning_management.entity.CleaningCode;
import com.dormclean.dorm_cleaning_management.exception.cleaningCode.CleaningCodeNotFoundException;
import com.dormclean.dorm_cleaning_management.exception.qr.QrNotFoundException;
import com.dormclean.dorm_cleaning_management.repository.CleaningCodeRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
    public void registration(@Valid RegistrationCleaningCodeRequestDto dto) {

        cleaningCodeRepository.findTopByOrderByIdDesc()
                .ifPresentOrElse(
                        existingCode -> {
                            // 이미 있으면 덮어쓰기
                            existingCode.updateCode(dto.cleaningCode());
                        },
                        () -> {
                            // 없으면 새로 생성
                            CleaningCode newCode = CleaningCode.builder()
                                    .cleaningCode(dto.cleaningCode())
                                    .build();
                            cleaningCodeRepository.save(newCode);
                        });
    }

    @Override
    public GetCleaningCodeResponseDto getCleaningCode() {
        CleaningCode cleaningCode = cleaningCodeRepository.findTopByOrderByIdDesc()
                .orElseThrow(CleaningCodeNotFoundException::new);

        return new GetCleaningCodeResponseDto(cleaningCode.getCleaningCode());
    }
}
