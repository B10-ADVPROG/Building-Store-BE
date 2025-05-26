package id.ac.ui.cs.advprog.buildingstore.authentication.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthorizationService authorizationService;

    @Test
    void testAuthorizeAdmin() {
        String token = "dummyToken";

        when(jwtService.isTokenValid(token)).thenReturn(true);
        when(jwtService.extractRole(token)).thenReturn("administrator");

        assertTrue(authorizationService.authorizeAdmin(token));

        when(jwtService.extractRole(token)).thenReturn("kasir");
        assertFalse(authorizationService.authorizeAdmin(token));

        when(jwtService.isTokenValid(token)).thenReturn(false);
        assertFalse(authorizationService.authorizeAdmin(token));
    }

    @Test
    void testAuthorizeKasir() {
        String token = "dummyToken";

        when(jwtService.isTokenValid(token)).thenReturn(true);
        when(jwtService.extractRole(token)).thenReturn("kasir");

        assertTrue(authorizationService.authorizeKasir(token));

        when(jwtService.extractRole(token)).thenReturn("administrator");
        assertFalse(authorizationService.authorizeKasir(token));

        when(jwtService.isTokenValid(token)).thenReturn(false);
        assertFalse(authorizationService.authorizeKasir(token));
    }
}