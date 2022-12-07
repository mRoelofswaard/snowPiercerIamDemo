package nl.techforce1.workshop.iam.snowpiercerdemo.domain;

import static java.util.List.of;
import static nl.techforce1.workshop.iam.snowpiercerdemo.domain.Role.BRAKEMAN;
import static nl.techforce1.workshop.iam.snowpiercerdemo.domain.Role.COMMON_PASSENGER;
import static nl.techforce1.workshop.iam.snowpiercerdemo.domain.Role.ENGINEER;
import static nl.techforce1.workshop.iam.snowpiercerdemo.domain.Role.QUOTE_500_PASSENGER;
import static nl.techforce1.workshop.iam.snowpiercerdemo.domain.Role.TAILY;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WagonClass {
    ENGINE(of(ENGINEER)),
    FIRST(of(BRAKEMAN, QUOTE_500_PASSENGER)),
    SECOND(of(BRAKEMAN, COMMON_PASSENGER)),
    TAIL(of(BRAKEMAN, TAILY));

    private final List<Role> allowedRoles;

    public boolean isAccessAllowed(final List<Role> grantedRoles) {
        return grantedRoles.stream().anyMatch(allowedRoles::contains);
    }
}
