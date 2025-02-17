package ca.uwindsor.ims.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SkillBoTest extends BaseTest {

    @Test
    void testDefaultConstructor() {
        SkillBo skill = new SkillBo();
        assertNotNull(skill);
        assertNull(skill.getSkillId());
        assertNull(skill.getSkillName());
        assertNull(skill.getSkillType());
    }

    @Test
    void testParameterizedConstructor() {
        SkillBo skill = new SkillBo("Java", "Technical");
        assertNotNull(skill);
        assertEquals("Java", skill.getSkillName());
        assertEquals("Technical", skill.getSkillType());
    }

    @Test
    void testSettersAndGetters() {
        SkillBo skill = new SkillBo();
        skill.setSkillId(1);
        skill.setSkillName("Python");
        skill.setSkillType("Programming");
        
        assertEquals(1, skill.getSkillId());
        assertEquals("Python", skill.getSkillName());
        assertEquals("Programming", skill.getSkillType());
    }

    @Test
    void testCreateMethod() {
        SkillBo skill = SkillBo.create("React", "Frontend");
        assertNotNull(skill);
        assertEquals("React", skill.getSkillName());
        assertEquals("Frontend", skill.getSkillType());
    }
}
