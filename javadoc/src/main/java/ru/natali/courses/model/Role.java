package ru.natali.courses.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * Перечисление ролей пользователей системы.
 */
public enum Role implements GrantedAuthority {
    USER, ADMIN;

    /**
     * Возвращает название роли как authority.
     * @return название роли
     */
    @Override
    public String getAuthority() {
        return name();
    }
}