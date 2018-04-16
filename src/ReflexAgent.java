import java.util.ArrayList;
import java.util.List;

public class ReflexAgent extends Agent implements Configuration{
	
	ReflexAgent(){
		name = "Reflex Agent";
	}
	
	/** perform an action
	 * @param state current state
	 * @return next state, with the updated state, position, and expanded nodes
	 */	
	@Override
	public AgentContent action(State state) {
			
			AgentContent nextContent = ruleOne(state);
			if (nextContent != null) return nextContent;

			nextContent = ruleTwo(state);
			if (nextContent != null) return nextContent;
			
			nextContent = ruleThree(state);
			if (nextContent != null) return nextContent;
			
			nextContent = ruleFour(state);
			if (nextContent != null) return nextContent;	
			
			return null;
	}
	
	/** Check whether the agent side is going to win by placing just one more stone. If so, place the stone which wins the game. 
	 * @param state current state
	 * @return next state, with the updated state, position, and expanded nodes
	 */	
	public AgentContent ruleOne(State state) {
		
		char player = state.player;
		
		List<String> stoneChain = new ArrayList<>();
		char[] chainArray = new char[chainLength];
		for (int j = 0; j < chainLength; j++) {
			chainArray[j] = '.';
		}
		
		// calculated all combinations of target chain string
		// in this case, "xxxx." or "xxx.x" or "xx.xx" or "x.xxx" or ".xxxxx"
		stoneChain = chainCombination(state, stoneChain, chainArray, 0, 4, player);
		
		int pos = ruleHelper(stoneChain, state);
		
		if (pos != -1) {
			state.update(pos / boardWidth, pos % boardWidth, player);
			return new AgentContent(pos, -1, state);
		}
		
		return null;
	}
	
	/** Then check whether the opponent has an unbroken chain formed by 4 stones and has an empty intersection on either head of the chain. 
	 * @param state current state
	 * @return next state, with the updated state, position, and expanded nodes
	 */	
	public AgentContent ruleTwo(State state) {
		
		int x = boardWidth, y = -1;
		char player = state.player;
		
		char target1[] = new char[chainLength];
		char target2[] = new char[chainLength];
		
		// ".oooo" or ".xxxx"
		target1[0] = '.';
		for (int i = 1; i < chainLength; i++) {
			target1[i] = state.opponent;
		}
		
		// "oooo." or "xxxx."
		target2[chainLength - 1] = '.';
		for (int i = 0; i < chainLength - 1; i++) {
			target2[i] = state.opponent;
		}
		
		String ts1 = new String(target1);
		String ts2 = new String(target2);
		
		for (String pos : state.sequences.keySet()) {
			int posX = x, posY = y;
			if (state.sequences.get(pos).equals(ts1)) {
				posX = Integer.parseInt(pos.split("-")[0]) / boardWidth;
				posY = Integer.parseInt(pos.split("-")[0]) % boardWidth;
			}
			if (state.sequences.get(pos).equals(ts2)) {
				posX = Integer.parseInt(pos.split("-")[1]) / boardWidth;
				posY = Integer.parseInt(pos.split("-")[1]) % boardWidth;
			}
			if (posX < x) {
				x = posX;
				y = posY;
			} else if (posX == x && posY > y) {
				x = posX;
				y = posY;
			}
		}
		
		if (y != -1) {
			state.update(x, y, player);
			return new AgentContent(x * boardWidth + y, -1, state);
		}
		
		return null;
	}
	
	/** Check whether the opponent has an unbroken chain formed by 3 stones and has empty spaces on both ends of the chain. 
	 * @param state current state
	 * @return next state, with the updated state, position, and expanded nodes
	 */	
	public AgentContent ruleThree(State state) {
		
		int x = -1, y = boardWidth;
		char player = state.player;
		
		char target1[] = new char[chainLength];
		
		// ".ooo." or ".xxx."
		target1[0] = '.';
		target1[chainLength - 1] = '.';
		for (int i = 1; i < chainLength - 1; i++) {
			target1[i] = state.opponent;
		}
		
		String ts1 = new String(target1);
		
		for (String pos : state.sequences.keySet()) {
			int posX1 = x, posY1 = y, posX2 = x, posY2 = y;
			if (state.sequences.get(pos).equals(ts1)) {
				posX1 = Integer.parseInt(pos.split("-")[0]) / boardWidth;
				posY1 = Integer.parseInt(pos.split("-")[0]) % boardWidth;
				posX2 = Integer.parseInt(pos.split("-")[1]) / boardWidth;
				posY2 = Integer.parseInt(pos.split("-")[1]) % boardWidth;
			}
			if (posY1 < y) {
				x = posX1;
				y = posY1;
			} else if (posY1 == y && posX1 > x) {
				x = posX1;
				y = posY1;
			}
			if (posY2 < y) {
				x = posX2;
				y = posY2;
			} else if (posY2 == y && posX2 > x) {
				x = posX2;
				y = posY2;
			}
		}
		
		if (x != -1) {
			state.update(x, y, player);
			return new AgentContent(x * boardWidth + y, -1, state);
		}
		
		return null;
		
	}
	
	/** If none of the previous conditions hold, then find all possible sequences of 5 consecutive spaces that contain none of the opponent's stones. Call each such block a "winning block," because it's a block in which victory is still possible. Then, find the winning block which has the largest number of the agent's stones. Last, in the winning block, place a stone next to a stone already in the winning block on board. 
	 * @param state current state
	 * @return next state, with the updated state, position, and expanded nodes
	 */	
	public AgentContent ruleFour(State state) {
		
		char player = state.player;
		
		for (int i = 4; i >= 0; i--) {
			List<String> stoneChain = new ArrayList<>();
			char[] chainArray = new char[chainLength];
			for (int j = 0; j < chainLength; j++) {
				chainArray[j] = '.';
			}
			// calculated all combinations of target chain string
			stoneChain = chainCombination(state, stoneChain, chainArray, 0, i, player);
			// for all chains satisfies the format of stoneChains, find the closest to the left and closest to the bottom
			int pos = ruleHelper(stoneChain, state);
			// if this position exist, then put player's stone on this position and return
			if (pos != -1) {
				state.update(pos / boardWidth, pos % boardWidth, player);
				return new AgentContent(pos, -1, state);
			}
		}
		
		return null;
	}
	
	/** Help to find the closest to left and bottom position that match requirements 
	 * @param stones a set of requirements described by string, such as "xx.x."
	 * @param state current state
	 * @return the position, if not position satisfies the requirements, return -1
	 */	
	public int ruleHelper(List<String> stones, State state) {
		
		int x = -1, y = boardWidth;
		
		int posX1 = x, posY1 = y, posX2 = x, posY2 = y;
		
		boolean isEmptyChain = true;
		for (int i = 0; i < stones.get(0).length(); i++) {
			if (stones.get(0).charAt(i) != '.') {
				isEmptyChain = false;
			}
		}
		
		for (String pos : state.sequences.keySet()) {
			for (String ss : stones){
				if (state.sequences.get(pos).equals(ss)) {
					
					posX1 = Integer.parseInt(pos.split("-")[0]) / boardWidth;
					posY1 = Integer.parseInt(pos.split("-")[0]) % boardWidth;
					posX2 = Integer.parseInt(pos.split("-")[1]) / boardWidth;
					posY2 = Integer.parseInt(pos.split("-")[1]) % boardWidth;
		
					for (int i = chainLength - 1; i >= 0; i--) {
						if (ss.charAt(i) == '.') {
							if (i > 0 && ss.charAt(i - 1) != '.' || (i <  chainLength - 1 && ss.charAt(i + 1) != '.') || isEmptyChain) {
								// vertical
								if (posX1 == posX2) {
									if (posY1 + i < y) {
										x = posX1;
										y = posY1 + i;
									} else if (posY1 + i == y && posX1 > x) {
										x = posX1;
										y = posY1 + i;
									}
								}
								// horizontal
								else if (posY1 == posY2) {
									if (posY1 < y) {
										x = posX1 + i;
										y = posY1;
									} else if (posY1 == y && posX1 + i > x) {
										x = posX1 + i;
										y = posY1;
									}
								}
								// diagonal
								else if (posX1 < posX2 && posY1 < posY2){
									if (posY1 + i < y) {
										x = posX1 + i;
										y = posY1 + i;
									} else if (posY1 + i == y && posX1 + i > x) {
										x = posX1 + i;
										y = posY1 + i;
									}
								} else {
									if (posY1 - i < y) {
										x = posX1 + i;
										y = posY1 - i;
									} else if (posY1 - i == y && posX1 + i > x) {
										x = posX1 + i;
										y = posY1 - i;
									}
								}
							}
						}
					}
				}
			}
		}
		
		if (x != -1 && y != -1) {
			return x * boardWidth + y;
		}
		
		return -1;
		
	}
}
