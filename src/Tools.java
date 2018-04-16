import java.util.List;

public class Tools {
	
	public List<String> chainCombination(State state, List<String> resultList, char[] chain, int startIndex, int len, char player) {
		
		if (startIndex > state.chainLength) {
			return resultList;
		}
		
		if (len == 0) {
			resultList.add(new String(chain));
			return resultList;
		}
		
		for (int i = startIndex; i < chain.length; i++) {
			chain[i] = player;
			chainCombination(state, resultList, chain, i + 1, len - 1, player);
			chain[i] = '.';
		}
		
		return resultList;
	}
	
}
