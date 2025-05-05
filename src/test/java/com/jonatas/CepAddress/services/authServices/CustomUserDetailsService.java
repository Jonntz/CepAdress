package com.jonatas.CepAddress.services.authServices;

import com.jonatas.CepAddress.models.User;
import com.jonatas.CepAddress.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void shouldReturnUserDetailsWhenUserExists() {
        String email = "test@user.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("password123");

        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername());
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
        String email = "nonexistent@user.com";
        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(email));
    }
}
