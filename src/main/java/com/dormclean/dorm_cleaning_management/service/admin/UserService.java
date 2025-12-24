package com.dormclean.dorm_cleaning_management.service.admin;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.dormclean.dorm_cleaning_management.dto.admin.AccountListResponseDto;
import com.dormclean.dorm_cleaning_management.entity.AdminUser;
import com.dormclean.dorm_cleaning_management.entity.enums.UserRole;

public interface UserService {
    public AdminUser create(String username, String password);

    public List<AccountListResponseDto> AllAdminAccounts(UserRole role);

    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException;

    public void delete(String username, String password);
}
