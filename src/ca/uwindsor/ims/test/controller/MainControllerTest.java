package ca.uwindsor.ims.test.controller;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import org.springframework.validation.support.BindingAwareModelMap;

import ca.uwindsor.ims.controller.MainController;
import ca.uwindsor.ims.dao.impl.StudentDao;
import ca.uwindsor.ims.model.StudentInfoBo;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {TestContext.class})
@ContextConfiguration(locations={"classpath:portal-dispatcher-servlet.xml"})
public class MainControllerTest {

	private MainController controller;
    
    private StudentDao studentDaoMock;

    @Before
    public void setUp() {
        controller = new MainController();

         studentDaoMock = mock(StudentDao.class);
         ReflectionTestUtils.setField(controller, "service", studentDaoMock);
    }
    
    @Test
    public void findList() throws Exception{
    	
    	BindingAwareModelMap model = new BindingAwareModelMap();
    	HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
    	
    	List<StudentInfoBo> info = new ArrayList<StudentInfoBo>();
    	
    	when(studentDaoMock.getallstudentdata()).thenReturn(info);
    	
    	String view = controller.loadinternship_status(mockedRequest, model);
    	
    	verify(studentDaoMock, times(1)).getallstudentdata();
    	
    	verifyNoMoreInteractions(studentDaoMock);
    	
    	assertEquals(MainController.VIEW_ADMIN_MAIN, view);
    	
    	assertEquals(info, model.asMap().get(MainController.MODEL_ATTRIBUTE_LIST));	
    }
}
