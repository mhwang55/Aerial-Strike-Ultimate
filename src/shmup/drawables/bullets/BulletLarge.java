package shmup.drawables.bullets;

import shmup.Framework;

import java.awt.image.BufferedImage;

public class BulletLarge extends Bullet{
    // For creating new bullets.
    public final static long timeBetweenNewBullets = Framework.secInNanosec / 10;
    public static long timeOfLastCreatedBullet = 0;

    // Position of the bullet on the screen. Must be of type double because movingXspeed and movingYspeed will not be a whole number.
    public double xCoordinate;
    public double yCoordinate;

    public double angle = 0;

    // Moving speed and direction.
    private double bulletSpeed;
    private double movingXspeed;
    private double movingYspeed;

    // Images of bullet or shell
    public static BufferedImage bulletImg;


    /**
     * Creates new machine gun bullet.
     *
     * @param xCoordinate From which x coordinate was bullet fired?
     * @param yCoordinate From which y coordinate was bullet fired?
     */

    public BulletLarge(int xCoordinate, int yCoordinate, double angle, double angleAdd, double speed, double accel)
    {
        super(xCoordinate, yCoordinate, angle, angleAdd, speed, bulletImg, 1, 15, accel);
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        bulletSpeed = speed;

        setDirectionAndSpeed(angle + angleAdd);
    }

    @Override
    public void restartDrawable() {

    }

    /**
     * Calculate the speed on a x and y coordinate.
     *
     * @param playerX, playerY, angleAdd
     */
    private void setDirectionAndSpeed(double angle)
    {
        this.movingXspeed = -bulletSpeed * (float)Math.cos(angle + Math.PI/2);
        this.movingYspeed = bulletSpeed * (float)Math.sin(angle + Math.PI/2);
    }

    /**
     * Moves the bullet.
     */
    /*
    public void Update(long gameTime)
    {
        xCoordinate += movingXspeed;
        yCoordinate += movingYspeed;
        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
    }
    */
}
