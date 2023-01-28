package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.data.entity.CustomerOrder;
import ir.maktab.finalproject.data.entity.ExpertOffer;
import ir.maktab.finalproject.data.entity.roles.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpertOfferRepository extends JpaRepository<ExpertOffer, Integer> {
    long countByIsChosen(boolean isChosen);

    List<ExpertOffer> findAllByExpert(Expert expert);

    List<ExpertOffer> findAllByExpertAndIsChosen(Expert expert,Boolean isChosen);

    //List<ExpertOffer> findAllByCustomerOrderOrderByPriceAsc(CustomerOrder customerOrder);
}
