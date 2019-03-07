package sports;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookieServiceImpl implements BookieService {

	@Autowired
	private BookieRepository bookieRepository;

	@Autowired
	private BetRepository betRepository;

//	@Autowired
//	private PlayerRepository playerRepository;

	private Bookie bookie;

	@Transactional
	@Override
	public Bet addNewBet(Bet betToAdd) throws SportsException {
		Date today = new Date();
		String betTitle = betToAdd.getBetTitle();
		Bet betFound = betRepository.findByBetTitle(betTitle);
		if (betToAdd.getBetId() != null || betFound != null) {
			throw new SportsException("The Bet to add must be new Bet");
		}
		if (betToAdd.getActionDate().before(today)) {
			throw new SportsException("Event Date can't be earlier than today");
		}
		if (betToAdd.getBetOdds() < 1) {
			throw new SportsException("Odds must be greater than 1");
		}
		if (betToAdd.getBetWager() < 0) {
			throw new SportsException("Wager must be greater than 0");
		}
		Collection<Bet> bookieBets = getMyBookieBets();
		Bet newBet = betRepository.save(betToAdd);
		bookieBets.add(betToAdd);
		this.bookie.setBookieBets(bookieBets);
		bookieRepository.save(this.bookie);
		return newBet;
	}

	@Transactional
	@Override
	public Bet updateBet(Long betId, Bet betToUpdate) throws SportsException {
		Bet originBet = getBet(betId);
		if (originBet == null) {
			throw new SportsException("no Bet ID with ID: " + betId);
		}
		betToUpdate.setBetId(betId);
		Bet updatedBet = betRepository.save(betToUpdate);
		return updatedBet;
	}

	// we do not allow deleting Bets so when the Bookie decides to cancel the Bet
	// (instead of deleting from the system) it simply makes the Bet void so no
	// wager and no odds will be available on the Bet and it will be
	// cancelled (voided)
	@Transactional
	@Override
	public void removeBet(Long betId, Bet betToRemove) throws SportsException {
		Bet originBet = getBet(betId);
		if (originBet == null) {
			throw new SportsException("no Bet ID with ID: " + betId);
		}
		betToRemove.setBetId(betId);
		betToRemove.setBetWager(0);
		betToRemove.setBetOdds(1);
		System.err.println("BET :" + betToRemove.toString() + "CANCELED !!");
		betRepository.save(betToRemove);
	}

	@Transactional
	@Override
	public Bet getBet(Long betId) throws SportsException {
		Bet betFound = betRepository.findByBetId(betId);
		return betFound;
	}

	@Transactional
	@Override
	public Collection<Bet> getMyBookieBets() throws SportsException {
		Collection<Bet> bookieBets = betRepository.findBookieBets(this.bookie.getBookieId());
		System.err.println("BOOKIE BETS: " + bookieBets);
		return bookieBets;
	}

	@Transactional
	@Override
	public BookieService bookieLogin(ClientType clentType, String userName, String password) throws SportsException {
		bookie = bookieRepository.findBookieByBookieUserNameAndBookiePassword(userName, password);
		if (bookie == null) {
			throw new SportsException("Bookie Login Faild");
		}
		bookieRepository.save(bookie);
		System.err.println("Bookie LogedIn is: " + bookie.toString());
		return this;
	}

	@Transactional
	@Override
	public Collection<Bet> getMyBookieBetsBySport(Sport sport) throws SportsException {
		Collection<Bet> bookieBets = getMyBookieBets();
		Collection<Bet> bookieBetsBySport = new ArrayList<Bet>();
		for (Bet bet : bookieBets) {
			if (bet.getSport().equals(sport)) {
				bookieBetsBySport.add(bet);
			}
		}
		return bookieBetsBySport;
	}

	@Transactional
	@Override
	public Collection<Bet> getMyBookieBetsMoreThenWager(double betWager) throws SportsException {
		Collection<Bet> bookieBets = getMyBookieBets();
		Collection<Bet> bookieBetsMoreThenWager = new ArrayList<Bet>();
		for (Bet bet : bookieBets) {
			if (bet.getBetWager() >= betWager) {
				bookieBetsMoreThenWager.add(bet);
			}
		}
		return bookieBetsMoreThenWager;
	}

}
