package nl.techforce1.workshop.iam.snowpiercerdemo.authentication;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import nl.techforce1.workshop.iam.snowpiercerdemo.domain.Role;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {

    public List<Role> getRoles() {
        return getAuthentication() //
                .map(Authentication::getAuthorities) //
                .map(this::respectMaAuthorita)
                .orElse(emptyList());
    }

    public Optional<String> getUserName() {
        return getAuthentication() //
                .map(Authentication::getPrincipal)
                .map(Jwt.class::cast)
                .map(Jwt::getClaims)
                .map(claims -> claims.get("preferred_username"))
                .map(Object::toString);
    }

    private Optional<Authentication> getAuthentication() {
        return ofNullable(SecurityContextHolder.getContext()
                .getAuthentication());
    }

    private List<Role> respectMaAuthorita(final Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(Role::fromRoleString)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
