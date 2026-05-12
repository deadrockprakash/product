package com.prakash.productservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prakash.productservice.entity.ROLE;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String username;
    private String password;
    private String email;
    private ROLE role;

}
