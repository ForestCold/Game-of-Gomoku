
class AgentContent {
	int pos;
	int expandedNodes;
	State state;
	AgentContent(int pos, int en, State state){
		this.pos = pos;
		this.expandedNodes = en;
		this.state = state;
	}
}

public abstract class Agent extends Tools{
	
	// the name of Agent
	public String name;
	
	// act with action
	public abstract AgentContent action(State state);
	
}
