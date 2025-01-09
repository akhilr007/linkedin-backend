package com.akhil.linkedin.connection_service.auth;

import org.springframework.stereotype.Component;

@Component
public class UserContextHolder {

    private static final ThreadLocal<Long> userId = new ThreadLocal<>();

    public static Long getCurrentUserId() {
        return userId.get();
    }

    static void setCurrentUserId(Long id) {
        userId.set(id);
    }

    static void clear() {
        userId.remove();
    }
}
