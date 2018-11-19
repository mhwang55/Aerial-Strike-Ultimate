package shmup.drawables.enemies;

import shmup.player.Player;
import shmup.drawables.bullets.Bullet;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Turret extends Enemy {
    // For creating new enemies.
    public boolean spawn = false;
    private boolean isShooting = false;
    private double time;
    private int bulletNum = 0;
    protected int ySpeed = 1;
    private boolean hitBottom = false;
    protected double angle;
    protected double radians;

    public Turret(int xCoordinate, int yCoordinate, BufferedImage enemyImg, int frameWidth, int frameHeight, int noFrames)
    {
        super(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
        Initialize(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
    }

    /**
     * Initialize enemy bomber
     *
     * @param xCoordinate Starting x coordinate of Turret
     * @param yCoordinate Starting y coordinate of Turret
     */
    public void Initialize(int xCoordinate, int yCoordinate, BufferedImage enemyImg, int frameWidth, int frameHeight, int noFrames)
    {
        health = 200;

        // Sets enemy position.
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;

        time = System.nanoTime();
        setRotatePoint();
    }

    public boolean isShooting(long gameTime) {
        if ((System.nanoTime() - time) > 500000000)
        {
            time = System.nanoTime();
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean isLeftScreen() {
        return false;
    }

    public abstract ArrayList<Bullet> createBullet(Player player, double angleAdd);

    public abstract void Update(Player player, long gameTime);

    @Override
    public void Draw(Graphics2D g2d) {
        double rotationRequired = angle;

        AffineTransform backup = g2d.getTransform();
        AffineTransform trans = new AffineTransform();
        trans.rotate( rotationRequired, locationX + xCoordinate, locationY + yCoordinate); // the points to rotate around

        g2d.transform( trans );
        g2d.drawImage( objectImg, null, (int) xCoordinate, (int) yCoordinate );  // the actual location of the sprite

        g2d.setTransform( backup ); // restore previous transform
    }
}
