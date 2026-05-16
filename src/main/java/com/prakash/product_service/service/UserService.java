package com.prakash.product_service.service;

import com.prakash.product_service.dto.UserDto;
import com.prakash.product_service.dto.UserDtoResponse;

import java.util.List;

public interface UserService {
    UserDto findUserByUserName(String userName);
    String saveUser(UserDto userDto);
    List<UserDtoResponse> getAllUsers();
}
