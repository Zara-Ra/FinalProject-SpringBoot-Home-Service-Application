package ir.maktab.finalproject.data.entity.roles;

import ir.maktab.finalproject.data.entity.Review;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.data.enums.ExpertStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Expert extends User {
    @Enumerated(value = EnumType.STRING)
    private ExpertStatus status;

    @Lob
    private byte[] photo;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<SubService> subServiceList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.MERGE)
    private List<Review> reviewList = new ArrayList<>();

    private double averageScore;

    public Expert(String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Expert)) return false;
        if (!super.equals(o)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

}
