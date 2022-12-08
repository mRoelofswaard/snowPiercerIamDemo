package nl.techforce1.workshop.iam.snowpiercerdemo.api.railcar;

import static org.springframework.http.HttpStatusCode.valueOf;

import java.util.Optional;

import nl.techforce1.workshop.iam.snowpiercerdemo.domain.Engine;
import nl.techforce1.workshop.iam.snowpiercerdemo.domain.SnowPiercer;
import nl.techforce1.workshop.iam.snowpiercerdemo.domain.Traction;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/snowpiercer/cars/engine/traction")
public class EngineRestController {

    private final SnowPiercer snowPiercer;

    @PutMapping
    public ResponseEntity<Traction> move(@RequestBody final Traction traction) {
        final Optional<Engine> engine = snowPiercer.getEngine();

        // @formatter:off

        if (engine.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (engine.get().changeTraction(traction)) {
            return ResponseEntity.ok(traction);
        }

        return ResponseEntity.of(badRequestBecauseOfPhysicsLawViolation()).build();

        // @formatter:on
    }

    private ProblemDetail badRequestBecauseOfPhysicsLawViolation() {
        return ProblemDetail.forStatusAndDetail( //
                valueOf(400), //
                "Cannot apply tractive change; Some wagons are too light. Add weight to all cars. Almost any person will do, but brakemen don't count.");
    }
}
