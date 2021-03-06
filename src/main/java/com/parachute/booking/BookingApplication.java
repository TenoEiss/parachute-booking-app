package com.parachute.booking;

import com.parachute.booking.admin.Admin;
import com.parachute.booking.admin.AdminRepository;
import com.parachute.booking.forecast.api.ForecastClient;
import com.parachute.booking.security.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
@RequiredArgsConstructor
@EnableJpaAuditing
@EnableScheduling
public class BookingApplication implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final ForecastClient forecastClient;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }

    @Scheduled(cron = "0 5 2-23/3 * * *")
    public void getCurrentForecastAndSaveToDatabase(){
        forecastClient.getForecast();
    }

    @Override
    public void run(String... args) throws Exception {

        adminRepository.deleteAll();
        Admin admin = new Admin();
        admin.setLogin("Admin1");
        admin.setPassword(passwordEncoder.encode("12345"));
        admin.setEmail("adminus@gmail.com");
        admin.setAuthority(Collections.singletonList(Roles.ADMIN::toString));
        adminRepository.save(admin);
    }
}
