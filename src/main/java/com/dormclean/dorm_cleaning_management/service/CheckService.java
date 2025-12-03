package com.dormclean.dorm_cleaning_management.service;

import com.dormclean.dorm_cleaning_management.entity.Dorm;

public interface CheckService {
    public void checkOut(Dorm dorm, String roomNumber);
    public void cleanCheck(Dorm dorm, String roomNumber);
}
