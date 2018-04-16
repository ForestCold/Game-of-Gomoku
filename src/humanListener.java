import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

public class humanListener extends MouseAdapter implements Configuration  {

	 GamePanel panel;
	 State state;
	 private Graphics2D g;  
	 
	 public humanListener(GamePanel panel)  
	 {  
	      this.panel = panel;
	      state = panel.state;
	      g = (Graphics2D) this.panel.getGraphics(); 
	      
	 }  
	 
	 public void mouseReleased (MouseEvent e)   
	 {  
		  
	        int x = e.getX();  
	        int y = e.getY();  
	        int row = (y - Y + Size / 2) / Size;  
	        int column = (x - X + Size / 2) / Size;  
	        
	        if (row < boardWidth && column < boardWidth)  
	        {  

	        		char m = 'i';
	        		for (int i = 0; i < boardWidth; i++) {
	        			for (int j = 0; j < boardWidth; j++) {
	        				if (i == row && j == column) { 
	        					m = state.board[i][j];
	        				}
	        				
	        			}
	        		}
	        		
	        		// directly use state.board[column][row] would be wrong, but don't know why
	        		
	        		if (m != '.') return;
	        		else {  
	            	
	              x = X + column * Size - chessSize / 2;  
	              y = Y + row * Size - chessSize / 2;  
	              
	              if( state.player == 'x' )  g.setColor(Color.red);  
	              else g.setColor(Color.blue);  
	                  
	              g.fillOval(x, y, chessSize, chessSize); 

	             }  
	        } 
	        
	        state.update(row, column, state.player);
	        
	        if (!state.end) {
	        	
	        		AgentContent ac = panel.agent.action(state);
	        		state = new State(ac.state);
	        		row = ac.pos / boardWidth;
	        		column = ac.pos % boardWidth;
	    	            	
	    	        x = X + column * Size - chessSize / 2;  
	    	        y = Y + row * Size - chessSize / 2;  
	    	              
	    	        if(state.board[row][column] == 'x' )  g.setColor(Color.red);  
	    	        else g.setColor(Color.blue);  
	    	                  
	    	        g.fillOval(x, y, chessSize, chessSize);  
	    	        
	    	        if (state.end) {
	    	        	    	        	
		        		if (state.winner == 'x' && panel.isFirstHand || state.winner == 'o' && !panel.isFirstHand) {
		        			showDialog(1);
		        		} else if (state.winner != '.') {
		        			showDialog(-1);
		        		} else {
		        			showDialog(0);
		        		}
		        		
	    	        }

	        } else {
	        		
	        		if (state.winner == 'x' && panel.isFirstHand || state.winner == 'o' && !panel.isFirstHand) {
	        			showDialog(1);
	        		} else if (state.winner != '.') {
	        			showDialog(-1);
	        		} else {
	        			showDialog(0);
	        		}
	        		
	        }
	   }  
	 
	 public void showDialog(int win) {
		 	
		 	String text;
		 	if (win == 1) text = "Congratulation! You Win!";
		 	else if (win == -1) text = "Oh, You lose ><";
		 	else text = "Tie...";
		 		
	        JOptionPane.showConfirmDialog(null, text, "Game End", JOptionPane.DEFAULT_OPTION);   
	        
	 }
	 
}
