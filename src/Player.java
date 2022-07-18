import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Player{
	//Holds The Player's X , Y And Their Order Number
	private int x, y;
	public int orderNum;

	//Holds The Player Name/Display String, Their Description, And The Room They Are Currently In
	private String name,description, playerName;
public String room;
	//Holds The Player's hand
	private Hand hand;

	//Holds A List Of Strings For The Options The Player Has
	private ArrayList<String> adjacentTiles = new ArrayList<>();
	public boolean active = true;

	/**
	 * Constructor
	 * @param name
	 * @param description
	 * @param x
	 * @param y
	 * @param i
	 */
	public Player(String name,String description,int x, int y,int i) {
		this.name = name;
		this.description = description;
		this.x = x;
		this.y = y;
		this.orderNum = i;
	}

	// SETTERS
	// Sets players current x location
	public void setX(int x) {
		this.x = x;
	}

	// Sets players current y location
	public void setY(int y) {
		this.y = y;
	}

	// Sets players hand
	public void setHand(Hand h) {
		this.hand = h;
	}
	
	// Sets player's real name
	public void setPlayerName(String n) {
		this.playerName = n;
	}

	// GETTERS
	// Gets players current x location
	public int getX() {
		return x;
	}

	// Gets players current y location
	public int getY() {
		return y;
	}

	// Gets players name
	public String getName() {
		return name;
	}
	
	// Gets players real name
	public String getPlayerName() {
		return playerName;
	}

	// Gets players description
	public String getDescription() { return description; }

	// Gets current Hand;
	public Hand getHand() {
		return hand;
	}


	/**
	 * Moves The Current Player By The Given X And Y While Testing If They Enter A Room
	 * @param X
	 * @param Y
	 * @param b
	 */
	private void move(int X, int Y, Board b) {
		Game.sum--;
		 Main.movesLeftLabel.setText("Moves Left: " + Game.sum);
		 
		 Main.mainFrame.repaint();
		 Main.mainFrame.revalidate();
		 
		System.out.println(Game.sum);
		this.y += Y;
		this.x += X;

		if(b.board[this.y][this.x] == "R"){
		if (this.x < 8 && this.y < 8) {
			this.room = "Kitchen";
		} else if (this.x < 11 && this.y < 18 && this.y > 11) {
			this.room = "Dining Room";
		} else if (this.x < 10 && this.y < 36 && this.y > 22) {
			this.room = "Lounge";
		} else if (this.x < 19 && this.x > 9 && this.y < 9) {
			this.room = "Ball Room";
		} else if (this.x < 28 && this.x > 20 && this.y < 7) {
			this.room = "Conservatory";
		} else if (this.x < 28 && this.x > 18 && this.y < 14 && this.y > 8) {
			this.room = "Billiard";
		} else if (this.x < 28 && this.x > 16 && this.y < 24 && this.y > 17) {
			this.room = "Library";
		} else if (this.x < 28 && this.x > 17 && this.y < 36 && this.y > 26) {
			this.room = "Study";
		} else if (this.x < 18 && this.x > 10 && this.y < 36 && this.y > 26) {
			this.room = "Hall";
		}}else {
			this.room = null;
		}
		if (Game.sum < 1) {
			Game.turn++;
			Game.runTurn();
		}
	}

	/**
	 * Causes The Current Player To Execute A Given Action
	 * @param act
	 * @param game
	 */
	public void action(String act, Game game) {
		switch (act) {
		case "UP":
			System.out.println("UP");
			this.move(0, -1, game.board);
			break;
		case "DOWN":
			System.out.println("DOWN");
			this.move(0, 1, game.board);
			break;
		case "LEFT":
			System.out.println("LEFT");
			this.move(-1, 0, game.board);
			break;
		case "RIGHT":
			System.out.println("RIGHT");
			this.move(1, 0, game.board);
			break;
		}
		game.board.drawBoard(game);
	}

	/**
	 * Tests The Adjacent Tiles To The Current Player To See What Actions They Can Do
	 * @param playerB
	 * @return
	 */
	public String testAdjacent(Board playerB) {

		String adjacentTiles = "";
		
		if (!playerB.wall.pattern().contains(playerB.board[this.y - 1][this.x])) {
			adjacentTiles += "UP";
		}
		if (!playerB.wall.pattern().contains(playerB.board[this.y + 1][this.x])) {
			adjacentTiles += "DOWN";
		}
		if (!playerB.wall.pattern().contains(playerB.board[this.y][this.x - 1])) {
			adjacentTiles += "LEFT";
		}
		if (!playerB.wall.pattern().contains(playerB.board[this.y][this.x + 1])) {
			adjacentTiles += "RIGHT";
		}
		if (playerB.board[this.y][this.x] == "R") {
			adjacentTiles += "SUGGEST";
		}
		return adjacentTiles;
	}

	/**
	 * Lets The Player Choose A Card From A List Of Cards While Displaying A Given String
	 * @param cards
	 * @param str
	 * @return
	 */
	private String chooseCard(ArrayList<Card> cards,String str){
		int count = 0;
		// player suggestion
		while(true) {
			count = 0;
			System.out.println(str);
			for (Card a : cards) {
				System.out.println("Press " + count + " For " + a.getName());
				count++;
			}
			Scanner playerInput = new Scanner(System.in);
			try{int input = playerInput.nextInt();
				if(input >-1 && input < cards.size()){
					return cards.get(input).getName();
				}}catch(Exception e){}
			System.out.println("Please Choose An Option From The List");
		}
	}

	/**
	 * Lets the player make an accusation
	 * @param character
	 * @param weapon
	 * @param room
	 * @param game
	 */
	public void makeAccusation(String character, String weapon, String room, Game game){
		if(weapon == game.murder_weapon.getName() && room == game.murder_room.getName() && character == game.murderer.getName()){
			gameFinishWindow(character, weapon, room);
		} 
		else {
			
			JFrame frame = new JFrame("Accusation Outcome"); 
	     	JPanel panel = new JPanel(); 
	     	JLabel playerLabel = new JLabel("Accusation was wrong! You are out!");
	     	JButton submit = new JButton("Ok");
	     	panel.setLayout(new FlowLayout());
	     	panel.add(playerLabel);
	     	panel.add(submit);
	     	
	     	submit.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					frame.dispose();
				}		
            });
	     	
	     	panel.setAlignmentY(JPanel.CENTER_ALIGNMENT);
	        frame.add(panel);
	        frame.setBounds(Main.windowWidth/2-200, Main.windowHeight/2-200, 300, 100);
	        frame.setResizable(false);
	        frame.show();
			
		}
	}

	/**
	 * Lets the player make a suggestion
	 * @param game
	 * @param character
	 * @param weapon
	 * @param room
	 */
	public void makeSuggestion(Game game, String character, String weapon, String room) {

		ArrayList<Player> playersRefuted = new ArrayList<Player>();
		ArrayList<Card> cards = new ArrayList<Card>();
		Player refute = null;
		String card = "";
		
		for(Weapon w : game.weaponDraw){
			if(w.description == weapon){
				w.y = this.y;
				w.x = this.x;
			}

		}

		for (Player p : game.activePlayers) {
			if(p.getDescription() == character){
				p.setX(this.x);
				p.setY(this.y);
			}

			// check if player is not itself
			if (!p.equals(this)) {
				cards = p.compareHand(room, character, weapon);
			}
			if (cards.size() > 0) {
				refute = p;
				break;
			}
		}
		if (cards.size() != 0) {
			createSuggestionOutcomeWindow(refute, cards);
		} else {
			JFrame frame = new JFrame("Suggestion Outcome"); 
	     	JPanel panel = new JPanel(); 
	     	JLabel playerLabel = new JLabel("No player can reveal a card");
	     	JButton submit = new JButton("Ok");
	     	panel.setLayout(new FlowLayout());
	     	panel.add(playerLabel);
	     	panel.add(submit);
	     	
	     	submit.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					frame.dispose();
				}		
            });
	     	
	     	panel.setAlignmentY(JPanel.CENTER_ALIGNMENT);
	        frame.add(panel);
	        frame.setBounds(Main.windowWidth/2-200, Main.windowHeight/2-200, 300, 100);
	        frame.setResizable(false);
	        frame.show();
		}
			
	}

	/**
	 * Compares The Hand With The Selected Cards For Similarities
	 * @param room
	 * @param player
	 * @param weapon
	 * @return
	 */
	private ArrayList<Card> compareHand(String room, String player, String weapon) {
		ArrayList<Card> cards = new ArrayList<>();
		for (Card a : this.hand.cards) {
			if (a.getName() == room || a.getName() == player || a.getName() == weapon) {
				cards.add(a);
			}
		}
		return cards;
	}
	
	/**
	 * Creates a window displaying the suggestions outcome
	 * @param player
	 * @param cards
	 * @return
	 */
	private void createSuggestionOutcomeWindow(Player player, ArrayList<Card> cards) {
	       //Window components
		JFrame frame = new JFrame("Suggestion Outcome"); 
     	JPanel panel = new JPanel(); 
     	JLabel playerLabel = new JLabel(player.description + " can reveal a card");
     	JButton submit = new JButton("Ok");
     	
        //Add components and layouts
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(Box.createVerticalStrut(100));
        panel.add(playerLabel);
        
     	for (int i = 0; i < cards.size(); i++) {
     		JPanel revealPanel = new JPanel();
     		JLabel cardNum = new JLabel("Card " + (i+1)); 
     		Card card = cards.get(i);
     		JButton reveal = new JButton("Reveal");
            
            reveal.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(card.getName());
					frame.remove(panel);
					JLabel l = new JLabel(card.getName());
					frame.add(l);
					frame.repaint();
					frame.revalidate();
				}		
            });
     		revealPanel.add(cardNum);
     		revealPanel.add(reveal);
            panel.add(revealPanel);
     	}
     	
        frame.add(panel);
        frame.setBounds(Main.windowWidth/2-200, Main.windowHeight/2-200, 300, 250);
        frame.setResizable(false);
        frame.show();
	}
	
	/**
	 * Creates a window displaying game over window
	 * @param character
	 * @param weapon
	 * @param room
	 * @return
	 */
	private void gameFinishWindow(String character, String weapon, String room) {
		
		//Window components
		JFrame frame = new JFrame("Accusation Outcome");
		JPanel panel = new JPanel();
		JLabel label1 = new JLabel("Game has been won!");
		JLabel murderer_label = new JLabel(character + " was the murderer.");
		JLabel weapon_label = new JLabel(weapon + " was the murder weapon.");
		JLabel room_label = new JLabel(room + " was where the murder occured.");
		JButton submit = new JButton("Quit");

		//Add components and layouts
		label1.setFont(new Font("Arial", Font.BOLD, 16));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		panel.add(Box.createVerticalStrut(50));
		panel.add(label1);
		panel.add(murderer_label);
		panel.add(weapon_label);
		panel.add(room_label);
		panel.add(Box.createVerticalStrut(50));
		panel.add(submit);
		
		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Game Over
				System.exit(0);
			}		
        });
		
		frame.add(panel);
		frame.setBounds(Main.windowWidth/2-200, Main.windowHeight/2-200, 300, 250);
		frame.setResizable(false);
		frame.show();
	}
}

//Used For Sorting The List Of Players
class SortbyOrder implements Comparator<Player>
{
	// Used for sorting in ascending order of
	// roll number
	public int compare(Player a, Player b)
	{
		return a.orderNum - b.orderNum;
	}

}
