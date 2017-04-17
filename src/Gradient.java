/**
 * Created by Pussy_penetrator on 10.03.2017.
 */
public class Gradient {

    private double precision;
    private Matrix coef;
    private Matrix free;

    private double[] answer;

    /**
     * Solves matrix equation with conjugate gradient method
     *
     * @param coef      symmetrical matrix!!!
     * @param free      free terms
     * @param precision accuracy of calculations
     * @throws IllegalArgumentException
     */
    public Gradient(double[][] coef, double[] free, double precision) throws IllegalArgumentException {
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
        Matrix csi = new Matrix(free);
        Matrix p = new Matrix(csi);
        Matrix q = coef.multiply(p);
        double scalar = csi.scalar(p) / q.scalar(p);
        Matrix x = p.multiply(scalar);
        csi = csi.subtract(q.multiply(scalar));
        if (csi.mNorm() <= precision) {
            answer = x.toOneDimenArray();
            return answer;
        }

        //next steps
        while (csi.mNorm() > precision) {
            double b = csi.scalar(q) / p.scalar(q);
            p = csi.subtract(p.multiply(b));
            q = coef.multiply(p);
            scalar = csi.scalar(p) / q.scalar(p);
            x = x.add(p.multiply(scalar));
            csi = csi.subtract(q.multiply(scalar));
        }

        answer = x.toOneDimenArray();
        return answer;
    }

}
