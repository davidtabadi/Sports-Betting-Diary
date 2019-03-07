package sports;

import java.util.Collection;

public interface PlayerService {

	public Bet placeBet(Bet betToPlace) throws SportsException;

	public Collection<Bet> getAllAvailableBets() throws SportsException;

	public Collection<Bet> getAllAvailableBetsBySport(Sport sport) throws SportsException;

	public Collection<Bet> getAllAvailableBetsMoreThenWager(double betWager) throws SportsException;

	public Collection<Bet> getMyPlayerBets() throws SportsException;

	public Collection<Bet> getMyPlayerBetsBySport(Sport sport) throws SportsException;

	public Collection<Bet> getMyPlayerBetsMoreThenWager(double betWager) throws SportsException;

	public PlayerService playerLogin(ClientType clientType, String userName, String password) throws SportsException;

}
