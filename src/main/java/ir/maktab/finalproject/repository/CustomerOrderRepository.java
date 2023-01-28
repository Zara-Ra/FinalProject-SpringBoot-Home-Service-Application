package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.data.entity.CustomerOrder;
import ir.maktab.finalproject.data.entity.ExpertOffer;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.data.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Integer> {
    @Query("FROM CustomerOrder o WHERE o.subService = ?1 AND o.status = ?2 or o.status = ?3")
    List<CustomerOrder> findAllBySubServiceAndStatus(SubService subService, OrderStatus status1, OrderStatus status2);

    CustomerOrder findByAcceptedExpertOffer(ExpertOffer acceptedOffer);
}
