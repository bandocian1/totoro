package sk.sicak.totoro.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

@Entity
public class Match {

    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="playerId")
    @JsonIdentityReference(alwaysAsId=true)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "firstPlayerId")
    private Player firstPlayer;

    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="playerId")
    @JsonIdentityReference(alwaysAsId=true)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "secondPlayerId")
    private Player secondPlayer;

    @Id
    @GeneratedValue
    private int matchId;

    @JsonIgnore
    @Column
    private String result;

    public Match() {
    }

    public Match(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(Player secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Match{" +
                "firstPlayerId=" + firstPlayer.getPlayerId() +
                ", secondPlayerId=" + secondPlayer.getPlayerId() +
                ", matchId=" + matchId +
                ", result='" + result + '\'' +
                '}';
    }
}
