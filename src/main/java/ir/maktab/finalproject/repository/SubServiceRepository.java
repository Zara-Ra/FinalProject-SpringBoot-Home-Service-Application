package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.data.entity.services.SubService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubServiceRepository extends JpaRepository<SubService, Integer> {
    List<SubService> findAllByBaseService_BaseName(String baseName);

    Optional<SubService> findBySubName(String subName);
}
