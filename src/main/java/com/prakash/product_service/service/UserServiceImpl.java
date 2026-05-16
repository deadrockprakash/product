package com.prakash.product_service.service;

import com.prakash.product_service.dto.UserDto;
import com.prakash.product_service.dto.UserDtoResponse;
import com.prakash.product_service.entity.UserEntity;
import com.prakash.product_service.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
                .email(userEntity.getEmail())
                .role(userEntity.getRole())
                .build();
    }

    @Override
    public String saveUser(UserDto userDto) {
        log.info("Saving user: {}", userDto);
        log.info("roles{}", userDto.getRole());
        UserEntity user = UserEntity.builder()
                .userName(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .role(userDto.getRole())
                .build();
       UserEntity userSaved = userRepository.save(user);
        log.info("User saved with username: {} {}", userSaved.getUserName(), userSaved.getPassword());
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
        UserEntity userEntity = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userEntity.getRole().name()));
        userEntity.getRole().getPermissions().forEach(permission -> authorities
                .add(new SimpleGrantedAuthority(permission.name())));
        return User.builder()
                .username(userEntity.getUserName())
                .password(userEntity.getPassword())
                .authorities(authorities)
                .build();
    }
}
