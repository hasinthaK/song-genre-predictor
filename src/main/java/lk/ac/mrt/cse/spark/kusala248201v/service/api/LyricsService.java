package lk.ac.mrt.cse.spark.kusala248201v.service.api;

import lk.ac.mrt.cse.spark.kusala248201v.dto.response.GenrePrediction;
import lk.ac.mrt.cse.spark.kusala248201v.service.common.LyricsPipeline;
import org.apache.spark.ml.tuning.CrossValidatorModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class LyricsService {

    @Resource(name = "${lyrics.pipeline}")
    private LyricsPipeline pipeline;

    public Map<String, Object> classifyLyrics() {
        CrossValidatorModel model = pipeline.classify();
        return pipeline.getModelStatistics(model);
    }

    public GenrePrediction predictGenre(final String unknownLyrics) {
        return pipeline.predict(unknownLyrics);
    }

}
