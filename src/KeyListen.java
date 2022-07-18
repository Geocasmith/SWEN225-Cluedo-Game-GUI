import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class KeyListen extends JFrame implements KeyListener {
    Game game;
   
   

    public KeyListen() {
        addKeyListener(this);
        setSize(200, 100);
        setVisible(true);
    }


    @Override
    public void keyPressed(KeyEvent e) {    	System.out.println("Test keys");
        String direction = null;
        String moves = game.currentPlayer.testAdjacent(game.board);
        System.out.println(moves);
        switch (e.getKeyCode()){
            case KeyEvent.VK_LEFT:
            		direction = "LEFT";
                
                break;
            case KeyEvent.VK_RIGHT:

            		direction = "RIGHT";
            
                
                break;
            case KeyEvent.VK_UP:

             //  if(moves.contains("UP"))
            		direction = "UP";
            		
                break;
            case KeyEvent.VK_DOWN:
            	
            //	if(moves.contains("DOWN"))
            		direction = "DOWN";
            		
                break;
        }
        //Runs Action On Current Player
        if(moves.contains(direction)&& Game.sum > 0) {
        	
        game.currentPlayer.action(direction,game);}
       


    }

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}