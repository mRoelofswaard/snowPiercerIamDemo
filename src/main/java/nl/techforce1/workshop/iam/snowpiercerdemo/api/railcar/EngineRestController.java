package nl.techforce1.workshop.iam.snowpiercerdemo.api.railcar;

import nl.techforce1.workshop.iam.snowpiercerdemo.domain.SnowPiercer;
import nl.techforce1.workshop.iam.snowpiercerdemo.domain.Traction;

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
        if (snowPiercer.getEngine()
                .isEmpty()) {
            return ResponseEntity.notFound()
                    .build();
        }

        if (snowPiercer.isTractionSupported(traction)) {
            return ResponseEntity.ok(traction);
        }

        return ResponseEntity.ok(Traction.INERT);
    }
}
