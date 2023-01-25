package ir.maktab.finalproject.data.entity;

import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.data.entity.services.SubService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Duration;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ExpertOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    Integer id;

    @OneToOne
    Expert expert;

    @ManyToOne
    CustomerOrder customerOrder;

    @ManyToOne
    SubService subService;

    @Temporal(value = TemporalType.DATE)
    @CreationTimestamp
    private Date registerDate;

    private double price;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date preferredDate;

    @Column(nullable = false)
    private Duration duration;
}
