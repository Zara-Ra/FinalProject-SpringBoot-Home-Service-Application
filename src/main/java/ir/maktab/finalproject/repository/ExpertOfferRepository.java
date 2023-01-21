package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.data.entity.ExpertOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertOfferRepository extends JpaRepository<ExpertOffer,Integer> {
}
