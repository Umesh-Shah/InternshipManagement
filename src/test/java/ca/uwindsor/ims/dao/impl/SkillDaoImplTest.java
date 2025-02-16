package ca.uwindsor.ims.dao.impl;

import ca.uwindsor.ims.dao.SkillDao;
import ca.uwindsor.ims.model.SkillBo;
import ca.uwindsor.ims.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SkillDaoImplTest extends BaseTest {
    
    @Autowired
    private SkillDao skillDao;
    
    @Test
    void getskilllist_ShouldReturnAllSkills() throws Exception {
        // When
        List<SkillBo> skills = skillDao.getskilllist();
        
        // Then
        assertThat(skills).isNotNull()
                         .hasSize(3)
                         .extracting(SkillBo::skillName)
                         .containsExactlyInAnyOrder(
                             "Java Programming",
                             "Python",
                             "Project Management"
                         );
    }
    
    @Test
    void saveSkill_ShouldPersistNewSkill() throws Exception {
        // Given
        SkillBo newSkill = SkillBo.create("JavaScript", "Technical");
        
        // When
        skillDao.saveSkill(newSkill);
        List<SkillBo> skills = skillDao.getskilllist();
        
        // Then
        assertThat(skills).isNotNull()
                         .hasSize(4)
                         .extracting(SkillBo::skillName)
                         .contains("JavaScript");
    }
} 