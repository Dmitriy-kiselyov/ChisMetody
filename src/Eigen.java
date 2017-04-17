import java.util.Arrays;

/**
 * Created by Pussy_penetrator on 02.04.2017.
 */
public class Eigen {

    private double[][] matx;
    private Double     maxEigenvalue;
    private double[]   eigenVector;
    private double     precision;

    public Eigen(double[][] matx, double precision) {
        this.matx = copyMatrix(matx);
        this.precision = precision;
    }

    public double getMaxEigenvalue() {
        if (maxEigenvalue == null)
            countMax();
        return maxEigenvalue;
    }

    public double[] getEigenVector() {
        if (eigenVector == null)
            countMax();
        return Arrays.copyOf(eigenVector, eigenVector.length);
    }

    private void countMax() {
        Matrix a = new Matrix(matx);

        double[] arr = new double[matx.length];
        Arrays.fill(arr, 1);
        Matrix x0 = new Matrix(arr, true);

        double lymPrev = 0;
        for (int tryCnt = 0; tryCnt < 1000; tryCnt++) {
            Matrix e = x0.multiply(1 / x0.euclidNorm());
            Matrix x1 = a.multiply(e);
            double lym = x1.scalar(e);

            if (Math.abs(lymPrev - lym) < precision) {
                maxEigenvalue = lym;
                eigenVector = e.toOneDimenArray();
                return;
            }

            x0 = x1;
            lymPrev = lym;
        }

        //error in algorithm, no result
    }

    private double[][] copyMatrix(double[][] m) {
        double[][] mc = new double[m.length][];
        for (int i = 0; i < m.length; i++)
            mc[i] = Arrays.copyOf(m[i], m[i].length);
        return mc;
    }
}
