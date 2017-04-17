import java.util.Arrays;

/**
 * Created by Pussy_penetrator on 21.03.2017.
 */
public class Holetskiy {

    private double[][] coef;
    private double[]   free;
    private double[]   answer;

    public Holetskiy(double[][] matrix) {
        coef = new double[matrix.length][];
        for (int i = 0; i < matrix.length; i++)
            this.coef[i] = Arrays.copyOf(matrix[i], matrix[i].length - 1);

        free = new double[matrix.length];
        for (int i = 0; i < free.length; i++)
            free[i] = matrix[i][matrix.length];
    }

    public double[] getAnswer() {
        if (answer != null)
            return answer;

        double[][] matxU = new double[coef.length][coef.length];
        for (int i = 0; i < coef.length; i++) {
            for (int j = 0; j < i; j++) {
                double sum = 0;
                for (int k = 0; k < j; k++)
                    sum += matxU[i][k] * matxU[j][k];
                matxU[i][j] = (coef[i][j] - sum) / matxU[j][j];
            }

            double sum = 0;
            for (int k = 0; k < i; k++)
                sum += matxU[i][k] * matxU[i][k];
            matxU[i][i] = Math.sqrt(coef[i][i] - sum);
        }

        // get Y
        double[] y = new double[matxU.length];
        for (int i = 0; i < matxU.length; i++) {
            double sum = 0;
            for (int j = 0; j < i; j++)
                sum += matxU[i][j] * y[j];
            sum = free[i] - sum;
            y[i] = sum / matxU[i][i];
        }

        //get X
        matxU = transpose(matxU);
        answer = new double[matxU.length];

        for (int i = answer.length - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < answer.length; j++)
                sum += matxU[i][j] * answer[j];
            sum = y[i] - sum;
            answer[i] = sum / matxU[i][i];
        }

        return answer;
    }

    private double[][] transpose(double[][] m) {
        double[][] t = new double[m[0].length][m.length];
        for (int i = 0; i < t.length; i++)
            for (int j = 0; j < t[i].length; j++)
                t[i][j] = m[j][i];
        return t;
    }

    private void print(double[][] m) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++)
                System.out.print(m[i][j] + " ");
            System.out.println();
        }
    }

}
