import java.util.Arrays;

public class Matrix {

    public static final String SIZE_NOT_MATCH   = "size_not_match";
    public static final String WRONG_POWER      = "wrong_power";
    public static final String NOT_SQUARE       = "not_square";
    public static final String ZERO_DETERMINANT = "zero_determinant";
    public static final String NOT_ARRAY        = "not_array";

    private final double[][] mMatx;
    private final int        mHeight, mWidth;

    public Matrix(double[][] matx) {
        this(matx.length, matx[0].length);
        for (int i = 0; i < mHeight; i++)
            for (int j = 0; j < mWidth; j++)
                set(i, j, matx[i][j]);
    }

    public Matrix(double[] arr, boolean vertical) {
        if (vertical) {
            mMatx = new double[arr.length][1];
            for (int i = 0; i < arr.length; i++)
                mMatx[i][0] = arr[i];
        } else {
            mMatx = new double[1][];
            mMatx[0] = Arrays.copyOf(arr, arr.length);
        }

        mHeight = mMatx.length;
        mWidth = mMatx[0].length;
    }

    public Matrix(int height, int width) {
        this.mHeight = height;
        this.mWidth = width;
        mMatx = new double[height][width];

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                set(i, j, 0);
    }

    public Matrix(Matrix matrix) {
        this(matrix.mMatx);
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void set(int i, int j, double number) {
        mMatx[i][j] = number;
    }

    public double get(int i, int j) {
        return mMatx[i][j];
    }

    /**
     * Adds this matrix to another
     *
     * @param other matrix to add
     * @return sum of two matrices
     * @throws ArithmeticException operation cannot be applied<br />
     *                             {@link #SIZE_NOT_MATCH} - matrices have different sizes
     */
    public Matrix add(Matrix other) throws ArithmeticException {
        //check for size match
        if (mHeight != other.mHeight || mWidth != other.mWidth)
            throw new ArithmeticException(SIZE_NOT_MATCH);

        Matrix matrix = new Matrix(mHeight, mWidth);
        for (int i = 0; i < mHeight; i++)
            for (int j = 0; j < mWidth; j++)
                matrix.set(i, j, get(i, j) + (other.get(i, j)));
        return matrix;
    }

    public Matrix negate() {
        Matrix matrix = new Matrix(mHeight, mWidth);
        for (int i = 0; i < mHeight; i++)
            for (int j = 0; j < mWidth; j++)
                matrix.set(i, j, -get(i, j));
        return matrix;
    }

    /**
     * Subtracts another matrix from this
     *
     * @param other matrix to subtract
     * @return difference of two matrices
     * @throws ArithmeticException operation cannot be applied<br />
     *                             {@link #SIZE_NOT_MATCH} - matrices have different sizes
     */
    public Matrix subtract(Matrix other) throws ArithmeticException {
        return other.negate().add(this);
    }

    public Matrix transpose() {
        Matrix matrix = new Matrix(mWidth, mHeight);
        for (int i = 0; i < mHeight; i++)
            for (int j = 0; j < mWidth; j++)
                matrix.set(j, i, get(i, j));
        return matrix;
    }

    public Matrix multiply(double number) {
        Matrix matrix = new Matrix(mHeight, mWidth);

        for (int i = 0; i < mHeight; i++)
            for (int j = 0; j < mWidth; j++)
                matrix.set(i, j, get(i, j) * (number));
        return matrix;
    }

    /**
     * Multiplies this matrix [N, M] to another [M, K]
     *
     * @param other matrix to multiply
     * @return product of two matrices
     * @throws ArithmeticException operation cannot be applied<br />
     *                             {@link #SIZE_NOT_MATCH} - number of columns of this matrix does not equal to number of rows of another matrix
     */
    public Matrix multiply(Matrix other) throws ArithmeticException {
        if (mWidth != other.mHeight)
            throw new ArithmeticException(SIZE_NOT_MATCH);

        Matrix matrix = new Matrix(mHeight, other.mWidth);
        for (int i = 0; i < matrix.mHeight; i++)
            for (int j = 0; j < matrix.mWidth; j++) {
                double res = 0;
                for (int k = 0; k < mWidth; k++)
                    res = res + (get(i, k) * (other.get(k, j)));
                matrix.set(i, j, res);
            }

        return matrix;
    }

    /**
     * Multiplies matrix to itself N times
     *
     * @param power times to multiply
     * @return matrix to the power
     * @throws ArithmeticException operation cannot be applied<br />
     *                             {@link #WRONG_POWER} - power argument is below one<br />
     *                             {@link #NOT_SQUARE} - only square matrices can be powered
     */
    public Matrix pow(int power) throws ArithmeticException {
        if (power == 1)
            return this;

        if (power < 1)
            throw new ArithmeticException(WRONG_POWER);
        if (mHeight != mWidth)
            throw new ArithmeticException(NOT_SQUARE);

        Matrix matrix = this;
        while (power-- != 1)
            matrix = matrix.multiply(this);
        return matrix;
    }

    /**
     * Calculates matrix determinant
     *
     * @return determinant
     * @throws ArithmeticException operation cannot be applied<br />
     *                             {@link #NOT_SQUARE} - only square matrices can have determinant
     */
    public double determinant() throws ArithmeticException {
        if (mHeight != mWidth)
            throw new ArithmeticException(NOT_SQUARE);

        switch (mWidth) {
            case 1:
                return get(0, 0);
            case 2:
                return get(0, 0) * (get(1, 1)) - (get(0, 1) * (get(1, 0)));
            default:
                //search zero row or column
                for (int i = 0; i < mHeight; i++)
                    if (isZeroRow(i))
                        return 0;
                for (int j = 0; j < mWidth; j++)
                    if (isZeroColumn(j))
                        return 0;

                //search for rows or columns that differ by a constant
                for (int i1 = 0; i1 < mHeight; i1++)
                    for (int i2 = i1 + 1; i2 < mHeight; i2++)
                        if (isRowsDifferByConstant(i1, i2))
                            return 0;
                for (int j1 = 0; j1 < mWidth; j1++)
                    for (int j2 = j1 + 1; j2 < mWidth; j2++)
                        if (isColumnsDifferByConstant(j1, j2))
                            return 0;

                //count it otherwise
                int j = 0;
                while (get(0, j) == 0)
                    j++;

                double det = subtractRow(0, j).submatrix(0, j).determinant() * (get(0, j));
                return j % 2 == 0 ? det : -det;
        }
    }

    private Matrix submatrix(int i, int j) {
        Matrix matrix = new Matrix(mHeight - 1, mWidth - 1);
        int mi = 0, mj = 0;

        for (int ti = 0; ti < mHeight; ti++) {
            if (ti == i)
                continue;
            for (int tj = 0; tj < mWidth; tj++)
                if (tj != j)
                    matrix.set(mi, mj++, get(ti, tj));
            mi++;
            mj = 0;
        }

        return matrix;
    }

    private boolean isZeroRow(int i) {
        for (double a : mMatx[i])
            if (a != 0)
                return false;
        return true;
    }

    private boolean isZeroColumn(int j) {
        for (int i = 0; i < mHeight; i++)
            if (get(i, j) != 0)
                return false;
        return true;
    }

    private Matrix subtractRow(int i, int j) {
        Matrix matrix = new Matrix(this);

        for (int ii = 0; ii < mHeight; ii++) {
            if (ii == i || get(ii, j) == 0)
                continue;

            double mul = get(ii, j) / (get(i, j));
            for (int jj = 0; jj < mWidth; jj++)
                matrix.set(ii, jj, get(ii, jj) - (get(i, jj) * (mul)));
        }

        return matrix;
    }

    private boolean isRowsDifferByConstant(int i1, int i2) {
        if (i1 == i2)
            return false;

        Double diff = null;
        for (int j = 0; j < mWidth; j++) {
            double a = get(i1, j), b = get(i2, j);
            if (a == 0 && b != 0 || a != 0 && b == 0)
                return false;
            if (a == 0 && b == 0)
                continue;

            if (diff == null)
                diff = a / (b);
            else if (!diff.equals(a / (b)))
                return false;
        }

        return true;
    }

    private boolean isColumnsDifferByConstant(int j1, int j2) {
        if (j1 == j2)
            return false;

        Double diff = null;
        for (int i = 0; i < mHeight; i++) {
            double a = get(i, j1), b = get(i, j2);
            if (a == 0 && b != 0 || a != 0 && b == 0)
                return false;
            if (a == 0 && b == 0)
                continue;

            if (diff == null)
                diff = a / (b);
            else if (diff.equals(a / (b)) == false)
                return false;
        }

        return true;
    }

    /**
     * Calculates inverse matrix to this one
     *
     * @return inverse matrix
     * @throws ArithmeticException operation cannot be applied<br />
     *                             {@link #NOT_SQUARE} - inverse matrix can be calculated only for square matrix<br/>
     *                             {@link #ZERO_DETERMINANT} - inverse matrix can be calculated only for matrices with non-zero determinant
     */
    public Matrix inverse() throws ArithmeticException {
        double determinant = determinant(); //throws Exception(NOT_SQUARE)
        if (determinant == 0)
            throw new ArithmeticException(ZERO_DETERMINANT);

        Matrix matrix = new Matrix(mHeight, mWidth);
        for (int i = 0; i < mHeight; i++)
            for (int j = 0; j < mWidth; j++) {
                double det = submatrix(i, j).determinant();
                matrix.set(i, j, (i + j) % 2 == 0 ? det : -det);
            }

        matrix = matrix.transpose();
        matrix = matrix.multiply(1.0 / determinant);

        return matrix;
    }

    public Matrix rang() {
        Matrix matrix = this;

        for (int i = 0, j = 0; i < mHeight && j < mWidth; j++) {
            if (matrix.isZeroColumn(j))
                continue;

            if (matrix.get(i, j) == 0)
                for (int ii = i + 1; ; ii++) //better exception than mistake
                    if (matrix.get(ii, j) != 0) {
                        matrix = matrix.swapRows(ii, i);
                        break;
                    }

            matrix = matrix.divideRow(i, matrix.get(i, j));
            matrix = matrix.subtractRow(i, j);
            for (int jj = j + 1; jj < mWidth; jj++)
                matrix.set(i, jj, 0);
            i++;
        }

        return matrix;
    }

    private Matrix swapRows(int i1, int i2) {
        Matrix matrix = new Matrix(this);
        double[] temp = matrix.mMatx[i1];
        matrix.mMatx[i1] = matrix.mMatx[i2];
        matrix.mMatx[i2] = temp;
        return matrix;
    }

    private Matrix divideRow(int i, double divider) {
        Matrix matrix = new Matrix(this);
        for (int j = 0; j < mWidth; j++)
            matrix.set(i, j, get(i, j) / (divider));
        return matrix;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < mHeight; i++) {
            for (int j = 0; j < mWidth; j++)
                builder.append(mMatx[i][j]).append(' ');
            builder.append('\n');
        }

        return builder.toString();
    }

    /**
     * Converts matrix into two-dimensional array
     *
     * @return two-dimensional array
     */
    public double[][] toArray() {
        double[][] m = new double[mHeight][];
        for (int i = 0; i < m.length; i++)
            m[i] = Arrays.copyOf(mMatx[i], mWidth);
        return m;
    }

    /**
     * Converts matrix into one-dimensional array
     *
     * @return one-dimensional array
     * @throws IllegalStateException matrix's width or height are not 1
     */
    public double[] toOneDimenArray() throws IllegalStateException {
        if (mHeight == 1)
            return Arrays.copyOf(mMatx[0], mWidth);
        if (mWidth == 1) {
            double[] arr = new double[mHeight];
            for (int i = 0; i < mHeight; i++)
                arr[i] = mMatx[i][0];
            return arr;
        }

        throw new IllegalStateException(NOT_ARRAY);
    }

    /**
     * Scalar product of two matrix
     *
     * @param other second one-dimensional matrix
     * @return scalar product
     * @throws IllegalStateException    one or both matrix are not one-dimensional
     * @throws IllegalArgumentException matrix sizes do not match
     */
    public double scalar(Matrix other) throws IllegalStateException, IllegalArgumentException {
        double[] m1 = this.toOneDimenArray();
        double[] m2 = other.toOneDimenArray();
        if (m1.length != m2.length)
            throw new IllegalArgumentException(SIZE_NOT_MATCH);

        double res = 0;
        for (int i = 0; i < m1.length; i++)
            res += m1[i] * m2[i];
        return res;
    }

    /**
     * Norm of matrix (max value of horizontal sum of absolute values)
     *
     * @return max-norm of matrix
     */
    public double mNorm() {
        double norm = 0;
        for (int i = 0; i < mHeight; i++) {
            double sum = 0;
            for (int j = 0; j < mWidth; j++)
                sum += Math.abs(mMatx[i][j]);
            norm = Math.max(norm, sum);
        }

        return norm;
    }

    /**
     * Norm of matrix (square root of sum of all elements squared)
     *
     * @return euclid norm of matrix
     */
    public double euclidNorm() {
        double norm = 0;
        for (int i = 0; i < mHeight; i++) {
            for (int j = 0; j < mWidth; j++)
                norm += mMatx[i][j] * mMatx[i][j];
        }

        return Math.sqrt(norm);
    }
}
