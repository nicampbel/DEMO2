import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

// @SuppressWarnings("unused")
public class GUI extends Agent {

    private JFrame frame;
    private JTextArea textArea;

    private Object[] args;

    @Override
    protected void setup() {

        createGUI();

        // Add the OneShot behavior to create and add the dashboard agent
        addBehaviour(new OneShotBehaviour() {
            public void action() {
                // Create dashboard agent
                try {
                    AgentController dash = getContainerController().createNewAgent("GUI", GUI.class.getName(), null);
                    dash.start();
                } catch (StaleProxyException e) {
                    e.printStackTrace();
                }
            }
        });
        addBehaviour(new Listener());
    }

    private void createGUI() {
        frame = new JFrame("User Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600/2, 900/2);
        frame.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        frame.add(scrollPane, BorderLayout.CENTER);

        // Create a panel for buttons and set its layout to FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Create the Tractor Button
        JButton tractorButton = new JButton("Add Tractor");
        tractorButton.addActionListener(e -> {
            // X
            // TODO get list of current agents to determine the next agent number
            Object[] tractorArgs = {1}; // Pass the same argument to fuelAgent
            try {
                String name = "tractorAgent" + args[0];
                AgentController tractor = getContainerController().createNewAgent(name, tractorAgent.class.getName(), tractorArgs);
                tractor.start();
            } catch (StaleProxyException ex) { // Rename the parameter to avoid duplication
                ex.printStackTrace();
            }
            // X
            System.out.println("Tractor add requested");
        });
        buttonPanel.add(tractorButton);

        // Create the Farm Button
        JButton farmButton = new JButton("Add Farm");
        farmButton.addActionListener(e -> {
        // Action for Farm Button
            System.out.println("Farm add requested");
        });
        buttonPanel.add(farmButton);

        // Add the button panel to the bottom of the frame
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private class Listener extends CyclicBehaviour {
        public void action() {
            ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
            if (msg != null) {
                String sender = msg.getSender().getLocalName(); // Get the sender's name
                String content = msg.getContent(); // Get the message content
                // System.out.println("Received message from " + sender + ": " + content);

                // Update the GUI directly ONLY FOR DEV PURPOSES
                SwingUtilities.invokeLater(() -> textArea.append("Received message from " + sender + ": " + content + "\n"));
            } else {
                block(); // Wait for next message
            }
        }
    }
}
