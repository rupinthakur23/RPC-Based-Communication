import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerClass {

    private ServerSocket serverSocket = null;

    //constructor
    public ServerClass(int port) throws IOException{

        serverSocket = new ServerSocket(port);

        //server keeps running all the time waiting for clients until forcefully stopped
        while(true){

            Socket socket = serverSocket.accept();

            System.out.println("Connection with client established");

            System.out.println("Instantiating a new thread fo this client.");

            Thread clientThred = new ServerThreadForEachClient(socket);

            //starting the thread
            clientThred.start();

        }

    }

    class ServerThreadForEachClient extends Thread{

        DataOutputStream dataOutput = null;
        DataInputStream dataInput = null;
        Socket socket = null;

        ServerThreadForEachClient(Socket socket) throws IOException{
            this.socket = socket;
            this.dataInput = new DataInputStream(socket.getInputStream());
            this.dataOutput = new DataOutputStream(socket.getOutputStream());

        }

        @Override
        public void run(){

            try {

                String messageFromClient = dataInput.readUTF();
                System.out.println("messageFromClient :: " + messageFromClient);
                String input[] = messageFromClient.split(";");

                if(input[0].equalsIgnoreCase("calculate_pi")){
                    new RemoteProcedureCalculatePi(dataOutput);
                }

                else if(input[0].equalsIgnoreCase("add")){
                    new RemoteProcedureForAdd(input[1], dataOutput);
                }

                else if(input[0].equalsIgnoreCase("sortArray")){
                    new RemoteProcedureForSortArray(input[1], dataOutput);
                }

                else if(input[0].equalsIgnoreCase("matrixMultiply")){
                    new RemoteProcedureForMatrixMultiply(input[1], dataOutput);
                }
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                try {
                    socket.close();
                    dataOutput.close();
                    dataInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Client finished ");
        }

    }

    public static void main(String[] args) {
        try {
            ServerClass server = new ServerClass(5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
