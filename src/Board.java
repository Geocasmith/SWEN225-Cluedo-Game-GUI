

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.function.DoubleToIntFunction;
import java.util.regex.Pattern;
public class Board {
    //For Coloured Text
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_WHITE = "\u001B[37m";

    //Variables to Hold The Columns And Rows Of The Game Board
    private int col = 31, row = 29;
    private int change = 6;

    //Array To Hold All Parts Of The Game Board
     String board[][] =  new String[col][row];
     private String TR = "┐", TL = "┌", BR = "┘",BL = "└",H = "─", V = "|";
     Pattern wall = Pattern.compile("┐|┌|┘|└|─|\\||");


    /**
     * Board Constructor
     */
    public Board(Game game){

        this.initializeBoard();
        game.board = this;
    }

    /**
     * Method To Add Each Of The Player Objects To The Board Copy For Display
     * @param game
     * @param board1
     */
    private void addPlayers(Game game,String[][] board1){
        for(Player a : game.allPlayers){
            board1[a.getY()][a.getX()] = "Player"+a.getName();
        }
    }

    /**
     * Method To Add Each Of The Weapon Objects To The Board Copy For Display
     * @param game
     * @param board1
     */
    private void addWeapons(Game game, String[][] board1){
        for(Weapon a : game.weaponDraw){
            board1[a.y][a.x] = "Weapon"+a.name;
        }
    }

    /**
     * Method To Print Each Of The Weapon Objects In A Red Colour So Players Can Easily Identify Where They Are
     * @param a
     * @param game
     */
    private void drawWeapon(String a, Game game){
        if(game.colour){
        System.out.print(" "+ANSI_RED+a.charAt(6)+ANSI_RESET);
        }else{
        System.out.print(" "+a.charAt(6));
        }
    }

    /**
     * Method To Print Each Of The Player Object In Their Respective Colour
     * @param a
     * @param game
     */
    private Color drawPlayer(String a, Game game){

        char b = a.charAt(6);
        Color color = null;
        String reset = "";
       
        switch(b)
        {
            case 'P' :
                color = Color.PINK;
                break;
            case 'M' :
                color = Color.yellow;
                break;
            case 'S' :
                color = Color.red;
                break;
            case 'W' :
                color = Color.gray;
                break;
            case 'G' :
                color = Color.GREEN;
                break;
            case 'C' :
                color = Color.blue;
                break;

        }
        return color;

    }



    /**
     * Method To Return The Board As A String For Displaying To Players
     */
    public void drawBoard(Game game){
        Main.boardPanel.removeAll();

        String[][] boardCopy = new String[col][row];
        for(int i = 0; i < col; i++){
            if (row >= 0) System.arraycopy(board[i], 0, boardCopy[i], 0, row);
        }
        addWeapons(game,boardCopy);
        addPlayers(game,boardCopy);

        //Iterate Through All Of The Squares To Find The Players And Weapons And Draw Them Accordingly
        for(int i = 0; i < col; i++){
            for(int g = 0; g <row; g++){
                //When The Players Are Added To The BoardCopy I Put "Player" In Front Of The String To Make It Easy To Find, Mostly For Colour
                if(boardCopy[i][g].contains("Player")){
                   Color a = drawPlayer(boardCopy[i][g],game);
                   createPanel(i,g,a);
                //When The Players Are Added To The BoardCopy I Put "Weapon" In Front Of The String To Make It Easy To Find, Mostly For Colour
                }else if(boardCopy[i][g].contains("Weapon")){
                    createPanel(i,g,new Color(255,0,0));
                    //drawWeapon(boardCopy[i][g],game);
                }else{
                    if(wall.toString().contains(boardCopy[i][g])){
                        createPanel(i,g,new Color(0,0,0));
                    }else{if(!boardCopy[i][g].equals("\n")){
                        createPanel(i,g,new Color(255,255,255));}
                    }
                }
            }
        }

        Main.boardPanel.repaint();
        Main.mainFrame.validate();

    }
    
    /**
     * Method To Create The Deafult Starting Board
     * @param i
     * @param g
     * @param color
     */
    public void createPanel(int i, int g, Color color ){
        JPanel PANEL = new JPanel();
        int sizeX = (Main.boardPanel.getWidth()-200)/30;
        int sizeY = (Main.boardPanel.getHeight())/30;
        PANEL.setBounds(g*sizeX,i*sizeY,sizeX,sizeY);
        PANEL.setBackground(color);
        Main.boardPanel.add(PANEL);
    }

    /**
     * Method To Create The Deafult Starting Board
     */
    private void initializeBoard(){

        //Make All Squares A Space
        for(int i = 0; i < col; i++){
            for(int g = 0; g <row; g++){
                board[i][g] = " ";
            }
        }

        //Run A Loop Which Add EndLines To Create The Board
        for(int i = 0; i <col; i++){
            board[i][row-1] = "\n";
            if(i > 0){

            //Add The Side Walls Of The Board
            board[i][0] = V;
            board[i][row-2] = V;
            }
        }
        //Add Each Corner
        board[0][0] = TL;
        board[0][row-2] = TR;
        board[col-1][0] = BL;
        board[col-1][row-2] = BR;

        //Add The Initial Horizontal Walls At Both The Top And The Bottom
        for(int i = 1; i <row-2; i++){
        board[col-1][i] = H;
        board[0][i] = H;
        }

        //Add The Kitchen
        for(int i = 1; i < 7; i++){
            board[7][i] = H;
            board[i][7] = V;
            for(int g = 1; g < 7; g++){
                board[i][g] = "R";
            }
        }
        board[7][5] = "R";
        board[7][7] = BR;


        //Add The Ball Room

        //Interier
        for(int i = 3; i < 8; i++){
            for(int g = 10; g < 18; g++){
                board[i][g] = "R";
            }
        }
        for(int i = 1; i <3; i++ ){
            for(int g = 13; g< 15; g++){
                board[i][g] = "R";
            }

        }

        //Hard Coding For Simplicity
        board[1][8] = BR;
        board[1][12] = V;
        board[1][15] = V;
        board[2][12] = BR;
        board[2][11] = H;
        board[2][10] = TL;
        board[2][17] = TR;
        board[2][16] = H;
        board[2][15] = BL;
        board[7][10] = BL;
        board[7][17] = BR;

        //Add South Wall Of Ball Room
        for(int i = 12; i < 16; i++){
            board[7][i] = H;
        }

        //Add The Side Walls Of Ball Room
        for(int i = 3; i <7; i++){
            if(i != 5) {
                board[i][10] = V;
                board[i][17] = V;
            }
        }

        //Add Conservatory
        board[1][19] = BL;
        for(int i = 1; i <4; i++){
            board[i][20] = V;
            board[6][22+i] = H;
        }
        board[4][20] = BL;
        board[6][22] = BL;
        board[6][26] = H;
        board[5][22] = V;
        for(int i = 21; i < 27; i++){
            if(i> 22){
                board[5][i] = "R";
            }
            for(int g = 1; g < 5; g++){
                board[g][i] = "R";
            }
        }

        //Add Dining Room
        for(int i = 11; i <18; i++){
            for(int g = 1; g <9; g++){
                board[i][g] = "R";
            }
        }
        for(int i = 6; i < 10; i++){
            board[11][i] = " ";
        }
        for(int i = 1; i <6; i++){
            board[10][i] = H;
            board[18][i] = H;
        }
        board[10][6] = TR;
        board[11][6] = BL;
        board[11][7] = H;
        board[11][8] = H;
        board[18][7] = H;
        board[18][8] = H;
        board[11][9] = TR;
        for(int i = 12; i< 18; i++){
            board[i][9] = V;
        }
        board[13][9] = "R";
        board[18][9] = BR;
        board[18][6] = "R";

        //Add Billard Room
        board[8][26] = TL;
        for(int i = 26; i > 20; i--){
            board[9][i] = H;
            if(i <25){
                board[15][i] = H;
            }
        }
        board[9][20] = TL;
        for(int i = 11; i <15; i++){
            board[i][20] = V;
        }
        board[15][20] = BL;
        for(int i = 10; i < 15; i ++){
            for(int g = 21; g < 27; g++){
                board[i][g] = "R";
            }

        }
        board[15][26] = TL;
        board[15][25] = "R";
        board[16][26] = V;
        board[10][20] = "R";

        //Add Basement

        //Add Corners
        board[10][12] = TL;
        board[10][16] = TR;
        board[19][16] = BR;
        board[19][16] = BR;
        board[19][12] = BL;
        for(int i = 11; i < 19; i++){
            board[i][12] = V;
            board[i][16] = V;
        }
        for(int i = 13; i < 16; i++){
            board[10][i] = H;
            board[19][i] = H;
        }

//bottom half of board

    //Add The Lounge

    //top wall
    for(int i = 1; i < 7; i++){
        board[17+change][i] = H;
    }
    //door
    board[17+change][8] = TR;
    board[17+change][7] = "R";
    //right wall
    for(int i = 18; i < 24; i++){
        board[i+change][8] = V;
    }
    //interior
    for(int i = 1; i < 8; i++){
        for(int g = 18; g < 24; g++){
            board[g+change][i] = "R";
        }
    }


    //Add The Hall

    //top wall
    board[16+change][10] = TL;
    for(int i = 11; i < 17; i++){
        board[16+change][i] = H;
    }
    board[16+change][17] = TR;
    //door
    board[16+change][13] = "R";
    board[16+change][14] = "R";

    //left wall
    for(int i = 17; i < 24; i++){
        board[i+change][10] = V;
    }
    //right wall
    for(int i = 17; i < 24; i++){
        board[i+change][17] = V;
    }
    board[19+change][17] = "R";

    //interior
    for(int i = 11; i < 17; i++){
        for(int g = 17; g < 24; g++){
            board[g+change][i] = "R";
        }
    }

    //Add The Study
    //top wall
    board[19+change][19] = TL;
    board[19+change][20] = "R";
    for(int i = 21; i < 27; i++){
        board[19+change][i] = H;
    }
    board[16+change][17] = TR;
    //door
    board[16+change][13] = "R";
    board[16+change][14] = "R";

    //left wall
    for(int i = 20; i < 23; i++){
        board[i+change][19] = V;
    }
    //interior
    for(int i = 20; i < 27; i++){
        for(int g = 20; g < 24; g++){
            board[g+change][i] = "R";
        }
    }
    //right indent
    board[23+change][19] = BL;
    board[23+change][20] = TR;

    //Add The Library
    //interior
   for(int i = 20; i < 27; i++){
        for(int g = 12; g < 17; g++){
            board[g+change][i] = "R";
        }
    }
    //top wall
    board[19][19] = TL;
   for(int i = 21; i < 26; i++){
        board[11+change][i] = H;
    }
    board[11+change][23] = "R";
    board[14+change][19] = "R";

    //bottom wall
   for(int i = 21; i < 26; i++){
        board[17+change][i] = H;
    }
    //top left indent
    board[11+change][20] = TL;
    board[13+change][19] = V;
    board[12+change][20] = BR;
    board[12+change][19] = TL;
    board[16+change][19] = "R";

    //bottom left indent
    board[15+change][19] = V;
    board[16+change][19] = BL;
    board[16+change][20] = TR;
    board[17+change][20] = BL;

    //top right indent
    board[11+change][26] = TR;
    board[12+change][26] = BL;

    //bottom right indent
    board[17+change][26] = BR;
    board[16+change][26] = TL;
    board[16+change][17] = TR;

    //left wall
   for(int i = 20; i < 22; i++){
        board[i][19] = V;
    }
   board[20][19] = "R";
}
}
