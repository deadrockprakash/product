package com.prakash.productservice.controller;

import com.prakash.productservice.Security.JwtService;
import com.prakash.productservice.dto.AuthRequest;
import com.prakash.productservice.dto.UserDto;
import com.prakash.productservice.dto.UserDtoResponse;
import com.prakash.productservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
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

    @GetMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(@RequestBody AuthRequest authRequest) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
       if(authenticate.isAuthenticated()) {
           String token = jwtService.generateToken(authRequest.getUsername());
           return new ResponseEntity<>(token, HttpStatus.OK);
       } else {
           return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
       }
    }
}
