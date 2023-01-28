package ir.maktab.finalproject.service.impl;

import ir.maktab.finalproject.data.entity.roles.Admin;
import ir.maktab.finalproject.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    @Value(value = "${admin.email}")
    private String email;

    @Value(value = "${admin.password}")
    private String password;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin assignAdmin() {
        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setPassword(password);
        return adminRepository.save(admin);
    }
}
