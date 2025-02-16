package ca.uwindsor.ims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class InternshipManagementApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(InternshipManagementApplication.class, args);
    }
} 