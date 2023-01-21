package ir.maktab.finalproject.data.entity;

import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.data.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Integer id;
    @ManyToOne
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
    @OneToOne(cascade = CascadeType.MERGE)
    private Address address;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;
    @OneToOne
    private Expert expert;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date doneDate;
}