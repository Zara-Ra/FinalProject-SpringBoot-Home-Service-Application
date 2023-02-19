package ir.maktab.finalproject.data.entity.roles;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class Admin extends Account {

    @Override
    public boolean isEnabled() {
        return true;
    }
}
