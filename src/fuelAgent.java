import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class fuelAgent extends Agent {
    private Object[] args;
    private String lastFuel = "0";

    @Override
    protected void setup() {

        args = getArguments();

        DFAgentDescription agentDes = new DFAgentDescription();
        ServiceDescription serviceDes = new ServiceDescription();
        serviceDes.setType("fuelAgent");
        serviceDes.setName("fuelAgent" + args[0]);
        agentDes.setName(getAID());
        agentDes.addServices(serviceDes);
        try {
            DFService.register(this, agentDes);
            System.out.println("Registered " + getAID().getName());
        } catch (FIPAException e) {
            e.printStackTrace();
            System.out.println("Failed to register " + getAID().getName());
        }

        if (args != null) {
            String arg = "900" + args[0].toString();
            String dataReceived;
            DataOutputStream outToServer;

            try {

                while (true) {
                    //setup socket
                    Socket socket = new Socket("localhost", Integer.parseInt(arg));

                    //send message over socket
                    outToServer = new DataOutputStream(socket.getOutputStream());
                    byte[] outByteString = "request".getBytes("UTF-8");
                    outToServer.write(outByteString);

                    //read replied message from socket
                    byte[] inByteString = new byte[500];
                    int numOfBytes = socket.getInputStream().read(inByteString);
                    dataReceived = new String(inByteString, 0, numOfBytes, "UTF-8");
                    // System.out.println("Received: " + dataReceived);

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
