import java.util.Arrays;

/**
 * Метод Гауса и обратная матрица
 */
public class Gauss {
    private double[][] matrix;
    private Solution   solution;

    public Gauss(double[][] m) {
        matrix = copyMatrix(m);
    }

    public Solution getSolution() {
        if (solution != null)
            return solution;

        double[][] matrix = copyMatrix(this.matrix);
        for (int i = 0; i < matrix.length; i++) {
            int maxLine = i;
            for (int j = i; j < matrix.length; j++)
                if (Math.abs(matrix[j][i]) > Math.abs(matrix[maxLine][i]))
                    maxLine = j;

            if (Math.abs(matrix[maxLine][i]) < 1e-7)
                continue;
            swapLines(matrix, i, maxLine);

            // scale
            for (int j = i + 1; j < matrix.length; j++) {
                double mult = matrix[j][i] / matrix[i][i];
                for (int k = 0; k < matrix[j].length; k++)
                    matrix[j][k] = matrix[i][k] * mult - matrix[j][k];
            }
        }

        State state = getMatrixState();
        if (state != State.ONE_SOLUTION) {
            solution = new Solution(state, null);
            return solution;
        }

        //reverse
        double[] answer = new double[matrix.length];
        for (int i = matrix.length - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < matrix.length; j++)
                sum += answer[j] * matrix[i][j];
            answer[i] = (matrix[i][matrix.length] - sum) / matrix[i][i];
        }

        //round answer
        for (int i = 0; i < answer.length; i++) {
            if (!Double.isNaN(answer[i])) {
                double v = answer[i] * 1_000_000_000;
                v = (long) v;
                v /= 1_000_000_000;
                answer[i] = v;
            }
        }

        solution = new Solution(State.ONE_SOLUTION, answer);
        return solution;
    }

    public Gauss getReversed() throws Exception {
        if (matrix.length != matrix[0].length)
            throw new Exception("Not a square matrix!");

        double[][] matrix = new double[this.matrix.length][];
        for (int i = 0; i < matrix.length; i++)
            matrix[i] = Arrays.copyOf(this.matrix[i], this.matrix.length + 1);
        double[][] ans = new double[matrix.length][matrix.length];

        for (int j = 0; j < matrix.length; j++) {
            for (int i = 0; i < matrix.length; i++)
                matrix[i][matrix.length] = 0;
            matrix[j][matrix.length] = 1;

            Solution solution = new Gauss(matrix).getSolution();
            if (solution.getState() != State.ONE_SOLUTION)
                return null;

            for (int i = 0; i < matrix.length; i++)
                ans[i][j] = solution.getAnswer()[i];
        }

        return new Gauss(ans);
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < matrix.length; i++) {
            if (i != 0)
                s += "\n";
            s += Arrays.toString(matrix[i]);
        }
        return s;
    }

    private State getMatrixState() {
        State state = State.ONE_SOLUTION;
        for (int i = 0; i < matrix.length; i++) {
            boolean zeroLine = true;
            for (int j = 0; j < matrix.length; j++) {
                if (Math.abs(matrix[i][j]) > 1e-7) {
                    zeroLine = false;
                    break;
                }
            }

            if (zeroLine) {
                if (Math.abs(matrix[i][matrix.length]) > 1e-7)
                    return State.NO_SOLUTION;
                else
                    state = State.INFINITE_SOLUTIONS;
            }
        }

        return state;
    }

    private void swapLines(double[][] matrix, int i, int j) {
        double[] arrI = matrix[i];
        matrix[i] = matrix[j];
        matrix[j] = arrI;
    }

    private double[][] copyMatrix(double[][] matrix) {
        double[][] m = new double[matrix.length][];
        for (int i = 0; i < m.length; i++)
            m[i] = Arrays.copyOf(matrix[i], matrix[i].length);
        return m;
    }

    public enum State {ONE_SOLUTION, NO_SOLUTION, INFINITE_SOLUTIONS}

    public class Solution {
        private final State    state;
        private final double[] ans;

        private Solution(State state, double[] ans) {
            this.state = state;
            this.ans = ans;
        }

        public State getState() {
            return state;
        }

        public double[] getAnswer() {
            return Arrays.copyOf(ans, ans.length);
        }
    }

}
