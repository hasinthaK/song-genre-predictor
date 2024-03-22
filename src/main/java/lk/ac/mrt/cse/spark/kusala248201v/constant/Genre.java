package lk.ac.mrt.cse.spark.kusala248201v.constant;

public enum Genre {

    POP("Pop", 1D),

    BLUES("Blues", 2D),

    COUNTRY("Country", 3D),

    JAZZ("Jazz", 4D),

    HIPHOP("Hip Hop", 5D),

    REGGAE("Reggae", 6D),

    ROCK("Rock", 7D),

    UNKNOWN("Don\'t know :(", -1D);

    private final String name;
    private final Double value;

    Genre(final String name, final Double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Double getValue() {
        return value;
    }

}
