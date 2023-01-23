package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.data.enums.ExpertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpertRepository extends JpaRepository<Expert, Integer> {
    Optional<Expert> findByEmail(String email);

    List<Expert> findAllByStatus(ExpertStatus status);
}
