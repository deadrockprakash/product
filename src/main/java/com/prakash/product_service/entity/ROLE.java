package com.prakash.product_service.entity;


import lombok.Getter;

import java.util.Set;

@Getter
public enum ROLE {
    ROLE_ADMIN(Set.of(PERMISSION.ADD, PERMISSION.VIEW, PERMISSION.VIEW_ALL)),
    ROLE_USER(Set.of(PERMISSION.VIEW)),
    ROLE_STAFF(Set.of(PERMISSION.VIEW,  PERMISSION.VIEW_ALL)),;

    private final Set<PERMISSION> permissions;

    ROLE(Set<PERMISSION> permissions) {
        this.permissions = permissions;
    }
}
