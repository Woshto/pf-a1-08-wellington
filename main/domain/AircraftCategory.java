package com.prettyflights.gates.model;

public enum AircraftCategory {
    A, B, C, D;

    public boolean fitsIn(AircraftCategory maxCategory) {
        return this.ordinal() <= maxCategory.ordinal();
    }
}