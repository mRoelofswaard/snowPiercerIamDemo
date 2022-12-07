package nl.techforce1.workshop.iam.snowpiercerdemo;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakLogoutHandler implements LogoutHandler {

    @Override
    public void logout(final HttpServletRequest request, //
            final HttpServletResponse response, //
            final Authentication auth) {

        logoutFromKeycloak((OidcUser) auth.getPrincipal());
    }

    private void logoutFromKeycloak(final OidcUser user) {
        final String endSessionEndpoint = user.getIssuer() + "/protocol/openid-connect/logout";

        final UriComponentsBuilder builder = UriComponentsBuilder //
                .fromUriString(endSessionEndpoint) //
                .queryParam("id_token_hint", user.getIdToken()
                        .getTokenValue());

        final ResponseEntity<String> logoutResponse = new RestTemplate().getForEntity(builder.toUriString(), String.class);
        if (logoutResponse.getStatusCode()
                .is2xxSuccessful()) {
            log.info("Successfully logged out from Keycloak");
        } else {
            log.error("Could not propagate logout to Keycloak");
        }
    }
}