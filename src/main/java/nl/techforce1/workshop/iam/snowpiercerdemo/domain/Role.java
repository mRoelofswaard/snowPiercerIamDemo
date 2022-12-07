package nl.techforce1.workshop.iam.snowpiercerdemo.domain;

public enum Role {
    ENGINEER,
    DIRECTOR,
    BRAKEMAN,
    QUOTE_500_PASSENGER,
    COMMON_PASSENGER,
    TAILY;

    public static final String ROLE_PREFIX = "ROLE_";

    public static Role fromRoleString(final String value) {
        try {
            return Role.valueOf(removePrefix(value));
        } catch (final IllegalArgumentException e) {
            return TAILY;
        }
    }

    private static String removePrefix(final String value) {
        return value.substring(ROLE_PREFIX.length());
    }
}
