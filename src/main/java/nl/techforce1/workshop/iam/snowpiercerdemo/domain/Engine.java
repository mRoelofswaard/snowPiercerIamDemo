package nl.techforce1.workshop.iam.snowpiercerdemo.domain;

import static nl.techforce1.workshop.iam.snowpiercerdemo.domain.Traction.INERT;

import lombok.Getter;

public class Engine extends Railcar {

    @Getter
    private Traction traction;

    private final Train trailer;

    public Engine(final String name, final WagonClass wagonClass, final Train trailer) {
        super(name, wagonClass);

        traction = INERT;
        this.trailer = trailer;
    }

    public boolean changeTraction(final Traction desiredState) {
        if (trailer.isTractionSupported(desiredState)) {
            traction = desiredState;

            return true;
        }

        return false;
    }
}
