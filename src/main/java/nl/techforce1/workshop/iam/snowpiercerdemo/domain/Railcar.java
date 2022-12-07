package nl.techforce1.workshop.iam.snowpiercerdemo.domain;

import static nl.techforce1.workshop.iam.snowpiercerdemo.domain.Role.BRAKEMAN;
import static nl.techforce1.workshop.iam.snowpiercerdemo.domain.Role.TAILY;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;

@Getter
public class Railcar {

    final String name;

    final WagonClass wagonClass;

    private final Set<Inhabitant> inhabitants;

    public Railcar(final String name, final WagonClass wagonClass) {
        this.name = name;
        this.wagonClass = wagonClass;

        inhabitants = new HashSet<>();
    }

    public void enter(final Inhabitant candidate) {
        if (isTaily(candidate) && isBrakeManMissing()) {
            return; //A taily will simply escape unless subjugated by a brakeman.
        }

        inhabitants.add(candidate);
    }

    public boolean isAccessAllowed(final List<Role> roles) {
        return wagonClass.isAccessAllowed(roles);
    }

    private boolean isBrakeManMissing() {
        return inhabitants.stream()
                .noneMatch(inhabitant -> inhabitant.roles()
                        .contains(BRAKEMAN));
    }

    private boolean isTaily(final Inhabitant candidate) {
        return candidate.roles().stream()
                .anyMatch(role -> role == TAILY);
    }

    public record Inhabitant(String name, List<Role> roles) {

    }
}
