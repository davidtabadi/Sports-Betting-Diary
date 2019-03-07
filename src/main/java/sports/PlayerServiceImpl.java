package sports;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerServiceImpl implements PlayerService {

	@Autowired
	private BetRepository betRepository;

	@Autowired
	private PlayerRepository playerRepository;

	private Player player;

	@Transactional
	@Override
	public Bet placeBet(Bet betToPlace) throws SportsException {
		Collection<Bet> allAvailableBets = getAllAvailableBets();
		Bet betFound = betRepository.findByBetTitle(betToPlace.getBetTitle());
		Collection<Bet> playerBets = getMyPlayerBets();
		for (Bet bet : allAvailableBets) {
			if (bet.equals(betFound)) {
				betRepository.save(betToPlace);
				playerBets.add(betToPlace);
				this.player.setPlayerBets(playerBets);
				playerRepository.save(this.player);
			}
		}
		return betToPlace;
	}

	@Transactional
	@Override
	public Collection<Bet> getMyPlayerBets() throws SportsException {
		Collection<Bet> playerBets = betRepository.findPlayerBets(this.player.getPlayerId());
		System.err.println("PLAYER BETS: " + playerBets);
		return playerBets;
	}

	@Transactional
	@Override
	public PlayerService playerLogin(ClientType clientType, String userName, String password) throws SportsException {
		player = playerRepository.findPlayerByPlayerUserNameAndPlayerPassword(userName, password);
		if (player == null) {
			throw new SportsException("Player Login Failed");
		}
		playerRepository.save(player);
		System.err.println("Player LogedIn is: " + player.toString());
		return this;
	}

	@Transactional
	@Override
	public Collection<Bet> getAllAvailableBets() throws SportsException {
		Collection<Bet> allBets = betRepository.findAll();
		Collection<Bet> myPlayerBets = getMyPlayerBets();
		Collection<Bet> availableBets = new ArrayList<Bet>();
		int check;
		if (myPlayerBets == null) {
			return allBets;
		} else {
			for (Bet betInAllBets : allBets) {
				check = 0;
				for (Bet betInMyBets : myPlayerBets) {
					if (betInMyBets.getBetId() == betInAllBets.getBetId()) {
						check = 1;
					}
				}
				if (check == 0) {
					availableBets.add(betInAllBets);
				}
			}
		}
		System.err.println("AVAILABLE BETS: " + availableBets);
		return availableBets;
	}

	@Transactional
	@Override
	public Collection<Bet> getMyPlayerBetsBySport(Sport sport) throws SportsException {
		Collection<Bet> playerBets = getMyPlayerBets();
		Collection<Bet> playerBetsBySport = new ArrayList<Bet>();
		for (Bet bet : playerBets) {
			if (bet.getSport().equals(sport)) {
				playerBetsBySport.add(bet);
			}
		}
		return playerBetsBySport;
	}

	@Transactional
	@Override
	public Collection<Bet> getMyPlayerBetsMoreThenWager(double betWager) throws SportsException {
		Collection<Bet> playerBets = getMyPlayerBets();
		Collection<Bet> playerBetsMoreThenWager = new ArrayList<Bet>();
		for (Bet bet : playerBets) {
			if (bet.getBetWager() >= betWager) {
				playerBetsMoreThenWager.add(bet);
			}
		}
		return playerBetsMoreThenWager;
	}

	@Transactional
	@Override
	public Collection<Bet> getAllAvailableBetsBySport(Sport sport) throws SportsException {
		Collection<Bet> availableBets = getAllAvailableBets();
		Collection<Bet> availableBetsBySport = new ArrayList<Bet>();
		for (Bet bet : availableBets) {
			if (bet.getSport().equals(sport)) {
				availableBetsBySport.add(bet);
			}
		}
		return availableBetsBySport;
	}

	@Transactional
	@Override
	public Collection<Bet> getAllAvailableBetsMoreThenWager(double betWager) throws SportsException {
		Collection<Bet> availableBets = getMyPlayerBets();
		Collection<Bet> availableBetsMoreThenWager = new ArrayList<Bet>();
		for (Bet bet : availableBets) {
			if (bet.getBetWager() >= betWager) {
				availableBetsMoreThenWager.add(bet);
			}
		}
		return availableBetsMoreThenWager;
	}

}
