package com.udacity;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by udacity 2016
 * The Main UI class containing game logic and backend 2D array
 */
public class GameUI extends JPanel {

    //object reference to the actual game to add plays and reflect plays in UI
    private Game game;
    //frame to display grid and plays
    private JFrame frame;
    //images
    private BufferedImage grid;
    private BufferedImage x;
    private BufferedImage o;

    /**
     * Creates a gameUI given a Game object
     * @param game
     */
    public GameUI(Game game) {

        this.game = game;

        // load images from resources files
        try {
            ClassLoader classLoader = GameUI.class.getClassLoader();
            grid = ImageIO.read(classLoader.getResourceAsStream("grid.png"));
            x = ImageIO.read(classLoader.getResourceAsStream("x.png"));
            o = ImageIO.read(classLoader.getResourceAsStream("o.png"));
        } catch (IOException ex) {
            System.out.println("Failed to load images");
        }

        //create new game buttons
        JButton newGameButton = new JButton("New Single Player Game");
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton.addActionListener(new ActionListener() {  // connects the new game button to its buttonPressed method
            public void actionPerformed(ActionEvent e)
            {
                newGameButtonPressed(false);
            }
        });

        JButton new2PlayerGameButton = new JButton("New 2 Player Game");
        new2PlayerGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        new2PlayerGameButton.addActionListener(new ActionListener() {  // connects the new game button to its buttonPressed method
            public void actionPerformed(ActionEvent e)
            {
                newGameButtonPressed(true);
            }
        });

        // control what happens when new game buttons cickes
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                //get grid dimensions and location relative to mouse click
                JPanel panel = (JPanel)e.getSource();
                double boardWidth = panel.getWidth();
                double boardHeight = panel.getHeight();
                double boardX = 10; // the horizontal gap left of the grid
                double boardY = 40; // the vertical gap above the grid
                //return if clicked outside the grid area
                if(e.getX()<boardX || e.getY() < boardY)
                    return;
                //calculate which part of the grid did they click
                int i = (int) (3*((e.getX()-boardX)/(boardWidth-boardX)));
                int j = (int) (3*((e.getY()-boardY)/(boardHeight-boardY)));
                //take action and update grid 2D array based on where they clicked
                panelMouseClicked(i,j);
            }
        });

        //add buttons to the panel for display
        this.add(newGameButton);
        this.add(new2PlayerGameButton);

        //set panel dimensions
        final int WIDTH = 620;
        final int HEIGHT = 650;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        //create the frame that would include this panel and siplay it
        frame = new JFrame();
        frame.add(this);            // add this panel to the frame
        frame.pack();               // shrink the window to the appropriate size
        frame.setResizable(false);  // make the window not resizable
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    // close the application when the window is closed
        frame.setVisible(true);     // show the window

    }




    @Override
    public void paint(Graphics g) {
        //draw everything as normal ..
        super.paint(g);
        // .. plus the grid lines ..
        g.drawImage(grid, 10, 40, null);
        // .. and loop over the game grid and display all x's and o's
        for(int i=0; i<3;i++){
            for(int j=2; j>=0;j--) {
                if(game.gridAt(i,j)=='x'){
                    //based on grid index, calculate the pixel location to draw the x image
                    g.drawImage(x, 200*i+40,200*j+70, null); // draw an x
                }
                else if(game.gridAt(i,j)=='o'){
                    //based on grid index, calculate the pixel location to draw the o image
                    g.drawImage(o, 200*i+40,200*j+70, null); // draw an o
                }
            }
        }


    }

    /**
     * Called when mouse is clicked within grid in a valid spot
     * @param i the x index of the 2D grid array
     * @param j the y index of the 2D grid array
     */
    private void panelMouseClicked(int i, int j) {
        //apply this play at position i and j
        if(!game.playAt(i,j)){
            //escape if play was invalid
            return;
        }
        //refresh the display to reflect the new play
        this.repaint();
        //check if game ended
        if(game.doChecks()){
            //if game over no need to continue, so return
            return;
        }
        //let computer take turn or switch turns for next player
        game.nextTurn();
        //refresh the display to reflect computer's turn if played
        this.repaint();
        //check if game ended again after computer's turn
        if(game.doChecks()){
            //if game over no need to continue, so return
            return;
        }
    }

    public void gameOver(String message){
        JOptionPane.showMessageDialog(null, message, "Game Over!", JOptionPane.INFORMATION_MESSAGE);
    }


    /**
     * Called when onw of the new game buttons is clicked
     * @param twoPlayer boolean: true if starting a 2 player game, false for single player game
     */
    private void newGameButtonPressed(boolean twoPlayer) {
        game.newGame(twoPlayer);
        this.repaint();
    }

}
