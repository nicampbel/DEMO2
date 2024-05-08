import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class fuelAgent extends Agent {
    private Object[] args;
    private String lastFuel = "0";

    @Override
    protected void setup() {
        args = getArguments();

        if (args != null) {
            String name = "fuelAgent" + args[0];
            System.out.println(name + " created.");

            String dataReceived;
            DataOutputStream outToServer;

            try {

                while (true) {
                    //setup socket
                    Socket socket = new Socket("localhost", Integer.parseInt("9001"));

                    //send message over socket
                    outToServer = new DataOutputStream(socket.getOutputStream());
                    byte[] outByteString = "request".getBytes("UTF-8");
                    outToServer.write(outByteString);

                    //read replied message from socket
                    byte[] inByteString = new byte[500];
                    int numOfBytes = socket.getInputStream().read(inByteString);
                    dataReceived = new String(inByteString, 0, numOfBytes, "UTF-8");
                    System.out.println("Received: " + dataReceived);

                    if (!dataReceived.equals(this.lastFuel)) {
                        this.lastFuel = dataReceived;
                        sendFuel(dataReceived);
                    }
                    
                    //close connection
                    socket.close();
                    Thread.sleep(3000);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("No arguments provided -> Exiting");
            doDelete();
        }
    }
    
    private void sendFuel(String fuelConsumption) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent(fuelConsumption);
        String name = "tractorAgent" + args[0];
        msg.addReceiver(getAID(name));
        send(msg);
    }

}
