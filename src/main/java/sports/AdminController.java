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
@RequestMapping("/api/admin/")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public Message handleException(Exception e) {
		String message = e.getMessage();
		if (message == null) {
			message = "Error in request - please login as ADMIN";
		}
		return new Message(message);
	}

	private AdminService getAdminServiceFromSession(HttpSession session) {
		adminService = (AdminService) session.getAttribute("admin");
		return adminService;
	}

	@RequestMapping(method = RequestMethod.GET, path = "login/{userName}/{password}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ClientDTO adminControllerLogin(@PathVariable("userName") String userName,
			@PathVariable("password") String password, HttpSession session) throws SportsException {
		adminService.adminLogin(ClientType.ADMIN, userName, password);
		if (adminService.adminLogin(ClientType.ADMIN, userName, password) != null && userName != null
				&& !userName.trim().isEmpty() && password != null && !password.trim().isEmpty()) {
			session.setAttribute("admin", adminService);
			String sessionId = session.getId();
			System.err.println("Admin Session ID: " + sessionId);
			return new ClientDTO(ClientType.ADMIN, userName, password);
		}
		throw new SportsException("Admin Login Failed");
	}

	@RequestMapping(method = RequestMethod.GET, path = "logout")
	public void adminLogout(HttpSession session) {
		session.invalidate();
	}

	@RequestMapping(method = RequestMethod.POST, path = "bookies", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Bookie addBookie(@RequestBody Bookie bookieToAdd, HttpSession session) throws SportsException {
		Bookie newBookie = getAdminServiceFromSession(session).addBookie(bookieToAdd);
		return newBookie;
	}

	@RequestMapping(method = RequestMethod.PUT, path = "bookies/{bookieId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Bookie updateBookie(@PathVariable("bookieId") Long bookieId, @RequestBody Bookie bookieToUpdate,
			HttpSession session) throws SportsException {
		Bookie updatedBookie = getAdminServiceFromSession(session).updateBookie(bookieId, bookieToUpdate);
		return updatedBookie;
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "bookies/{bookieId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void removeBookie(@PathVariable("bookieId") Long bookieId, @RequestBody Bookie bookieToRemove,
			HttpSession session) throws SportsException {
		getAdminServiceFromSession(session).removeBookie(bookieId, bookieToRemove);
	}

	@RequestMapping(method = RequestMethod.GET, path = "bookies/{bookieId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Bookie getBookie(@PathVariable("bookieId") Long bookieId, HttpSession session) throws SportsException {
		Bookie bookieFound = getAdminServiceFromSession(session).getBookie(bookieId);
		return bookieFound;
	}

	@RequestMapping(method = RequestMethod.GET, path = "bookies", produces = MediaType.APPLICATION_JSON_VALUE)
	public Bookie[] getAllBookies(HttpSession session) throws SportsException {
		Bookie[] allBookies = getAdminServiceFromSession(session).getAllBookies().toArray(new Bookie[0]);
		return allBookies;
	}

	@RequestMapping(method = RequestMethod.POST, path = "players", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Player addPlayer(@RequestBody Player playerToAdd, HttpSession session) throws SportsException {
		Player newPlayer = getAdminServiceFromSession(session).addPlayer(playerToAdd);
		return newPlayer;

	}

	@RequestMapping(method = RequestMethod.PUT, path = "players/{playerId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Player updatePlayer(@PathVariable("playerId") Long playerId, @RequestBody Player playerToUpdate,
			HttpSession session) throws SportsException {
		Player updatedPlayer = getAdminServiceFromSession(session).updatePlayer(playerId, playerToUpdate);
		return updatedPlayer;
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "players/{playerId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void removePlayer(@PathVariable("playerId") Long playerId, @RequestBody Player playerToRemove,
			HttpSession session) throws SportsException {
		getAdminServiceFromSession(session).removePlayer(playerId, playerToRemove);
	}

	@RequestMapping(method = RequestMethod.GET, path = "players/{playerId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Player getPlayer(@PathVariable("playerId") Long playerId, HttpSession session) throws SportsException {
		Player playerFound = getAdminServiceFromSession(session).getPlayer(playerId);
		return playerFound;
	}

	@RequestMapping(method = RequestMethod.GET, path = "players", produces = MediaType.APPLICATION_JSON_VALUE)
	public Player[] getAllPlayers(HttpSession session) throws SportsException {
		Player[] allPlayers = getAdminServiceFromSession(session).getAllPlayers().toArray(new Player[0]);
		return allPlayers;
	}

}
