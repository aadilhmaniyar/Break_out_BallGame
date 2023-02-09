import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

import static java.awt.Color.*;

public class GamePlay extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;
    private  int score = 0;
    private int totalBricks = 48;
    private Timer timer;
    private int delay = 8;

    private int playerX = 310;
    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballXDir = -1;
    private int ballYDir =-2;

    private MapGenerator map;

    public GamePlay()
    {
        map = new MapGenerator(4,12);
        addKeyListener(this );
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay,this);
        timer.start();
    }

    public void paint(Graphics g){
        //background
        g.setColor(BLACK);
        g.fillRect(1,1,692,592);

        //drawing map
        map.draw((Graphics2D)g);

        //borders
        g.setColor(yellow);
        g.fillRect(0,0,3, 592);
        g.fillRect(0,0,692,3);
        g.fillRect(692,0,3,592);

        //Scores
        g.setColor(WHITE);
        g.setFont(new Font("serif", Font.BOLD,25));
        g.drawString(""+score, 590,30);

        //Paddle
        g.setColor(GREEN);
        g.fillRect(playerX,550, 100,8);


        //The Ball
        g.setColor(yellow);
        g.fillOval(ballPosX,ballPosY,20,20);


        // when you win game
        if(totalBricks <= 0){
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            g.setColor(RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won!!", 260,300);

            g.setColor(RED);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press (ENTER) to restart the game", 230,350);
        }

        //When you loose the game

        if(ballPosY > 570)
        {
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            g.setColor(RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("--GAME OVER--, Score: " +score  , 260,300);
            g.setColor(RED);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press (ENTER) to restart the game", 230,350);

        }

        g.dispose();

    }

    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(playerX >=600) {
                playerX = 600;
            }
            else {
                moveRight();
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX < 10) {
                playerX = 10;
            }
            else {
                moveLeft();
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            if(!play)
            {
                play = true;
                ballPosX = 120;
                ballPosY = 350;
                ballXDir = -1;
                ballYDir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3,7);
                repaint();
            }
        }
    }

    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}

    //Right move
    public void moveRight(){
        play = true;
        playerX +=20;
    }

    ///Left Move
    public void moveLeft(){
        play = true;
        playerX -=20;
    }

    public void actionPerformed(ActionEvent e){
        timer.start();
        if(play)
        {
            if(new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 30, 8)))
            {
                ballYDir = -ballYDir;
                ballXDir = -2;
            }
            else if(new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX +70, 550, 30, 8)))
            {
                ballYDir = -ballYDir;
                ballXDir = ballXDir + 1;

            }
            else if(new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX +30, 550, 40, 8)))
            {
                ballYDir = -ballYDir;
            }

            A:for(int i = 0; i < map.map.length ; i++)
            {
                for (int j = 0; j < map.map[0].length; j++)
                {
                    if (map.map[i][j] > 0)
                    {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
                        Rectangle brickRect = rect;


                        //brick mechanism of the game
                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            score += 5;
                            totalBricks--;

                            //Ball hit to right or left of brick
                            if (ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width) {
                                ballXDir = -ballXDir;
                            }

                            // when top or bottom

                            else {
                                ballYDir = -ballYDir;
                            }
                            break A;
                        }
                    }
                }
            }
            ballXDir += ballXDir;
            ballYDir += ballYDir;

            if (ballPosX < 0) {
                ballXDir = -ballXDir;
            }

            if (ballPosY < 0) {
                ballYDir = -ballYDir;
            }

            if (ballPosX > 670) {
                ballXDir = -ballXDir;
            }

            repaint();


        }
    }
}
