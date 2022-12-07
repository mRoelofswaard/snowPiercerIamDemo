package nl.techforce1.workshop.iam.snowpiercerdemo.api.railcar;

import java.net.URI;
import java.util.List;

import nl.techforce1.workshop.iam.snowpiercerdemo.authentication.AuthenticationService;
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

    private final AuthenticationService authenticationService;

    private final SnowPiercer snowPiercer;

    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<Railcar> add(@RequestBody final Railcar railcar, final UriComponentsBuilder uriComponentsBuilder) {
        if(snowPiercer.add(railcar)) {
            final URI location = buildLocation(railcar, uriComponentsBuilder);

            return ResponseEntity.created(location).build();
        }

        return ResponseEntity.badRequest().build();
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
        return uriComponentsBuilder.path("{name}")
                .buildAndExpand(railcar.getName())
                .toUri();
    }
}
