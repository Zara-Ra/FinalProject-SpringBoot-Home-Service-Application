package ir.maktab.finalproject.data.entity.roles;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    protected Integer id;
    @Column(unique = true, nullable = false, updatable = false)
    protected String email;
    @Column(length = 8, nullable = false)
    protected String password;

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }
}