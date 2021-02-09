package sk.sicak.totoro.util;

import sk.sicak.totoro.model.Match;
import sk.sicak.totoro.model.Player;

import java.util.ArrayList;
import java.util.List;

public class TestHelper {

    public static List<Player> generatePlayers(){
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Player player = new Player();
            player.setPlayerId(i);
            player.setName("Player_" + i);
            player.setAge(i + 20);
            players.add(player);
        }
        return players;
    }

    public static List<Match> generateDummyMatches(){
        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            Match match = new Match();
            match.setMatchId(i);
            match.setSecondPlayer(new Player());
            match.setFirstPlayer(new Player());
            match.setResult("2:0");
            matches.add(match);
        }
        return matches;
    }

}
