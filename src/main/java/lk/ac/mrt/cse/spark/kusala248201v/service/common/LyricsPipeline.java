package lk.ac.mrt.cse.spark.kusala248201v.service.common;

import lk.ac.mrt.cse.spark.kusala248201v.dto.response.GenrePrediction;
import org.apache.spark.ml.tuning.CrossValidatorModel;

import java.util.Map;

public interface LyricsPipeline {

    CrossValidatorModel classify();

    GenrePrediction predict(String unknownLyrics);

    Map<String, Object> getModelStatistics(CrossValidatorModel model);

}
