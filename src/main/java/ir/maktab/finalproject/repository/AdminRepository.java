package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.data.entity.roles.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
}
