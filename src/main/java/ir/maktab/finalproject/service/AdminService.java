package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.entity.roles.Admin;
import ir.maktab.finalproject.repository.AdminRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    private final AdminRepository adminRepository;

    public Admin assignAdmin() {
        Admin admin = new Admin();
        admin.setEmail("admin@admin.com");
        admin.setPassword("12345678");
        return adminRepository.save(admin);
    }
}
