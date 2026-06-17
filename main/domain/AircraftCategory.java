package br.univali.es2.prettyflights.main.domain;

public enum AircraftCategory {
    A, B, C, D;

    public boolean fitsIn(AircraftCategory maxCategory) {
        return this.ordinal() <= maxCategory.ordinal();
    }
}