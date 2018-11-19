package shmup.player;

import shmup.Framework;
import shmup.drawables.Drawable;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * fighter laser
 * 
 * @author www.gametutorial.net
 */

public class Missile extends Drawable{

    // For creating new bullets.
    public final static long timeBetweenNewBullets = Framework.secInNanosec / 2;
    public static long timeOfLastCreatedBullet = 0;

    // Damage that is made to an enemy when it is hit with a bullet.
    public static int damagePower = 2;

    // Position of the bullet on the screen. Must be of type double because movingXspeed and movingYspeed will not be a whole number.
    public double xCoordinate;
    public double yCoordinate;

    // Moving speed and direction.
    private static int bulletSpeed = 15;
    private double movingXspeed;
    private double movingYspeed;

    // Images of fighter laser. Image is loaded and set in Game class in LoadContent() method.
    public static BufferedImage missileImg;


    /**
     * Creates new machine gun bullet.
     *
     * @param xCoordinate From which x coordinate was bullet fired?
     * @param yCoordinate From which y coordinate was bullet fired?
     * @param mousePosition Position of the mouse at the time of the shot.
     */
    public Missile(int xCoordinate, int yCoordinate, BufferedImage img, int noFrames)
    {
        super(xCoordinate, yCoordinate, img, img.getWidth() / noFrames, img.getHeight(), noFrames);
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        
        setDirectionAndSpeed();
    }
    
    
    /**
     * Calculate the speed on a x and y coordinate.
     * 
     * @param mousePosition 
     */
    //private void setDirectionAndSpeed(Point mousePosition)
    private void setDirectionAndSpeed()
    {
        this.movingYspeed = -bulletSpeed;
        this.movingXspeed = 0;
    }
    
    
    /**
     * Checks if the bullet is left the screen.
     * 
     * @return true if the bullet left the screen, false otherwise.
     */
    public boolean isItLeftScreen()
    {
        if(xCoordinate > 0 && xCoordinate < Framework.frameWidth &&
           yCoordinate > 0 && yCoordinate < Framework.frameHeight)
            return false;
        else
            return true;
    }

    @Override
    public void restartDrawable() {
        return;
    }

    /**
     * Moves the bullet.
     */
    public void Update()
    {
        xCoordinate += movingXspeed;
        yCoordinate += movingYspeed;

        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
    }

    @Override
    public void Update(long gameTime) {
        return;
    }

    /**
     * Draws the bullet to the screen.
     * 
     * @param g2d Graphics2D
     */
}
