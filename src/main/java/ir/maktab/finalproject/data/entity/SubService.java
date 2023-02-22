package ir.maktab.finalproject.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class SubService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Integer id;

    @ManyToOne
    private BaseService baseService;

    @Column(unique = true, updatable = false, nullable = false)
    private String subName;

    private double basePrice;

    private String description;
}