public class run {
	
	public static void main(String[] args) {

		String[] args1 = new String[3];
		args1[0] = "-gui";
		args1[1] = "-agents";

		//  the args1[2] field must contain a list of the agents to execute upon startup (i.e. when running this main file).
		// the list must be a string (i.e. in quotes), withe the agents specified as AgentName:AgentClass(*input args if needed*), and different agents separated by a semi-colon.
		// e.g.
		args1[2] = "MySniffer:jade.tools.sniffer.Sniffer; GUI:GUI()";
		// FeedAgent:agents.ResourceAgent(feed); TransportAgent:agents.TransportAgent(transport); OrderManagerAgent:agents.OrderManagerAgent
		// tractorAgent:tractorAgent(1)

		jade.Boot.main(args1);
		
	}
}