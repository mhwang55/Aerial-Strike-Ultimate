package shmup.drawables.bullets;

import shmup.Framework;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BulletStarburst extends Bullet{
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

    public BulletStarburst(int xCoordinate, int yCoordinate, double angle, double angleAdd, double speed, int type, double accel)
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
        hitbox = new Rectangle((int) xCoordinate + (objectImg.getWidth()/noFrames)/8, (int) yCoordinate + (objectImg.getWidth()/noFrames)/8,
                (objectImg.getWidth()/noFrames)*3/4, (objectImg.getHeight()/noFrames)*3/4);
        switch (this.type) {
            case 0:
                if (yCoordinate - startYCoord >= 50)
                    explode = true;
                break;
            case 1:
                if (Math.sqrt(Math.pow(Math.abs(yCoordinate - startYCoord), 2) + Math.pow(Math.abs(xCoordinate - startXCoord), 2)) >= 100)
                    explode = true;
                break;
        }
    }

    public boolean isExploded ()
    {
        return explode;
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
                return explosion0();
        }
    }

    public ArrayList<Bullet> explosion0 ()
    {
        ArrayList bullets = new ArrayList<Bullet>();
        BulletMedium b;
        BulletLarge bL;
        for (int x = 0; x < 24; x++)
        {
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    0,  2 * Math.PI * (x + 1) / 24, 3, 0);
            bullets.add(b);
        }
        for (int x = 0; x < 24; x++)
        {
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    2 * Math.PI / 48,  2 * Math.PI * (x + 1) / 24, 2, 0);
            bullets.add(b);
        }
        for (int x = 0; x < 7; x++)
        {
            bL = new BulletLarge((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    2 * Math.PI / 96,  2 * Math.PI * (x + 1) / 96, 1.25, 0);
            bullets.add(bL);
        }
        for (int x = 0; x < 7; x++)
        {
            bL = new BulletLarge((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    -2 * Math.PI / 96,  -2 * Math.PI * (x + 1) / 96, 1.25, 0);
            bullets.add(bL);
        }
        return bullets;
    }

    public ArrayList<Bullet> explosion1 ()
    {
        ArrayList bullets = new ArrayList<Bullet>();
        BulletMedium b;
        BulletLarge bL;
        /*
        for (int x = 0; x < 24; x++)
        {
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    0,  2 * Math.PI * (x + 1) / 24, 3, 1);
            bullets.add(b);
        }
        for (int x = 0; x < 24; x++)
        {
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    2 * Math.PI / 48,  2 * Math.PI * (x + 1) / 24, 2, 1);
            bullets.add(b);
        }
        //*/
        double speedCenterL = 0.5;
        double speedCenterM = 0.75;
        double speedCenterH = 1.0;
        double accelLH = 0.0025 * 2.5;
        double accelL = 0.005 * 2.5;
        double accelM = 0.0075 * 2.5;
        double accelML = 0.008 * 2.5;
        double accelH = 0.01 * 2.5;
        double accelHL = 0.02 * 2.5;
        for (int x = 0; x < 2; x++)
        {
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + 0 * Math.PI / 96,  2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterH, accelLH);
            bullets.add(b);
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + 0 * Math.PI / 96,  2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterH, accelL);
            bullets.add(b);
            bL = new BulletLarge((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + 0 * Math.PI / 96,  2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterM, accelM);
            bullets.add(bL);
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + 0 * Math.PI / 96,  2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterL, accelH);
            bullets.add(b);
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + 0 * Math.PI / 96,  2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterL, accelHL);
            bullets.add(b);
        }
        for (int x = 0; x < 2; x++)
        {
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + -0 * Math.PI / 96,  -2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterH, accelLH);
            bullets.add(b);
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + -0 * Math.PI / 96,  -2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterH, accelL);
            bullets.add(b);
            bL = new BulletLarge((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + -0 * Math.PI / 96,  -2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterM, accelM);
            bullets.add(bL);
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + -0 * Math.PI / 96,  -2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterL, accelH);
            bullets.add(b);
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + -0 * Math.PI / 96,  -2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterL, accelHL);
            bullets.add(b);
        }

        for (int x = 0; x < 2; x++)
        {
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + 1 * Math.PI / 2,  2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterL, accelML);
            bullets.add(b);
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + 1 * Math.PI / 2,  2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterL, accelH);
            bullets.add(b);
        }
        for (int x = 0; x < 2; x++)
        {
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + -1 * Math.PI / 2,  -2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterL, accelML);
            bullets.add(b);
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + -1 * Math.PI / 2,  -2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterL, accelH);
            bullets.add(b);
        }

        for (int x = 0; x < 2; x++)
        {
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + 3 * Math.PI / 16,  2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterL, accelL);
            bullets.add(b);
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + 3 * Math.PI / 16,  2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterL, accelM);
            bullets.add(b);
        }
        for (int x = 0; x < 2; x++)
        {
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + -3 * Math.PI / 16,  -2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterL, accelL);
            bullets.add(b);
            b = new BulletMedium((int) xCoordinate + 16 - 4, (int) yCoordinate,
                    this.angleOrig + -3 * Math.PI / 16,  -2 * Math.PI * (x + 1) / 48 + this.angleAdd, speedCenterL, accelM);
            bullets.add(b);
        }

        /*
        for (int x = 0; x < 2; x++)
        {
            bL = new BulletLarge((int) xCoordinate + 16 - 4 + 10, (int) yCoordinate,
                    this.angleOrig + 2 * Math.PI / 96,  2 * Math.PI * (x + 1) / 24, 1.25, 0);
            bullets.add(bL);
        }
        for (int x = 0; x < 2; x++)
        {
            bL = new BulletLarge((int) xCoordinate + 16 - 4 - 10, (int) yCoordinate,
                    this.angleOrig + -2 * Math.PI / 96,  -2 * Math.PI * (x + 1) / 24, 1.25, 0);
            bullets.add(bL);
        }
        */
        return bullets;
    }
}