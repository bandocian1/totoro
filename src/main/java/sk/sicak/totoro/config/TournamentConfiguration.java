package sk.sicak.totoro.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@ConfigurationProperties(prefix = "match")
@Primary
@Configuration
public class TournamentConfiguration {

    private int players = 6;
    private int matchesPerPlayer = 3;

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public int getMatchesPerPlayer() {
        return matchesPerPlayer;
    }

    public void setMatchesPerPlayer(int matchesPerPlayer) {
        this.matchesPerPlayer = matchesPerPlayer;
    }
}
