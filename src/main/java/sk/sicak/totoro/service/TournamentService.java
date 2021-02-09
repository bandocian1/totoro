package sk.sicak.totoro.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.sicak.totoro.algorithm.TournamentCreator;
import sk.sicak.totoro.config.TournamentConfiguration;
import sk.sicak.totoro.dao.MatchDao;
import sk.sicak.totoro.dao.PlayerDao;
import sk.sicak.totoro.exception.TotoroException;
import sk.sicak.totoro.model.Match;
import sk.sicak.totoro.model.Player;
import sk.sicak.totoro.model.Result;
import sk.sicak.totoro.utli.Results;

import java.util.*;

@Service
@Transactional
public class TournamentService {


    private final TournamentConfiguration config;
    private final PlayerDao playerDao;
    private final MatchDao matchDao;
    private final TournamentCreator tournamentCreator;


    public TournamentService(TournamentConfiguration config, PlayerDao playerDao, MatchDao matchDao, TournamentCreator tournamentCreator) {
        this.config = config;
        this.playerDao = playerDao;
        this.matchDao = matchDao;
        this.tournamentCreator = tournamentCreator;
    }

    public Player savePlayer(Player player){
        return playerDao.save(player);
    }

    public List<Match> getMatches() throws TotoroException {
        if(matchDao.count() == 0){
            return generateMatches();
        } else {
            List<Match> matches = new ArrayList<>();
            var matchIterator = matchDao.findAll().iterator();
            for (int i = 0; i < (config.getMatchesPerPlayer() * config.getPlayers() / 2); i++) {
                if(matchIterator.hasNext()){
                    matches.add(matchIterator.next());
                } else {
                    throw new TotoroException("Not enough matches generated");
                }
            }
            return matches;
        }
    }

    private List<Match> generateMatches() throws TotoroException {

        var playerIterator = playerDao.findAll().iterator();
        List<Player> playerList = new ArrayList<>(config.getPlayers());
        for (int i = 0; i < config.getPlayers(); i++) {
            if(playerIterator.hasNext()){
                playerList.add(playerIterator.next());
            } else {
                throw new TotoroException("Not enough players");
            }
        }
        List<Match> allMatches = tournamentCreator.generateMatches(playerList);
        for (Match match : allMatches) {
            matchDao.save(match);
        }
        return allMatches;
    }

    public int addResult(int matchId, Result result){
        if(result.getResult() != null && Results.isValidValue(result.getResult())){
            return matchDao.setMatchResult(matchId, result.getResult());
        } else {
            return -1;
        }
    }

    public Optional<Result> getResult(int matchId){
        Optional<Match> match = matchDao.findById(matchId);
        return match.map(Result::new);
    }

    public List<Player> getWinners() throws TotoroException {
        var matchIter = matchDao.findAll().iterator();
        List<Match> matchesPlayed = new ArrayList<>();
        for (int i = 0; i < (config.getMatchesPerPlayer() * config.getPlayers() / 2); i++) {
            if(matchIter.hasNext()){
                var match = matchIter.next();
                if(match.getResult() == null){
                    throw new TotoroException("Match has no result");
                }
                matchesPlayed.add(match);
            } else {
                throw new TotoroException("Not enough matches");
            }
        }
        Map<Player, Integer> scores = new HashMap<>();
        for (Match match : matchesPlayed) {
            Player playerA = match.getFirstPlayer();
            Player playerB = match.getSecondPlayer();
            String result = match.getResult();
            int playerAScore;
            int playerBScore;
            if(Results.TWO_ONE.value.equals(result) || Results.TWO_ZERO.value.equals(result)){
                playerAScore = 3;
                playerBScore = 1;
            } else {
                playerAScore = 1;
                playerBScore = 3;
            }
            playerAScore += scores.getOrDefault(playerA, 0);
            playerBScore += scores.getOrDefault(playerB, 0);
            scores.put(playerA, playerAScore);
            scores.put(playerB, playerBScore);
        }
        int maxScore = 0;
        List<Player> playerList = new ArrayList<>();
        for (Map.Entry<Player, Integer> entry : scores.entrySet()) {
            if(entry.getValue() > maxScore){
                maxScore = entry.getValue();
                playerList.clear();
                playerList.add(entry.getKey());
            } else if (entry.getValue() == maxScore){
                playerList.add(entry.getKey());
            }
        }
        return playerList;
    }
}
