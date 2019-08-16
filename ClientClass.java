import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientClass {

    private Socket socket = null;
    private DataInputStream dataInput = null;
    private DataOutputStream dataOutput = null;
    private int procChosen = 0;
    private String message = "";
    private int n=0, m1r=0, m1c=0, m2r=0, m2c=0, m3r=0, m3c=0;

    public ClientClass(InetAddress address, int port) {
        // establish a connection
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");
            // takes input from terminal
            dataInput = new DataInputStream(socket.getInputStream());
            //sends output through the socket
            dataOutput = new DataOutputStream(socket.getOutputStream());

            getProcedureCallDetails();

        } catch (UnknownHostException u) {
            System.out.println(u);
        } catch (IOException i) {
            System.out.println(i);
        } finally {
            try {
                dataInput.close();
                dataOutput.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getProcedureCallDetails() throws IOException{

        Scanner scn = new Scanner(System.in);
        System.out.println("Which procedure do you want to call? Enter a number from (1-4):");
        System.out.println("1. calculate_pi()");
        System.out.println("2.  add(i, j)");
        System.out.println("3. sort(arrayA)");
        System.out.println("4. matrix_multiply(matrixA, matrixB, matrixC)");

        procChosen = Integer.parseInt(scn.nextLine());

        switch (procChosen){
            case 1:
                    System.out.println("calculate_pi() has been chosen.");
                    System.out.println("Client calling calculate_pi");
                    clientStubForCalPi("calculate_pi");

                    System.out.println("Result obtained from server: ");
                    System.out.println(dataInput.readDouble());
                    break;

            case 2:
                    System.out.println("add two numbers has been chosen.");
                    System.out.println("Read inputs: i,j from client.");
                    readInputForAdd(scn);

                    System.out.println("Sum returned from Server : ");
                    System.out.println(dataInput.readDouble());
                    break;

            case 3:
                    System.out.println("sort Array has been chosen.");
                    System.out.println("Read array elements as inputs from client.");
                    readInputForSort(scn);

                    System.out.println("Array returned after sorting from server:");
                    for (int i=0; i < n; i++){
                        System.out.println(dataInput.readDouble());
                    }
                    break;
            case 4:
                    System.out.println("matrix_multiply has been chosen.");
                    System.out.println("Read 3 matrices from client.");
                    readInputForMatrixMultiply(scn);

                    System.out.println("Matrix multiplication result returned from server:");
                    String[] result = dataInput.readUTF().split(";");
                    if (result[0].equalsIgnoreCase("failure")){
                        System.out.println(result[1]);
                    }
                    // successful , print the resultant matrix
                    else {
                        System.out.println(result[0] + "\n" + result[1]);
                        for (int i=0; i < m1r; i++){
                            System.out.println();
                            for (int j=0; j< m3c; j++){
                                System.out.print(dataInput.readInt() + " ");
                            }
                        }
                    }
                    break;

            default:
                    System.out.println("Invalid option chosen.");
                    break;
        }
    }

    private void clientStubForCalPi(String proc) throws IOException{
        dataOutput.writeUTF(proc);
    }

    private void readInputForAdd(Scanner scn) throws IOException{
        System.out.println("Enter the first number (i) :");
        double i = scn.nextDouble();
        System.out.println("Enter the Second number (j) :");
        double j = scn.nextDouble();
        System.out.println("Client calling add("+i+", "+j+")");
        clientStubForAdd("add", i, j);

    }

    private void clientStubForAdd(String proc, double i, double j) throws IOException {
        System.out.println("Client stub building message.");
        message += "add;";
        message += i + ",";
        message += j;
        dataOutput.writeUTF(message);
    }

    private void readInputForSort(Scanner scn) throws IOException{

        System.out.println("Enter number of elements to be inserted in the Array");

        n = scn.nextInt();
        double[] array = new double[n];
        double x = -100;

        System.out.println("Enter " + n + " elements into the array.");

        for (int i=0; i < n; i++) {
            x = scn.nextDouble();
            array[i] = x;
        }

        System.out.println("Client calling - sort(array).");
        clientStubForSortArray("sort", array);
    }

    private void clientStubForSortArray(String sort, double[] array) throws IOException{

        System.out.println("Client stub building message.");
        message += "sortArray;";
        for(int i=0; i< array.length-1; i++)
        {
            message += array[i]+",";
        }
        message += array[array.length-1];

        dataOutput.writeUTF(message);
    }


    private void readInputForMatrixMultiply(Scanner scn) throws IOException {

        System.out.println("Enter number of rows for first matrix :");
        m1r = scn.nextInt();
        System.out.println("Enter number of columns for first matrix :");
        m1c = scn.nextInt();
        int[][] matrixA = new int[m1r][m1c];
        System.out.println("Enter elements of MatrixA with " + m1r + " rows and "+ m1c + " columns.");
        readMatrixElememts(scn, matrixA, m1r, m1c);

        System.out.println("Enter number of rows for second matrix :");
        m2r = scn.nextInt();
        System.out.println("Enter number of columns for second matrix :");
        m2c = scn.nextInt();
        int[][] matrixB = new int[m2r][m2c];
        System.out.println("Enter elements of MatrixB with " + m1r + " rows and "+ m1c + " columns.");
        readMatrixElememts(scn, matrixB, m2r, m2c);

        System.out.println("Enter number of rows for third matrix :");
        m3r = scn.nextInt();
        System.out.println("Enter number of columns for third matrix :");
        m3c = scn.nextInt();
        int[][] matrixC = new int[m3r][m3c];
        System.out.println("Enter elements of MatrixC with " + m1r + " rows and "+ m1c + " columns.");
        readMatrixElememts(scn, matrixC, m3r, m3c);

        clientStubForMatrixMultiply("matrixMultiply", matrixA, matrixB, matrixC);
    }

    private void clientStubForMatrixMultiply(String matrixMultiply, int[][] matrixA, int[][] matrixB, int[][] matrixC) throws IOException {

        System.out.println("Client stub building message.");

        message += matrixMultiply+";matrix";
        wrapMatrixIntoMessage(matrixA, m1r, m1c);
        message += "matrix";
        wrapMatrixIntoMessage(matrixB, m2r, m2c);
        message += "matrix";
        wrapMatrixIntoMessage(matrixC, m3r, m3c);
        String x[] = message.split(";");
        String y[] = x[1].split("matrix");
        dataOutput.writeUTF(message);
    }

    private void wrapMatrixIntoMessage(int[][] matrix, int rows, int cols) {
        for (int i = 0; i<rows; i++){
            message += "row";
            for (int j=0; j< cols-1; j++){
                message += matrix[i][j]+",";
            }
            message += matrix[i][cols-1];
        }
    }

    private void readMatrixElememts(Scanner scn, int[][] matrix, int rows, int cols) {
        for (int i=0; i<rows; i++){
            for (int j=0; j<cols; j++){
                matrix[i][j] = scn.nextInt();
            }
        }
    }

    public static void main(String args[])
    {
        try {
            InetAddress ip = InetAddress.getByName("localhost");
            ClientClass client = new ClientClass(ip, 5000);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
}
