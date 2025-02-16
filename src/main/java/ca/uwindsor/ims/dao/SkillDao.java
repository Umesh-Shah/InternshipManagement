package ca.uwindsor.ims.dao;

import java.util.List;
import org.springframework.stereotype.Component;
import ca.uwindsor.ims.model.SkillBo;
import ca.uwindsor.ims.model.StudentJobSkillbo;

@Component
public interface SkillDao {
    List<SkillBo> getskilllist() throws Exception;
    List<StudentJobSkillbo> getstudentfromjjob(String job_id) throws Exception;
    void savestudent_skill(int student_id, String[] shah);
    SkillBo saveSkill(SkillBo skill);
} 