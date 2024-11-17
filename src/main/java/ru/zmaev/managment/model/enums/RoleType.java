package ru.zmaev.managment.model.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    USER("user"),
    ADMIN("admin");

    private final String keycloakRoleName;

    RoleType(String keycloakRoleName) {
        this.keycloakRoleName = keycloakRoleName;
    }
}
