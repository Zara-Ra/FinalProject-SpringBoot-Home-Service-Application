package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.data.entity.CustomerOrder;
import ir.maktab.finalproject.data.entity.ExpertOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpertOfferRepository extends JpaRepository<ExpertOffer, Integer> {
    List<ExpertOffer> findAllByCustomerOrderOrderByPriceAsc(CustomerOrder customerOrder);
}
