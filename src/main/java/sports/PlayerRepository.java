package sports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlayerRepository extends JpaRepository<Player, Long> {

	public Player findByPlayerId(Long playerId) throws SportsException;

	public Player findByPlayerUserName(String userName) throws SportsException;

	@Query(value = " SELECT p FROM Player p WHERE (p.playerUserName = ?1) AND (p.playerPassword = ?2)")
	public Player findPlayerByPlayerUserNameAndPlayerPassword(@Param("userName") String userName,
			@Param("password") String password) throws SportsException;

	// TODO query to find Player by betId
	// @Query(value = "SELECT p FROM Player p WHERE p.playerId IN (SELECT
	// p.playerBets FROM p.playerBets pb WHERE (pb.betId=?1) )")
	// public Player getPlayerFromPlayerBets(@Param("betId") Long betId) throws
	// SportsException;

	@Query(value = " SELECT p FROM Player p WHERE p.playerId IN (SELECT p.playerId FROM p.playerBets pb WHERE pb.betId=?1)  ")
	public Player getPlayerIdByBetId(@Param("betId") Long betId) throws SportsException;

}
