import java.util.function.Function;

public class Secant {

    private Function<Double, Double> func;

    public Secant(Function<Double, Double> func) {
        this.func = func;
    }

    public double getRoot(double a, double b, double eps) {
        if (a > b) {
            double t = a;
            a = b;
            b = t;
        }

        double x = b - (b - a) / (func.apply(b) - func.apply(a)) * func.apply(b);
        while (Math.abs(func.apply(x)) >= eps) {
            a = b;
            b = x;
            x = b - (b - a) / (func.apply(b) - func.apply(a)) * func.apply(b);
        }

        return x;
    }

}
