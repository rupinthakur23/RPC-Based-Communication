import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class RemoteProcedureForSortArray {

    DataOutputStream dataOutput;
    double[] array;

    public RemoteProcedureForSortArray(String inputParameter, DataOutputStream dataOutput) throws IOException{

        this.dataOutput = dataOutput;
        serverStubForSortArray(inputParameter);
    }

    private void serverStubForSortArray(String inputParameter) throws IOException{
        System.out.println("Server stub unpacking message.");
        String numbersInArray[] = inputParameter.split(",");

        array = new double[numbersInArray.length];

        for(int i=0; i< numbersInArray.length ; i++){
            array[i] = Double.parseDouble(numbersInArray[i]);
        }

        System.out.println("server stub making local call to sortArray.");
        sortArray(array);

    }

    private void sortArray(double[] arrayA) throws IOException{
        System.out.println("Implementation of sortArray on Server.");

        Arrays.sort(arrayA);

        for(int i=0 ; i< array.length ; i++){
            dataOutput.writeDouble(array[i]);
        }
    }
}