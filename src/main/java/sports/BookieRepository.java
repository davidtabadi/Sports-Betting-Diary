package sports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookieRepository extends JpaRepository<Bookie, Long> {

	public Bookie findByBookieId(Long bookieId) throws SportsException;

	public Bookie findByBookieUserName(String userName) throws SportsException;

	@Query(value = "SELECT b FROM Bookie b WHERE (b.bookieUserName = ?1) AND (b.bookiePassword = ?2)")
	public Bookie findBookieByBookieUserNameAndBookiePassword(@Param("userName") String userName,
			@Param("password") String password) throws SportsException;
}
