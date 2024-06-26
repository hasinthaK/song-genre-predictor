package lk.ac.mrt.cse.spark.kusala248201v.dto.response;

public class GenrePrediction {

    private String genre;
    private Double metalProbability;
    private Double popProbability;

    // TODO: add other genres here

    public GenrePrediction(String genre, Double metalProbability, Double popProbability) {
        this.genre = genre;
        this.metalProbability = metalProbability;
        this.popProbability = popProbability;
    }

    public GenrePrediction(String genre) {
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    public Double getMetalProbability() {
        return metalProbability;
    }

    public Double getPopProbability() {
        return popProbability;
    }
}
