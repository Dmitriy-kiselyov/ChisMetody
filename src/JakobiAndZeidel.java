import java.util.Arrays;

public class JakobiAndZeidel {

    private double     precision;
    private double[][] alpha;
    private double     norm;
    private double[]   free;

    private double[] answerJakobi;
    private double[] answerZeidel;

    public JakobiAndZeidel(double[][] matrix, double precision) throws IllegalArgumentException {
        this.precision = precision;
        check(matrix);
    }

    private void check(double[][] matrix) throws IllegalArgumentException {
        //get alpha and free
        alpha = new double[matrix.length][];
        for (int i = 0; i < matrix.length; i++)
            alpha[i] = Arrays.copyOf(matrix[i], matrix.length);
        free = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++)
            free[i] = matrix[i][matrix.length];

        //convert alpha and free
        for (int i = 0; i < matrix.length; i++) {
            double val = matrix[i][i];
            for (int j = 0; j < matrix.length; j++)
                alpha[i][j] /= (-val);
            free[i] /= val;
        }
        for (int i = 0; i < matrix.length; i++)
            alpha[i][i] = 0;

        //check if solvable
        this.norm = norm(alpha);
        if (norm >= 1)
            throw new IllegalArgumentException("Норма больше 1, решайте систему методом Гауса");
    }

    private double norm(double[][] m) {
        double norm = 0;
        for (int i = 0; i < m.length; i++) {
            double sum = 0;
            for (int j = 0; j < m[i].length; j++)
                sum += Math.abs(m[i][j]);
            norm = Math.max(norm, sum);
        }

        return norm;
    }

    private double norm(double[] m) {
        double norm = 0;
        for (double a : m) {
            norm = Math.max(norm, Math.abs(a));
        }

        return norm;
    }

    public double[] getZeidelAnswer() {
        if (answerZeidel != null)
            return answerZeidel;

        int tries = 1000;

        double[] x = Arrays.copyOf(free, free.length);
        while (tries != 0) {
            //count new x
            double[] newX = Arrays.copyOf(free, free.length);
            for (int i = 0; i < newX.length; i++) {
                for (int j = 0; j < i; j++)
                    newX[i] += alpha[i][j] * newX[j];
                for (int j = i; j < alpha.length; j++)
                    newX[i] += alpha[i][j] * x[j];
            }

            //compare
            double norm = norm(subtract(newX, x));
            norm *= (this.norm / (1 - this.norm));
            if (norm < precision)
                break;

            //else
            x = newX;
            tries--;
        }

        if (tries == 0)
            return null; //no answer for some reason

        answerZeidel = x;
        return answerZeidel;
    }

    public double[] getJakobiAnswer() {
        if (answerJakobi != null)
            return answerJakobi;

        int tries = 1000;

        double[] x = Arrays.copyOf(free, free.length);
        while (tries != 0) {
            //count new x
            double[] newX = Arrays.copyOf(free, free.length);
            for (int i = 0; i < newX.length; i++) {
                for (int j = 0; j < alpha.length; j++)
                    newX[i] += alpha[i][j] * x[j];
            }

            //compare
            double norm = norm(subtract(newX, x));
            norm *= (this.norm / (1 - this.norm));
            if (norm < precision)
                break;

            //else
            x = newX;
            tries--;
        }

        if (tries == 0)
            return null; //no answer for some reason

        answerJakobi = x;
        return answerJakobi;
    }

    private double[] subtract(double[] m1, double[] m2) {
        double[] m = new double[m1.length];
        for (int i = 0; i < m.length; i++)
            m[i] = m1[i] - m2[i];
        return m;
    }

    @Override
    public String toString() {
        String s = "norma = " + norm + "\n";
        s += "alpha = \n";
        for (int i = 0; i < alpha.length; i++) {
            if (i != 0)
                s += "\n";
            s += Arrays.toString(alpha[i]);
        }

        s += "\nfree = ";
        s += Arrays.toString(free);

        s += "\nanswerJakobi = ";
        s += Arrays.toString(answerJakobi);

        s += "\nanswerZeidel = ";
        s += Arrays.toString(answerZeidel);

        return s;
    }

}
