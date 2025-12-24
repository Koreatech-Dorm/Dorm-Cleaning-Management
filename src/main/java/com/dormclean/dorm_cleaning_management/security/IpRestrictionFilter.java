package com.dormclean.dorm_cleaning_management.security;

import com.dormclean.dorm_cleaning_management.config.AppProperties;
import com.dormclean.dorm_cleaning_management.util.ClientIpUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class IpRestrictionFilter extends OncePerRequestFilter {

    private final AppProperties appProperties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String uri = request.getRequestURI();
        List<String> prefixes = appProperties.getAllowedIpPrefix();

        if (uri.startsWith("/admin")){
            String clientIp = ClientIpUtils.getClientIp(request);

            boolean allowed = prefixes.stream()
                    .anyMatch(clientIp::startsWith);

            if(!allowed){
                log.warn("ADMIN IP BLOCKED - ip={}, uri={}", clientIp, uri);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}