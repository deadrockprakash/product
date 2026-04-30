package com.prakash.productservice.service;

import com.prakash.productservice.dto.UserDto;
import com.prakash.productservice.dto.UserDtoResponse;

import java.util.List;

public interface UserService {
    UserDto findUserByUserName(String userName);
    String saveUser(UserDto userDto);
    List<UserDtoResponse> getAllUsers();
}
