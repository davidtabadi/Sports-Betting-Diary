package sports;

import java.util.Collection;

public interface BookieService {

	public Bet addNewBet(Bet betToAdd) throws SportsException;

	public Bet updateBet(Long betId, Bet betToUpdate) throws SportsException;

	public void removeBet(Long betId, Bet betToRemove) throws SportsException;

	public Bet getBet(Long betId) throws SportsException;

	public Collection<Bet> getMyBookieBets() throws SportsException;

	public Collection<Bet> getMyBookieBetsBySport(Sport sport) throws SportsException;

	public Collection<Bet> getMyBookieBetsMoreThenWager(double betWager) throws SportsException;

	public BookieService bookieLogin(ClientType clentType, String userName, String password) throws SportsException;

}
