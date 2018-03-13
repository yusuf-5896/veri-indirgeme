import javafx.util.Pair;

public class Point  extends Pair<Double, Double> {

    Point(Double key, Double value) {
        super(key, value);
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", getKey(), getValue());
    }
}
