import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class RegressionTraining extends Tools implements Configuration{

	String filePath;
	List<ArrayList<Integer>> dataList = new ArrayList<ArrayList<Integer>>();
	List<Integer> value = new ArrayList<>();
	
	RegressionTraining (String file){
		filePath = file;
	}
	
	public void run (int sampleSize) {
		
		int count  = 0;
		while (count < sampleSize) {
			
			ArrayList<Integer> feature = new ArrayList<>();		
			
			State state = new State();
			state.randomGenerateState();
			
			// generate feature
			for (int i = 5; i > 0; i--) {
				feature.add (trainHelper(state, 'x', i));
				feature.add (trainHelper(state, 'o', i));
			}
			
			dataList.add(feature);
			
			// calculate value
			float v = 0;
			for (int i = 0; i < 500; i++) {
				v += evaluateStochastic(state, 'x');
			}
			int score = (int)(v / 500 * 10000);
			
			value.add(score);
			
			count ++;
			
			System.out.println("Finished " + count);
		}
		
	}
	
	public void writeToFile() {
		
	    try {
	        File file = new File(filePath);
	        if(file.exists()){
	            FileWriter fw = new FileWriter(file, false);
	            BufferedWriter bw = new BufferedWriter(fw);
	            for (int i = 0; i < value.size(); i++) {
	            		for (int j = 0; j < dataList.get(i).size(); j++) {
	            			bw.write(Integer.toString(dataList.get(i).get(j)));
	            			bw.write(",");
	            		}
	            		bw.write(Integer.toString(value.get(i)));
	            		bw.write("\n");
	            }
	            bw.close(); 
	            fw.close();
	            System.out.println("success!");
	        }
	         
	    } catch (Exception e) {
	        // TODO: handle exception
	    }
	    
	}
	
	/** train function helper
	 * @param state current state
	 * @param player current player
	 * @param len the number of player's stone in any valid chain, e.g. if len is four, find ".xxxx" or "xxxx." or "x.xxx" or "xx.xx" or "xxx.x"
	 * @return the number of such chains exists in this state
	 */	
	private int trainHelper(State state, char player, int len) {
		
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
	
	/** evaluation function for Stochastic search, randomly generate a game
	 * @param state current state
	 * @param player current player
	 * @return a score to evaluate this state
	 */	
	public float evaluateStochastic(State state, char maxPlayer) {
		
		State currentState = new State(state);
		
		while (!currentState.end) {
			
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
			
			if (i > 49) i = 49;
			else if (i < 1) i = 1;
			
			currentState.update((i - 1) / boardWidth, (i - 1) % boardWidth, currentState.player);	
			
		}
		
		if (currentState.winner == maxPlayer) return 1;
		else if (currentState.winner == '.') return (float) 0.5;
		return 0;
	}
	
}
