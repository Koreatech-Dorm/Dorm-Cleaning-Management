package com.dormclean.dorm_cleaning_management.service.admin;

import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dormclean.dorm_cleaning_management.dto.admin.AccountListResponseDto;
import com.dormclean.dorm_cleaning_management.entity.AdminUser;
import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;
import com.dormclean.dorm_cleaning_management.entity.enums.UserRole;
import com.dormclean.dorm_cleaning_management.exception.admin.AdminAccountLoadFailedException;
import com.dormclean.dorm_cleaning_management.exception.admin.AdminAlreadyExistsException;
import com.dormclean.dorm_cleaning_management.exception.admin.AdminNotFoundException;
import com.dormclean.dorm_cleaning_management.exception.admin.AdminPasswordMismatchException;
import com.dormclean.dorm_cleaning_management.exception.admin.SuperAdminNotFoundException;
import com.dormclean.dorm_cleaning_management.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        public AdminUser create(String username, String password) {
                if (userRepository.existsByUsername(username)) {
                        throw new AdminAlreadyExistsException();
                }
                AdminUser user = AdminUser.builder()
                                .username(username)
                                .password(passwordEncoder.encode(password))
                                .build();
                return userRepository.save(user);
        }

        @Override
        public List<AccountListResponseDto> AllAdminAccounts(UserRole role) {
                try {
                        return userRepository.findAllByRole(role)
                                        .stream()
                                        .map(user -> new AccountListResponseDto(
                                                        user.getUsername()))
                                        .toList();
                } catch (Exception err) {
                        // DB 조회 자체가 실패한 경우
                        throw new AdminAccountLoadFailedException();
                }
        }

        @Override
        public UserDetails loadUserByUsername(String username)
                        throws UsernameNotFoundException {
                AdminUser adminUser = userRepository.findByUsername(username)
                                .orElseThrow(AdminNotFoundException::new);

                return User.builder()
                                .username(adminUser.getUsername())
                                .password(adminUser.getPassword())
                                .authorities(new SimpleGrantedAuthority(adminUser.getRole().getValue()))
                                .build();
        }

        @Override
        public void delete(String username, String password) {
                AdminUser superAdmin = userRepository.findByRole(UserRole.SUPERADMIN)
                                .orElseThrow(SuperAdminNotFoundException::new);

                // 입력받은 비밀번호 확인
                if (!passwordEncoder.matches(password, superAdmin.getPassword())) {
                        throw new AdminPasswordMismatchException();
                }
                AdminUser adminUser = userRepository.findByUsername(username)
                                .orElseThrow(AdminNotFoundException::new);
                userRepository.delete(adminUser);
        }
}
