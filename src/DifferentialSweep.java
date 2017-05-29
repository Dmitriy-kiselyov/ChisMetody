import java.util.function.BiFunction;
import java.util.function.Function;

public class DifferentialSweep {
    private final double H = 0.1;

    private Function<Double, Double> rx;
    private Function<Double, Double> px;
    private Function<Double, Double> qx;
    private Function<Double, Double> fx;

    public DifferentialSweep(Function<Double, Double> rx,
                             Function<Double, Double> px,
                             Function<Double, Double> qx,
                             Function<Double, Double> fx) {
        this.rx = rx;
        this.px = px;
        this.qx = qx;
        this.fx = fx;
    }

    public void result(double a0, double b0, double a, double a1, double b1, double b) {
        double z1 = -a0 / b0;
        BiFunction<Double, Double, Double> f1 = (x, z) -> -(z * z) - px.apply(x) * z + qx.apply(x);
        runge(f1, z1);

//        double z2 = a / b0;
//        BiFunction<Double, Double, Double> f2 = (x, z) -> -z * (z + px.apply(x)) + fx.apply(x);
//        runge(f2, z2);
    }

    private double runge(BiFunction<Double, Double, Double> func, double y0) {
        printHeader();

        double y = y0;
        for (int i = 0; i < 10; i++) {
            double x = 0.1 * i;

            double k1 = H * func.apply(x, y);
            double k2 = H * func.apply(x + H / 2, y + k1 / 2);
            double k3 = H * func.apply(x + H / 2, y + k2 / 2);
            double k4 = H * func.apply(x + H, y + k3);

            print(x, y);
            y += (k1 + 2 * k2 + 2 * k3 + k4) / 6.0;
        }

        print(1, y);
        System.out.println("--------------------");

        return y;
    }

    private void printHeader() {
        System.out.printf("%3s | %12s\n", "x", "y");
        System.out.println("--------------------");

    }

    private void print(double x, double y) {
        System.out.printf("%3s | %12s\n", round(x, 3), round(y, 9));
    }

    private double round(double x, double n) {
        return (long) (x * Math.pow(10, n)) / Math.pow(10, n);
    }

}
