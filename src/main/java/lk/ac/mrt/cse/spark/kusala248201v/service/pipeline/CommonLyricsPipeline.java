package lk.ac.mrt.cse.spark.kusala248201v.service.pipeline;

import lk.ac.mrt.cse.spark.kusala248201v.constant.Genre;
import lk.ac.mrt.cse.spark.kusala248201v.dto.response.GenrePrediction;
import lk.ac.mrt.cse.spark.kusala248201v.service.common.LyricsPipeline;
import lk.ac.mrt.cse.spark.kusala248201v.service.common.MLService;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.linalg.DenseVector;
import org.apache.spark.ml.tuning.CrossValidatorModel;
import org.apache.spark.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static lk.ac.mrt.cse.spark.kusala248201v.constant.Column.*;

public abstract class CommonLyricsPipeline implements LyricsPipeline {

    @Autowired
    protected SparkSession sparkSession;

    @Autowired
    private MLService mlService;

    @Value("${lyrics.training.set.directory.path}")
    private String lyricsTrainingSetDirectoryPath;

    @Value("${lyrics.model.directory.path}")
    private String lyricsModelDirectoryPath;

    @Override
    public GenrePrediction predict(final String unknownLyrics) {
        String lyrics[] = unknownLyrics.split("\\r?\\n");
        Dataset<String> lyricsDataset = sparkSession.createDataset(Arrays.asList(lyrics),
                Encoders.STRING());

        Dataset<Row> unknownLyricsDataset = lyricsDataset
                .withColumn(LABEL.getName(), functions.lit(Genre.UNKNOWN.getValue()))
                .withColumn(ID.getName(), functions.lit("unknown.txt"));

        CrossValidatorModel model = mlService.loadCrossValidationModel(getModelDirectory());
        getModelStatistics(model);

        PipelineModel bestModel = (PipelineModel) model.bestModel();

        Dataset<Row> predictionsDataset = bestModel.transform(unknownLyricsDataset);
        Row predictionRow = predictionsDataset.first();

        System.out.println("\n------------------------------------------------");
        final Double prediction = predictionRow.getAs("prediction");
        System.out.println("Prediction: " + Double.toString(prediction));

        if (Arrays.asList(predictionsDataset.columns()).contains("probability")) {
            final DenseVector probability = predictionRow.getAs("probability");
            System.out.println("Probability: " + probability);
            System.out.println("------------------------------------------------\n");

            return new GenrePrediction(getGenre(prediction).getName(), probability.apply(0), probability.apply(1));
        }

        System.out.println("------------------------------------------------\n");
        return new GenrePrediction(getGenre(prediction).getName());
    }

    public Dataset<Row> readLyrics() {
        Dataset input = readLyricsForGenre(lyricsTrainingSetDirectoryPath, Genre.POP)
                .union(readLyricsForGenre(lyricsTrainingSetDirectoryPath, Genre.BLUES))
                .union(readLyricsForGenre(lyricsTrainingSetDirectoryPath, Genre.JAZZ))
                .union(readLyricsForGenre(lyricsTrainingSetDirectoryPath, Genre.COUNTRY))
                .union(readLyricsForGenre(lyricsTrainingSetDirectoryPath, Genre.REGGAE))
                .union(readLyricsForGenre(lyricsTrainingSetDirectoryPath, Genre.HIPHOP));
        // Reduce the input amount of partition minimal amount (spark.default.parallelism OR 2, whatever is less)
        input = input.coalesce(sparkSession.sparkContext().defaultMinPartitions()).cache();
        // Force caching.
        input.count();

        return input;
    }

    private Dataset<Row> readLyricsForGenre(String inputDirectory, Genre genre) {
        Dataset<Row> lyrics = readLyrics(inputDirectory, genre.name().toLowerCase() + ".csv");
        Dataset<Row> labeledLyrics = lyrics.withColumn(LABEL.getName(), functions.lit(genre.getValue()));

        System.out.println(genre.name() + " music sentences = " + lyrics.count());

        return labeledLyrics;
    }

    private Dataset<Row> readLyrics(String inputDirectory, String path) {
        String filePath = Paths.get(inputDirectory).resolve(path).toString();
        Dataset<Row> rawLyrics = sparkSession.read().csv(filePath);
        rawLyrics = rawLyrics.filter(rawLyrics.col(VALUE.getName()).notEqual(""));
        rawLyrics = rawLyrics.filter(rawLyrics.col(VALUE.getName()).contains(" "));

        // Add source filename column as a unique id.
        Dataset<Row> lyrics = rawLyrics.withColumn(ID.getName(), functions.input_file_name());

        return lyrics;
    }

    private Genre getGenre(Double value) {
        for (Genre genre: Genre.values()){
            if (genre.getValue().equals(value)) {
                return genre;
            }
        }

        return Genre.UNKNOWN;
    }

    @Override
    public Map<String, Object> getModelStatistics(CrossValidatorModel model) {
        Map<String, Object> modelStatistics = new HashMap<>();

        Arrays.sort(model.avgMetrics());
        modelStatistics.put("Best model metrics", model.avgMetrics()[model.avgMetrics().length - 1]);

        return modelStatistics;
    }

    void printModelStatistics(Map<String, Object> modelStatistics) {
        System.out.println("\n------------------------------------------------");
        System.out.println("Model statistics:");
        System.out.println(modelStatistics);
        System.out.println("------------------------------------------------\n");
    }

    void saveModel(CrossValidatorModel model, String modelOutputDirectory) {
        this.mlService.saveModel(model, modelOutputDirectory);
    }

    void saveModel(PipelineModel model, String modelOutputDirectory) {
        this.mlService.saveModel(model, modelOutputDirectory);
    }

    public void setLyricsTrainingSetDirectoryPath(String lyricsTrainingSetDirectoryPath) {
        this.lyricsTrainingSetDirectoryPath = lyricsTrainingSetDirectoryPath;
    }

    public void setLyricsModelDirectoryPath(String lyricsModelDirectoryPath) {
        this.lyricsModelDirectoryPath = lyricsModelDirectoryPath;
    }

    protected abstract String getModelDirectory();

    String getLyricsModelDirectoryPath() {
        return lyricsModelDirectoryPath;
    }
}

