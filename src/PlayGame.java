import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayGame implements Configuration{
	
	// the board records process
	static char[][] processboard;
	
	/** play the Gomoku game
	 * @param a1 the first player
	 * @param a2 the second player
	 * @param init the initial state of board
	 */
	public static void playOnConsole(Agent a1, Agent a2, State init, boolean se) {
		
		System.out.println(a1.name + " VS " + a2.name + "!");
		
		// initialization
		State initialState = new State(init);
		State currentState = initialState;
		boolean showExpanded = se;
		initialProcessBoard(initialState);
		
		// play the game
		while (currentState.round <= boardWidth * boardWidth || !currentState.end) {
			
//			currentState.printBoard();
			
			AgentContent ac;
			if (currentState.player == 'x') ac = a1.action(currentState);
			else ac = a2.action(currentState);
			
			if (ac != null) { 
				currentState = ac.state;
				// update processboard
				processboard[ac.pos / boardWidth][ac.pos % boardWidth]  = currentState.player == 'o' ? (char) ('a' + currentState.round / 2) : (char) ('A' + currentState.round / 2 - 1);
			} else return;
			
			// print expanded nodes
			if (showExpanded)
				System.out.println("expanded nodes for " + currentState.opponent + " at round " + currentState.round + " is " + ac.expandedNodes);
			
			// if somebody win, end the game
			if (currentState.winner != '.') {
				if (currentState.winner == 'x') System.out.println(a1.name + " wins!");
				else System.out.println(a2.name + " wins!");
				break;
			}
			
			// if tie, end the game
			if (currentState.end) {
				System.out.println("Tie...");
				break;
			}	
		}
		
		printProcessBoard();
	}
	
	/** initial the process board
	 * @param initialState the initial state of board
	 */	
	public static void initialProcessBoard(State initialState) {
		
		processboard = new char[boardWidth][boardWidth];
		
		for (int i = 0; i < processboard.length; i++) {
			for (int j = 0; j < processboard[i].length; j++) {
				if (initialState.board[i][j] == 'x') {
					processboard[i][j] = 'a';
				} else if (initialState.board[i][j] == 'o') {
					processboard[i][j] = 'A';
				} else {
					processboard[i][j] = '.';
				}	
			}
		}
		
	}
	
	/** print the process board
	 */	
	public static void printProcessBoard() {
		for (int i = 0; i < processboard.length; i++) {
			for (int j = 0; j < processboard[i].length; j++) {
				System.out.print(processboard[i][j]);
			}
		System.out.println();
		}
	}
	
	public static void playOnUI(Agent computer) {
		
		BoardUI ui = new BoardUI();
		ui.createWindow();
		
		ui.crButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  if (ui.redB.isSelected()) ui.startGame(new State(), computer, true);
			  else ui.startGame(new State(), computer, false);
		  }
		});
		
	}
	
}
