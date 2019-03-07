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
@RequestMapping("/api/player/")
public class PlayerController {

	@Autowired
	private PlayerService playerService;

	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public Message handleException(Exception e) {
		String message = e.getMessage();
		if (message == null) {
			message = "Error in request - please login as PLAYER";
		}
		return new Message(message);
	}

	private PlayerService getPlayerServiceFromSession(HttpSession session) throws SportsException {
		playerService = (PlayerService) session.getAttribute("player");
		return playerService;
	}

	@RequestMapping(method = RequestMethod.GET, path = "login/{userName}/{password}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ClientDTO playerControllerLogin(@PathVariable("userName") String userName,
			@PathVariable("password") String password, HttpSession session) throws SportsException {
		playerService.playerLogin(ClientType.PLAYER, userName, password);
		if (playerService.playerLogin(ClientType.PLAYER, userName, password) != null && userName != null
				&& !userName.trim().isEmpty() && password != null && !password.trim().isEmpty()) {
			session.setAttribute("player", playerService);
			String SessionId = session.getId();
			System.err.println("Player Session ID: " + SessionId);
			return new ClientDTO(ClientType.PLAYER, userName, password);
		}
		throw new SportsException("Player Login Failed");
	}

	@RequestMapping(method = RequestMethod.GET, path = "logout")
	public void playerLogout(HttpSession session) {
		session.invalidate();
	}

	@RequestMapping(method = RequestMethod.POST, path = "placebet", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Bet placeBet(@RequestBody Bet betToPlace, HttpSession session) throws SportsException {
		Bet betPlaced = getPlayerServiceFromSession(session).placeBet(betToPlace);
		return betPlaced;
	}

	@RequestMapping(method = RequestMethod.GET, path = "getavailablebets", produces = MediaType.APPLICATION_JSON_VALUE)
	public Bet[] getAllAvailableBets(HttpSession session) throws SportsException {
		Bet[] availableBets = getPlayerServiceFromSession(session).getAllAvailableBets().toArray(new Bet[0]);
		return availableBets;
	}

	@RequestMapping(method = RequestMethod.GET, path = "getplayerbets", produces = MediaType.APPLICATION_JSON_VALUE)
	public Bet[] getMyPlayerBets(HttpSession session) throws SportsException {
		Bet[] myPlayerBets = getPlayerServiceFromSession(session).getMyPlayerBets().toArray(new Bet[0]);
		return myPlayerBets;
	}

	@RequestMapping(method = RequestMethod.GET, path = "getplayerbetsbysport/{sport}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Bet[] getMyPlayerBetsBySport(@PathVariable("sport") Sport sport, HttpSession session)
			throws SportsException {
		Bet[] myPlayerBetsBySport = getPlayerServiceFromSession(session).getMyPlayerBetsBySport(sport)
				.toArray(new Bet[0]);
		return myPlayerBetsBySport;
	}

	@RequestMapping(method = RequestMethod.GET, path = "getplayerbetsmorethanwager/{wager}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Bet[] getMyPlayerBetsMoreThenWager(@PathVariable("wager") double betWager, HttpSession session)
			throws SportsException {
		Bet[] myPlayerBetsMoreThenWager = getPlayerServiceFromSession(session).getMyPlayerBetsMoreThenWager(betWager)
				.toArray(new Bet[0]);
		return myPlayerBetsMoreThenWager;
	}

	@RequestMapping(method = RequestMethod.GET, path = "getavailablebetsbysport/{sport}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Bet[] getAllAvailableBetsBySport(@PathVariable("sport") Sport sport, HttpSession session)
			throws SportsException {
		Bet[] availableBetsBySport = getPlayerServiceFromSession(session).getAllAvailableBetsBySport(sport)
				.toArray(new Bet[0]);
		return availableBetsBySport;
	}

	@RequestMapping(method = RequestMethod.GET, path = "getavailablebetsmorethanwager/{wager}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Bet[] getAllAvailableBetsMoreThenWager(@PathVariable("wager") double betWager, HttpSession session)
			throws SportsException {
		Bet[] availableBetsMoreThenWager = getPlayerServiceFromSession(session)
				.getAllAvailableBetsMoreThenWager(betWager).toArray(new Bet[0]);
		return availableBetsMoreThenWager;
	}
}
