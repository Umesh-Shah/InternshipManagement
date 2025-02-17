package ca.uwindsor.ims.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StudentSkillBoTest extends BaseTest {

    @Test
    void testDefaultConstructor() {
        StudentSkillBo studentSkill = new StudentSkillBo();
        assertNotNull(studentSkill);
        assertNull(studentSkill.getStudentSkillId());
        assertNull(studentSkill.getStudentId());
        assertNull(studentSkill.getSkillId());
        assertNull(studentSkill.getSkillLevel());
    }

    @Test
    void testParameterizedConstructor() {
        StudentSkillBo studentSkill = new StudentSkillBo(1, 2, "Intermediate");
        assertNotNull(studentSkill);
        assertEquals(1, studentSkill.getStudentId());
        assertEquals(2, studentSkill.getSkillId());
        assertEquals("Intermediate", studentSkill.getSkillLevel());
    }

    @Test
    void testSettersAndGetters() {
        StudentSkillBo studentSkill = new StudentSkillBo();
        studentSkill.setStudentSkillId(1);
        studentSkill.setStudentId(2);
        studentSkill.setSkillId(3);
        studentSkill.setSkillLevel("Advanced");
        
        assertEquals(1, studentSkill.getStudentSkillId());
        assertEquals(2, studentSkill.getStudentId());
        assertEquals(3, studentSkill.getSkillId());
        assertEquals("Advanced", studentSkill.getSkillLevel());
    }

    @Test
    void testCreateMethod() {
        StudentSkillBo studentSkill = StudentSkillBo.create(1, 2, "Beginner");
        assertNotNull(studentSkill);
        assertEquals(1, studentSkill.getStudentId());
        assertEquals(2, studentSkill.getSkillId());
        assertEquals("Beginner", studentSkill.getSkillLevel());
    }
}
