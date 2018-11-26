package com.bk.funrestroboard.service;

import com.bk.funrestroboard.model.User;
import com.bk.funrestroboard.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @MockBean
    UserRepository userRepository;

    UserService userService;

    @Before
    public void setUp() throws Exception {
        this.userService = new UserService(userRepository);
    }

    @Test
    public void loadUserByUsername_HappyPath_ShouldReturn1UserDetail() {
        // given
        User user = new User();
        user.setUsername("boonkuaeb");
        user.setPassword("password");
        user.setRole("USER");

        when(userRepository.findByUsername(anyString())).thenReturn(user);

        // When
        UserDetails actualUser = userService.loadUserByUsername("boonkuaeb");

        // then
        assertThat(actualUser.getUsername()).isEqualTo(user.getUsername());

        verify(userRepository, times(1)).findByUsername(anyString());

    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_Fail_ShouldReturnException() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(null);

        // When
        UserDetails actualUser = userService.loadUserByUsername("boonkuaeb");

        // Verify
        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    public void create_HappyPath_ShouldReturn1User() {
        // Given
        User user = new User();
        user.setUsername("boonkuaeb");
        user.setPassword("password");
        user.setRole("USER");

        when(userRepository.save(user)).thenReturn(user);

        // When
        User acutalUser = userService.create(user);

        // then
        assertThat(acutalUser.getUsername()).isEqualTo(user.getUsername());


        // verify
        verify(userRepository, times(1)).save(user);

    }
}