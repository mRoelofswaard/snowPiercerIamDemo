package nl.techforce1.workshop.iam.snowpiercerdemo.api.railcar;

import static nl.techforce1.workshop.iam.snowpiercerdemo.domain.WagonClass.ENGINE;
import static org.springframework.http.HttpStatusCode.valueOf;
import static org.springframework.http.ProblemDetail.forStatusAndDetail;

import java.net.URI;
import java.util.List;

import nl.techforce1.workshop.iam.snowpiercerdemo.domain.Engine;
import nl.techforce1.workshop.iam.snowpiercerdemo.domain.Railcar;
import nl.techforce1.workshop.iam.snowpiercerdemo.domain.SnowPiercer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/snowpiercer/cars")
public class RailCarsRestController {

    private final SnowPiercer snowPiercer;

    //@PreAuthorize("hasRole('ROLE_DIRECTOR')")
    @PostMapping(consumes = { "application/json" })
    public ResponseEntity<Railcar> add(@RequestBody final Railcar railcar, final UriComponentsBuilder uriComponentsBuilder) {
        if (snowPiercer.add(upgradeWhenNeeded(railcar))) {
            final URI location = buildLocation(railcar, uriComponentsBuilder);

            return ResponseEntity.created(location)
                    .build();
        }

        return ResponseEntity.of(forStatusAndDetail(valueOf(400), "Railcar already exists."))
                .build();
    }

    @GetMapping("/{name}")
    public ResponseEntity<Railcar> get(@PathVariable final String name) {
        return ResponseEntity.of(snowPiercer.getRailcar(name));
    }

    @GetMapping
    public List<Railcar> list() {
        return snowPiercer.getRailcars();
    }

    private URI buildLocation(final Railcar railcar, final UriComponentsBuilder uriComponentsBuilder) {
        return uriComponentsBuilder.path("/snowpiercer/cars/{name}")
                .buildAndExpand(railcar.getName())
                .toUri();
    }

    private Railcar upgradeWhenNeeded(final Railcar railcar) {
        if (railcar.getWagonClass() == ENGINE) {
            return new Engine(railcar.getName(), railcar.getWagonClass(), snowPiercer);
        }

        return railcar;
    }
}
