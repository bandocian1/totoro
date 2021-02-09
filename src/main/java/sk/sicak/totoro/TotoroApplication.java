package sk.sicak.totoro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import sk.sicak.totoro.config.TournamentConfiguration;

@SpringBootApplication
@EnableConfigurationProperties(TournamentConfiguration.class)
public class TotoroApplication {

    public static void main(String[] args) {
        SpringApplication.run(TotoroApplication.class, args);
    }

}
