package com.dormclean.dorm_cleaning_management.config;

import com.dormclean.dorm_cleaning_management.entity.AdminUser;
import com.dormclean.dorm_cleaning_management.entity.enums.UserRole;
import com.dormclean.dorm_cleaning_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()) {
            AdminUser admin = AdminUser.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("1111"))
                    .role(UserRole.SUPERADMIN) // 또는 Role.SUPERADMIN (Enum 사용 시)
                    .build();

            userRepository.save(admin);
            System.out.println(">>> 초기 관리자 계정 생성 완료 (admin / 1111)");
        }
    }
}