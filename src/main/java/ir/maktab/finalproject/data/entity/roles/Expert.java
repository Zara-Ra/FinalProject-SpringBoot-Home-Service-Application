package ir.maktab.finalproject.data.entity.roles;

import ir.maktab.finalproject.data.entity.ExpertOffer;
import ir.maktab.finalproject.data.entity.Review;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.data.enums.ExpertStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Expert extends User {

    @Enumerated(value = EnumType.STRING)
    private ExpertStatus status;

    @ToString.Exclude
    @Lob
    private byte[] photo;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SubService> subServiceList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.MERGE)
    private List<Review> reviewList = new ArrayList<>();

    private double averageScore;

    @OneToMany
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<ExpertOffer> acceptedOfferList = new ArrayList<>();


    @Column(length = 64)
    private String verificationCode;

    public Expert(String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName);
    }


    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Expert)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}
