package com.prakash.productservice.service;

import com.prakash.productservice.dto.UserDto;

public interface UserService {
    UserDto findUserByUserName(String userName);
    String saveUser(UserDto userDto);
}
