package sk.sicak.totoro.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sk.sicak.totoro.model.Player;

@Repository
public interface PlayerDao extends CrudRepository<Player, Integer> {
}
