package com.circular.Circular.economy;

import com.circular.Circular.economy.entity.Role;
import com.circular.Circular.economy.entity.User;
import com.circular.Circular.economy.repository.UserRepository;
import com.circular.Circular.economy.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostServiceTests {

    @Autowired
    private UserRepository userRepository;
    private User savedUser;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .username("Jacob")
                .email("jacob.johnson@gmail.com")
                .phoneNumber("+37068898744")
                .password("password123")
                .role(Role.USER).build();
        savedUser = userRepository.save(user);
    }

    @Test
    @DisplayName("Save User: Returns Saved User with ID > 0")
    public void save_returnSavedUser() {

        Assertions.assertNotNull(savedUser);
        Assertions.assertTrue(savedUser.getUserId() > 0);
    }

    @Test
    @DisplayName("Get All Users: Returns More Than One User")
    public void getAll_returnMoreThanOneUser() {

        User user2 = User.builder()
                .username("Jacob")
                .email("jacob.johnson@gmail.com")
                .phoneNumber("+37068898744")
                .password("password123")
                .role(Role.USER).build();

        userRepository.save(user2);
        List<User> userList = userRepository.findAll();

        Assertions.assertEquals(2, userList.size());
    }

    @Test
    @DisplayName("Save user: find user by ID")
    public void findByID_returnUser() {

        User retrievedUser = userRepository.findById(Long.valueOf(savedUser.getUserId())).orElse(null);

        Assertions.assertEquals(savedUser.getUserId(), retrievedUser.getUserId(), "IDs should match");
        Assertions.assertEquals(savedUser.getEmail(), retrievedUser.getEmail(), "Emails should match");
    }

    @Test
    @DisplayName("Update user and verify changes")
    public void updateUser_returnUpdatedUser() {

        User updatedUser = User.builder()
                .username("Jacob")
                .email("jacob.johnson@gmail.com")
                .phoneNumber("+37068898744")
                .password("password123")
                .role(Role.USER).build();

        userRepository.save(updatedUser);
        User retrievedUser = userRepository.findById(Long.valueOf(savedUser.getUserId())).orElse(null);

        Assertions.assertEquals("UpdatedJacob", retrievedUser.getUsername(), "First names should match");
        Assertions.assertEquals("+311111111", retrievedUser.getPhoneNumber(), "Phone numbers should match");
    }

    @Test
    @DisplayName("Delete User: Repository Should Not Contain Deleted User")
    public void deleteUser_returnUserIsEmpty() {

        userRepository.deleteById(Long.valueOf(savedUser.getUserId()));

        Optional<User> deletedUser = userRepository.findById(Long.valueOf(savedUser.getUserId()));

        Assertions.assertTrue(deletedUser.isEmpty());

    }

    @Test
    @DisplayName("Find User by Existing Email: Returns User")
    public void findByUsername_withExistingUsername_returnsUser() {

        Optional<User> foundUser = userRepository.findByUsername(savedUser.getEmail());
        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals(savedUser.getEmail(), foundUser.get().getEmail());
    }

    @Test
    @DisplayName("Save User with User Role: Has User Role Authorities")
    public void saveUser_withUserRole_hasUserRoleAuthorities() {

        Assertions.assertTrue(savedUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    @DisplayName("Save User with ADMIN Role: Has ADMIN Role Authorities")
    public void saveUser_withAdminRole_hasAdminRoleAuthorities() {
        User adminUser = User.builder()
                .username("Jacob")
                .email("jacob.johnson@gmail.com")
                .phoneNumber("+37068898744")
                .password("password123")
                .role(Role.USER).build();

        User savedAdminUser = userRepository.save(adminUser);

        Assertions.assertTrue(savedAdminUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }


}