package com.oma.productmanagementsystem.enums;

public enum ApplicationUserPermission {
    PROFILE_READ("profile:read"),
    PROFILE_WRITE("profile:write"),
    PROFILE_DELETE("profile:delete");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission(){
        return permission;
    }
}
