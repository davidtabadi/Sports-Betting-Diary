package sports;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "BETS")
public class Bet {

	private Long betId;
	private Date actionDate;
	private Sport sport;
	private String betTitle;
	private double betWager;
	private double betOdds;
	private String betImage;
	private String betReceipt;

	private Bookie bookie;
	private Collection<Player> players;

	public Bet(Date actionDate, Sport sport, String betTitle, double betWager, double betOdds, String betImage,
			String betReceipt) {
		super();
		this.actionDate = actionDate;
		this.sport = sport;
		this.betTitle = betTitle;
		this.betWager = betWager;
		this.betOdds = betOdds;
		this.betImage = betImage;
		this.betReceipt = betReceipt;
	}

	public Bet() {
		super();
	}

	@JsonIgnore
	@ManyToOne
	public Bookie getBookie() {
		return bookie;
	}

	public void setBookie(Bookie bookie) {
		this.bookie = bookie;
	}

	@JsonIgnore
	@ManyToMany(mappedBy = "playerBets")
	public Collection<Player> getPlayers() {
		return players;
	}

	public void setPlayers(Collection<Player> players) {
		this.players = players;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BET_ID")
	public Long getBetId() {
		return betId;
	}

	public void setBetId(Long betId) {
		this.betId = betId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ACTION_DATE", nullable = false)
	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "BET_SPORT", nullable = false)
	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}

	@Column(name = "BET_TITLE", unique = true, nullable = false)
	public String getBetTitle() {
		return betTitle;
	}

	public void setBetTitle(String betTitle) {
		this.betTitle = betTitle;
	}

	@Column(name = "BET_WAGER", nullable = false)
	public double getBetWager() {
		return betWager;
	}

	public void setBetWager(double betWager) {
		this.betWager = betWager;
	}

	@Column(name = "BET_ODDS", nullable = false)
	public double getBetOdds() {
		return betOdds;
	}

	public void setBetOdds(double betOdds) {
		this.betOdds = betOdds;
	}

	@Column(name = "BET_RECEIPT", nullable = false)
	public String getBetReceipt() {
		return betReceipt;
	}

	public void setBetReceipt(String betReceipt) {
		this.betReceipt = betReceipt;
	}

	@Column(name = "BET_IMAGE", nullable = false)
	public String getBetImage() {
		return betImage;
	}

	public void setBetImage(String betImage) {
		this.betImage = betImage;
	}

	@Override
	public String toString() {
		return "Bet [betId=" + betId + ", actionDate=" + actionDate + ", sport=" + sport + ", betTitle=" + betTitle
				+ ", betWager=" + betWager + ", betOdds=" + betOdds + ", betImage=" + betImage + ", betReceipt="
				+ betReceipt + "]";
	}

}
