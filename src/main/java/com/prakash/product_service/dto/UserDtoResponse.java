package com.prakash.product_service.dto;

import com.prakash.product_service.entity.ROLE;
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
