package com.parachute.booking.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceCreate {

    private final AdminRepository adminRepository;

    Admin createNewAdmin(AdminDto adminDto, AdminDataValidate adminDataValidate) {

        adminDataValidate.validateData(adminDto);
        // check if exists login/mail pass is ok

        Admin admin = new Admin();

        admin.setPassword(adminDto.getPassword());
        admin.setLogin(adminDto.getLogin());
        admin.setEmail(adminDto.getEmail());

        return adminRepository.save(admin);
    }
}