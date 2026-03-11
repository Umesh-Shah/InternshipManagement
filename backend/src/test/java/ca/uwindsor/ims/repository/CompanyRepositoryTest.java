package ca.uwindsor.ims.repository;

import ca.uwindsor.ims.entity.Company;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class CompanyRepositoryTest {

    @Autowired CompanyRepository repo;
    @Autowired TestEntityManager em;

    @Test
    void save_thenFindById_returnsCompany() {
        Company c = new Company();
        c.setCompanyName("Acme Corp");
        c.setCity("Windsor");
        c.setCountry("Canada");
        Company saved = repo.save(c);
        em.flush();
        em.clear();

        Optional<Company> found = repo.findById(saved.getCompanyId());
        assertThat(found).isPresent();
        assertThat(found.get().getCompanyName()).isEqualTo("Acme Corp");
        assertThat(found.get().getCity()).isEqualTo("Windsor");
    }

    @Test
    void findAll_returnsAllSavedCompanies() {
        Company c1 = new Company(); c1.setCompanyName("Alpha Inc");
        Company c2 = new Company(); c2.setCompanyName("Beta Ltd");
        repo.saveAll(List.of(c1, c2));
        em.flush();

        assertThat(repo.findAll()).hasSize(2)
                .extracting(Company::getCompanyName)
                .containsExactlyInAnyOrder("Alpha Inc", "Beta Ltd");
    }

    @Test
    void delete_removesCompany() {
        Company c = new Company();
        c.setCompanyName("To Delete");
        Company saved = repo.save(c);
        em.flush();

        repo.deleteById(saved.getCompanyId());
        em.flush();

        assertThat(repo.findById(saved.getCompanyId())).isEmpty();
    }
}
