/*
 * a drawable object.  Drawables must have a position on the screen, a movement vector, and a representation (for drawing)
 */

package shmup.drawables;

import shmup.Animation;
import shmup.Framework;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

public abstract class Drawable {
    // Position of the enemy on the screen.
    public double xCoordinate;
    public double yCoordinate;

    // hitbox of drawable
    protected Rectangle hitbox;

    // Moving speed and direction.
    public double ySpeed;
    public double xSpeed;
    public double speed;

    // point to rotate about
    protected double locationX;
    protected double locationY;

    // Image of object
    protected BufferedImage objectImg;

    // Animation of the object
    protected Animation objectAnim;

    // Number of frames
    protected int noFrames;

    // Score when collected or destroyed
    protected int score = 0;

    /**
     * Initialize object
     *
     * @param xCoordinate Starting x coordinate of object
     * @param yCoordinate Starting y coordinate of object
     */
    public Drawable(int xCoordinate, int yCoordinate, BufferedImage objectImg, int frameWidth, int frameHeight, int noFrames)
    {
        // Sets enemy position.
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.objectImg = objectImg;
        this.noFrames = noFrames;

        // Initialize animation object.
        objectAnim = new Animation(objectImg, frameWidth, frameHeight, noFrames, 20, true, xCoordinate, yCoordinate, 0);
        hitbox = new Rectangle(-objectImg.getWidth()/noFrames - 100, -objectImg.getHeight() - 100,
                objectImg.getWidth()/noFrames, objectImg.getHeight());
    }

    public Drawable()
    {
    }

    /**
     * It sets speed and time between enemies to the initial properties.
     */
    public abstract void restartDrawable();

    /**
     * Checks if the drawable left the screen.
     */
    public boolean isLeftScreen()
    {
        if(xCoordinate > 0 && xCoordinate < Framework.frameWidth &&
                yCoordinate > 0 && yCoordinate < Framework.frameHeight)
            return false;
        else
            return true;

    }


    /**
     * Updates position of drawable
     */
    //public abstract void Update();

    /**
     * Updates position of drawable given a gameTime
     */
    public abstract void Update(long gameTime);

    /**
     * Draws object to the screen.
     *
     * @param g2d Graphics2D
     */
    public void Draw(Graphics2D g2d)
    {
        objectAnim.Draw(g2d);
    }

    /**
     * Draws object to the screen.
     *
     * @param g2d Graphics2D
     */
    public void Draw(Graphics2D g2d, double angle, int locX, int locY)
    {
        objectAnim.Draw(g2d, angle, locX, locY);
    }

    public int getRotateX()
    {
        return (int) locationX;
    }

    public int getRotateY()
    {
        return (int) locationY;
    }

    public Rectangle getHitbox(){
        return hitbox;
    }

    public int getScore(){
        return score;
    }
}
