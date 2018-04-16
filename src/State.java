import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State extends Tools implements Configuration{
	
	// board
	public char[][] board;
	
	// sequences
	public Map<String, String> sequences = new HashMap<>();
	
	// current player
	public char player;
	
	// current opponent
	public char opponent;
	
	// round
	public int round;
	
	// the winner
	public char winner;
	
	// is game terminate
	public boolean end = false;
	
    /** constructor
     * @param state 
     */
	public State(State state) {
		
		// copy board
		board = new char[state.board.length][state.board[0].length];
		for (int i = 0; i < state.board.length; i++) {
			for (int j = 0; j < state.board[0].length; j++) {
				board[i][j] = state.board[i][j];
			}
		}
		
		// copy sequences
		for (String s : state.sequences.keySet()) {
			sequences.put(s, new String(state.sequences.get(s)));
		}
		
		// copy other parameters
		player = state.player;
		opponent = state.opponent;
		round = state.round;
		winner = state.winner;
		end = state.end;
		
	}
	
    /** constructor
     * @param n boardWidth of board
     * @param winLength the minimal chain length to win
     */
	public State() {
		
		// initial board
		board = new char[boardWidth][boardWidth];
		for (int i = 0; i < boardWidth; i++) {
			for (int j = 0; j < boardWidth; j++) {
				board[i][j] = '.';
			}
		}
		
		// initial sequences
		
		// first initial values for sequence map
		char dots[] = new char[chainLength];
		for (int i = 0; i < chainLength; i++) {
			dots[i] = '.';
		}
		// put all horizontal sequence in sequences map
		for (int i = 0; i < boardWidth; i++) {
			for (int j = 0; j <= boardWidth - chainLength; j++) {
				int start = i * boardWidth + j;
				int end = start + chainLength - 1;
				String pos = "" + start + "-" + end;
				sequences.put(pos, new String(dots));
			}
		}
		// put all vertical sequence in sequences map
		for (int j = 0; j < boardWidth; j++) {
			for (int i = 0; i <= boardWidth - chainLength; i++) {
				int start = i * boardWidth + j;
				int end = (i + chainLength - 1) * boardWidth + j;
				String pos = "" + start + "-" + end;
				sequences.put(pos, new String(dots));
			}
		}
		// put all diagonally sequence in sequences map
		for (int i = 0; i <= boardWidth - chainLength; i++) {
			for (int j = 0; j <= boardWidth - chainLength; j++) {
				int start1 = i * boardWidth + j;
				int start2 = start1 + chainLength - 1;
				int end2 = (i + chainLength - 1) * boardWidth + j;
				int end1 = end2 + chainLength - 1;
				String pos1 = "" + start1 + "-" + end1;
				String pos2 = "" + start2 + "-" + end2;
				sequences.put(pos1, new String(dots));
				sequences.put(pos2, new String(dots));
			}
		}
		
		// initial other parameters
		player = 'x';
		opponent = 'o';
		round = 0;
		winner = '.';
		
	}
	
    /** random generate a state (for regression data training)
     * @param n boardWidth of board
     * @param winLength the minimal chain length to win
     */
	public void randomGenerateState() {
		
		int r = (int) Math.ceil(Math.random() * 49);
		
		// randomly put x
		for (int i = 0; i < r / 2; i++) {
			int index = (int) Math.ceil(Math.random() * 48);
			while (board[index / boardWidth][index % boardWidth] != '.') {
				index = (int) Math.ceil(Math.random() * 48);
			}
			board[index / boardWidth][index % boardWidth] = 'x';
		}
		
		// randomly put o
		for (int i = 0; i < r / 2; i++) {
			int index = (int) Math.ceil(Math.random() * 48);
			while (board[index / boardWidth][index % boardWidth] != '.') {
				index = (int) Math.ceil(Math.random() * 48);
			}
			board[index / boardWidth][index % boardWidth] = 'o';
		}
		
		// update all horizontal sequence in sequences map
		for (int i = 0; i < boardWidth; i++) {
			for (int j = 0; j <= boardWidth - chainLength; j++) {
				int start = i * boardWidth + j;
				int end = start + chainLength - 1;
				String pos = "" + start + "-" + end;
				String chain = "";
				for (int k = 0; k < chainLength; k++) {
					chain += board[i][j + k];
				}
				sequences.put(pos, chain);
			}
		}
		// update all vertical sequence in sequences map
		for (int j = 0; j < boardWidth; j++) {
			for (int i = 0; i <= boardWidth - chainLength; i++) {
				int start = i * boardWidth + j;
				int end = (i + chainLength - 1) * boardWidth + j;
				String pos = "" + start + "-" + end;
				String chain = "";
				for (int k = 0; k < chainLength; k++) {
					chain += board[i + k][j];
				}
				sequences.put(pos, chain);
			}
		}
		// update all diagonally sequence in sequences map
		for (int i = 0; i <= boardWidth - chainLength; i++) {
			for (int j = 0; j <= boardWidth - chainLength; j++) {
				int start1 = i * boardWidth + j;
				int start2 = start1 + chainLength - 1;
				int end2 = (i + chainLength - 1) * boardWidth + j;
				int end1 = end2 + chainLength - 1;
				String pos1 = "" + start1 + "-" + end1;
				String pos2 = "" + start2 + "-" + end2;
				String chain1 = "";
				String chain2 = "";
				for (int k = 0; k < chainLength; k++) {
					chain1 += board[i + k][j + k];
				}
				for (int k = 0; k < chainLength; k++) {
					chain2 += board[i + k][j + chainLength - 1 - k];
				}
				sequences.put(pos1, chain1);
				sequences.put(pos2, chain2);
			}
		}
		
		// initial other parameters
		player = 'x';
		opponent = 'o';
		round = r;
		winner = '.';
		
	}
	
    /** print board
     */
	public void printBoard() {
		System.out.println("Board: ");
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
	}
	
    /** print sequences
     */
	public void printSequences() {
		System.out.println("Sequences: ");
		for (String s : sequences.keySet()) {
			System.out.print(s);
			System.out.print(" : ");
			System.out.print(sequences.get(s));
			System.out.println();
		}
	}
	
	/** update state
	 * @param x vertical index
	 * @param y horizontal index
	 * @param player current player
	 */	
	public void update(int x, int y, char player) {
		
		// update board
		board[x][y] = player;
		
		// update all horizontal sequence in sequences map
		for (int i = 0; i < boardWidth; i++) {
			for (int j = 0; j <= boardWidth - chainLength; j++) {
				int start = i * boardWidth + j;
				int end = start + chainLength - 1;
				String pos = "" + start + "-" + end;
				String chain = "";
				for (int k = 0; k < chainLength; k++) {
					chain += board[i][j + k];
				}
				sequences.put(pos, chain);
			}
		}
		// update all vertical sequence in sequences map
		for (int j = 0; j < boardWidth; j++) {
			for (int i = 0; i <= boardWidth - chainLength; i++) {
				int start = i * boardWidth + j;
				int end = (i + chainLength - 1) * boardWidth + j;
				String pos = "" + start + "-" + end;
				String chain = "";
				for (int k = 0; k < chainLength; k++) {
					chain += board[i + k][j];
				}
				sequences.put(pos, chain);
			}
		}
		// update all diagonally sequence in sequences map
		for (int i = 0; i <= boardWidth - chainLength; i++) {
			for (int j = 0; j <= boardWidth - chainLength; j++) {
				int start1 = i * boardWidth + j;
				int start2 = start1 + chainLength - 1;
				int end2 = (i + chainLength - 1) * boardWidth + j;
				int end1 = end2 + chainLength - 1;
				String pos1 = "" + start1 + "-" + end1;
				String pos2 = "" + start2 + "-" + end2;
				String chain1 = "";
				String chain2 = "";
				for (int k = 0; k < chainLength; k++) {
					chain1 += board[i + k][j + k];
				}
				for (int k = 0; k < chainLength; k++) {
					chain2 += board[i + k][j + chainLength - 1 - k];
				}
				sequences.put(pos1, chain1);
				sequences.put(pos2, chain2);
			}
		}
		
		// check if updated state is terminal state
		if (isTerminate()) end = true;
		else {
			end = false;
			winner = '.';
		}
		
		// update round
		round ++;
		
		// update player
		switchPlayer();
		
	}
	
	/** to judge if the game is terminated
	 * @return true if game terminate
	 */	
	public boolean isTerminate() {
		
		// has winner
		String winStringX = "", winStringO = "";
		for (int i = 0; i < chainLength; i++) {
			winStringX += "x";
			winStringO += "o";
		}
		for (String v : sequences.values()) {
			if (v.equals(winStringX)) {
				winner = 'x';
				return true;
			} else if (v.equals(winStringO)) {
				winner = 'o';
				return true;
			}
		}
		
		// tie
		int validCount = 0;
		for (String v : sequences.values()) {
			boolean hasO = false, hasX = false;
			for (int i = 0; i < v.length(); i++) {
				if (v.charAt(i) == 'o') {
					hasO = true;
				}
				if (v.charAt(i) == 'x') {
					hasX = true;
				}
			}
			if (hasO && hasX) continue;
			else validCount ++;
		}
		
		if (validCount == 0) return true;
		
		return false;
	}
	
	/** randomly generate initial positions
	 */	
	public void randomlyGeneratePositions() {
		
		// randomly generate for x
		int x1 = (int) Math.ceil(Math.random() * boardWidth);
		int y1 = (int) Math.ceil(Math.random() * boardWidth);
		if (x1 >= boardWidth) x1 = boardWidth - 1;
		if (y1 >= boardWidth) y1 = boardWidth - 1;
		update(x1, y1, player);
//		update(5, 1, player);
		
		// randomly generate for o
		int x2 = (int) Math.ceil(Math.random() * boardWidth);
		int y2 = (int) Math.ceil(Math.random() * boardWidth);
		if (x2 >= boardWidth) x2 = boardWidth - 1;
		if (y2 >= boardWidth) y2 = boardWidth - 1;
		update(x2, y2, player);
//		update(1, 5, player);
		
	}
	
	/** switch player
	 */	
	public char switchPlayer() {
		if (player == 'o') {
			player = 'x';
			opponent = 'o';
		} else {
			player = 'o';
			opponent = 'x';
		}
		return player;
	}
	
	public boolean empty() {
		for (int i = 0; i < boardWidth; i++) {
			for (int j = 0; j < boardWidth; j++) {
				if (board[i][j] != '.') return false;
			}
		}
		return true;
	}
	
	public int nearlyWin() {
		
		List<String> stoneChain = new ArrayList<>();
		char[] chainArray = new char[chainLength];
		for (int j = 0; j < chainLength; j++) {
			chainArray[j] = '.';
		}
		
		stoneChain = chainCombination(this, stoneChain, chainArray, 0, 4, player);
		
		for (String pos : sequences.keySet()) {
			for (String ss : stoneChain){
				if (sequences.get(pos).equals(ss)) {
					
					int posX1 = Integer.parseInt(pos.split("-")[0]) / boardWidth;
					int posY1 = Integer.parseInt(pos.split("-")[0]) % boardWidth;
					int posX2 = Integer.parseInt(pos.split("-")[1]) / boardWidth;
					int posY2 = Integer.parseInt(pos.split("-")[1]) % boardWidth;
					
					for (int i = chainLength - 1; i >= 0; i--) {
						if (ss.charAt(i) == '.') {
							if (posX1 == posX2) {
								return posX1 * boardWidth + posY1 + i;
							} else if (posY1 == posY2) {
								return (posX1 + i) * boardWidth + posY1;
							} else if (posX1 < posX2 && posY1 < posY2) {
								return (posX1 + i) * boardWidth + posY1 + i;
							} else {
								return (posX1 + i) * boardWidth + posY1 - i;
							}
						}
					}
				}
			}
		}
		
		return -1;
	}
	
	public int nearlyLose() {
		List<String> stoneChain = new ArrayList<>();
		char[] chainArray = new char[chainLength];
		for (int j = 0; j < chainLength; j++) {
			chainArray[j] = '.';
		}
		
		stoneChain = chainCombination(this, stoneChain, chainArray, 0, 4, opponent);
		
		for (String pos : sequences.keySet()) {
			for (String ss : stoneChain){
				if (sequences.get(pos).equals(ss)) {
					
					int posX1 = Integer.parseInt(pos.split("-")[0]) / boardWidth;
					int posY1 = Integer.parseInt(pos.split("-")[0]) % boardWidth;
					int posX2 = Integer.parseInt(pos.split("-")[1]) / boardWidth;
					int posY2 = Integer.parseInt(pos.split("-")[1]) % boardWidth;
					
					for (int i = chainLength - 1; i >= 0; i--) {
						if (ss.charAt(i) == '.') {
							if (posX1 == posX2) {
								return posX1 * boardWidth + posY1 + i;
							} else if (posY1 == posY2) {
								return (posX1 + i) * boardWidth + posY1;
							} else if (posX1 < posX2 && posY1 < posY2) {
								return (posX1 + i) * boardWidth + posY1 + i;
							} else {
								return (posX1 + i) * boardWidth + posY1 - i;
							}
						}
					}
				}
			}
		}
		
		return -1;
	}
	
}
