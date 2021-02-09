package sk.sicak.totoro.algorithm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.sicak.totoro.config.TournamentConfiguration;
import sk.sicak.totoro.model.Match;
import sk.sicak.totoro.model.Player;
import sk.sicak.totoro.util.TestHelper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TournamentCreatorTest {


    @InjectMocks
    private TournamentCreator tournamentCreator;

    @Mock
    private TournamentConfiguration config;

    @Test
    void generateMatches() {
        when(config.getMatchesPerPlayer()).thenReturn(3);
        List<Player> players = TestHelper.generatePlayers();
        //To beat the randomness
        for (int i = 0; i < 100; i++) {
            List<Match> result = tournamentCreator.generateMatches(players);
            assertEquals(9, result.size());

        }

    }


}