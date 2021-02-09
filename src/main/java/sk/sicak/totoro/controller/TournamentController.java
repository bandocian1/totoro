package sk.sicak.totoro.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sk.sicak.totoro.exception.TotoroException;
import sk.sicak.totoro.model.Match;
import sk.sicak.totoro.model.Message;
import sk.sicak.totoro.model.Player;
import sk.sicak.totoro.model.Result;
import sk.sicak.totoro.service.TournamentService;

import java.util.List;
import java.util.Map;

@RestController
public class TournamentController {

    private static final Logger logger = LoggerFactory.getLogger(TournamentController.class);

    private final TournamentService tournamentService;


    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @PostMapping("/players")
    public @ResponseBody Player registerNewPlayer(@RequestBody Player player){
        Player savedPlayer = tournamentService.savePlayer(player);
        logger.debug("Saving player: [{}]", savedPlayer);
        return savedPlayer;
    }

    @GetMapping("/tournaments/draw")
    public @ResponseBody Map<String, List<Match>> drawMatches(){
        try{
            var list = tournamentService.getMatches();
            return Map.of("matches", list);
        } catch (TotoroException e ){
            throw new ResponseStatusException(HttpStatus.PRECONDITION_REQUIRED, e.getMessage() );
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error");
        }

    }

    @PutMapping("/tournaments/results/{matchId}")
    public @ResponseBody Message enterResult(@PathVariable int matchId, @RequestBody Result resultEntry){
        logger.debug("Entering result [{}] for match [{}]", resultEntry, matchId);
        int updated = tournamentService.addResult(matchId, resultEntry);
        if(updated > 0){
            return new Message("Score registered");
        } else if (updated == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No matches updated");
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid score provided");
        }

    }

    @GetMapping("/tournaments/results/{matchId}")
    public @ResponseBody Result getMatchResult(@PathVariable int matchId){
        logger.debug("Getting match [{}] result", matchId);
        var result = tournamentService.getResult(matchId);
        if(result.isPresent()) {
            logger.debug("Found this: {}", result.get());
            return result.get();
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find match with id [" + matchId + "]");
        }

    }

    @GetMapping("/tournaments/winner")
    public @ResponseBody Map<String, List<Player>> getWinners(){
        logger.debug("Getting the winner(s) of the tournament");
        try {
            List<Player> winners = tournamentService.getWinners();
            return Map.of("winners", winners);
        } catch (TotoroException totoroException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not enough matches");
        }
    }


}
