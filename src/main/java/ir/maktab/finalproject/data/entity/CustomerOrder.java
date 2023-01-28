package ir.maktab.finalproject.data.entity;

import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.data.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Integer id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Customer customer;

    @OneToOne
    private SubService subService;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date preferredDate;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Address address;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.EAGER)
    private List<ExpertOffer> expertOfferList;

    @EqualsAndHashCode.Exclude
    @OneToOne(cascade = CascadeType.MERGE)
    private ExpertOffer acceptedExpertOffer;

    @EqualsAndHashCode.Exclude
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date startDate;

    @EqualsAndHashCode.Exclude
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date finishDate;
}
