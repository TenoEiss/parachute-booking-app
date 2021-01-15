package com.parachute.booking;

import com.parachute.booking.admin.Admin;
import com.parachute.booking.admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
@RequiredArgsConstructor
public class BookingApplication implements CommandLineRunner {

    private final AdminRepository adminRepository;

    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }

    @Scheduled(cron = "5 2-23/3 * * *")
    public void getCurrentForecastAndSaveToDatabase(){
        //todo
    }

    @Override
    public void run(String... args) throws Exception {

        adminRepository.deleteAll();
        Admin admin = new Admin();
        admin.setLogin("Admin1");
        admin.setPassword("12345");
        admin.setEmail("adminus@gmail.com");
        adminRepository.save(admin);


    }
}
