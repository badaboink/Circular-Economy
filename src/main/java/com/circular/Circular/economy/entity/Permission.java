package com.circular.Circular.economy.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    NORMAL_READ("management:read"),
    NORMAL_UPDATE("management:update"),
    NORMAL_CREATE("management:create"),
    NORMAL_DELETE("management:delete")

    ;

    @Getter
    private final String permission;
}
