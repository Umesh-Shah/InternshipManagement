package ca.uwindsor.ims.repository;

import ca.uwindsor.ims.entity.StudentInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class StudentInfoRepositoryTest {

    @Autowired StudentInfoRepository repo;
    @Autowired TestEntityManager em;

    @Test
    void save_thenFindByStudentId() {
        StudentInfo s = new StudentInfo();
        s.setStudentId(1001);
        s.setFname("Alice");
        s.setLname("Smith");
        repo.save(s);
        em.flush();
        em.clear();

        Optional<StudentInfo> found = repo.findByStudentId(1001);

        assertThat(found).isPresent();
        assertThat(found.get().getFname()).isEqualTo("Alice");
        assertThat(found.get().getLname()).isEqualTo("Smith");
    }

    @Test
    void findAll_returnsAll() {
        StudentInfo s1 = new StudentInfo(); s1.setStudentId(1001); s1.setFname("Alice");
        StudentInfo s2 = new StudentInfo(); s2.setStudentId(1002); s2.setFname("Bob");
        repo.saveAll(List.of(s1, s2));
        em.flush();

        assertThat(repo.findAll()).hasSize(2)
                .extracting(StudentInfo::getFname)
                .containsExactlyInAnyOrder("Alice", "Bob");
    }
}
