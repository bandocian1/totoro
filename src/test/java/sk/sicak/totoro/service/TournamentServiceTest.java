package sk.sicak.totoro.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.sicak.totoro.algorithm.TournamentCreator;
import sk.sicak.totoro.config.TournamentConfiguration;
import sk.sicak.totoro.dao.MatchDao;
import sk.sicak.totoro.dao.PlayerDao;
import sk.sicak.totoro.exception.TotoroException;
import sk.sicak.totoro.model.Match;
import sk.sicak.totoro.model.Player;
import sk.sicak.totoro.model.Result;
import sk.sicak.totoro.util.TestHelper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentServiceTest {

    @InjectMocks
    private TournamentService tournamentService;

    @Mock
    private TournamentConfiguration config;

    @Mock
    private MatchDao matchDao;

    @Mock
    private PlayerDao playerDao;

    @Mock
    private TournamentCreator creator;

    @Test
    void savePlayer() {
        Player player = new Player();
        when(playerDao.save(any(Player.class))).thenReturn(player);
        tournamentService.savePlayer(player);
        verify(playerDao).save(any(Player.class));
    }

    @Test
    void getMatches_newTournament_matchesAreCreated() throws TotoroException {
        when(matchDao.count()).thenReturn(0L);
        when(playerDao.findAll()).thenReturn(TestHelper.generatePlayers());
        when(config.getPlayers()).thenReturn(6);
        when(config.getMatchesPerPlayer()).thenReturn(3);
        List<Match> dummyMatches = TestHelper.generateDummyMatches();
        when(creator.generateMatches(anyList())).thenReturn(dummyMatches);

        var result = tournamentService.getMatches();
        verify(matchDao, times(dummyMatches.size())).save(any(Match.class));
        assertEquals(dummyMatches.size(), result.size());

    }

    @Test
    void getMatches_existingTournament_matchesAreReturned() throws TotoroException {
        when(matchDao.count()).thenReturn(9L);
        when(matchDao.findAll()).thenReturn(TestHelper.generateDummyMatches());
        when(config.getMatchesPerPlayer()).thenReturn(3);
        when(config.getPlayers()).thenReturn(6);
        var result = tournamentService.getMatches();
        assertEquals(9, result.size());
    }

    @Test
    void addResult_validResult() {
        when(matchDao.setMatchResult(anyInt(), anyString())).thenReturn(1);
        Result result = new Result();
        result.setResult("2:0");
        var testResult = tournamentService.addResult(1, result);
        assertEquals(1, testResult);
    }

    @Test
    void addResult_invalidResult() {
        Result result = new Result();
        result.setResult("2:4");
        var testResult = tournamentService.addResult(1, result);
        assertEquals(-1, testResult);
    }

    @Test
    void getResult() {
        Match dummyMatch = new Match();
        dummyMatch.setFirstPlayer(new Player());
        dummyMatch.setSecondPlayer(new Player());
        dummyMatch.setResult("2:0");
        when(matchDao.findById(anyInt())).thenReturn(Optional.of(dummyMatch));
        var result = tournamentService.getResult(1);
        assertTrue(result.isPresent());
        assertEquals("2:0", result.get().getResult());

    }

    @Test
    void getWinners() throws TotoroException {
        when(matchDao.findAll()).thenReturn(TestHelper.generateDummyMatches());
        when(config.getMatchesPerPlayer()).thenReturn(3);
        when(config.getPlayers()).thenReturn(6);
        var result = tournamentService.getWinners();
        assertFalse(result.isEmpty());
    }
}