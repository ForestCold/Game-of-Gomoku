import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
  
public class BoardUI implements Configuration{  
	
	JFrame jf;
	JPanel jp;
	GamePanel gp;
	JButton crButton;
	JRadioButton redB;
	JRadioButton blueB;
	ButtonGroup bGroup;
      
    public void createWindow()  
    {  
    		
    		// initial window
        jf = new JFrame();
		jf.setTitle("Gomoku-By-Yami");
		jf.setSize(800, 700);
		jf.setDefaultCloseOperation(3);
		jf.setLocationRelativeTo(null);
		
		// initial left panel
		jp = new JPanel();
		jp.setBackground(new Color(245,245,245));
		jf.add(jp,BorderLayout.WEST);
		
		jp.setLayout(new java.awt.FlowLayout(5,5,60));
		jp.setPreferredSize(new Dimension(160,0));
		
		// add radio button
		redB = new JRadioButton("First Hand");
		blueB = new JRadioButton("Second Hand");
		redB.setPreferredSize(new Dimension(140,30));
		blueB.setPreferredSize(new Dimension(140,30));
		bGroup = new javax.swing.ButtonGroup();  
		bGroup.add(redB);  
		bGroup.add(blueB);  
		jp.add(redB);
		jp.add(blueB);
																											
		// add button
		crButton = new javax.swing.JButton("Start!");
		crButton.setPreferredSize(new Dimension(140,40));
		jp.add(crButton);
		
		// set visible
		jf.setVisible(true);
	
    }  
    
    public void startGame(State s, Agent a, boolean isFirstHand) {
    	
		// initial board
    		State state = new State(s);
		gp = new GamePanel(state, a, isFirstHand);
		gp.setBackground(new Color(219,234,255));
		jf.add(gp,BorderLayout.CENTER);
		jf.setVisible(true);
		
    }
  
}  