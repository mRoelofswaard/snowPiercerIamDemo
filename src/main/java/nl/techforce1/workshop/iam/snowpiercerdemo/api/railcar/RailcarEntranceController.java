package nl.techforce1.workshop.iam.snowpiercerdemo.api.railcar;

import nl.techforce1.workshop.iam.snowpiercerdemo.authentication.AuthenticationService;
import nl.techforce1.workshop.iam.snowpiercerdemo.domain.Railcar;
import nl.techforce1.workshop.iam.snowpiercerdemo.domain.SnowPiercer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/snowpiercer/cars")
@RequiredArgsConstructor
public class RailcarEntranceController {

    private final AuthenticationService authenticationService;

    private final SnowPiercer snowPiercer;

    @PostMapping("/{carName}/enter")
    @SuppressWarnings("java:S3655") //False positive
    public ResponseEntity<Entrance> enter(@PathVariable final String carName) {

        final var grantedRoles = authenticationService.getRoles();
        final var userName = authenticationService.getUserName().orElse("-");

        if (snowPiercer.getRailcar(carName).isPresent()) {
            final var railcar = snowPiercer.getRailcar(carName)
                    .get();
            if (railcar.isAccessAllowed(grantedRoles)) {
                railcar.enter(new Railcar.Inhabitant(userName, grantedRoles));

                return ResponseEntity.ok(new Entrance(carName, userName));
            }

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.notFound().build();
    }
}
