package lk.ac.mrt.cse.spark.kusala248201v.util;

import org.apache.spark.api.java.function.Function;
import scala.Tuple2;


public class VerifyPredictionsFunction implements Function<Tuple2<Object, Object>, Boolean> {

    @Override
    public Boolean call(Tuple2<Object, Object> predictions) {
        return Double.valueOf((double)predictions._1()).intValue() ==  Double.valueOf((double)predictions._2()).intValue();
    }
}