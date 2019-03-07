package sports;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BetRepository extends JpaRepository<Bet, Long> {

	public Bet findByBetId(Long betId) throws SportsException;

	public Bet findByBetTitle(String betTitle) throws SportsException;

	// Join Query for getting Player Bets
	// Relationship between Player and playerBets is Many to Many == simple query
	@Query(value = "SELECT b FROM Bet b WHERE b.betId IN (SELECT b.betId FROM b.players p WHERE p.playerId=?1)")
	public Collection<Bet> findPlayerBets(@Param("playerId") Long playerId) throws SportsException;

	// Join Query for getting Bookie Bets
	// Relationship between Bookie and bookieBets is One to Many == different query
	@Query(value = "SELECT b FROM Bet b WHERE b.betId IN (SELECT b.betId FROM Bookie bk WHERE bk.bookieId=?1)")
	public Collection<Bet> findBookieBets(@Param("bookieId") Long bookieId) throws SportsException;

	@Query(value = "SELECT b FROM Bet b WHERE b.betId IN (SELECT b.betId FROM b.players pl WHERE pl.playerId=?1 AND b.betId = ?2)")
	public Bet getPlayerBetByBetId(@Param("playerId") Long playerId, @Param("betId") Long betId) throws SportsException;

	@Query(value = "select b from Bet b where b.betId = ?1 AND b.bookie = ?2")
	public Bet findBookieBetByBetId(@Param("betId") Long betId, @Param("bookie") Bookie bookie) throws SportsException;

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM Bet b WHERE b.betId = :betId")
	public void removeBet(@Param("betId") Long betId) throws SportsException;

	// @Transactional
	// @Modifying
	// @Query(value = "DELETE FROM PlayerBets pb WHERE pb.betId = :betId")
	// public void removeBetFromPlayerBets(Long betId) throws SportsException;

	// @Transactional
	// @Modifying
	// @Query(value = "DELETE FROM BookieBets bb WHERE bb.betId = :betId")
	// public void removeBetFromBookieBets(Long betId) throws SportsException;

	// @Transactional
	// @Modifying
	// @Query(value = "DELETE FROM Bet b WHERE b.betId = :betId")
	// public void removeBetFromBets(Long betId) throws SportsException;
}
