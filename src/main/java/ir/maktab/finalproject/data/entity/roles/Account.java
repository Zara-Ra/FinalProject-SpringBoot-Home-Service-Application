package ir.maktab.finalproject.data.entity.roles;

import ir.maktab.finalproject.data.entity.roles.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
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

    @Enumerated(value = EnumType.STRING)
    protected Role role;

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
