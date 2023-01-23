package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.data.entity.services.BaseService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseServiceRepository extends JpaRepository<BaseService, Integer> {
}
