package shmup.drawables.bullets;

import shmup.Framework;
import shmup.drawables.Drawable;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class BulletOld extends Drawable{
    // For creating new bullets.
    public final static long timeBetweenNewBullets = Framework.secInNanosec / 10;
    public static long timeOfLastCreatedBullet = 0;

    // Damage that is made to an enemy when it is hit with a bullet.
    private int damagePower;

    // Position of the bullet on the screen. Must be of type double because movingXspeed and movingYspeed will not be a whole number.
    public double xCoordinate;
    public double yCoordinate;

    public double angle = 0;

    // Moving speed and direction.
    private double bulletSpeed = 3;
    private double accel = 0;
    private double movingXspeed;
    private double movingYspeed;

    // Images of bullet or shell
    public static BufferedImage bulletImg;

    protected Rectangle hitbox;

    protected int noFrames;

    /**
     * Creates new machine gun bullet.
     *
     * @param xCoordinate From which x coordinate was bullet fired?
     * @param yCoordinate From which y coordinate was bullet fired?
     */
    /*
    public Bullet(int xCoordinate, int yCoordinate)
    {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;

        setDirectionAndSpeed();
    }

    public Bullet(int xCoordinate, int yCoordinate, int playerX, int playerY, double angle)
    {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;

        setDirectionAndSpeed(playerX, playerY, angle);
    }
    */


    public BulletOld(int xCoordinate, int yCoordinate, double angle, double angleAdd, double speed,
                     BufferedImage img, int noFrames, int bulletDmg, double accel)
    {
        super(xCoordinate, yCoordinate, img, img.getWidth(), img.getHeight(), noFrames);
        bulletImg = img;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.accel = accel;
        bulletSpeed = speed;
        this.noFrames = noFrames;
        damagePower = bulletDmg;

        setDirectionAndSpeed(angle + angleAdd);
    }


    /**
     * Calculate the speed on a x and y coordinate.
     *
     */
    //private void setDirectionAndSpeed(Point mousePosition)
    private void setDirectionAndSpeed()
    {
        // Unit direction vector of the bullet.
        /*
        double directionVx = mousePosition.x - this.xCoordinate;
        double directionVy = mousePosition.y - this.yCoordinate;
        double lengthOfVector = Math.sqrt(directionVx * directionVx + directionVy * directionVy);
        directionVx = directionVx / lengthOfVector; // Unit vector
        directionVy = directionVy / lengthOfVector; // Unit vector

        // Set speed.
        this.movingXspeed = bulletSpeed * directionVx;
        this.movingYspeed = bulletSpeed * directionVy;
        */
        this.movingYspeed = bulletSpeed;
    }

    /**
     * Calculate the speed on a x and y coordinate.
     *
     * @param playerX, playerY
     */
    //private void setDirectionAndSpeed(Point mousePosition)
    private void setDirectionAndSpeed(int playerX, int playerY)
    {
        // Unit direction vector of the bullet.
        double directionVx = playerX - this.xCoordinate;
        double directionVy = playerY - this.yCoordinate;

        double radians = Math.atan2(this.yCoordinate - playerY, this.xCoordinate - playerX);

        double angle = -(radians + Math.PI/2);
        //speed = new Vector2(bulletSpeed * (float)Math.Cos(angle + MathHelper.PiOver2), -bulletSpeed * (float)Math.Sin(angle + MathHelper.PiOver2));
        this.movingXspeed = -bulletSpeed * (float)Math.cos(angle + Math.PI/2);
        this.movingYspeed = bulletSpeed * (float)Math.sin(angle + Math.PI/2);

        double lengthOfVector = Math.sqrt(directionVx * directionVx + directionVy * directionVy);
        directionVx = directionVx / lengthOfVector; // Unit vector
        directionVy = directionVy / lengthOfVector; // Unit vector

        // Set speed.
        //this.movingXspeed = bulletSpeed * directionVx;
        //this.movingYspeed = bulletSpeed * directionVy;
    }

    /**
     * Calculate the speed on a x and y coordinate.
     *
     * @param playerX, playerY, angleAdd
     */
    //private void setDirectionAndSpeed(Point mousePosition)
    private void setDirectionAndSpeed(int playerX, int playerY, double angleAdd)
    {
        double radians = Math.atan2(this.yCoordinate - playerY, this.xCoordinate - playerX);
        double angle = -(radians + Math.PI/2);
        this.movingXspeed = -bulletSpeed * (float)Math.cos(angle + angleAdd * Math.PI / 180 + Math.PI/2);
        this.movingYspeed = bulletSpeed * (float)Math.sin(angle + angleAdd * Math.PI / 180 + Math.PI/2);
    }

    /**
     * Calculate the speed on a x and y coordinate.
     *
     * @param playerX, playerY, angleAdd
     */
    //private void setDirectionAndSpeed(Point mousePosition)
    private void setDirectionAndSpeed(double angle)
    {
        this.movingXspeed = -bulletSpeed * (float)Math.cos(angle + Math.PI/2);
        this.movingYspeed = bulletSpeed * (float)Math.sin(angle + Math.PI/2);
    }

    @Override
    /*
    public void restartDrawable() {
        return;
    }
    */

    /**
     * Checks if the bullet is left the screen.
     *
     * @return true if the bullet left the screen, false otherwise.
     */
    /*
    public boolean isItLeftScreen()
    {
        if(xCoordinate > 0 && xCoordinate < Framework.frameWidth &&
                yCoordinate > 0 && yCoordinate < Framework.frameHeight)
            return false;
        else
            return true;
    }
    */


    /**
     * Moves the bullet.
     */
    public void Update(long gameTime)
    {
        xCoordinate += movingXspeed;
        yCoordinate += movingYspeed;
        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
        hitbox = new Rectangle((int) xCoordinate + (objectImg.getWidth()/noFrames)/8, (int) yCoordinate + (objectImg.getWidth()/noFrames)/8,
                (objectImg.getWidth()/noFrames)*3/4, (objectImg.getHeight()/noFrames)*3/4);
    }

    /**
     * gets bullet hitbox
     */
    public Rectangle getHitbox() {
        return hitbox;
    }

    /**
     * gets damage dealt by bullet
     */
    public int getDamagePower() {
        return damagePower;
    }

    /**
     * Checks if the drawable left the screen.
     */
    public boolean isLeftScreen()
    {
        return !(xCoordinate + objectImg.getWidth() / noFrames > 0 && xCoordinate < Framework.frameWidth &&
                yCoordinate + objectImg.getHeight() > 0 && yCoordinate < Framework.frameHeight);
    }

    /**
     * Draws the bullet to the screen.
     *
     * @param g2d Graphics2D
     */
    /*
    public void Draw(Graphics2D g2d)
    {
        g2d.drawImage(bulletImg, (int)xCoordinate, (int)yCoordinate, null);
    }
    */
}
