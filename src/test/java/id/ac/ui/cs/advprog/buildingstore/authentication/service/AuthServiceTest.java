package id.ac.ui.cs.advprog.buildingstore.authentication.service;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.authentication.repository.AuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private AuthRepository authRepository;

    @BeforeEach
    void setUp() {
        authRepository = mock(AuthRepository.class);
        authService = new AuthService(authRepository);
    }

    @Test
    void registerUserShouldSaveUserWhenEmailIsNotTaken() {
        User newUser = new User();
        newUser.setEmail("test@example.com");
        newUser.setPassword("password");

        when(authRepository.findByEmail("test@example.com")).thenReturn(null);
        when(authRepository.save(newUser)).thenReturn(newUser);

        User result = authService.registerUser(newUser);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(authRepository).save(newUser);
    }

    @Test
    void registerUserShouldThrowExceptionWhenEmailIsAlreadyTaken() {
        User existingUser = new User();
        existingUser.setEmail("test@example.com");

        User newUser = new User();
        newUser.setEmail("test@example.com");

        when(authRepository.findByEmail("test@example.com")).thenReturn(existingUser);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.registerUser(newUser);
        });

        assertEquals("Email is already taken", exception.getMessage());
        verify(authRepository, never()).save(any());
    }

    @Test
    void authenticateUserShouldReturnTrueWhenCredentialsAreCorrect() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("correctPassword");

        when(authRepository.findByEmail("user@example.com")).thenReturn(user);

        boolean isAuthenticated = authService.authenticateUser("user@example.com", "correctPassword");

        assertTrue(isAuthenticated);
    }

    @Test
    void authenticateUserShouldReturnFalseWhenPasswordIsIncorrect() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("correctPassword");

        when(authRepository.findByEmail("user@example.com")).thenReturn(user);

        boolean isAuthenticated = authService.authenticateUser("user@example.com", "wrongPassword");

        assertFalse(isAuthenticated);
    }

    @Test
    void authenticateUserShouldReturnFalseWhenUserIsNotFound() {
        when(authRepository.findByEmail("notfound@example.com")).thenReturn(null);

        boolean isAuthenticated = authService.authenticateUser("notfound@example.com", "anyPassword");

        assertFalse(isAuthenticated);
    }

    @Test
    void findByEmailShouldReturnUserWhenEmailExists() {
        User user = new User();
        user.setEmail("lookup@example.com");

        when(authRepository.findByEmail("lookup@example.com")).thenReturn(user);

        User result = authService.findByEmail("lookup@example.com");

        assertNotNull(result);
        assertEquals("lookup@example.com", result.getEmail());
    }

    @Test
    void findByEmailShouldReturnNullWhenUserDoesNotExist() {
        when(authRepository.findByEmail("notexist@example.com")).thenReturn(null);

        User result = authService.findByEmail("notexist@example.com");

        assertNull(result);
    }
}
