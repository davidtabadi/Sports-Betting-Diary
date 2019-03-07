package sports;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookie/")
public class BookieController {

	@Autowired
	private BookieService bookieService;

	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public Message handleException(Exception e) {
		String message = e.getMessage();
		if (message == null) {
			message = "Error in request - please login as BOOKIE";
		}
		return new Message(message);
	}

	private BookieService getBookieServiceFromSession(HttpSession session) {
		bookieService = (BookieService) session.getAttribute("bookie");
		return bookieService;
	}

	@RequestMapping(method = RequestMethod.GET, path = "login/{userName}/{password}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ClientDTO bookieControllerLogin(@PathVariable("userName") String userName,
			@PathVariable("password") String password, HttpSession session) throws SportsException {
		bookieService.bookieLogin(ClientType.BOOKIE, userName, password);
		if (bookieService.bookieLogin(ClientType.BOOKIE, userName, password) != null && userName != null
				&& !userName.trim().isEmpty() && password != null && !password.trim().isEmpty()) {
			session.setAttribute("bookie", bookieService);
			String sessionId = session.getId();
			System.err.println("Bookie Session ID: " + sessionId);
			return new ClientDTO(ClientType.BOOKIE, userName, password);
		}
		throw new SportsException("Bookie Login Failed");
	}

	@RequestMapping(method = RequestMethod.GET, path = "logout")
	public void bookieLogout(HttpSession session) {
		session.invalidate();
	}

	@RequestMapping(method = RequestMethod.POST, path = "addbet", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Bet addNewBet(@RequestBody Bet betToAdd, HttpSession session) throws SportsException {
		Bet newBet = getBookieServiceFromSession(session).addNewBet(betToAdd);
		return newBet;
	}

	@RequestMapping(method = RequestMethod.PUT, path = "updatebet/{betId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Bet updateBet(@PathVariable("betId") Long betId, @RequestBody Bet betToUpdate, HttpSession session)
			throws SportsException {
		Bet updatedBet = getBookieServiceFromSession(session).updateBet(betId, betToUpdate);
		return updatedBet;
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "removebet/{betId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void removeBet(@PathVariable("betId") Long betId, @RequestBody Bet betToRemove, HttpSession session)
			throws SportsException {
		getBookieServiceFromSession(session).removeBet(betId, betToRemove);
	}

	@RequestMapping(method = RequestMethod.GET, path = "getbet/{betId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Bet getBet(@PathVariable Long betId, HttpSession session) throws SportsException {
		Bet betFound = getBookieServiceFromSession(session).getBet(betId);
		return betFound;
	}

	@RequestMapping(method = RequestMethod.GET, path = "getbookiebets", produces = MediaType.APPLICATION_JSON_VALUE)
	public Bet[] getMyBookieBets(HttpSession session) throws SportsException {
		Bet[] myBookieBets = getBookieServiceFromSession(session).getMyBookieBets().toArray(new Bet[0]);
		return myBookieBets;
	}

	@RequestMapping(method = RequestMethod.GET, path = "getbookiebetsbysport/{sport}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Bet[] getMyBookieBetsBySport(@PathVariable("sport") Sport sport, HttpSession session)
			throws SportsException {
		Bet[] myBookieBetsBySport = getBookieServiceFromSession(session).getMyBookieBetsBySport(sport)
				.toArray(new Bet[0]);
		return myBookieBetsBySport;

	}

	@RequestMapping(method = RequestMethod.GET, path = "getbookiebetsmorethanwager/{wager}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Bet[] getMyBookieBetsMoreThenWager(@PathVariable("wager") double betWager, HttpSession session)
			throws SportsException {
		Bet[] myBookieBetsMoreThenWager = getBookieServiceFromSession(session).getMyBookieBetsMoreThenWager(betWager)
				.toArray(new Bet[0]);
		return myBookieBetsMoreThenWager;
	}

}
