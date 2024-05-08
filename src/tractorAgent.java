import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class tractorAgent extends Agent {
    private Object[] args;
    private Object[] ID;

    public void getName(Object[] ID) {
        if (ID != null && ID.length > 0 && (ID[0] instanceof Integer || ID[0] instanceof String)) {
            this.ID = ID;
            setup();
        }
        else {
            System.out.println("Invalid ID");
        }
    }

    @Override
    protected void setup() {
        
        if (ID == null) {
            args = getArguments();
            System.out.println("Imported from Jade:" + args[0]);
        }
        else {
            args = ID;
            System.out.println("Imported from program" + args[0]);
        }
        
        if (args != null) {
            String name = "tractorAgent"+ args[0];
            System.out.println(name + " created.");
            
            // Add the OneShot behavior to create and add the fuelAgent
            addBehaviour(new OneShotBehaviour() {
                public void action() {
                    // Create fuelAgent
                    Object[] fuelArgs = {args[0]}; // Pass the same argument to fuelAgent
                    try {
                        AgentController fuelAgent = getContainerController().createNewAgent("fuelAgent" + args[0], fuelAgent.class.getName(), fuelArgs);
                        fuelAgent.start();
                    } catch (StaleProxyException e) {
                        e.printStackTrace();
                    }
                }
            });

            addBehaviour(new listenForFuel());
        } else {
            System.out.println("No arguments provided -> Exiting");
            doDelete();
        }
    }

    private class listenForFuel extends CyclicBehaviour {
        public void action() {
            ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
            if (msg != null) {
                String fuelConsumption = msg.getContent();
                System.out.println("Received fuel consumption: " + fuelConsumption);
                sendFuel(fuelConsumption);
                
            }
            else {
                block(); // Wait for next message
            }
        }
    }

    private void sendFuel(String fuelConsumption) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent(fuelConsumption);
        msg.addReceiver(getAID("GUI"));
        send(msg);
    }

}
