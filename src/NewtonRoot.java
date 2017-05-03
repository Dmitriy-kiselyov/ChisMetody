public class NewtonRoot {

    private Derivative2Function func;

    public NewtonRoot(Derivative2Function func) {
        this.func = func;
    }

    public double getRoot(double a, double b, double x0, double eps) throws IllegalArgumentException {
        if (Math.abs(a - b) <= eps)
            throw new IllegalArgumentException("a и b слишком близко друг к другу");
        if (a > b) {
            double t = a;
            a = b;
            b = t;
        }

        if (x0 < a || x0 > b)
            throw new IllegalArgumentException("x0 не принадлежит промежутку [a, b]");

        if (func.get(x0) * func.derivative2(x0) <= 0)
            throw new IllegalArgumentException("f(x0) * f''(x0) > 0 не выполняется!");

        double x = x0;
        while (Math.abs(func.get(x) / func.derivative(x)) >= eps) {
            x = x - func.get(x) / func.derivative(x);
        }

        return x;
    }

    public interface Derivative2Function {
        double get(double x);

        double derivative(double x);

        double derivative2(double x);
    }
}
