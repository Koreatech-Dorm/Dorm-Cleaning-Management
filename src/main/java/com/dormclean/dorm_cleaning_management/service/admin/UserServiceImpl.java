package com.dormclean.dorm_cleaning_management.service.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dormclean.dorm_cleaning_management.dto.admin.AccountListResponseDto;
import com.dormclean.dorm_cleaning_management.entity.AdminUser;
import com.dormclean.dorm_cleaning_management.entity.enums.UserRole;
import com.dormclean.dorm_cleaning_management.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        public AdminUser create(String username, String password) {
                AdminUser user = AdminUser.builder()
                                .username(username)
                                .password(passwordEncoder.encode(password))
                                .build();
                return userRepository.save(user);
        }

        @Override
        public List<AccountListResponseDto> AllAdminAccounts(UserRole role) {
                return userRepository.findAllByRole(role)
                                .stream()
                                .map(user -> new AccountListResponseDto(
                                                user.getUsername()))
                                .toList();

        }

        @Override
        public UserDetails loadUserByUsername(String username)
                        throws UsernameNotFoundException {
                AdminUser adminUser = userRepository.findByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

                return User.builder()
                                .username(adminUser.getUsername())
                                .password(adminUser.getPassword())
                                .authorities(new SimpleGrantedAuthority(adminUser.getRole().getValue()))
                                .build();
        }

        @Override
        public void delete(String username, String password) {
                AdminUser superAdmin = userRepository.findByRole(UserRole.SUPERADMIN)
                                .orElseThrow(() -> new RuntimeException("SUPERADMIN not found"));

                // 입력받은 비밀번호 확인
                if (!passwordEncoder.matches(password, superAdmin.getPassword())) {
                        throw new RuntimeException("SUPERADMIN 비밀번호가 일치하지 않습니다.");
                }
                AdminUser adminUser = userRepository.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("계정을 찾을 수 없습니다."));
                userRepository.delete(adminUser);
        }
}
