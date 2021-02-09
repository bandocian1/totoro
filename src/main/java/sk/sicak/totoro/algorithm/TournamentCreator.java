package sk.sicak.totoro.algorithm;

import org.springframework.stereotype.Component;
import sk.sicak.totoro.config.TournamentConfiguration;
import sk.sicak.totoro.model.Match;
import sk.sicak.totoro.model.Player;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class TournamentCreator {


    private final TournamentConfiguration config;

    public TournamentCreator(TournamentConfiguration config) {
        this.config = config;
    }

    public List<Match> generateMatches(List<Player> playerList){

        List<Match> allMatches = new ArrayList<>();

        Map<Integer, Player> idToPlayers = playerList.stream().collect(Collectors.toMap(Player::getPlayerId, p -> p));

        List<Integer> players = playerList.stream().map(Player::getPlayerId).collect(Collectors.toList());
        Map<Integer, List<Match>> matches = players.stream().collect(Collectors.toMap(l -> l, l -> new ArrayList<>()));
        while(!players.isEmpty()){
            Collections.shuffle(players);
            Integer actualPlayer = players.remove(players.size()-1);
            List<Match> actualPlayerMatches = matches.get(actualPlayer);
            if(actualPlayerMatches.size() >= config.getMatchesPerPlayer()){
                continue;
            }
            List<Integer> opponents = new ArrayList<>(players);
            int preSize = actualPlayerMatches.size();
            for (int i = 0; i < config.getMatchesPerPlayer() - preSize; i++) {
                if(opponents.isEmpty()){
                    break;
                }
                Collections.shuffle(opponents);
                Integer opponent = opponents.remove(opponents.size()-1);
                if(matches.get(opponent).size() >= config.getMatchesPerPlayer()){
                    i--;
                    continue;
                }
                Match newMatch = new Match(idToPlayers.get(actualPlayer), idToPlayers.get(opponent));
                allMatches.add(newMatch);
                matches.get(actualPlayer).add(newMatch);
                matches.get(opponent).add(newMatch);

            }

        }

        Collections.shuffle(allMatches);
        List<Integer> lackingPlayers = new ArrayList<>();
        for (var entry : matches.entrySet()) {
            int playerMatches = entry.getValue().size();
            if(playerMatches < config.getMatchesPerPlayer()){
                for (int i = 0; i < config.getMatchesPerPlayer() - playerMatches; i++) {
                    lackingPlayers.add(entry.getKey());
                }
            }
        }
        while(!lackingPlayers.isEmpty()){
            int lackingPlayer1 = lackingPlayers.remove(0);
            int lackingPlayer2 = lackingPlayers.remove(0);

            boolean notDone = true;
            while(notDone){
                Match matchToBeSplit = allMatches.remove(0);
                int matchFirstPlayer = matchToBeSplit.getFirstPlayer().getPlayerId();
                int matchSecondPlayer = matchToBeSplit.getSecondPlayer().getPlayerId();
                Set<Integer> playerAIllegalPlayers = illegalPlayers(lackingPlayer1, matches);
                Set<Integer> playerBIllegalPlayers = illegalPlayers(lackingPlayer2, matches);
                if(!playerAIllegalPlayers.contains(matchFirstPlayer) && !playerBIllegalPlayers.contains(matchSecondPlayer)){
                    splitMatch(allMatches, idToPlayers, matches, lackingPlayer1, lackingPlayer2, matchFirstPlayer, matchSecondPlayer);
                    notDone = false;
                } else if (!playerAIllegalPlayers.contains(matchSecondPlayer) && !playerBIllegalPlayers.contains(matchFirstPlayer)){
                    splitMatch(allMatches, idToPlayers, matches, lackingPlayer2, lackingPlayer1, matchFirstPlayer, matchSecondPlayer);
                    notDone = false;
                } else {
                    allMatches.add(matchToBeSplit);
                }
            }
        }
        return allMatches;
    }

    private void splitMatch(List<Match> result, Map<Integer, Player> idToPlayers, Map<Integer, List<Match>> matches, int playerA, int playerB, int matchFirstPlayer, int matchSecondPlayer) {
        Match newMatch1 = new Match(idToPlayers.get(matchFirstPlayer), idToPlayers.get(playerA));
        Match newMatch2 = new Match(idToPlayers.get(matchSecondPlayer), idToPlayers.get(playerB));
        matches.get(playerA).add(newMatch1);
        matches.get(playerB).add(newMatch2);
        result.add(newMatch1);
        result.add(newMatch2);
    }

    private Set<Integer> illegalPlayers(int player, Map<Integer, List<Match>> matches){
        Set<Integer> result = new HashSet<>();
        List<Match> playerMatches = matches.get(player);
        for (Match playerMatch : playerMatches) {
            result.add(playerMatch.getFirstPlayer().getPlayerId());
            result.add(playerMatch.getSecondPlayer().getPlayerId());
        }
        return result;
    }




}
