package com.prakash.productservice.controller;

import com.prakash.productservice.dto.UserDto;
import com.prakash.productservice.dto.UserDtoResponse;
import com.prakash.productservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> saveUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.saveUser(userDto), HttpStatus.CREATED);
    }
    @GetMapping("/{userName}")
    public ResponseEntity<UserDto> getAllUsers(@PathVariable String userName) {
        return new ResponseEntity<>(userService.findUserByUserName(userName), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDtoResponse>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }
}
