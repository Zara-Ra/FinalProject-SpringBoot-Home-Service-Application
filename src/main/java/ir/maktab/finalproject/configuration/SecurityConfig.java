package ir.maktab.finalproject.configuration;

import ir.maktab.finalproject.configuration.jwt.JwtConfig;
import ir.maktab.finalproject.configuration.jwt.JwtTokenVerifier;
import ir.maktab.finalproject.configuration.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import ir.maktab.finalproject.repository.AdminRepository;
import ir.maktab.finalproject.repository.CustomerRepository;
import ir.maktab.finalproject.repository.ExpertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final ExpertRepository expertRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtTokenVerifier jwtTokenVerifier;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public SecurityConfig(AdminRepository adminRepository,
                          CustomerRepository customerRepository,
                          ExpertRepository expertRepository,
                          BCryptPasswordEncoder passwordEncoder,
                          JwtTokenVerifier jwtTokenVerifier, JwtConfig jwtConfig, SecretKey secretKey) {
        this.adminRepository = adminRepository;
        this.customerRepository = customerRepository;
        this.expertRepository = expertRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenVerifier = jwtTokenVerifier;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), jwtConfig, secretKey))
                .addFilterAfter(jwtTokenVerifier, JwtUsernameAndPasswordAuthenticationFilter.class)

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
        .httpBasic();
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
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
