package com.dormclean.dorm_cleaning_management.service.room;

import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;

public interface ExcelService {
    public void downloadExcel(HttpServletResponse res) throws Exception;

    public void registerByExcel(MultipartFile file) throws Exception;
}
