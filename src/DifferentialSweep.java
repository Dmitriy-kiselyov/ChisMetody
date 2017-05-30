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
        double[] z1, z2;
        z1 = new double[11];
        z2 = new double[11];
        z1[0] = -a0 / b0;
        z2[0] = a / b0;

        for (int i = 1; i <= 10; i++) {
            double xi = H * (i - 1);

            BiFunction<Double, Double, Double> f1 = (x, z) -> -(z * z) - px.apply(x) * z + qx.apply(x);
            z1[i] = z1[i - 1] + runge(f1, xi, z1[i - 1]);

            final double z1F = z1[i];
            BiFunction<Double, Double, Double> f2 = (x, z) -> -z * (z1F + px.apply(x)) + fx.apply(x);
            z2[i] = z2[i - 1] + runge(f2, xi, z2[i - 1]);
        }

        printHeader3();
        for (int i = 0; i <= 10; i++)
            print(H * i, z1[i], z2[i]);

        //backwards
        double[] y = new double[11];
        y[10] = (b - b1 * z2[10]) / (a1 + b1 * z1[10]);

        for (int i = 9; i >= 0; i--) {
            double xi = H * (i + 1);

            final double z1F = z1[i + 1];
            final double z2F = z2[i + 1];
            BiFunction<Double, Double, Double> f = (x, yy) -> z1F * yy + z2F;
            y[i] = y[i + 1] - runge(f, xi, y[i + 1]);
        }

        printHeader2();
        for (int i = 10; i >= 0; i--)
            print(H * i, y[i]);
    }

    private double runge(BiFunction<Double, Double, Double> func, double x, double y) {
        double k1 = H * func.apply(x, y);
        double k2 = H * func.apply(x + H / 2, y + k1 / 2);
        double k3 = H * func.apply(x + H / 2, y + k2 / 2);
        double k4 = H * func.apply(x + H, y + k3);
        return (k1 + 2 * k2 + 2 * k3 + k4) / 6.0;
    }

    private void printHeader2() {
        System.out.printf("%3s | %12s\n", "x", "y");
        System.out.println("--------------------");
    }

    private void printHeader3() {
        System.out.printf("%3s | %12s | %12s\n", "x", "z1", "z2");
        System.out.println("---------------------------------");
    }

    private void print(double x, double y) {
        System.out.printf("%3s | %12s\n", round(x, 3), round(y, 9));
    }

    private void print(double x, double z1, double z2) {
        System.out.printf("%3s | %12s | %12s\n", round(x, 3), round(z1, 9), round(z2, 9));
    }

    private double round(double x, double n) {
        return (long) (x * Math.pow(10, n)) / Math.pow(10, n);
    }

}
