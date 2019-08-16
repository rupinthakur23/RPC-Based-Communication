import java.io.DataOutputStream;
import java.io.IOException;

public class RemoteProcedureForAdd {

    DataOutputStream dataOutput;
    double x;
    double y;

    public RemoteProcedureForAdd(String inputParameters, DataOutputStream dataOutput) throws IOException{

        this.dataOutput = dataOutput;
        serverStubForAdd(inputParameters);

    }

    private void serverStubForAdd(String inputParameters) throws IOException{
        System.out.println("Server stub unpacking message.");
        String numbersToAdd[] = inputParameters.split(",");
        x = Double.parseDouble(numbersToAdd[0]);
        y = Double.parseDouble(numbersToAdd[1]);

        System.out.println("server stub making local call to add.");
        add(x, y);
    }

    private void add(double x, double y) throws IOException{
        System.out.println("Implementation of add on Server.");
        double sum = 0;
        sum = x + y;
        dataOutput.writeDouble(x+y);
    }
}
