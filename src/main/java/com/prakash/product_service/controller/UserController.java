package com.prakash.product_service.controller;

import com.prakash.product_service.security.JwtService;
import com.prakash.product_service.dto.AuthRequest;
import com.prakash.product_service.dto.UserDto;
import com.prakash.product_service.dto.UserDtoResponse;
import com.prakash.product_service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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
    @PreAuthorize("hasAnyAuthority('ADD','VIEW','VIEW_ALL')")
    public ResponseEntity<UserDto> getAllUsers(@PathVariable String userName) {
        return new ResponseEntity<>(userService.findUserByUserName(userName), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADD','VIEW','VIEW_ALL')")
    public ResponseEntity<List<UserDtoResponse>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(@RequestBody AuthRequest authRequest) {
        log.info("Authenticating user {}", authRequest.getUsername());
        try{
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            if(authenticate.isAuthenticated()) {
               log.info("User {} authenticated successfully", authRequest.getUsername());
                String token = jwtService.generateToken(authRequest.getUsername());
                return new ResponseEntity<>(token, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception e){
            log.info("Authentication failed {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }
}
