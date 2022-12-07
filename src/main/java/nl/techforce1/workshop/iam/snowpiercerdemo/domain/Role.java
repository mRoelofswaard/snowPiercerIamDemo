package nl.techforce1.workshop.iam.snowpiercerdemo.domain;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;

public enum Role {
    ENGINEER,
    DIRECTOR,
    BRAKEMAN,
    QUOTE_500_PASSENGER,
    COMMON_PASSENGER,
    TAILY;

    public static final String ROLE_PREFIX = "ROLE_";

    public static Optional<Role> fromRoleString(final String value) {
        try {
            return of(Role.valueOf(removePrefix(value)));
        } catch (final IllegalArgumentException e) {
            return empty();
        }
    }

    private static String removePrefix(final String value) {
        return value.substring(ROLE_PREFIX.length());
    }
}
