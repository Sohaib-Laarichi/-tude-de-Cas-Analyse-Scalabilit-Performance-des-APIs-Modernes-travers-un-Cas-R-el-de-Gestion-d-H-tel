package com.example.soapapiversion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.example.soapapiversion.services",
        "com.example.soapapiversion.repositories",
        "com.example.soapapiversion"
})
@EntityScan(basePackages = "com.example.soapapiversion.entities")
@EnableJpaRepositories(basePackages = "com.example.soapapiversion.repositories")
public class SoapapiVersionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoapapiVersionApplication.class, args);
    }

}
