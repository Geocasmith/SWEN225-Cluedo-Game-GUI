import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MouseAdapterClick extends MouseAdapter {
	Game game;
	

	public MouseAdapterClick(Game g) {
		// TODO Auto-generated constructor stub
		this.game = g;
	}
	
	final JTextField hover = new JTextField();
	
	public void mouseEntered(MouseEvent e) {
		
		
		
		Point point = MouseInfo.getPointerInfo().getLocation();
		int mouseX = point.x;
		int mouseY = point.y;
		
		hover.setBounds(600, 100 , 150, 100);
		hover.setBackground(Color.white);

	

		JPanel clicked = (JPanel) e.getSource();
		int x, y;
		x = clicked.getX() / 20;
		y = clicked.getY() / 18;


		for (Player p : game.allPlayers) {
			if (p.getX() == x || p.getX() > x - 2 && p.getX() < x + 2) {
				if (p.getY() == y || p.getY() > y - 2 && p.getY() < y + 2) {
					
				hover.setText(p.getDescription());
				Main.mainFrame.repaint();
				Main.mainFrame.revalidate();
				
				}
			}
		}

		
		Main.boardPanel.add(hover);
		Main.mainFrame.repaint();
		Main.mainFrame.revalidate();
		

	}
	public void mouseExited(MouseEvent e) {
		hover.setText("Hover over player");
		
	}

}