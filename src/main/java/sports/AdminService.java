package sports;

import java.util.Collection;

public interface AdminService {

	public Bookie addBookie(Bookie bookieToAdd) throws SportsException;

	public Bookie updateBookie(Long bookieId, Bookie bookieToUpdate) throws SportsException;

	public void removeBookie(Long bookieId, Bookie bookieToRemove) throws SportsException;

	public Bookie getBookie(Long bookieId) throws SportsException;

	public Collection<Bookie> getAllBookies() throws SportsException;

	public Player addPlayer(Player playerToAdd) throws SportsException;

	public Player updatePlayer(Long playerId, Player playerToUpdate) throws SportsException;

	public void removePlayer(Long playerId, Player playerToRemove) throws SportsException;

	public Player getPlayer(Long playerId) throws SportsException;

	public Collection<Player> getAllPlayers() throws SportsException;

	public AdminService adminLogin(ClientType clientType, String userName, String password) throws SportsException;

}
