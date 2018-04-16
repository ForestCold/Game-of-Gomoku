import java.util.ArrayList;
import java.util.List;

public class SupervisedAgent extends Agent implements Configuration{
	
	SupervisedAgent(){
		name = "Supervised Agent";
	}
	
	@Override
	public AgentContent action(State state) {
		
		int score = 0;
		int pos = -1;
		
		for (int i = 0; i < boardWidth; i++) {
			for (int j = 0; j < boardWidth; j++) {
				if (state.board[i][j] == '.') {
					State tmpState = new State(state);
					tmpState.update(i, j, state.player);
					int value = evaluate(tmpState);
					if (value >= score) {
						score = value;
						pos = i * boardWidth + j;
					}
				}
			}
		}
		
		if (pos != -1) { 
			state.update(pos / boardWidth, pos % boardWidth, state.player);
			return new AgentContent(pos, -1, state);
		}
		
		return null;
	}
	
	public int evaluate(State state) {
		
		int score = 0;
		
		double coefficient[] = {5463.46443, 1156.58088, -1857.58189, 1630.26436, -181.98111, 212.78943, -102.92375, 132.26728, -57.69176, 31.30934, -73.54265};
			
		for (int m = 5; m > 0; m--) {
			score += evaluateHelper(state, 'x', m) * coefficient[(5 - m) * 2]; 
			score += evaluateHelper(state, 'o', m) * coefficient[(5 - m) * 2 + 1]; 
		}
		
		if (state.player == 'o') score = 10000 - score;
		
		return score;
		
	}
	
	/** evaluation function helper
	 * @param state current state
	 * @param player current player
	 * @param len the number of player's stone in any valid chain, e.g. if len is four, find ".xxxx" or "xxxx." or "x.xxx" or "xx.xx" or "xxx.x"
	 * @return the number of such chains exists in this state
	 */	
	private int evaluateHelper(State state, char player, int len) {
		
		int count = 0;
		
		List<String> stoneChain = new ArrayList<>();
		char[] chainArray = new char[chainLength];
		for (int j = 0; j < chainLength; j++) {
			chainArray[j] = '.';
		}
		
		stoneChain = chainCombination(state, stoneChain, chainArray, 0, len, player);
		
		for (String c : state.sequences.values()) {
			for (String s : stoneChain) {
				if (c.equals(s)) {
					count ++;
				}
			}
		}
		
		return count;
	}
	
}
