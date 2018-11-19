package shmup.drawables.bullets;

import shmup.Framework;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BulletStarburstShell extends Bullet{
    // For creating new bullets.
    public final static long timeBetweenNewBullets = Framework.secInNanosec / 10;
    public static long timeOfLastCreatedBullet = 0;

    // Position of the bullet on the screen. Must be of type double because movingXspeed and movingYspeed will not be a whole number.
    public double xCoordinate;
    public double yCoordinate;

    private double startXCoord;
    private double startYCoord;
    private boolean explode = false;

    public double angle = 0;
    private double angleAdd = 0;

    // Moving speed and direction.
    private double bulletSpeed;
    private double angleOrig;
    private double movingXspeed;
    private double movingYspeed;

    // Images of bullet or shell
    public static BufferedImage bulletImg;

    // type of starburst shell
    private int type;


    /**
     * Creates new machine gun bullet.
     *
     * @param xCoordinate From which x coordinate was bullet fired?
     * @param yCoordinate From which y coordinate was bullet fired?
     */

    public BulletStarburstShell(int xCoordinate, int yCoordinate, double angle, double angleAdd, double speed, int type, double accel)
    {
        super(xCoordinate, yCoordinate, angle, angleAdd, speed, bulletImg, 1, 100, accel);
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.angleOrig = angle;
        this.angleAdd = angleAdd;
        startYCoord = yCoordinate;
        startXCoord = xCoordinate;
        bulletSpeed = speed;
        this.type = type;

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
    @Override
    public void Update(long gameTime)
    {
        xCoordinate += movingXspeed;
        yCoordinate += movingYspeed;
        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
        hitbox = new Rectangle((int) xCoordinate + (objectImg.getWidth()/noFrames)/8,
                (int) yCoordinate + (objectImg.getWidth()/noFrames)/8,
                (objectImg.getWidth()/noFrames)*3/4, (objectImg.getHeight()/noFrames)*3/4);
        switch (this.type) {
            case 0:
                break;
            case 1:
                if (Math.sqrt(Math.pow(Math.abs(yCoordinate - startYCoord), 2)
                        + Math.pow(Math.abs(xCoordinate - startXCoord), 2)) >= 100)
                {
                    startYCoord = yCoordinate;
                    startXCoord = xCoordinate;
                    this.explode = true;
                }
                else
                    this.explode = false;
                break;
        }
    }

    public boolean isExploded ()
    {
        return this.explode;
    }

    public ArrayList<Bullet> explode()
    {
        switch (this.type)
        {
            case 0:
                return explosion0();
            case 1:
                return explosion1();
            default:
                return explosion1();
        }
    }

    public ArrayList<Bullet> explosion0 ()
    {
        ArrayList bullets = new ArrayList<Bullet>();
        return bullets;
    }

    public ArrayList<Bullet> explosion1 ()
    {
        ArrayList bullets = new ArrayList<Bullet>();
        BulletMedium b;
        BulletLarge bL;
        double speedCenterL = 0.5;
        double speedCenterM = 0.75;
        double speedCenterH = 1.0;
        double accelLH = 0.0025 * 2.5;
        double accelL = 0.005 * 2.5;
        double accelM = 0.0075 * 2.5;
        double accelML = 0.008 * 2.5;
        double accelH = 0.01 * 2.5;
        double accelHL = 0.02 * 2.5;

        for (int x = 0; x < 3; x++)
        {
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + 1 * Math.PI / 2,  2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterL, accelML);
            bullets.add(b);
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + 1 * Math.PI / 2,  2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterL, accelH);
            bullets.add(b);
        }
        for (int x = 0; x < 3; x++)
        {
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + -1 * Math.PI / 2,  -2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterL, accelML);
            bullets.add(b);
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + -1 * Math.PI / 2,  -2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterL, accelH);
            bullets.add(b);
        }

        return bullets;
    }
}