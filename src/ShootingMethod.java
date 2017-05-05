import java.util.function.BiFunction;

public class ShootingMethod {

    private final double H = 0.1;

    private BiFunction<Double, Double, Double> func;
    private double                             y0, y1;

    public ShootingMethod(BiFunction<Double, Double, Double> func, double y0, double y1) {
        this.func = func;
        this.y0 = y0;
        this.y1 = y1;
    }

    public void printResult(double n0, double n1, double eps) {
        double p0 = p(n0), p1 = p(n1);

        double n2 = n1 - (n1 - n0) / (p1 - p0) * (p1 - y1);
        double p2 = p(n2);
        while (Math.abs(p2 - y1) > eps) {
            n0 = n1;
            p0 = p1;
            n1 = n2;
            p1 = p2;

            n2 = n1 - (n1 - n0) / (p1 - p0) * (p1 - y1);
            p2 = p(n2);
        }

        System.out.println("Result: " + n2);
    }

    private double p(double z0) {
        System.out.println("z0 = " + z0);
        printHeader();
        double y0 = this.y0;

        for (int i = 0; i < 10; i++) {
            double x0 = 0.1 * i;
            double k1 = H * z0;
            double l1 = H * func.apply(x0, y0);

            double k2 = H * (z0 + l1 / 2);
            double l2 = H * func.apply(x0 + H / 2, y0 + k1 / 2);

            double k3 = H * (z0 + l2 / 2);
            double l3 = H * func.apply(x0 + H / 2, y0 + k2 / 2);

            double k4 = H * (z0 + l3);
            double l4 = H * func.apply(x0 + H, y0 + k3);

            print(x0, y0, z0);

            y0 += (k1 + 2 * k2 + 2 * k3 + k4) / 6.0;
            z0 += (l1 + 2 * l2 + 2 * l3 + l4) / 6.0;
        }

        print(1, y0, z0);

        System.out.println("-----------------------------");
        return y0;
    }

    private void printHeader() {
        System.out.printf("%3s | %7s | %7s\n", "x", "y", "z");
        System.out.println("-----------------------");

    }

    private void print(double x, double y, double z) {
        System.out.printf("%3s | %7s | %7s\n", round(x, 3), round(y, 4), round(z, 4));
    }

    private double round(double x, double n) {
        return (long) (x * Math.pow(10, n)) / Math.pow(10, n);
    }

}
