import java.io.DataOutputStream;
import java.io.IOException;

public class RemoteProcedureCalculatePi {

    DataOutputStream dataOutput;
    double pi;

    public RemoteProcedureCalculatePi(DataOutputStream dataOutput) throws IOException{

        System.out.println("Server stub unpacking message.");
        this.dataOutput = dataOutput;
        calcultePi();
    }

    private void calcultePi() throws IOException{
        System.out.println("Implementation of calcultePi on server.");
        pi = 0;
        // Leibniz Formula for PI (aka Gregory Leibniz Series)
        for (int i=1; i<100000; i+=2){
            pi += ( (1.0/(2*i-1)) - (1.0/(2*i+1)) );
        }
        pi *= 4;

        dataOutput.writeDouble(pi);
    }
}
