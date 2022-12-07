package nl.techforce1.workshop.iam.snowpiercerdemo.domain;

public enum Role {
    ENGINEER,
    DIRECTOR,
    BRAKEMAN,
    QUOTE_500_PASSENGER,
    COMMON_PASSENGER,
    TAILY;

    public static Role fromRoleString(final String value) {
        try {
            return Role.valueOf(value);
        } catch (final IllegalArgumentException e) {
            return TAILY;
        }
    }
}
