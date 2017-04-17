import java.util.function.Function;

/**
 * Created by Pussy_penetrator on 04.04.2017.
 */
public class Dichotomy {

    Function<Double, Double> func;
    double                   start, end;

    public Dichotomy(Function<Double, Double> func, double start, double end) {
        this.func = func;
        this.start = start;
        this.end = end;
    }

    public double getRoot(double EPS) {
        double a = start;
        double b = end;

        while (b - a > EPS) {
            double x = (a + b) / 2;
            if (func.apply(a) * func.apply(x) <= 0)
                b = x;
            else
                a = x;
        }

        return (a + b) / 2;
    }
}
