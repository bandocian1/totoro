package sk.sicak.totoro.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sk.sicak.totoro.model.Match;


@Repository
public interface MatchDao extends CrudRepository<Match, Integer> {

    @Modifying
    @Query("UPDATE Match m set m.result = :result where m.matchId = :matchId")
    int setMatchResult(@Param("matchId") int matchId, @Param("result") String result);

}
