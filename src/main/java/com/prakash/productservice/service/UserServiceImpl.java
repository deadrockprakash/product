package com.prakash.productservice.service;

import com.prakash.productservice.dto.UserDto;
import com.prakash.productservice.entity.UserEntity;
import com.prakash.productservice.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        UserEntity user = UserEntity.builder()
                .userName(userDto.getUsername())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .build();
        userRepository.save(user);
        return "Saved Successfully";
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = findUserByUserName(username);
      return User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .build();

    }
}
