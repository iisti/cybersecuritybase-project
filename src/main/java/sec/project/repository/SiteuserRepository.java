package sec.project.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import sec.project.domain.Siteuser;

/**
 *
 * @author iisti
 */
public interface SiteuserRepository extends JpaRepository<Siteuser, Long> {
    Siteuser findByUsername(String username);
}
