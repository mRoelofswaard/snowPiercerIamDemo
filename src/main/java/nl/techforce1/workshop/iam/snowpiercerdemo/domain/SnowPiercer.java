package nl.techforce1.workshop.iam.snowpiercerdemo.domain;

import static nl.techforce1.workshop.iam.snowpiercerdemo.domain.Role.TAILY;
import static nl.techforce1.workshop.iam.snowpiercerdemo.domain.Traction.INERT;
import static nl.techforce1.workshop.iam.snowpiercerdemo.domain.WagonClass.ENGINE;
import static nl.techforce1.workshop.iam.snowpiercerdemo.domain.WagonClass.TAIL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

@Component
public class SnowPiercer implements Train {

    private final Map<String, Railcar> railcars = new HashMap<>();

    public SnowPiercer() {
        final var mansion = new Railcar("mansion", WagonClass.FIRST);
        final var suburb = new Railcar("suburb", WagonClass.SECOND);
        final var slum = new Railcar("slum", WagonClass.TAIL);

        railcars.put(mansion.getName(), mansion);
        railcars.put(suburb.getName(), suburb);
        railcars.put(slum.getName(), slum);
    }

    public boolean add(final Railcar railcar) {
        final boolean isExisting = railcars.containsKey(railcar.getName());

        if (isExisting) {
            return false;
        }

        railcars.put(railcar.getName(), railcar);

        return true;
    }

    public Optional<Railcar> getEngine() {
        return getRailcars().stream()
                .filter(railcar -> railcar.getWagonClass() == ENGINE)
                .findFirst();
    }

    public Optional<Railcar> getRailcar(final String name) {
        return Optional.ofNullable(railcars.get(name));
    }

    public List<Railcar> getRailcars() {
        return railcars.values()
                .stream()
                .toList();
    }

    public List<Railcar> getTailCars() {
        return getRailcars().stream()
                .filter(railcar -> railcar.getWagonClass() == TAIL)
                .toList();
    }

    public boolean isTractionSupported(final Traction traction) {
        return traction == INERT || getRailcars().stream()
                .allMatch(containAtLeastOneInhabitant()) && hasAllTailCarsCaryingEnoughTailies();
    }

    private Predicate<Railcar> containAtLeastOneInhabitant() {
        return railcar -> !railcar.getInhabitants()
                .isEmpty();
    }

    private boolean hasAllTailCarsCaryingEnoughTailies() {
        return getTailCars().stream()
                .flatMap(railcar -> railcar.getInhabitants()
                        .stream())
                .filter(inhabitant -> inhabitant.roles()
                        .contains(TAILY))
                .count() >= getTailCars().size();
    }
}
