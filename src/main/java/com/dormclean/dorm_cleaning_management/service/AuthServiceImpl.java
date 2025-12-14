package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.dto.admin.AdminLoginRequestDto;
import com.dormclean.dorm_cleaning_management.dto.admin.AdminLoginResponseDto;
import com.dormclean.dorm_cleaning_management.entity.AdminUser;
import com.dormclean.dorm_cleaning_management.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final AdminRepository adminRepository;

    @Override
    public AdminLoginResponseDto login(AdminLoginRequestDto dto) {
        AdminUser adminUser = adminRepository.findByUserId(dto.userId())
                .orElseThrow(() -> new IllegalArgumentException("해당되는 admin 계정이 존재하지 않습니다."));

        if(!adminUser.getPassword().equals(dto.password())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return new AdminLoginResponseDto(
                adminUser.getUserId(),
                "로그인 성공"
        );
    }
}
