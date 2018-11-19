package shmup;

import shmup.drawables.Drawable;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class MenuPointer extends Drawable{

    private int startPosition;
    //int xCoordinate, yCoordinate;
    private int timer = 0;
    private int moveInterval = 10;

    public MenuPointer(int xCoordinate, int yCoordinate, BufferedImage enemyImg, int frameWidth, int frameHeight, int noFrames)
    {
        super(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
        Initialize(xCoordinate, yCoordinate);
    }

    /**
     * Initialize ambulatamuere body
     *
     * @param xCoordinate Starting x coordinate of body
     * @param yCoordinate Starting y coordinate of body
     */
    public void Initialize(int xCoordinate, int yCoordinate)
    {
        // Sets enemy position.
        this.xCoordinate = xCoordinate;
        this.yCoordinate = startPosition = yCoordinate;
    }

    @Override
    public void restartDrawable() {
        int x = 0;
    }

    //@Override
    public void Update() {
        int x = 0;
    }

    @Override
    public boolean isLeftScreen() {
        return false;
    }

    public void Update(long gameTime) {
        // Moving on the x coordinate.
        if((Canvas.keyboardKeyState(KeyEvent.VK_D) || Canvas.keyboardKeyState(KeyEvent.VK_RIGHT)) &&
                xCoordinate <= 500 && timer >= moveInterval)
        {
            xCoordinate += 100;
            timer = 0;
        }
        else if((Canvas.keyboardKeyState(KeyEvent.VK_D) || Canvas.keyboardKeyState(KeyEvent.VK_RIGHT)) &&
                xCoordinate > 500 && timer >= moveInterval)
        {
            xCoordinate -= 500;
            timer = 0;
        }
        else if((Canvas.keyboardKeyState(KeyEvent.VK_A) || Canvas.keyboardKeyState(KeyEvent.VK_LEFT)) &&
                xCoordinate >= 200 && timer >= moveInterval)
        {
            xCoordinate -= 100;
            timer = 0;
        }
        else if((Canvas.keyboardKeyState(KeyEvent.VK_A) || Canvas.keyboardKeyState(KeyEvent.VK_LEFT)) &&
                xCoordinate < 200 && timer >= moveInterval)
        {
            xCoordinate += 500;
            timer = 0;
        }

        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
        timer++;
    }

    /**
     * returns x position of pointer
     */
    public double getXPosition()
    {
        return this.xCoordinate;
    }

    /**
     * returns y position of pointer
     */
    public double getYPosition()
    {
        return this.yCoordinate;
    }
}
