package ir.maktab.finalproject.configuration;

import ir.maktab.finalproject.repository.AdminRepository;
import ir.maktab.finalproject.repository.CustomerRepository;
import ir.maktab.finalproject.repository.ExpertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final ExpertRepository expertRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public SecurityConfig(AdminRepository adminRepository,
                          CustomerRepository customerRepository,
                          ExpertRepository expertRepository,
                          BCryptPasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.customerRepository = customerRepository;
        this.expertRepository = expertRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/customer/register").permitAll()
                .requestMatchers("/expert/register").permitAll()
                .requestMatchers("/base/find").permitAll()
                .requestMatchers("/base/find-all").permitAll()
                .requestMatchers("/sub/find-all").permitAll()
                .requestMatchers("/sub/find").permitAll()
                .requestMatchers("/expert/verify").permitAll()

                .anyRequest().authenticated()
                .and()
                //.formLogin();
                .httpBasic();
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(email -> adminRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException(String
                                .format("%s Not Found", email))))
                .passwordEncoder(passwordEncoder).and()

                .userDetailsService(email -> customerRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException(String
                                .format("%s Not Found", email))))
                .passwordEncoder(passwordEncoder).and()

                .userDetailsService(email -> expertRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException(String
                                .format("%s Not Found", email))))
                .passwordEncoder(passwordEncoder);
    }
}
