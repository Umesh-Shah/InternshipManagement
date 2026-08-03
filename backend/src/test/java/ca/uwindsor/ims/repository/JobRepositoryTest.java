package ca.uwindsor.ims.repository;

import ca.uwindsor.ims.entity.Job;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class JobRepositoryTest {

    @Autowired JobRepository repo;
    @Autowired TestEntityManager em;

    @Test
    void save_thenFindByCompanyId_returnsJob() {
        Job j = new Job();
        j.setJobPosition("Software Developer");
        j.setCompanyId(10);
        Job saved = repo.save(j);
        em.flush();
        em.clear();

        List<Job> found = repo.findByCompanyId(10);

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getJobId()).isEqualTo(saved.getJobId());
        assertThat(found.get(0).getJobPosition()).isEqualTo("Software Developer");
    }

    @Test
    void findAll_returnsAll() {
        Job j1 = new Job(); j1.setJobPosition("Dev"); j1.setCompanyId(1);
        Job j2 = new Job(); j2.setJobPosition("QA");  j2.setCompanyId(2);
        repo.saveAll(List.of(j1, j2));
        em.flush();

        assertThat(repo.findAll()).hasSize(2)
                .extracting(Job::getJobPosition)
                .containsExactlyInAnyOrder("Dev", "QA");
    }

    @Test
    void deleteById_removesRecord() {
        Job j = new Job();
        j.setJobPosition("To Delete");
        j.setCompanyId(1);
        Job saved = repo.save(j);
        em.flush();

        repo.deleteById(saved.getJobId());
        em.flush();

        assertThat(repo.findById(saved.getJobId())).isEmpty();
    }
}
