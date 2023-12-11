package com.circular.Circular.economy;

import com.circular.Circular.economy.controller.UserController;
import com.circular.Circular.economy.dto.UserDTO;
import com.circular.Circular.economy.entity.User;
import com.circular.Circular.economy.service.PostService;
import com.circular.Circular.economy.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {

    @Mock
    private UserService userService;

    @Mock
    private PostService postService;

    @InjectMocks
    private UserController userController;

    @Test
    void testGetUser_Success() {
        // Mocking
        when(userService.getUser(anyString())).thenReturn(new UserDTO());

        // Test the method
        ResponseEntity<?> responseEntity = userController.getUser("testUsername");

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof UserDTO);
    }

    @Test
    void testGetUser_UserNotFound() {
        // Mocking
        when(userService.getUser(anyString())).thenThrow(new NoSuchElementException("User not found"));

        // Test the method
        ResponseEntity<?> responseEntity = userController.getUser("nonexistentUsername");
    }
    @Test
    void testUpdateUser_Success() {
        // Mocking
        when(userService.updateUser(anyLong(), any(User.class))).thenReturn(new UserDTO());

        // Test the method
        ResponseEntity<?> responseEntity = userController.updateUser(1L, new User());

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof UserDTO);
    }

    @Test
    void testDeleteUser_Success() {
        when(postService.getPostIdsByUserId(anyLong())).thenReturn(Collections.singletonList(1L));
        doNothing().when(userService).deleteUser(anyLong());

        // Test the method
        ResponseEntity<?> responseEntity = userController.deleteUser(1L, "testToken");

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        // Verify interactions
        verify(postService, times(1)).getPostIdsByUserId(anyLong());
        verify(postService, times(1)).deletePosts(anyList(), anyString());
        verify(userService, times(1)).deleteUser(anyLong());
    }
    @Test
    void testDeleteUser_UserNotFound() {
        // Mocking
        when(postService.getPostIdsByUserId(anyLong())).thenThrow(new NoSuchElementException("User not found"));

        // Test the method
        ResponseEntity<?> responseEntity = userController.deleteUser(1L, "testToken");

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("User not found", responseEntity.getBody());

        // Verify interactions
        verify(postService, times(1)).getPostIdsByUserId(anyLong());
        verifyNoMoreInteractions(postService, userService);
    }
}