import java.util.ArrayList;
import java.util.List;

class mmResult {
	int value;
	int pos;
	mmResult(int v, int p){
		value = v;
		pos = p;
	}
}

public class MiniMax extends Agent implements Configuration{
	
	// if do alpha-beta pruning
	boolean alpha_beta;
	
	// breadth for stochastic search
	int breadth;
	
	// the maximum depth of MiniMax tree
	int depth;
	
	// for calculating expanded nodes
	int expanded = 0;
	int leafExpanded = 0;
	
	// show the process, with upper case indicates x and lower case indicates o
	char[][] processBoard;
	
	MiniMax(boolean ab, int b, int d){
		alpha_beta = ab;
		breadth = b;
		depth = d;
		if (!ab) name = "MiniMax Agent";
		else if (b == -1) name = "Alpha-beta Agent";
		else name = "Stochastic Agent";
	}
	
	/** perform an action
	 * @param state current state
	 * @return next state, with the updated state, position, and expanded nodes
	 */	
	@Override
	public AgentContent action(State state) {
		
		expanded = 0;
		leafExpanded = 0;
		State tmpState = new State(state);
		mmResult result;
		int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
		
		if (state.empty()) { 
			state.update((int)(boardWidth / 2), (int)(boardWidth / 2), 'x');
			return new AgentContent((int)(boardWidth / 2) * boardWidth + (int)(boardWidth / 2), leafExpanded, state);
		}
		
		if (!alpha_beta) result = minimaxHelper(tmpState, depth, state.player);
		else if (breadth == -1) result = alphaBetaHelper(tmpState, depth, state.player, alpha, beta);
		else {
			result = stochasticHelper(tmpState, depth, state.player, alpha, beta, breadth);
			System.out.println("The possibility for " + state.player + " to win in round " + state.round + " is " + ((float)result.value / 10000));
		}
		
		state.update(result.pos / boardWidth, result.pos % boardWidth, state.player);
		
		if (breadth == -1) return new AgentContent(result.pos, expanded, state);
		else return new AgentContent(result.pos, leafExpanded * expanded, state);
		
	}
	
	/** a MiniMax algorithm
	 * @param state current state
	 * @param depth depth of MiniMax tree
	 * @param maxPlayer current player
	 * @return the best score and it's position
	 */	
	public mmResult minimaxHelper(State state, int depth, char maxPlayer) {
		
		// expanded nodes ++
		expanded ++;
		
		int pos = -1;
		int score;
		if (state.player == maxPlayer) score = Integer.MIN_VALUE;
		else score = Integer.MAX_VALUE;

		if (depth == 0 || state.end) {
			score = evaluate(state, maxPlayer);
			return new mmResult(score, pos);
		}

		// find all nodes
		for (int i = 0; i < boardWidth; i++) {
			for (int j = 0; j < boardWidth; j++) {
				if (state.board[i][j] == '.') {
					
					// if this is a max node
					if (state.player == maxPlayer) {
						// try a move
						State tmpState = new State(state);
						tmpState.update(i, j, state.player);
						mmResult result = minimaxHelper(tmpState, depth - 1, maxPlayer);
						if (result.value >= score) {
							score = result.value;
							pos = i * boardWidth + j;
						}
					}
					// else if this is a min node
					else {
						// try a move
						State tmpState = new State(state);
						tmpState.update(i, j, state.player);
						mmResult result = minimaxHelper(tmpState, depth - 1, maxPlayer);
						if (result.value <= score) {
							score = result.value;
							pos = i * boardWidth + j;
						}
						
					}
				}
			}
		}

		return new mmResult(score, pos);
		
	}
	
	/** a MiniMax algorithm with alpha-beta pruning
	 * @param state current state
	 * @param depth depth of MiniMax tree
	 * @param maxPlayer current player
	 * @param alpha the best score that MAX can force MIN to accept
	 * @param beta the best score that MIN can force MAX to accept
	 * @return the best score and it's position
	 */	
	public mmResult alphaBetaHelper(State state, int depth, char maxPlayer, int alpha, int beta) {
		
		// expanded nodes ++
		expanded ++;

		int pos = -1;
		int score;
		if (state.player == maxPlayer) score = Integer.MIN_VALUE;
		else score = Integer.MAX_VALUE;

		if (depth == 0 || state.end) {
			score = evaluate(state, maxPlayer);
			return new mmResult(score, pos);
		}

		// find all nodes
		for (int i = 0; i < boardWidth; i++) {
			for (int j = 0; j < boardWidth; j++) {
				if (state.board[i][j] == '.') {
					
					// if this is a MAX node
					if (state.player == maxPlayer) {
						// try a move
						State tmpState = new State(state);
						tmpState.update(i, j, state.player);
						mmResult result = alphaBetaHelper(tmpState, depth - 1, maxPlayer, alpha, beta);
						if (result.value >= score) {
							score = result.value;
							pos = i * boardWidth + j;
						}
						alpha = Math.max(alpha, score);
						// pruning
						if (beta < alpha) break;
					}
					// else if this is a MIN node
					else {
						// try a move
						State tmpState = new State(state);
						tmpState.update(i, j, state.player);
						mmResult result = alphaBetaHelper(tmpState, depth - 1, maxPlayer, alpha, beta);
						if (result.value <= score) {
							score = result.value;
							pos = i * boardWidth + j;
						}
						beta = Math.min(beta, score);
						// pruning
						if (beta < alpha) break;
					}
				}
			}
		}
		
		return new mmResult(score, pos);
		
	}
	
	/** a stochastic MiniMax algorithm with alpha-beta pruning
	 * @param state current state
	 * @param depth depth of MiniMax tree
	 * @param maxPlayer current player
	 * @param alpha the best score that MAX can force MIN to accept
	 * @param beta the best score that MIN can force MAX to accept
	 * @param breadth the breadth of leaf nodes 
	 * @return the best score and it's position
	 */	
	public mmResult stochasticHelper(State state, int depth, char maxPlayer, int alpha, int beta, int breadth) {
		
		int pos = -1;
		int score;
		if (state.player == maxPlayer) score = Integer.MIN_VALUE;
		else score = Integer.MAX_VALUE;

		if (depth == 0 || state.end) {
			expanded ++;
			float v = 0;
			for (int i = 0; i < breadth; i++) {
				v += evaluateStochastic(state, maxPlayer);
			}
			score = (int)(v / breadth * 10000);
			return new mmResult(score, pos);
		}

		// find all nodes
		for (int i = 0; i < boardWidth; i++) {
			for (int j = 0; j < boardWidth; j++) {
				if (state.board[i][j] == '.') {
					
					// if this is a MAX node
					if (state.player == maxPlayer) {
						// try a move
						State tmpState = new State(state);
						tmpState.update(i, j, state.player);
						mmResult result = stochasticHelper(tmpState, depth - 1, maxPlayer, alpha, beta, breadth);
						if (result.value >= score) {
							score = result.value;
							pos = i * boardWidth + j;
						}
						alpha = Math.max(alpha, score);
						// pruning
						if (beta < alpha) break;
					}
					// else if this is a MIN node
					else {
						// try a move
						State tmpState = new State(state);
						tmpState.update(i, j, state.player);
						mmResult result = stochasticHelper(tmpState, depth - 1, maxPlayer, alpha, beta, breadth);
						if (result.value <= score) {
							score = result.value;
							pos = i * boardWidth + j;
						}
						beta = Math.min(beta, score);
						// pruning
						if (beta < alpha) break;
					}
				}
			}
		}
		
		return new mmResult(score, pos);
		
	}
	
	/** evaluation function 
	 * @param state current state
	 * @param player current player
	 * @return a score to evaluate this state
	 */	
	public int evaluate(State state, char player){
		
		int score = 0;
		char opponent;
		if (player == 'x') opponent = 'o';
		else opponent = 'x';
		
		// if the player has five stones in a chain, maximize the score
		if (evaluateHelper(state, player, 5) > 0) {
			return Integer.MAX_VALUE - 1;
		}
		
		// if the opponent has four stones in a chain, minimize the score
		if (evaluateHelper(state, opponent, 5) > 0) {
			return Integer.MIN_VALUE + 1;
		}
		
		if (state.player == player && evaluateHelper(state, player, 4) > 0) {
			return Integer.MAX_VALUE - 1;
		}
		
		if (state.player != player && evaluateHelper(state, state.player, 4) > 0) {
			return Integer.MIN_VALUE - 1;
		}
		
		// if the player has at least two four continue stones(like ".xxxx") in a chain, maximize the score
		String s1 = ".", s2 = "";
		for (int i = 1; i < chainLength; i++) {
			s1 += player; // ".xxxx"
		}
		for (int i = 0; i < chainLength - 1; i++) {
			s2 += player; // "xxxx."
		}
		s2 += ".";
		
		if (evaluateHelper(state, s1) + evaluateHelper(state, s2) >= 2) {
			return Integer.MAX_VALUE - 2;
		}

		// otherwise, calculate the score by "count times stones"
		for (int i = 4; i > 0; i--) {
			score += evaluateHelper(state, player, i) * Math.pow(boardWidth, i * 2); 
			score -= evaluateHelper(state, opponent, i) * Math.pow(boardWidth, i * 2 + 1); 
		}
	
		return score;
	}
	
	/** evaluation function for Stochastic search, randomly generate a game
	 * @param state current state
	 * @param player current player
	 * @return a score to evaluate this state
	 */	
	public float evaluateStochastic(State state, char maxPlayer) {
		
		State currentState = new State(state);
		
		while (!currentState.end) {
			
			leafExpanded ++;
			
			int win = currentState.nearlyWin();
			if (win != -1) {
				currentState.update(win / boardWidth, win % boardWidth, currentState.player);
				continue;
			}
			
			int lose = currentState.nearlyLose();
			if (lose != -1) {
				currentState.update(lose / boardWidth, lose % boardWidth, currentState.player);
				continue;
			}
			
			// randomly generate next step
			int index = (int) Math.ceil(Math.random() * (boardWidth * boardWidth - currentState.round));
			int count = 0, i = 0;
			
			while (count < index) {
				if (i < boardWidth * boardWidth) {
					if (currentState.board[i / boardWidth][i % boardWidth] == '.') {
						count ++;
					}
					i ++;
				}
			}
			
			currentState.update((i - 1) / boardWidth, (i - 1) % boardWidth, currentState.player);	
			
		}
		
		if (currentState.winner == maxPlayer) return 1;
		else if (currentState.winner == '.') return (float) 0.5;
		return 0;
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
	
	/** evaluation function helper (overwrite)
	 * @param state current state
	 * @param the requirement
	 * @return the number of such chains exists in this state
	 */	
	private int evaluateHelper(State state, String s) {
		
		int count = 0;
		
		for (String c : state.sequences.values()) {
			if (c.equals(s)) count ++;
		}
		
		return count;
	
	}
	
}
