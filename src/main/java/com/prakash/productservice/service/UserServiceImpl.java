package com.prakash.productservice.service;

import com.prakash.productservice.dto.UserDto;
import com.prakash.productservice.dto.UserDtoResponse;
import com.prakash.productservice.entity.UserEntity;
import com.prakash.productservice.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Log4j2
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder  ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public UserDto findUserByUserName(String userName) {
        UserEntity userEntity = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userName));
        return UserDto.builder()
                .username(userEntity.getUserName())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .build();
    }

    @Override
    public String saveUser(UserDto userDto) {
        log.info("Saving user: " + userDto);
        UserEntity user = UserEntity.builder()
                .userName(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .build();
       UserEntity usersaved = userRepository.save(user);
       log.info("User saved with username: " + usersaved.getUserName() + " " + usersaved.getPassword());
        return "Saved Successfully";
    }

    @Override
    public List<UserDtoResponse> getAllUsers() {
       List<UserEntity> users = userRepository.findAll();
       return users.stream()
               .map(user -> UserDtoResponse.builder()
                       .userName(user.getUserName())
                       .email(user.getEmail())
                       .build())
               .toList();
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = findUserByUserName(username);
      return User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
              .authorities(Collections.emptyList())
                .build();

    }
}
