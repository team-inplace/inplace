package my.inplace.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@ConfigurationPropertiesScan(basePackages = {"my.inplace.*"})
@SpringBootApplication(scanBasePackages = {"my.inplace.*"})
@EnableJpaRepositories(basePackages = {"my.inplace.infra"})
@EntityScan(basePackages = {"my.inplace.domain"})
public class InplaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InplaceApplication.class, args);
    }

}
