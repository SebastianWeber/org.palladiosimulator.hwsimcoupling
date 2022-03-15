package org.palladiosimulator.hwsimcoupling.util;

/**
 * @author Sebastian Paths processed by the {@link FileManager} are prefixed by "local:" means the
 *         path is relative to a project root with which it begins "absolute:" means the path is
 *         absolute, i.e. /home/user/... or C:/User/...
 */
public enum Locations {

    LOCAL("local:"), ABSOLUTE("absolute:");

    private final String keyword;

    private Locations(String keyword) {
        this.keyword = keyword;
    }

    public String toString() {
        return keyword;
    }

    public static String getLocationsString() {
        return "\"" + Locations.ABSOLUTE.toString() + "\" or \"" + Locations.LOCAL.toString() + "\"";
    }
}