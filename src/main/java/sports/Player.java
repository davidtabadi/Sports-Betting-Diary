package sports;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "PLAYERS")
public class Player {

	private Long playerId;
	private String playerUserName;
	private String playerPassword;
	private String playerEmail;

	private Collection<Bet> playerBets;

	public Player() {
		super();
	}

	public Player(String playerUserName, String playerPassword, String playerEmail) {
		super();
		this.playerUserName = playerUserName;
		this.playerPassword = playerPassword;
		this.playerEmail = playerEmail;
	}

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "PLAYER_BETS", joinColumns = @JoinColumn(name = "PLAYER_ID"), inverseJoinColumns = @JoinColumn(name = "BET_ID"))
	public Collection<Bet> getPlayerBets() {
		return playerBets;
	}

	public void setPlayerBets(Collection<Bet> playerBets) {
		this.playerBets = playerBets;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PLAYER_ID")
	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	@Column(name = "PLAYER_USER_NAME", nullable = false, unique = true)
	public String getPlayerUserName() {
		return playerUserName;
	}

	public void setPlayerUserName(String playerUserName) {
		this.playerUserName = playerUserName;
	}

	@Column(name = "PLAYER_PASSWORD", nullable = false)
	public String getPlayerPassword() {
		return playerPassword;
	}

	public void setPlayerPassword(String playerPassword) {
		this.playerPassword = playerPassword;
	}

	@Column(name = "PLAYER_EMAIL", nullable = false, unique = true)
	public String getPlayerEmail() {
		return playerEmail;
	}

	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}

	@Override
	public String toString() {
		return "Player [playerId=" + playerId + ", playerUserName=" + playerUserName + ", playerPassword="
				+ playerPassword + ", playerEmail=" + playerEmail + "]";
	}

}
