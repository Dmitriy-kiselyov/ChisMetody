/**
 * Created by Pussy_penetrator on 10.03.2017.
 */
public class MinDiscrepancy {

    private double precision;
    private Matrix coef;
    private Matrix free;

    private double[] answer;

    /**
     * Solves matrix equation with minimum discrepancy method
     *
     * @param coef      symmetrical matrix!!!
     * @param free      free terms
     * @param precision accuracy of calculations
     * @throws IllegalArgumentException
     */
    public MinDiscrepancy(double[][] coef, double[] free, double precision) throws IllegalArgumentException {
        if (coef.length != free.length || coef.length != coef[0].length)
            throw new IllegalArgumentException();

        this.precision = precision;
        this.coef = new Matrix(coef);
        this.free = new Matrix(free, true);
    }

    public double[] getAnswer() {
        if (answer != null)
            return answer;

        //discrepancy
        Matrix x = new Matrix(coef.getHeight(), 1);
        Matrix csi = new Matrix(coef.getHeight(), 1);
        csi.set(0, 0, Integer.MAX_VALUE);

        while(csi.mNorm() > precision) {
            csi = coef.multiply(x).subtract(free);
            Matrix coefAndCsi = coef.multiply(csi);
            double scalar = coefAndCsi.scalar(csi) / coefAndCsi.scalar(coefAndCsi);
            x = x.subtract(csi.multiply(scalar));
        }

        answer = x.toOneDimenArray();
        return answer;
    }

}
