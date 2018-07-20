package ca.uwindsor.ims.test.model;

import org.junit.Test;

import ca.uwindsor.ims.model.SkillBo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;



public class SkillTest {

	private static final int SKILL_ID = 1;
	private static final int SKILL_ID_UPDATED = 2;
    private static final String SKILL_NAME = "Foo";
    private static final String SKILL_NAME_UPDATED = "Foo1";
    private static final String SKILL_TYPE = "Bar";
    private static final String SKILL_TYPE_UPDATED = "Bar1";

    @Test
    public void build() {
        SkillBo built = SkillBo.getBuilder(SKILL_NAME, SKILL_TYPE, SKILL_ID).build();
        
        assertEquals(SKILL_NAME, built.getSkill_name());
        assertEquals(SKILL_TYPE, built.getSkill_type());
        assertEquals(SKILL_ID, built.getSkill_id());
        
        assertNotNull(built.getSkill_id());

    }
    
	@Test
    public void getSkillSet() {
        SkillBo built = SkillBo.getBuilder(SKILL_NAME, SKILL_TYPE, SKILL_ID).build();
        
        String expectedSet = constructSkillSet(SKILL_NAME, SKILL_TYPE);
        assertEquals(expectedSet, built.getSet());
    }
    
    private String constructSkillSet(String skillName, String skillType) {
        
    	StringBuilder name = new StringBuilder();
        
        name.append(skillName);
        name.append(" ");
        name.append(skillType);
        
        return name.toString();
    }
    
    
    @Test
    public void update() {
        SkillBo built = SkillBo.getBuilder(SKILL_NAME, SKILL_TYPE, SKILL_ID).build();
        built.update(SKILL_NAME_UPDATED, SKILL_TYPE_UPDATED, SKILL_ID_UPDATED);
        assertEquals(SKILL_ID_UPDATED, built.getSkill_id());
        assertEquals(SKILL_NAME_UPDATED, built.getSkill_name());
        assertEquals(SKILL_TYPE_UPDATED, built.getSkill_type());
    }
}
