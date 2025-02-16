package ca.uwindsor.ims.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import ca.uwindsor.ims.config.TestConfig;
import ca.uwindsor.ims.dao.SkillDao;
import ca.uwindsor.ims.model.SkillBo;

@SpringBootTest(classes = TestConfig.class)
@ActiveProfiles("test")
@Transactional
class SkillDaoImplTest {

    @Autowired
    private SkillDao skillDao;

    private SkillBo testSkill;

    @BeforeEach
    void setUp() {
        testSkill = new SkillBo(
            null,
            "Test Skill",
            "Technical"
        );
    }

    @Test
    void saveSkill_ShouldSaveSkillSuccessfully() throws Exception {
        skillDao.saveSkill(testSkill);
        
        // Verify skill was saved
        var skills = skillDao.getskilllist();
        assertThat(skills).isNotNull()
                         .extracting(SkillBo::getSkillName)
                         .contains("Test Skill");
    }

    @Test
    void getskilllist_ShouldReturnAllSkills() throws Exception {
        // First save a skill
        skillDao.saveSkill(testSkill);
        
        // Then get all skills
        var skills = skillDao.getskilllist();
        
        assertThat(skills).isNotNull()
                         .extracting(SkillBo::getSkillName)
                         .contains("Test Skill");
    }
} 