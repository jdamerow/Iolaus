package edu.asu.lerna.iolaus.domain.queryobject;

public enum PropertyOf {
SOURCE,
TARGET,
RELATION;

@Override
public String toString() {

    String value;

    switch (this) {

        case SOURCE:
            value = "source";
            break;

        case TARGET:
            value = "target";
            break;

        case RELATION:
            value = "r";
            break;

        default:
            value = null;
            break;
    }

    return value;
}
}
