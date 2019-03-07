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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "BOOKIES")
public class Bookie {

	private Long bookieId;
	private String bookieUserName;
	private String bookiePassword;
	private String bookieEmail;

	private Collection<Bet> bookieBets;

	public Bookie() {
		super();
	}

	public Bookie(String bookieUserName, String bookiePassword, String bookieEmail) {
		super();
		this.bookieUserName = bookieUserName;
		this.bookiePassword = bookiePassword;
		this.bookieEmail = bookieEmail;
	}

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "BOOKIE_BETS", joinColumns = @JoinColumn(name = "BOOKIE_ID"), inverseJoinColumns = @JoinColumn(name = "BET_ID"))
	public Collection<Bet> getBookieBets() {
		return bookieBets;
	}

	public void setBookieBets(Collection<Bet> bookieBets) {
		this.bookieBets = bookieBets;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BOOKIE_ID")
	public Long getBookieId() {
		return bookieId;
	}

	public void setBookieId(Long bookieId) {
		this.bookieId = bookieId;
	}

	@Column(name = "BOOKIE_USERNAME", nullable = false, unique = true)
	public String getBookieUserName() {
		return bookieUserName;
	}

	public void setBookieUserName(String bookieUserName) {
		this.bookieUserName = bookieUserName;
	}

	@Column(name = "BOOKIE_PASSWORD", nullable = false)
	public String getBookiePassword() {
		return bookiePassword;
	}

	public void setBookiePassword(String bookiePassword) {
		this.bookiePassword = bookiePassword;
	}

	public String getBookieEmail() {
		return bookieEmail;
	}

	@Column(name = "BOOKIE_EMAIL", nullable = false, unique = true)
	public void setBookieEmail(String bookieEmail) {
		this.bookieEmail = bookieEmail;
	}

	@Override
	public String toString() {
		return "Bookie [bookieId=" + bookieId + ", bookieUserName=" + bookieUserName + ", bookiePassword="
				+ bookiePassword + ", bookieEmail=" + bookieEmail + "]";
	}

}
