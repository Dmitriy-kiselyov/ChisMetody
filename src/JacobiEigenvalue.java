import java.util.ArrayList;
import java.util.Arrays;

public class JacobiEigenvalue {

    private double[][] matx;

    private ArrayList<StepInfo> steps;
    private double[]            eigenvalue;

    private double[][] rotation;

    public JacobiEigenvalue(double[][] m) {
        matx = new double[m.length][m.length];
        for (int i = 0; i < m.length; i++)
            matx[i] = Arrays.copyOf(m[i], m.length);
    }

    public double[] getEigenvalue() {
        if (eigenvalue != null)
            return Arrays.copyOf(eigenvalue, eigenvalue.length);
        steps = new ArrayList<>();

        double[][] a = copyMatrix(matx);
        while (!isDiagonalMatrix(a)) {
            Coord max = findMax(a);
            double p = 2 * a[max.i][max.j];
            double q = a[max.i][max.i] - a[max.j][max.j];
            double d = Math.sqrt(p * p + q * q);

            double c, s;
            if (q == 0)
                c = s = Math.sqrt(2) / 2;
            else {
                double r = Math.abs(q) / 2 / d;
                double sign = p / q > 0 ? 1 : -1;
                c = Math.sqrt(0.5 + r);
                s = Math.sqrt(0.5 - r) * sign;
            }

            //save this step
            steps.add(new StepInfo(max, c, s));

            double[][] b = copyMatrix(a);
            b[max.i][max.i] = c * c * a[max.i][max.i] + s * s * a[max.j][max.j] + 2 * c * s * a[max.i][max.j];
            b[max.j][max.j] = s * s * a[max.i][max.i] + c * c * a[max.j][max.j] - 2 * c * s * a[max.i][max.j];

            b[max.i][max.j] = b[max.j][max.i] = 0;

            for (int m = 0; m < matx.length; m++) {
                if (m == max.i || m == max.j)
                    continue;

                b[max.i][m] = b[m][max.i] = c * a[m][max.i] + s * a[m][max.j];
                b[max.j][m] = b[m][max.j] = -s * a[m][max.i] + c * a[m][max.j];
            }

            a = b;
        }

        eigenvalue = getDiagonal(a);
        return Arrays.copyOf(eigenvalue, eigenvalue.length);
    }

    public double[][] getRotationMatrix() {
        if (rotation != null)
            return copyMatrix(rotation);
        getEigenvalue();

        //build e
        double[][] e = new double[matx.length][matx.length];
        for (int i = 0; i < matx.length; i++)
            e[i][i] = 1;

        Matrix res = null;

        for (StepInfo info : steps) {
            double[][] a = copyMatrix(e);
            a[info.i][info.i] = a[info.j][info.j] = info.c;
            a[info.j][info.i] = info.s;
            a[info.i][info.j] = -info.s;

            if (res == null)
                res = new Matrix(a);
            else {
                res = res.multiply(new Matrix(a));
            }
        }

        rotation = res.toArray();
        return rotation;
    }

    private double[] getDiagonal(double[][] m) {
        double[] diag = new double[m.length];
        for (int i = 0; i < m.length; i++)
            diag[i] = m[i][i];
        return diag;
    }

    private double[][] copyMatrix(double[][] m) {
        double[][] mc = new double[m.length][];
        for (int i = 0; i < m.length; i++)
            mc[i] = Arrays.copyOf(m[i], m[i].length);
        return mc;
    }

    private boolean isDiagonalMatrix(double[][] m) {
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m.length; j++)
                if (i != j && m[i][j] != 0)
                    return false;

        for (int i = 0; i < m.length; i++)
            if (m[i][i] != 0)
                return true;

        return false;
    }

    private Coord findMax(double[][] m) {
        int ci = 1, cj = 0;
        for (int i = 1; i < m.length; i++) {
            for (int j = 0; j < i; j++)
                if (m[i][j] > m[ci][cj]) {
                    ci = i;
                    cj = j;
                }
        }

        return new Coord(ci, cj);
    }

    private class Coord {
        int i, j;

        Coord(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    private class StepInfo {
        int i, j;
        double c, s;

        StepInfo(Coord coord, double c, double s) {
            i = coord.i;
            j = coord.j;
            this.c = c;
            this.s = s;
        }
    }

}
