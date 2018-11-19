package shmup.drawables;

import shmup.Framework;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PowerUp extends Drawable {
    // For creating new enemies.
    public boolean spawn = false;
    private boolean isShooting = false;
    private double time;
    private int bulletNum = 0;
    private boolean hitBottom = false;
    private boolean hitRight = false;
    public static BufferedImage powerUpImg;

    public PowerUp(int xCoordinate, int yCoordinate)
    {
        super(xCoordinate, yCoordinate, powerUpImg, powerUpImg.getWidth(), powerUpImg.getHeight(), 1);
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
        this.yCoordinate = yCoordinate;

        time = System.nanoTime();
        this.xSpeed = 2;
        this.ySpeed = 2;
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
        if (xCoordinate < Framework.frameWidth * 3 / 4 && !hitRight) {
            xSpeed = 2;
        }
        else if (xCoordinate >= Framework.frameWidth * 3 / 4 && !hitRight) {
            hitRight = true;
        }
        else if (xCoordinate >= Framework.frameWidth / 4 && hitRight) {
            xSpeed = -2;
        }
        else
            hitRight = false;

        if (yCoordinate < Framework.frameHeight * 3 / 4 && !hitBottom) {
            ySpeed = 2;
        }
        else if (yCoordinate >= Framework.frameHeight * 3 / 4 && !hitBottom) {
            hitBottom = true;
        }
        else if (yCoordinate >= Framework.frameHeight / 4 && hitBottom) {
            ySpeed = -4;
        }
        else
            hitBottom = false;

        xCoordinate += xSpeed;
        yCoordinate += ySpeed;
        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
        hitbox = new Rectangle((int) xCoordinate, (int) yCoordinate,
                objectImg.getWidth(), objectImg.getHeight());
    }
}
