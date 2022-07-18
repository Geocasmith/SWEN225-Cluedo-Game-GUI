import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

import javax.swing.*;

public class Main {
	
    static JMenuBar menuBar;  
    static JMenu menu, moves; 
    static JMenuItem menuItemQuit, menuItemSuggestion, menuItemAccusation;
  
    static JFrame mainFrame;
    static JPanel handPanel;
    static JPanel boardPanel;
    
    static JPanel cardOne = new JPanel();
    static JPanel cardTwo = new JPanel();
    static JPanel cardThree = new JPanel();
    static JPanel cardFour = new JPanel();
    static JPanel cardFive = new JPanel();
    static JPanel cardSix = new JPanel();
    
    static JLabel cardLabel = new JLabel();
    static JLabel movesLeftLabel = new JLabel();
    
    static ArrayList<JPanel> hand = new ArrayList<JPanel>();
    
    static int windowHeight = 800;
    static int windowWidth = 800;
    
    public static boolean diceRolled = false;
    
    public static Game game;
    public static KeyListen key = new KeyListen();
    public static MouseAdapter ML;
    
	
    public static void main(String[] args) throws InterruptedException {
   
    	// create a new frame
    	mainFrame = new JFrame("Cluedo");   
    	
        createFramework();
    	// creates new game
        game = new Game();
        key.game = game;
        ML = new MouseAdapterClick(game);
    }
    
    
    public static void createFramework() {
    	
        // create a menu bar 
    	menuBar = new JMenuBar();
  
        // create a new menu 
        menu = new JMenu("Menu");
        moves = new JMenu("Moves");
  
        // create menu items 
        menuItemSuggestion = new JMenuItem("Suggestion");
        menuItemAccusation = new JMenuItem("Accusation");
        menuItemQuit = new JMenuItem("Quit");
  
        // add quit actionListeners
        menuItemQuit.addActionListener(new ActionListener() {
        	 
            @Override
            public void actionPerformed(ActionEvent event) {
            	System.exit(0);
            }
        });
        // add suggestion actionListeners
        menuItemSuggestion.addActionListener(new ActionListener() {
        	
            @Override
            public void actionPerformed(ActionEvent event) {
            	if(
            	game.board.board[game.currentPlayer.getX()][game.currentPlayer.getY()] == "R") {
            	game.createSuggestionWindow();}
            }
        });
        // add accusation actionListeners
        menuItemAccusation.addActionListener(new ActionListener() {
        	
            @Override
            public void actionPerformed(ActionEvent event) {
            	game.createAccusationWindow();
            }
        });
        
        // add menu items to menus
        moves.add(menuItemSuggestion); 
        moves.add(menuItemAccusation); 
        menu.add(menuItemQuit); 
  
        // add menu to menu bar 
        menuBar.add(menu); 
        menuBar.add(moves);
  
        // add menubar to frame 
        mainFrame.setJMenuBar(menuBar); 
    	
        mainFrame.setLayout(null);  
        mainFrame.setSize(windowWidth, windowHeight);  
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(mainFrame.EXIT_ON_CLOSE);
        mainFrame.setForeground(new Color(255, 0, 0));
    	
    	handPanel = new JPanel();
    	boardPanel = new JPanel();
    	
    	boardPanel.setBounds(0, 0, windowWidth, windowHeight-250);
    	boardPanel.setBackground(Color.orange);
    	
    	//Card Label
    	cardLabel.setBounds(200, 20, 300, 30);
    	cardLabel.setFont(new Font("Arial", Font.BOLD, 20));
    	cardLabel.setForeground(Color.white);
    	cardLabel.setText("Cards:");
    	handPanel.add(cardLabel);
    	
    	//Moves left label
    	movesLeftLabel.setBounds(10, 10, 300, 30);
    	movesLeftLabel.setFont(new Font("Arial", Font.BOLD, 20));
    	movesLeftLabel.setForeground(Color.white);
    	movesLeftLabel.setText("Moves Left:");
    	handPanel.add(movesLeftLabel);
    	
    	handPanel.setBounds(0, windowHeight - 250, windowWidth, 250);    
        handPanel.setLayout(null);
        boardPanel.setLayout(null);

        handPanel.setBackground(new Color(1, 66, 12));
      
        mainFrame.add(handPanel);
        mainFrame.add(boardPanel);
        mainFrame.addMouseListener(ML);
        boardPanel.addMouseListener(ML);
        mainFrame.addKeyListener(key);
    }
}
