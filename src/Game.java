import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Game {
    boolean GameOver = false;
    private ArrayList<Card> characters = new ArrayList<Card>();
    private ArrayList<Card> weapons = new ArrayList<Card>();
    public  ArrayList<Card> allCharacters = new ArrayList<Card>();
    public  ArrayList<Card> allWeapons = new ArrayList<Card>();
    private ArrayList<Card> rooms = new ArrayList<Card>();
    public  ArrayList<Card> allRooms = new ArrayList<Card>();
    private Stack<Card> allCards = new Stack<Card>();
    public ArrayList<Weapon> weaponDraw = new ArrayList<>();
    public boolean colour;
    public Board board;
    static int sum;
    static int turn = 0;
    static ArrayList<Player> activePlayers = new ArrayList<>();
    static ArrayList<Player> allPlayers = new ArrayList<>();
    
    static Player currentPlayer;
    
    static ArrayList<Hand> currentDisplayHand = new ArrayList();
    
    Card murderer;
    Card murder_weapon;
    Card murder_room;
    
    int playerCount = 0;

    /**
     * Constructor
     */
    public Game(){
         this.board = new Board(this);
         this.startGame();
    }
    
    /**
     * Helper method to create all the players and add them to an ArrayList
     * @return list of all players
     */
    private static ArrayList<Player> createPlayers(){
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("S","Miss Scarlett",9,29,1));
        players.add(new Player("M","Colonel Mustard",1,19,2));
        players.add(new Player("W","Mrs. White",11,1,3));
        players.add(new Player("G","Mr. Green",16,1,4));
        players.add(new Player("C","Mrs. Peacock",26,7,5));
        players.add(new Player("P","Professor Plum",26,24,6));
        return players;
    }

    /**
     * Helper method to create all cards in the game and add them to their respective lists
     */
    private void createCards(){
        //Creates all character cards
        characters.add(new Card("Miss Scarlett"));
        characters.add(new Card("Colonel Mustard"));
        characters.add(new Card("Mrs. White"));
        characters.add(new Card("Mr. Green"));
        characters.add(new Card("Mrs. Peacock"));
        characters.add(new Card("Professor Plum"));

        //Adds cards to list of all Characters
        allCharacters.addAll(characters);

        //Creates all weapon cards
        weapons.add(new Card("Candlestick"));
        weapons.add(new Card("Dagger"));
        weapons.add(new Card("Lead Pipe"));
        weapons.add(new Card("Revolver"));
        weapons.add(new Card("Rope"));
        weapons.add(new Card("Spanner"));

        //Fields For Randomly Placing Weapons
        ArrayList<Integer> coordX = new ArrayList<Integer>(){};
        ArrayList<Integer> coordY = new ArrayList<Integer>(){};
        coordX.add(4); coordY.add(4);
        coordX.add(4); coordY.add(14);
        coordX.add(24); coordY.add(3);
        coordX.add(14); coordY.add(5);
        coordX.add(24); coordY.add(12);
        coordX.add(4); coordY.add(28);
        coordX.add(14); coordY.add(28);
        coordX.add(24); coordY.add(28);

        //Create The Weapon Objects And Get Them Ready To Be Displayed
        createWeaponObject(coordX,coordY,"c","Candlestick" );
        createWeaponObject(coordX,coordY, "d","Dagger" );
        createWeaponObject(coordX,coordY,"l","Lead Pipe");
        createWeaponObject(coordX,coordY,"g","Revolver");
        createWeaponObject(coordX,coordY, "r","Rope" );
        createWeaponObject(coordX,coordY, "s","Spanner");

        //Adds cards to list of all Characters
        allWeapons.addAll(weapons);

        //Creates all room cards
        rooms.add(new Card("Kitchen"));
        rooms.add(new Card("Ball Room"));
        rooms.add(new Card("Conservatory"));
        rooms.add(new Card("Billard Room"));
        rooms.add(new Card("Dining Room"));
        rooms.add(new Card("Library"));
        rooms.add(new Card("Lounge"));
        rooms.add(new Card("Hall"));
        rooms.add(new Card("Study"));

        //Adds cards to a list of all rooms
        allRooms.addAll(rooms);
    }

    /**
     * Method that gets the user to input a valid number of players (from 3-6 players).
     */
    private void chooseNumPlayers(){
        
        //Window components
        JFrame frame = new JFrame("Setup: Player Count"); 
        JPanel panel = new JPanel(); 
        JComboBox<Integer> playerCombo = new JComboBox<Integer>(new Integer[] { 3, 4, 5, 6 });
        JLabel label1 = new JLabel("How many Players?");
        JButton submit = new JButton("Ok");
        
        //Add components and layouts
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(Box.createVerticalStrut(100));
        panel.add(label1);
        panel.add(playerCombo);
        panel.add(submit);
        frame.add(panel);
        frame.setBounds(Main.windowWidth/2-200, Main.windowHeight/2-200, 300, 250);
        frame.setResizable(false);
        frame.show();
        
        submit.addActionListener(new ActionListener() {
        	 
            @Override
            public void actionPerformed(ActionEvent event) {
            	frame.dispose();
                int playerCount = (int) playerCombo.getSelectedItem();
                choosePlayers(allPlayers, playerCount);
            }
        });
    }

    /**
     * Starts The Game
     */
    public void startGame(){
    	
    	//create card objects
        createCards();
        createRoll();
        
        
        
        //create player objects
        allPlayers = createPlayers();
        
        //create murder case
        this.createMurder();
        
        //choose number of players
        chooseNumPlayers();
        
    }

    private void beginGame() {
    	
    	
    	runTurn();
    	currentPlayer = activePlayers.get(0);
    	Main.game.board.drawBoard(Main.game);
    	Main.mainFrame.repaint();
    	Main.mainFrame.revalidate();
//	    while(!GameOver) {
//	    	Hand h = activePlayers.get(0).getHand();
//
//	    	for(Player a : activePlayers) {
//	    		if(a.active) {
//		            System.out.println("Starting Turn: " + a.getDescription());
//		            runTurn(a);
//		        }
//	    	}
//	    }
    	
    	
   }
    
    /**
     * Method that creates a suggestion window 
     * 
     */
    public void createSuggestionWindow() {
    	
    	//Window components
        JFrame frame = new JFrame("Make a Suggestion"); 
        JPanel whoPanel = new JPanel(); 
        JPanel whatPanel = new JPanel();
        JPanel wherePanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        JPanel blank = new JPanel();
        ButtonGroup characterButtons = new ButtonGroup();
        ButtonGroup weaponButtons = new ButtonGroup();
        ButtonGroup roomButtons = new ButtonGroup();
        ArrayList<JRadioButton> characterList = new ArrayList<JRadioButton>();
        ArrayList<JRadioButton> weaponList = new ArrayList<JRadioButton>();
        ArrayList<JRadioButton> roomList = new ArrayList<JRadioButton>();
        JLabel label1 = new JLabel("Who did it?");
        JLabel label2 = new JLabel("What weapon?");
        JLabel label3 = new JLabel("What room?");
        JButton submit = new JButton("Suggest");
        
        label1.setFont(new Font("Arial", Font.BOLD, 16));
        label2.setFont(new Font("Arial", Font.BOLD, 16));
        label3.setFont(new Font("Arial", Font.BOLD, 16));
        
        whoPanel.setLayout(new BoxLayout(whoPanel, BoxLayout.Y_AXIS));
        whatPanel.setLayout(new BoxLayout(whatPanel, BoxLayout.Y_AXIS));
        wherePanel.setLayout(new BoxLayout(wherePanel, BoxLayout.Y_AXIS));
        
        whoPanel.setBounds(0, 0, 200, 300);
        whatPanel.setBounds(200, 0, 200, 300);
        wherePanel.setBounds(400, 0, 200, 300);
        bottomPanel.setBounds(0, 300, 600, 100);
        
        whoPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        whatPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        wherePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        whoPanel.add(label1);
        whatPanel.add(label2);
        wherePanel.add(label3);
        bottomPanel.add(submit);
        
        whoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        whatPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        wherePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        label1.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        label2.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        label3.setAlignmentX(JPanel.CENTER_ALIGNMENT);
       
        //Load radioButton arrays
        for (Card c : allCharacters) {
        	JRadioButton button = new JRadioButton(c.getName());
        	characterButtons.add(button);
        	characterList.add(button);
        	button.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        	whoPanel.add(button);
        }
        for (Card c : allWeapons) {
        	JRadioButton button = new JRadioButton(c.getName());
        	weaponButtons.add(button);
        	weaponList.add(button);
        	button.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        	whatPanel.add(button);
        }
        for (Card c : allRooms) {
        	JRadioButton button = new JRadioButton(c.getName());
        	roomButtons.add(button);
        	roomList.add(button);
        	button.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        	wherePanel.add(button);
        }
        
        frame.setBounds(Main.windowWidth/2-300, Main.windowHeight/2-150, 600, 400);
        frame.setResizable(false);
        frame.add(whoPanel);
        frame.add(whatPanel);
        frame.add(wherePanel);
        frame.add(bottomPanel);
        frame.add(blank);
        frame.show();
        
        submit.addActionListener(new ActionListener() {
        	
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		String who = "";
        		String what = "";
        		String where = currentPlayer.room;
        		
        		//For each button in the character ArrayList, check if it is selected
           		for (JRadioButton button : characterList) {
        			if (button.isSelected()) {
        				who = button.getText();
        			}
        		}
           		//For each button in the weapon ArrayList, check if it is selected
           		for (JRadioButton button : weaponList) {
        			if (button.isSelected()) {
        				what = button.getText();
        			}
        		}
           		//For each button in the room ArrayList, check if it is selected	
           		for (JRadioButton button : roomList) {
        			if (button.isSelected()) {
        				where = button.getText();
        			}
        		}
           		//Make the suggestion
           		
           		currentPlayer.makeSuggestion(Main.game, who, what, where);
           		frame.dispose();
        	}
        });
    }
    private String getLastPlayer(ArrayList<Player> activePlayers){
        for(Player a : activePlayers){
            if(a.active) {
                return a.getDescription();
            }
        }
        return "(Method getLastPlayer) Error: no players left";
    }

    private void endGame(String s){

        //Creates New Frames..buttons
        JFrame frame = new JFrame();
        JButton restartButton = new JButton("Restart Game");//1) create button
        JButton endButton = new JButton("Close Game");//1) create button
        JLabel label = new JLabel(s);


        //Creates Panel and sets layout+border
        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(restartButton);//2) add buton to panel
        restartButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        panel.add(endButton);//2) add buton to panel
        endButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel.setLayout(new GridLayout(0,1));

        //Sets close frame, title, window to match size, window being visible & in focus
        frame.add(panel,BorderLayout.CENTER);
        frame.setBounds(Main.windowWidth/2-200, Main.windowHeight/2-200, 300, 150);//centers it
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Game Over");
        frame.pack();
        frame.setVisible(true);


        //actionlisteners


        //Actionlistener to close the game
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Game game = new Game();
                game.startGame();
            }
        });

        //actionlistener to end the game
        endButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                System.exit(0);

            }
        });
    }
    public void createAccusationWindow() {
    	
    	//Window components
        JFrame frame = new JFrame("Make an Accusation"); 
        JPanel whoPanel = new JPanel(); 
        JPanel whatPanel = new JPanel();
        JPanel wherePanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        JPanel blank = new JPanel();
        ButtonGroup characterButtons = new ButtonGroup();
        ButtonGroup weaponButtons = new ButtonGroup();
        ButtonGroup roomButtons = new ButtonGroup();
        ArrayList<JRadioButton> characterList = new ArrayList<JRadioButton>();
        ArrayList<JRadioButton> weaponList = new ArrayList<JRadioButton>();
        ArrayList<JRadioButton> roomList = new ArrayList<JRadioButton>();
        JLabel label1 = new JLabel("Who did it?");
        JLabel label2 = new JLabel("What weapon?");
        JLabel label3 = new JLabel("What room?");
        JButton submit = new JButton("Accuse!");
        
        label1.setFont(new Font("Arial", Font.BOLD, 16));
        label2.setFont(new Font("Arial", Font.BOLD, 16));
        label3.setFont(new Font("Arial", Font.BOLD, 16));
        
        whoPanel.setLayout(new BoxLayout(whoPanel, BoxLayout.Y_AXIS));
        whatPanel.setLayout(new BoxLayout(whatPanel, BoxLayout.Y_AXIS));
        wherePanel.setLayout(new BoxLayout(wherePanel, BoxLayout.Y_AXIS));
        
        whoPanel.setBounds(0, 0, 200, 300);
        whatPanel.setBounds(200, 0, 200, 300);
        wherePanel.setBounds(400, 0, 200, 300);
        bottomPanel.setBounds(0, 300, 600, 100);
        
        whoPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        whatPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        wherePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        whoPanel.add(label1);
        whatPanel.add(label2);
        wherePanel.add(label3);
        bottomPanel.add(submit);
        
        whoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        whatPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        wherePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        label1.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        label2.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        label3.setAlignmentX(JPanel.CENTER_ALIGNMENT);
       
        //Load character radio buttons
        for (Card c : allCharacters) {
        	JRadioButton button = new JRadioButton(c.getName());
        	characterButtons.add(button);
        	characterList.add(button);
        	button.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        	whoPanel.add(button);
        }
        //Load weapon radio buttons
        for (Card c : allWeapons) {
        	JRadioButton button = new JRadioButton(c.getName());
        	weaponButtons.add(button);
        	weaponList.add(button);
        	button.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        	whatPanel.add(button);
        }
        //Load room radio buttons
        for (Card c : allRooms) {
        	JRadioButton button = new JRadioButton(c.getName());
        	roomButtons.add(button);
        	roomList.add(button);
        	button.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        	wherePanel.add(button);
        }
        
        //Frame setup
        frame.setBounds(Main.windowWidth/2-300, Main.windowHeight/2-150, 600, 400);
        frame.setResizable(false);
        frame.add(whoPanel);
        frame.add(whatPanel);
        frame.add(wherePanel);
        frame.add(bottomPanel);
        frame.add(blank);
        frame.show();
        
        //Submit button functionality
        submit.addActionListener(new ActionListener() {

        	@Override
        	public void actionPerformed(ActionEvent e) {
        		String who = "";
        		String what = "";
        		String where = "";

        		for (JRadioButton button : characterList) {
        			if (button.isSelected()) {
        				who = button.getText();
        			}
        		}
        		for (JRadioButton button : weaponList) {
        			if (button.isSelected()) {
        				what = button.getText();
        			}
        		}
        		for (JRadioButton button : roomList) {
        			if (button.isSelected()) {
        				where = button.getText();
        			}
        		}
        		currentPlayer.makeAccusation(who, what, where, Main.game);

        		frame.dispose();
        	}
        });
    }
    
    /**
     * Method that lets you choose what players you want to play as
     * @param allPlayers list of all players
     * @param numPlayers list of number of players chosen
     * @return
     */
    private void choosePlayers(ArrayList<Player> currentPlayers, int numPlayers){
    	
    	playerCount++;
    	if (activePlayers.size() == numPlayers) {
    		this.assignCards();
    		
    		currentPlayers.addAll(activePlayers);
    		
            this.beginGame();
    		return;
    	}
	    	//Window components
	        JFrame frame = new JFrame("Setup: Character Selection"); 
	        JPanel panel = new JPanel(); 
	        
	        JTextField nameInput = new JTextField();
	        nameInput.setBounds(20, 20, 100, 20);
	        nameInput.setText("Enter player name");
	        nameInput.setHorizontalAlignment(JTextField.CENTER);
	        
	        JComboBox<String> characterCombo = new JComboBox<String>();
	        JLabel label1 = new JLabel("Enter Name:");
	        label1.setAlignmentX(JLabel.CENTER_ALIGNMENT);
	        JLabel label2 = new JLabel("Choose a character:");
	        label2.setAlignmentX(JLabel.CENTER_ALIGNMENT);
	        JButton submit = new JButton("Ok");
	        submit.setAlignmentX(JButton.CENTER_ALIGNMENT);
	        
	        //Add components and layouts
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	        panel.add(label1);
	        panel.add(nameInput);
	        panel.add(Box.createVerticalStrut(30));
	        panel.add(label2);
	        panel.add(characterCombo);
	        panel.add(Box.createVerticalStrut(30));
	        panel.add(submit);
	        frame.add(panel);
	        frame.setBounds(Main.windowWidth/2-200, Main.windowHeight/2-200, 300, 250);
	        frame.setResizable(false);
	        frame.show();
	    	
	        for (Player p : currentPlayers) {
	        	characterCombo.addItem(p.getDescription());
	        }
	        
	        submit.addActionListener(new ActionListener() {
	
	        	@Override
	        	public void actionPerformed(ActionEvent e) {
	        		String playerName = nameInput.getText();
	        		String name = (String) characterCombo.getSelectedItem();
	        		int temp = 0;
	        		Player player = null;
	        		for (int i = 0; i < currentPlayers.size(); i++) {
	        			if (name == currentPlayers.get(i).getDescription()) {
	        				player = currentPlayers.get(i);
	        				temp = i;
	        			}
	        		}
	        		player.setPlayerName(playerName);
	        		currentPlayers.remove(temp);
	        		activePlayers.add(player);
	        		frame.dispose();
	        		choosePlayers(currentPlayers, numPlayers);
	        	}
	        });
    }
    
    //returns the sum of two random numbers from 1-6 for one dice roll
    private static int diceRoll() {
        int r1 = (int)(Math.random() * 6) + 1;
        int r2 = (int)(Math.random() * 6) + 1;
        int sum = r1 + r2;
        return sum;
    }

    /**
     * Creates a random murder case
     */
    private void createMurder() {

        //Generates three random indexes for players, weapons, rooms
        int pIndex = (int)(Math.random() * characters.size() + 0);
        int wIndex = (int)(Math.random() * weapons.size() + 0);
        int rIndex = (int)(Math.random() * rooms.size() + 0);

        //Gets the corresponding names from the indexes
         murderer = characters.get(pIndex);
         murder_weapon = weapons.get(wIndex);
         murder_room = rooms.get(rIndex);
        //Removes each object from array lists
        characters.remove(pIndex);
        weapons.remove(wIndex);
        rooms.remove(rIndex);
    }

    /**
     *
     * @param player
     */
    static void runTurn(){
   
    	sum = -1;
    	if (turn > activePlayers.size()-1) {
    		turn = 0;
    	}
    	Player player = activePlayers.get(turn);
    	Hand hand = player.getHand();
    	currentPlayer = player;
    	
    	//Draws hand on screen
    	for (int i = 0; i < hand.cards.size(); i++) {
    		JPanel cardPanel = new JPanel();
    		cardPanel.setLayout(new GridBagLayout());
    		cardPanel.setBounds(200 + (i * 95), 60, 90, 120);
    		
    		//Scales image and adds
    		Image dimg = getImage(hand.cards.get(i).getName()).getScaledInstance(cardPanel.getWidth(), cardPanel.getHeight(),
                    Image.SCALE_SMOOTH);
    		JLabel picLabel = new JLabel(new ImageIcon(dimg));
            cardPanel.add(picLabel);
    		
       		
    		
    		Main.handPanel.add(cardPanel);
    		Main.cardLabel.setText(player.getPlayerName() + "'s Hand");
    	}
    	
    	Main.handPanel.repaint();
		Main.mainFrame.validate();
}
    /**
     * Helper method to get image
     * @param s
     * @return
     */
    private static Image getImage(String s){
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("assets/"+s+".png"));
        } catch (IOException e) {
            System.out.println("CANT LOAD IMAGE");
            e.printStackTrace();
        }
        return image;
    }

    /**
     * assigns cards evenly amongst each player
     */
    private void assignCards() {
        //adds players, weapons, and rooms into a master list of cards
        allCards.addAll(characters);
        allCards.addAll(weapons);
        allCards.addAll(rooms);
        Collections.shuffle(allCards); //shuffles deck
       // System.out.println(allCards);

        //initiates players hands
    	for (Player p : activePlayers) {
			p.setHand(new Hand());
    	}

    	//deals out cards
        while (!allCards.isEmpty()) {
        	for (Player p : activePlayers) {
        		if (!allCards.isEmpty()) {
        			p.getHand().addToHand(allCards.pop());
        		}
        	}
        }
    }
    
    /**
     * Method that creates dice roll button and functionality
     */
    public static void createRoll() {
    	Font font1 = new Font("Arial", Font.BOLD, 20);
        Font font2 = new Font("Arial", Font.BOLD, 16);

        //creates left dice
        final JTextField leftDice = new JTextField();
        leftDice.setBounds(45,45, 50,50);
        leftDice.setText("");
        leftDice.setHorizontalAlignment(JTextField.CENTER);
        leftDice.setFont(font1);
        leftDice.setEditable(false);

        //creates right dice
        final JTextField rightDice = new JTextField();
        rightDice.setBounds(100,45, 50,50);
        rightDice.setText(""); 
        rightDice.setHorizontalAlignment(JTextField.CENTER);
        rightDice.setFont(font1);
        rightDice.setEditable(false);

        //creates roll dice button
        JButton roll=new JButton("Roll Dice"); 
        roll.setBounds(50,110,100,60); 
        roll.setFont(font2);
        roll.setFocusable(false);
        roll.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e){
            	
                    int r1 = (int)(Math.random() * 6) + 1;
                    int r2 = (int)(Math.random() * 6) + 1;
                    sum = r1 + r2;
                    Main.movesLeftLabel.setText("Moves Left: " + sum);

                    leftDice.setText(Integer.toString(r1));
                    rightDice.setText(Integer.toString(r2));
            }
       });
        
        Main.handPanel.add(roll);
        Main.handPanel.add(leftDice);
        Main.handPanel.add(rightDice);
    }

    /**
     * Creates Weapon Object
     */
    private void createWeaponObject(ArrayList<Integer> coordX,ArrayList<Integer> coordY,String name,String description){
        int num =(int)(Math.random()*(coordX.size()));
        weaponDraw.add(new Weapon(coordX.get(num),coordY.get(num),name,description));
        coordX.remove(num);
        coordY.remove(num);

    }
}
