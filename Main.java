import java.util.Scanner;

// Abstraction: Define an abstract class for matrix operations
abstract class MatrixOperations {
    protected double[][] matrix;
    protected int size;

    public MatrixOperations(double[][] inputMatrix) {
        this.size = inputMatrix.length;
        if (size != inputMatrix[0].length) {
            throw new IllegalArgumentException("Matrix must be square");
        }
        this.matrix = inputMatrix;
    }

    // Encapsulation: Hide internal implementation details
    protected double[][] getMinorMatrix(int row, int col) {
        double[][] minor = new double[size - 1][size - 1];
        int mRow = 0, mCol;
        for (int i = 0; i < size; i++) {
            if (i == row)
                continue;
            mCol = 0;
            for (int j = 0; j < size; j++) {
                if (j == col)
                    continue;
                minor[mRow][mCol] = matrix[i][j];
                mCol++;
            }
            mRow++;
        }
        return minor;
    }

    // Abstraction: Define abstract methods for subclasses to implement
    public abstract double getDeterminant();

    public abstract double[][] getInverse();
}

// Inheritance: Extend the base class for specific matrix operations
class MatrixCalculator extends MatrixOperations {
    public MatrixCalculator(double[][] inputMatrix) {
        super(inputMatrix);
    }

    @Override
    public double getDeterminant() {
        return calculateDeterminant(matrix);
    }

    private double calculateDeterminant(double[][] mat) {
        int n = mat.length;
        if (n == 1)
            return mat[0][0];
        if (n == 2)
            return mat[0][0] * mat[1][1] - mat[0][1] * mat[1][0];

        double det = 0;
        for (int j = 0; j < n; j++) {
            det += mat[0][j] * getCofactor(0, j, mat);
        }
        return det;
    }

    private double getCofactor(int row, int col, double[][] mat) {
        double[][] minor = getMinorMatrix(row, col, mat);
        return Math.pow(-1, row + col) * calculateDeterminant(minor);
    }

    // Make this method public to access it outside the class
    public double[][] getMinorMatrix(int row, int col, double[][] mat) {
        int n = mat.length;
        double[][] minor = new double[n - 1][n - 1];
        int mRow = 0, mCol;
        for (int i = 0; i < n; i++) {
            if (i == row)
                continue;
            mCol = 0;
            for (int j = 0; j < n; j++) {
                if (j == col)
                    continue;
                minor[mRow][mCol] = mat[i][j];
                mCol++;
            }
            mRow++;
        }
        return minor;
    }

    @Override
    public double[][] getInverse() {
        double determinant = getDeterminant();
        if (determinant == 0) {
            throw new ArithmeticException("Matrix is singular and cannot have an inverse.");
        }

        double[][] adjoint = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                adjoint[i][j] = getCofactor(i, j, matrix) / determinant;
            }
        }

        return transposeMatrix(adjoint);
    }

    // Make this method public to access it outside the class
    public double[][] transposeMatrix(double[][] mat) {
        double[][] transpose = new double[mat.length][mat.length];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                transpose[i][j] = mat[j][i];
            }
        }
        return transpose;
    }

    // Polymorphism: Overload methods for different functionalities
    public double getMinor(int row, int col) {
        return calculateDeterminant(getMinorMatrix(row, col, matrix));
    }

    public double getCofactor(int row, int col) {
        return Math.pow(-1, row + col) * getMinor(row, col);
    }

    public double[][] getCofactorMatrixAtPosition(int row, int col) {
        double[][] cofactorMatrixAtPosition = new double[size - 1][size - 1];
        int mRow = 0, mCol;
        for (int i = 0; i < size; i++) {
            if (i == row)
                continue;
            mCol = 0;
            for (int j = 0; j < size; j++) {
                if (j == col)
                    continue;
                cofactorMatrixAtPosition[mRow][mCol] = Math.pow(-1, i + j) * matrix[i][j];
                mCol++;
            }
            mRow++;
        }
        return cofactorMatrixAtPosition;
    }

    // Utility method to print matrix
    public static void printMatrix(double[][] mat) {
        for (double[] row : mat) {
            for (double val : row) {
                System.out.printf("%.2f\t", val);
            }
            System.out.println();
        }
        System.out.println();
    }

    // New method for matrix addition
    public double[][] addMatrix(double[][] mat1, double[][] mat2) {
        if (mat1.length != mat2.length || mat1[0].length != mat2[0].length) {
            throw new IllegalArgumentException("Matrices must have the same dimensions for addition.");
        }

        double[][] result = new double[mat1.length][mat1[0].length];
        for (int i = 0; i < mat1.length; i++) {
            for (int j = 0; j < mat1[0].length; j++) {
                result[i][j] = mat1[i][j] + mat2[i][j];
            }
        }
        return result;
    }

    // New method for matrix subtraction
    public double[][] subtractMatrix(double[][] mat1, double[][] mat2) {
        if (mat1.length != mat2.length || mat1[0].length != mat2[0].length) {
            throw new IllegalArgumentException("Matrices must have the same dimensions for subtraction.");
        }

        double[][] result = new double[mat1.length][mat1[0].length];
        for (int i = 0; i < mat1.length; i++) {
            for (int j = 0; j < mat1[0].length; j++) {
                result[i][j] = mat1[i][j] - mat2[i][j];
            }
        }
        return result;
    }

    // New method for matrix multiplication
    public double[][] multiplyMatrix(double[][] mat1, double[][] mat2) {
        if (mat1[0].length != mat2.length) {
            throw new IllegalArgumentException(
                    "Number of columns in the first matrix must be equal to the number of rows in the second matrix.");
        }

        double[][] result = new double[mat1.length][mat2[0].length];
        for (int i = 0; i < mat1.length; i++) {
            for (int j = 0; j < mat2[0].length; j++) {
                for (int k = 0; k < mat1[0].length; k++) {
                    result[i][j] += mat1[i][k] * mat2[k][j];
                }
            }
        }
        return result;
    }
}

// Encapsulation: Separate user input handling into a dedicated class
class MatrixInputHandler {
    private static Scanner scanner = new Scanner(System.in);

    public static double[][] getUserMatrix() {
        System.out.print("Enter the size of square matrix (1-10): ");
        int size = scanner.nextInt();

        // Validate size
        while (size < 1 || size > 10) {
            System.out.print("Invalid size! Enter size (1-10): ");
            size = scanner.nextInt();
        }

        double[][] matrix = new double[size][size];
        System.out.println("Enter matrix elements row by row:");

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.printf("Enter element at position [%d][%d]: ", i, j);
                matrix[i][j] = scanner.nextDouble();
            }
        }

        return matrix;
    }

    public static Scanner getScanner() {
        return scanner;
    }

    public static void setScanner(Scanner scanner) {
        MatrixInputHandler.scanner = scanner;
    }
}

// Main class to demonstrate the program
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Display menu
        while (true) {
            System.out.println("\nMatrix Calculator Menu:");
            System.out.println("1. Matrix Addition");
            System.out.println("2. Matrix Subtraction");
            System.out.println("3. Matrix Multiplication");
            System.out.println("4. Display Original Matrix");
            System.out.println("5. Calculate Minor at position");
            System.out.println("6. Get Minor Matrix at position");
            System.out.println("7. Calculate Cofactor at position");
            System.out.println("8. Get Cofactor Matrix at position");
            System.out.println("9. Get Transpose Matrix");
            System.out.println("10. Get Inverse Matrix");
            System.out.println("11. Calculate Determinant");
            System.out.println("12. Exit");
            System.out.print("Enter your choice (1-12): ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1: {
                    // Matrix Addition
                    System.out.println("Enter the first matrix:");
                    double[][] matrix1 = MatrixInputHandler.getUserMatrix();
                    System.out.println("Enter the second matrix:");
                    double[][] matrix2 = MatrixInputHandler.getUserMatrix();
                    MatrixCalculator calc = new MatrixCalculator(matrix1);
                    System.out.println("Matrix Addition Result:");
                    MatrixCalculator.printMatrix(calc.addMatrix(matrix1, matrix2));
                }
                case 2: {
                    // Matrix Subtraction
                    System.out.println("Enter the first matrix:");
                    double[][] matrix1 = MatrixInputHandler.getUserMatrix();
                    System.out.println("Enter the second matrix:");
                    double[][] matrix2 = MatrixInputHandler.getUserMatrix();
                    MatrixCalculator calc = new MatrixCalculator(matrix1);
                    System.out.println("Matrix Subtraction Result:");
                    MatrixCalculator.printMatrix(calc.subtractMatrix(matrix1, matrix2));
                }
                case 3: {
                    // Matrix Multiplication
                    System.out.println("Enter the first matrix:");
                    double[][] matrix1 = MatrixInputHandler.getUserMatrix();
                    System.out.println("Enter the second matrix:");
                    double[][] matrix2 = MatrixInputHandler.getUserMatrix();
                    MatrixCalculator calc = new MatrixCalculator(matrix1);
                    System.out.println("Matrix Multiplication Result:");
                    MatrixCalculator.printMatrix(calc.multiplyMatrix(matrix1, matrix2));
                }
                case 4: {
                    // Display Original Matrix
                    System.out.println("Enter the matrix:");
                    double[][] matrix1 = MatrixInputHandler.getUserMatrix();
                    @SuppressWarnings("unused")
                    MatrixCalculator calc = new MatrixCalculator(matrix1);
                    System.out.println("Original Matrix:");
                    MatrixCalculator.printMatrix(matrix1);
                }
                case 5: {
                    // Calculate Minor at position
                    System.out.println("Enter the matrix:");
                    double[][] matrix1 = MatrixInputHandler.getUserMatrix();
                    MatrixCalculator calc = new MatrixCalculator(matrix1);
                    System.out.print("Enter row: ");
                    int row = scanner.nextInt();
                    System.out.print("Enter column: ");
                    int col = scanner.nextInt();
                    System.out.println("Minor: " + calc.getMinor(row, col));
                }
                case 6: {
                    // Get Minor Matrix at position
                    System.out.println("Enter the matrix:");
                    double[][] matrix1 = MatrixInputHandler.getUserMatrix();
                    MatrixCalculator calc = new MatrixCalculator(matrix1);
                    System.out.print("Enter row: ");
                    int row = scanner.nextInt();
                    System.out.print("Enter column: ");
                    int col = scanner.nextInt();
                    System.out.println("Minor Matrix:");
                    MatrixCalculator.printMatrix(calc.getMinorMatrix(row, col, matrix1));
                }
                case 7: {
                    // Calculate Cofactor at position
                    System.out.println("Enter the matrix:");
                    double[][] matrix1 = MatrixInputHandler.getUserMatrix();
                    MatrixCalculator calc = new MatrixCalculator(matrix1);
                    System.out.print("Enter row: ");
                    int row = scanner.nextInt();
                    System.out.print("Enter column: ");
                    int col = scanner.nextInt();
                    System.out.println("Cofactor: " + calc.getCofactor(row, col));
                }
                case 8: {
                    // Get Cofactor Matrix at position
                    System.out.println("Enter the matrix:");
                    double[][] matrix1 = MatrixInputHandler.getUserMatrix();
                    MatrixCalculator calc = new MatrixCalculator(matrix1);
                    System.out.print("Enter row: ");
                    int row = scanner.nextInt();
                    System.out.print("Enter column: ");
                    int col = scanner.nextInt();
                    System.out.println("Cofactor Matrix at position [" + row + "][" + col + "]:");
                    MatrixCalculator.printMatrix(calc.getCofactorMatrixAtPosition(row, col));
                }
                case 9: {
                    // Get Transpose Matrix
                    System.out.println("Enter the matrix:");
                    double[][] matrix1 = MatrixInputHandler.getUserMatrix();
                    MatrixCalculator calc = new MatrixCalculator(matrix1);
                    System.out.println("Transpose Matrix:");
                    MatrixCalculator.printMatrix(calc.transposeMatrix(matrix1));
                }
                case 10: {
                    // Get Inverse Matrix
                    System.out.println("Enter the matrix:");
                    double[][] matrix1 = MatrixInputHandler.getUserMatrix();
                    MatrixCalculator calc = new MatrixCalculator(matrix1);
                    try {
                        System.out.println("Inverse Matrix:");
                        MatrixCalculator.printMatrix(calc.getInverse());
                    } catch (ArithmeticException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 11: {
                    // Calculate Determinant
                    System.out.println("Enter the matrix:");
                    double[][] matrix1 = MatrixInputHandler.getUserMatrix();
                    MatrixCalculator calc = new MatrixCalculator(matrix1);
                    System.out.println("Determinant: " + calc.getDeterminant());
                }
                case 12: {
                    // Exit
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                }
                default: {
                    System.out.println("Invalid choice! Please select 1-12");
                }
            }
        }
    }
}