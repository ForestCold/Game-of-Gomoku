import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Configuration{
	
	State state;
	Agent agent;
	boolean isFirstHand;
	
	public GamePanel(State s, Agent a, boolean f){
	    if (!f) {
	    	  	state = new State(a.action(new State()).state);
	    } else {
	    	  	state = new State();		
	    } 
		agent = a;
		isFirstHand = f;
	}
	
	public void paint(Graphics g){
		
		super.paint(g);
		Graphics2D gg = (Graphics2D) g; 
		
		for(int i = 0; i < boardWidth; i++){
			gg.drawLine(X, Y + i * Size, X + Size * (boardWidth - 1), Y + i * Size);  
			gg.drawLine(X + i * Size, Y, X + i * Size, Y + Size * (boardWidth - 1));  
		}
		
		paintChess(gg);
		
		humanListener listener = new humanListener(this);  
        this.addMouseListener(listener);  
        
	}
	
	public void paintChess(Graphics2D gg){
		
		for(int i = 0; i < state.board.length; i++)  
			for(int j = 0; j < state.board[i].length; j++)  
			{  
				if(state.board[i][j] != '.')  
				{  
					if(state.board[i][j] == 'x')  
					{  
						gg.setColor(Color.red);  
					} else {  
						gg.setColor(Color.blue);  
					}  
					int x = Y + j * Size - chessSize / 2;  
                		int y = X + i * Size - chessSize / 2;  
                		gg.fillOval(x, y, chessSize, chessSize);  
            }  
        }  
	}
	
}  
	