import java.io.DataOutputStream;
import java.io.IOException;

public class RemoteProcedureForMatrixMultiply {

    DataOutputStream dataOutput;
    private int m1r;
    private int m1c;
    private int m2r;
    private int m2c;
    private int m3r;
    private int m3c;
    private int[][] matrixA;
    private int[][] matrixB;
    private int[][] matrixC;
    private String result ="";


    public RemoteProcedureForMatrixMultiply(String inputParameters, DataOutputStream dataOutput) throws IOException{

        this.dataOutput = dataOutput;
        serverStubForMatrixMultiply(inputParameters);

    }

    private void serverStubForMatrixMultiply(String inputParameters) throws IOException{

        System.out.println("Server stub unpacking message.");
        String[] matrices = inputParameters.split("matrix");
        String rowsOfMatrixA[] = matrices[1].split("row");
        String rowsOfMatrixB[] = matrices[2].split("row");
        String rowsOfMatrixC[] = matrices[3].split("row");

        m1r = rowsOfMatrixA.length-1;
        m2r = rowsOfMatrixB.length-1;
        m3r = rowsOfMatrixC.length-1;

        String rowElementsOfMatrixA[][] = new String[m1r][];
        for (int i=1; i<= m1r; i++){
            rowElementsOfMatrixA[i-1] = rowsOfMatrixA[i].split(",");
        }

        String rowElementsOfMatrixB[][] = new String[m2r][];
        for (int i=1; i<= m2r; i++){
            rowElementsOfMatrixB[i-1] = rowsOfMatrixB[i].split(",");
        }

        String rowElementsOfMatrixC[][] = new String[m3r][];
        for (int i=1; i<= m3r; i++){
            rowElementsOfMatrixC[i-1] = rowsOfMatrixC[i].split(",");
        }

        m1c = rowElementsOfMatrixA[0].length;
        m2c = rowElementsOfMatrixB[0].length;
        m3c = rowElementsOfMatrixC[0].length;

        matrixA = new int[m1r][m1c];
        matrixB = new int[m2r][m2c];
        matrixC = new int[m3r][m3c];

        constructMatrix(matrixA, rowElementsOfMatrixA, m1r, m1c);
        constructMatrix(matrixB, rowElementsOfMatrixB, m2r, m2c);
        constructMatrix(matrixC, rowElementsOfMatrixC, m3r, m3c);

        System.out.println("\n server stub making local call to matrixMultiply.");
        matrixMultiply(matrixA, matrixB, matrixC);
    }

    private void constructMatrix(int[][] matrix, String[][] rowElementsOfMatrix, int rows, int cols) {
        System.out.println("\n Matrix::");
        for (int i=0; i < rows; i++){
            System.out.println();
            for (int j=0; j< cols; j++){
                matrix[i][j] = Integer.parseInt(rowElementsOfMatrix[i][j]);
                System.out.print(matrix[i][j] + " ");
            }
        }
    }

    private void matrixMultiply(int[][] matrixA, int[][] matrixB, int[][] matrixC) throws IOException{

        System.out.println("Implementation of Matrix Multiplication on Server.");
        if( m1c == m2r && m2c == m3r){

            result += "Success;matrix_multiply(matrixA, matrixB, matrixC) is equal to ::";
            dataOutput.writeUTF(result);
            int[][] interMediateResult = new int[m1r][m2c];
            int[][] finalResult = new int[m1r][m3c];

            for (int i=0; i< m1r; i++){
                for (int j=0; j< m2c; j++){
                    for (int k=0; k<m2r; k++){
                        interMediateResult[i][j] += matrixA[i][k] * matrixB[k][j];
                    }
                }
            }

            // calcu;te the final resulting matrix and send it to client
            for (int i=0; i< m1r; i++){
                System.out.println();
                for (int j=0; j< m3c; j++){
                    for (int k=0; k<m3r; k++){
                        finalResult[i][j] += interMediateResult[i][k] * matrixC[k][j];
                    }
                    System.out.print(finalResult[i][j] + " ");
                    dataOutput.writeInt(finalResult[i][j]);
                }
            }

        }
        else {
            result += "Failure;Matrix multiplication not possible due to invalid orders of the matrices.";
            System.out.println(result);
            dataOutput.writeUTF(result);
        }

    }
}
