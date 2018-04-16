public class GomokuMain implements Configuration{
	
	public static void main(String[] args) {
		
		PlayGame pg = new PlayGame();
		
		State initialState = new State();
		initialState.randomlyGeneratePositions();
		
		/*********** set agents ***********/ 
		
		// Reflex Agent
		ReflexAgent agent0 = new ReflexAgent();
		
		// MiniMax Agent
		MiniMax agent1 = new MiniMax(false, -1, depth);
		
		// Alpha-beta Agent
		MiniMax agent2 = new MiniMax(true, -1, depth);
		
		// Stochastic Agent
		MiniMax agent3 = new MiniMax(true, breadth, depth);
		
		// Supervised Agent
		SupervisedAgent agent4 = new SupervisedAgent();
		
		
		/***********  play on console ***********/ 
		
//		// Reflect Agent VS Reflect Agent
//		PlayGame.playOnConsole(agent0, agent0, initialState, false);
//		
//		// Reflect Agent VS MiniMax
//		pg.playOnConsole(agent0, agent1, initialState, false);
//		
//		// Reflect Agent VS Alpha-bata
//		pg.playOnConsole(agent0, agent2, initialState, false);
//		
//		// MiniMax VS Reflect Agent
//		pg.playOnConsole(agent1, agent0, initialState, false);
//		
//		// Alpha-beta VS Reflect Agent
//		pg.playOnConsole(agent2, agent0, initialState, false);
		
		// MiniMax Agent VS Alpha-beta Agent
//		showExpanded = true;
//		pg.playOnConsole(agent1, agent2, initialState, false);
		
		// Alpha-beta Agent VS MiniMax Agent
//		pg.playOnConsole(agent2, agent1, initialState, false);
		
		// Alpha-beta Agent VS Stochastic Agent
		pg.playOnConsole(agent2, agent3, initialState, true);
		
		// Stochastic Agent VS Alpha-beta Agent
		pg.playOnConsole(agent3, agent2, initialState, true);
		
		// Supervised Agent VS Alpha-beta Agent
//		pg.playOnConsole(agent4, agent2, initialState, false);
		
		// Alpha-beta Agent VS Supervised Agent  
//		pg.playOnConsole(agent2, agent4, initialState, false);
		
		/***********  play on UI ***********/ 
		
//		pg.playOnUI(agent2);
		
		/***********  regression  ***********/ 
		
		// training data
//		RegressionTraining rt = new RegressionTraining("data.txt");
//		rt.run(1000);
//		rt.writeToFile();
		
	}
}
