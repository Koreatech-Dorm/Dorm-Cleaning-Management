package com.dormclean.dorm_cleaning_management.config;

import com.dormclean.dorm_cleaning_management.security.IpRestrictionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final IpRestrictionFilter ipRestrictionFilter;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/check", "/api/check/**", "/login", "/loginProc")
                                                // .requestMatchers("/**")
                                                .permitAll()
                                                .requestMatchers("/admin/account-manager").hasAnyRole("SUPERADMIN")
                                                .requestMatchers("/admin/**").hasAnyRole("ADMIN", "SUPERADMIN")
                                                .anyRequest().authenticated())
                                .addFilterBefore(
                                        ipRestrictionFilter,
                                        UsernamePasswordAuthenticationFilter.class
                                )
                                .csrf(csrf -> csrf.disable())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/loginProc")
                                                .defaultSuccessUrl("/", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout") // 로그아웃 처리 URL
                                                .logoutSuccessUrl("/login?logout") // 로그아웃 성공 후 이동
                                                .invalidateHttpSession(true) // 세션 무효화
                                                .deleteCookies("JSESSIONID") // 쿠키 삭제
                                );
                ;

                return http.build();
        }

        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        AuthenticationManager authenticationManager(
                        AuthenticationConfiguration authenticationConfiguration) throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
                return web -> web.ignoring()
                                .requestMatchers(
                                                "/css/**",
                                                "/js/**",
                                                "/img/**",
                                                "/webjars/**");
        }
}