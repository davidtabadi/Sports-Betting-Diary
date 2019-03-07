package sports;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private BookieRepository bookieRepository;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private BetRepository betRepository;

	@Transactional
	@Override
	public Bookie addBookie(Bookie bookieToAdd) throws SportsException {
		String bookieName = bookieToAdd.getBookieUserName();
		Bookie bookieFound = bookieRepository.findByBookieUserName(bookieName);
		if (bookieToAdd.getBookieId() != null || bookieFound != null) {
			throw new SportsException("The Bookie to add must be new Bookie");
		}
		Bookie newBookie = bookieRepository.save(bookieToAdd);
		// newBookie.setBookieBets(new ArrayList<Bet>());
		bookieRepository.save(newBookie);
		return newBookie;
	}

	@Transactional
	@Override
	public Bookie updateBookie(Long bookieId, Bookie bookieToUpdate) throws SportsException {
		Bookie originBookie = getBookie(bookieId);
		if (originBookie == null) {
			throw new SportsException("no Bookie with ID: " + bookieId);
		}
		bookieToUpdate.setBookieId(bookieId);
		Bookie updatedBookie = bookieRepository.save(bookieToUpdate);
		return updatedBookie;
	}

	@Transactional
	@Override
	public void removeBookie(Long bookieId, Bookie bookieToRemove) throws SportsException {
		Bookie originBookie = getBookie(bookieId);
		if (originBookie == null) {
			throw new SportsException("Bookie with ID: " + bookieId + "cannot removed !");
		}
		Collection<Bet> bookieBets = betRepository.findBookieBets(bookieId);
		bookieBets.clear();
		bookieRepository.deleteById(bookieId);
	}

	@Transactional
	@Override
	public Bookie getBookie(Long bookieId) throws SportsException {
		Bookie bookieFoundById = bookieRepository.findByBookieId(bookieId);
		return bookieFoundById;
	}

	@Transactional
	@Override
	public Collection<Bookie> getAllBookies() throws SportsException {
		Collection<Bookie> allBookies = bookieRepository.findAll();
		return allBookies;
	}

	@Transactional
	@Override
	public Player addPlayer(Player playerToAdd) throws SportsException {
		String playerName = playerToAdd.getPlayerUserName();
		Player playerFound = playerRepository.findByPlayerUserName(playerName);
		if (playerToAdd.getPlayerId() != null || playerFound != null) {
			throw new SportsException("The Player added must be new Player");
		}
		Player newPlayer = playerRepository.save(playerToAdd);
		// newPlayer.setPlayerBets(new ArrayList<Bet>());
		playerRepository.save(newPlayer);
		return newPlayer;

	}

	@Transactional
	@Override
	public Player updatePlayer(Long playerId, Player playerToUpdate) throws SportsException {
		Player originplayer = getPlayer(playerId);
		if (originplayer == null) {
			throw new SportsException("no Player with ID: " + playerId);
		}
		playerToUpdate.setPlayerId(playerId);
		Player updatedPlayer = playerRepository.save(playerToUpdate);
		return updatedPlayer;
	}

	@Transactional
	@Override
	public void removePlayer(Long playerId, Player playerToRemove) throws SportsException {
		Player originplayer = getPlayer(playerId);
		if (originplayer == null) {
			throw new SportsException("Player with ID: " + playerId + "cannot removed !");
		}
		Collection<Bet> playerBets = betRepository.findPlayerBets(playerId);
		playerBets.clear();
		playerRepository.deleteById(playerId);
	}

	@Transactional
	@Override
	public Player getPlayer(Long playerId) throws SportsException {
		Player playerFound = playerRepository.findByPlayerId(playerId);
		return playerFound;
	}

	@Transactional
	@Override
	public Collection<Player> getAllPlayers() throws SportsException {
		Collection<Player> allPlayers = playerRepository.findAll();
		return allPlayers;
	}

	@Transactional
	@Override
	public AdminService adminLogin(ClientType clientType, String userName, String password) throws SportsException {
		if (userName.equals("admin") && password.equals("1111")) {
			return this;
		}
		throw new SportsException("Admin Login Failed");
	}

}
