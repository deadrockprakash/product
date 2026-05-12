package com.prakash.productservice.entity;


import java.util.Set;

public enum ROLE {
    ROLE_ADMIN(Set.of(PERMISSION.ADD, PERMISSION.VIEW, PERMISSION.VIEW_ALL)),
    ROLE_USER(Set.of(PERMISSION.VIEW)),
    ROLE_STAFF(Set.of(PERMISSION.VIEW,  PERMISSION.VIEW_ALL)),;

    private final Set<PERMISSION> permissions;

    ROLE(Set<PERMISSION> permissions) {
        this.permissions = permissions;
    }
    public Set<PERMISSION> getPermissions() {
        return permissions;
    }
}
