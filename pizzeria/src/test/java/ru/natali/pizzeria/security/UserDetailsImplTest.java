package ru.natali.pizzeria.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.natali.pizzeria.model.ERole;
import ru.natali.pizzeria.model.Role;
import ru.natali.pizzeria.model.User;

import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    @Test
    void testUserDetailsImpl() {

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER")
        );

        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,
                "testuser",
                "password",
                authorities
        );

        assertEquals(1L, userDetails.getId());
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().iterator().next().getAuthority().equals("ROLE_USER"));
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void testBuildMethod() {

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");

        Role role = new Role();
        role.setName(ERole.ROLE_USER);
        user.setRoles(Collections.singleton(role));

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        assertEquals(1L, userDetails.getId());
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals("ROLE_USER", userDetails.getAuthorities().iterator().next().getAuthority());
    }
}