package ir.maktab.finalproject.data.entity.roles;


import ir.maktab.finalproject.data.entity.Credit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class User extends Account {
    @Column(nullable = false)
    protected String firstName;
    @Column(nullable = false)
    protected String lastName;
    @Temporal(value = TemporalType.DATE)
    @CreationTimestamp
    protected Date registerDate;
    @EqualsAndHashCode.Exclude
    @OneToOne(cascade = CascadeType.MERGE)
    protected Credit credit;

    public User(String email, String password, String firstName, String lastName) {
        super(email, password);
        this.firstName = firstName;
        this.lastName = lastName;
    }
}