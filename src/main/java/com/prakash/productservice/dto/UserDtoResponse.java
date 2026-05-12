package com.prakash.productservice.dto;

import com.prakash.productservice.entity.ROLE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoResponse {
    private String userName;
    private String email;
    private ROLE rOle;
}
