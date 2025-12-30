package com.dormclean.dorm_cleaning_management.service.room;

import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;

public interface ExcelService {
    public byte[] downloadExcel() throws Exception;

    public void registerByExcel(MultipartFile file);
}
