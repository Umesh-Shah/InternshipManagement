package ca.uwindsor.ims.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import ca.uwindsor.ims.model.LoginBo;
import ca.uwindsor.ims.model.VbctLoginBO;

@ExtendWith(MockitoExtension.class)
class RegLoginDaoImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<VbctLoginBO> criteriaQuery;

    @Mock
    private Root<VbctLoginBO> root;

    @Mock
    private TypedQuery<VbctLoginBO> typedQuery;

    @InjectMocks
    private RegLoginDaoImpl regLoginDao;

    @BeforeEach
    void setUp() {
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(VbctLoginBO.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(VbctLoginBO.class)).thenReturn(root);
    }

    @Test
    void changePassword_Success() {
        // Arrange
        String loginId = "testUser";
        String newPass = "newPassword123";
        var query = mock(jakarta.persistence.Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        // Act
        boolean result = regLoginDao.changePassword(loginId, newPass);

        // Assert
        assertTrue(result);
        verify(query).setParameter("loginId", loginId);
        verify(query).setParameter("newPass", newPass);
    }

    @Test
    void checkPassword_Success() {
        // Arrange
        String loginId = "testUser";
        String password = "password123";
        Path<Object> loginIdPath = mock(Path.class);
        Path<Object> passwordPath = mock(Path.class);
        
        when(root.get("login_id")).thenReturn(loginIdPath);
        when(root.get("login_password")).thenReturn(passwordPath);
        when(criteriaBuilder.equal(any(), any())).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.and(any())).thenReturn(mock(Predicate.class));
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(new VbctLoginBO()));

        // Act
        boolean result = regLoginDao.checkPassword(loginId, password);

        // Assert
        assertTrue(result);
        verify(criteriaQuery).select(root);
        verify(criteriaQuery).where(any(Predicate.class));
    }

    @Test
    void getEmployeeList_Success() {
        // Arrange
        String username = "testUser";
        String password = "password123";
        VbctLoginBO expectedUser = new VbctLoginBO();
        Path<Object> usernamePath = mock(Path.class);
        Path<Object> passwordPath = mock(Path.class);
        
        when(root.get("login_name")).thenReturn(usernamePath);
        when(root.get("login_password")).thenReturn(passwordPath);
        when(criteriaBuilder.equal(any(), any())).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.and(any())).thenReturn(mock(Predicate.class));
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(expectedUser));

        // Act
        Optional<VbctLoginBO> result = regLoginDao.getEmployeeList(username, password);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
        verify(criteriaQuery).select(root);
        verify(criteriaQuery).where(any(Predicate.class));
    }

    @Test
    void saveDataComon_Success() {
        // Arrange
        LoginBo entity = new LoginBo();
        doNothing().when(entityManager).persist(any());

        // Act
        LoginBo result = regLoginDao.saveDataComon(entity);

        // Assert
        assertNotNull(result);
        assertEquals(entity, result);
        verify(entityManager).persist(entity);
    }

    @Test
    void checkLogin_Success() {
        // Arrange
        String username = "testUser";
        String password = "password123";
        
        // Create new mocks for LoginBo specific query
        CriteriaQuery<LoginBo> loginCriteriaQuery = mock(CriteriaQuery.class);
        Root<LoginBo> loginRoot = mock(Root.class);
        TypedQuery<LoginBo> loginTypedQuery = mock(TypedQuery.class);
        Path<Object> usernamePath = mock(Path.class);
        Path<Object> passwordPath = mock(Path.class);
        
        when(criteriaBuilder.createQuery(LoginBo.class)).thenReturn(loginCriteriaQuery);
        when(loginCriteriaQuery.from(LoginBo.class)).thenReturn(loginRoot);
        when(loginRoot.get("username")).thenReturn(usernamePath);
        when(loginRoot.get("pwd")).thenReturn(passwordPath);
        when(criteriaBuilder.equal(any(), any())).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.and(any())).thenReturn(mock(Predicate.class));
        when(entityManager.createQuery(loginCriteriaQuery)).thenReturn(loginTypedQuery);
        when(loginTypedQuery.getResultList()).thenReturn(List.of(new LoginBo()));

        // Act
        boolean result = regLoginDao.checkLogin(username, password);

        // Assert
        assertTrue(result);
        verify(loginCriteriaQuery).select(loginRoot);
        verify(loginCriteriaQuery).where(any(Predicate.class));
    }
} 