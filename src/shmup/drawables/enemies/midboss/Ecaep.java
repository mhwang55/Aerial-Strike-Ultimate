package shmup.drawables.enemies.midboss;

import shmup.drawables.enemies.Enemy;
import shmup.drawables.bullets.Bullet;
import shmup.drawables.bullets.BulletLarge;
import shmup.drawables.bullets.BulletMedium;
import shmup.player.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Ecaep extends Enemy{
    // For creating new enemies.
    public boolean spawn = false;
    private boolean isShooting = false;
    private int bulletNum2 = 0;
    private int bulletNum3 = 0;
    private int clipSize1 = 4;
    private int clipSize2 = 3;
    private int clipSize3 = 50;
    private int coolDown1 = 0;
    private int coolDown2 = 0;
    private int coolDown3 = 0;
    private boolean spreadFire1 = false;
    private boolean spreadFire2 = false;
    private boolean spreadFire3 = false;
    private boolean firing1 = false;
    private boolean firing2 = false;
    private boolean firing3 = false;
    private int hardPoint = 0;
    private boolean clockwise = false;

    public Ecaep(int xCoordinate, int yCoordinate, int type,
                 BufferedImage enemyImg, int frameWidth, int frameHeight, int noFrames)
    {
        super(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
        this.type = type;
        Initialize(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
    }

    /**
     * Initialize enemy bomber
     *
     * @param xCoordinate Starting x coordinate of fighter
     * @param yCoordinate Starting y coordinate of fighter
     */
    public void Initialize(int xCoordinate, int yCoordinate, BufferedImage enemyImg, int frameWidth, int frameHeight, int noFrames)
    {
        health = 700;

        // Sets enemy position.
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;

        // Initialize animation object.
        //Animation anim = new Animation(enemyImg, 146, 93, 2, 20, true, xCoordinate, yCoordinate, 0);
        //this.setEnemyAnim(anim);

        // Moving speed and direction of enemy.
        ySpeed = 0.75;
    }

    /**
     * It sets speed and time between enemies to the initial properties.
     */
    public static void restartEnemy(){
        Ecaep.timeBetweenNewEnemies = timeBetweenNewEnemiesInit;
        Ecaep.timeOfLastCreatedEnemy = 0;
    }

    public boolean isShooting(long gameTime)
    {
        int timeDiff1 = timer - bulletNum;
        int timeDiff2 = timer - bulletNum2;
        int timeDiff3 = timer - bulletNum3;
        // Checks if it is the time for a new bullet.

        if (timeDiff1 == 200 && clipSize1 == 4)
        {
            bulletNum = timer;
            clipSize1--;
            spreadFire1 = true;
            firing1 = true;
        }
        else if (clipSize1 > 0 && coolDown1 < 20 && spreadFire1)
        {
            coolDown1++;
            firing1 = false;
        }
        else if (clipSize1 > 0 && coolDown1 == 20 && spreadFire1)
        {
            coolDown1 = 0;
            clipSize1--;
            firing1 = true;
        }
        else
        {
            spreadFire1 = false;
            firing1 = false;
            clipSize1 = 4;
        }

        if (timeDiff2 == 150 && clipSize2 == 3)
        {
            bulletNum2 = timer;
            clipSize2--;
            spreadFire2 = true;
            firing2 = true;
        }
        else if (clipSize2 > 0 && coolDown2 < 8 && spreadFire2)
        {
            coolDown2+=2;
            firing2 = false;
        }
        else if (clipSize2 > 0 && coolDown2 == 8 && spreadFire2)
        {
            coolDown2 = 0;
            clipSize2--;
            firing2 = true;
        }
        else
        {
            spreadFire2 = false;
            firing2 = false;
            clipSize2 = 3;
        }

        if (timeDiff3 == 250 && clipSize3 == 50)
        {
            bulletNum3 = timer;
            clipSize3--;
            spreadFire3 = true;
            firing3 = true;
        }
        else if (clipSize3 > 0 && coolDown3 < 6 && spreadFire3)
        {
            coolDown3+=2;
            firing3 = false;
        }
        else if (clipSize3 > 0 && coolDown3 == 6 && spreadFire3)
        {
            coolDown3 = 0;
            clipSize3--;
            firing3 = true;
        }
        else
        {
            spreadFire3 = false;
            firing3 = false;
            clipSize3 = 50;
            clockwise = !clockwise;
        }

        if (firing1)
            hardPoint++;
        if (firing2)
            hardPoint+=2;
        if (firing3)
            hardPoint+=6;

        return hardPoint != 0;
    }


    public ArrayList<Bullet> createBullet(Player player, double angleAdd) {
        ArrayList bullets = new ArrayList<Bullet>();
        BulletMedium b;
        BulletLarge b2;
        int locX;
        int locY;
        double radianBull;
        double angleBull;

        if (firing1) {
            radianBull = Math.atan2(this.yCoordinate + objectImg.getHeight() / 2 - 4 - player.yCoordinate - player.playerImg.getHeight() / 2,
                    this.xCoordinate + objectImg.getWidth() / noFrames / 2 - 4 - player.xCoordinate - player.playerImg.getWidth() / 4);
            angleBull = -(radianBull + Math.PI / 2);
            locX = (int) this.xCoordinate + objectImg.getWidth() / noFrames / 2 - 4;
            locY = (int) this.yCoordinate + objectImg.getHeight() / 2 - 4;

            switch (clipSize1 % 2) {
                case 0:
                    /*
                    b2 = new BulletLarge(locX, locY,
                            angleBull, -16 * Math.PI / 180, 5, 0);
                    bullets.add(b2);
                    */
                    b2 = new BulletLarge(locX, locY,
                            angleBull, -12 * Math.PI / 180, 5, 0);
                    bullets.add(b2);
                    b2 = new BulletLarge(locX, locY,
                            angleBull, -4 * Math.PI / 180, 5, 0);
                    bullets.add(b2);
                    b2 = new BulletLarge(locX, locY,
                            angleBull, 4 * Math.PI / 180, 5, 0);
                    bullets.add(b2);
                    b2 = new BulletLarge(locX, locY,
                            angleBull, 12 * Math.PI / 180, 5, 0);
                    bullets.add(b2);
                    break;
                case 1:
                    b2 = new BulletLarge(locX, locY,
                            angleBull, -24 * Math.PI / 180, 5, 0);
                    bullets.add(b2);
                    b2 = new BulletLarge(locX, locY,
                            angleBull, -16 * Math.PI / 180, 5, 0);
                    bullets.add(b2);
                    b2 = new BulletLarge(locX, locY,
                            angleBull, -8 * Math.PI / 180, 5, 0);
                    bullets.add(b2);
                    b2 = new BulletLarge(locX, locY,
                            angleBull, 0 * Math.PI / 180, 5, 0);
                    bullets.add(b2);
                    b2 = new BulletLarge(locX, locY,
                            angleBull, 8 * Math.PI / 180, 5, 0);
                    bullets.add(b2);
                    b2 = new BulletLarge(locX, locY,
                            angleBull, 16 * Math.PI / 180, 5, 0);
                    bullets.add(b2);
                    b2 = new BulletLarge(locX, locY,
                            angleBull, 24 * Math.PI / 180, 5, 0);
                    bullets.add(b2);
            }
        }

        ///*
        if (firing2) {
            radianBull = Math.atan2(this.yCoordinate + objectImg.getHeight() / 16 - 4 - player.yCoordinate - player.playerImg.getHeight() / 2,
                    this.xCoordinate + objectImg.getWidth() / noFrames / 2 - 4 - player.xCoordinate - player.playerImg.getWidth() / 4);
            angleBull = -(radianBull + Math.PI / 2);

            locX = (int) this.xCoordinate + objectImg.getWidth() / noFrames / 2 - 4;
            locY = (int) this.yCoordinate + objectImg.getHeight() / 16 - 4;
            b2 = new BulletLarge(locX, locY,
                    angleBull, 0,10, 0);
            bullets.add(b2);
        }

        if (firing3) {
            double spiralSpeed = 7;
            locX = (int) this.xCoordinate + objectImg.getWidth() / noFrames / 2 - 4;
            locY = (int) this.yCoordinate + objectImg.getHeight() / 2 - 4;
            double clock, modifier;
            modifier = 0.45;

            if (clockwise)
                clock = 1 * modifier;
            else
                clock = -1 * modifier;

            b2 = new BulletLarge(locX, locY,
                    clock * clipSize3 * 13 * Math.PI / 180, (clipSize1 - 1) * 90 * Math.PI / 180, spiralSpeed, 0);
            bullets.add(b2);
            b2 = new BulletLarge(locX, locY,
                    clock * clipSize3 * 13 * Math.PI / 180, (clipSize1 - 2) * 90 * Math.PI / 180, spiralSpeed, 0);
            bullets.add(b2);
            b2 = new BulletLarge(locX, locY,
                    clock * clipSize3 * 13 * Math.PI / 180, (clipSize1 - 3) * 90 * Math.PI / 180, spiralSpeed, 0);
            bullets.add(b2);
            b2 = new BulletLarge(locX, locY,
                    clock * clipSize3 * 13 * Math.PI / 180, (clipSize1 - 4) * 90 * Math.PI / 180, spiralSpeed, 0);
            bullets.add(b2);
        }
        //*/

        return bullets;
    }

    /**
     * Checks if the enemy is left the screen.
     *
     * @return true if the enemy is left the screen, false otherwise.
     */
    /*
    public boolean isLeftScreen()
    {
        if(yCoordinate > 900 + enemyImg.getHeight()) // When the entire enemy is out of the screen.
            return true;
        else
            return false;
    }
    */

    private void setSpeed(int planeType)
    {
        switch (planeType)
        {
            case 1:
                if (timer <= 400)
                {
                    xSpeed = 0;
                    ySpeed = 0.75;
                }
                else
                {
                    xSpeed = 0;
                    ySpeed = 0;
                }
        }
    }

    /**
     * Updates position of bomber
     */
    public void Update(Player player, long gameTime)
    {
        // Move enemy on x coordinate.
        yCoordinate += ySpeed;
        xCoordinate += xSpeed;

        // Moves fighter animation
        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
        hitbox = new Rectangle((int) xCoordinate + (objectImg.getWidth()/noFrames)/8, (int) yCoordinate + objectImg.getHeight()/8,
                (objectImg.getWidth()/noFrames)*3/4, objectImg.getHeight()*1/4);
        setSpeed(type);
        timer++;
    }


    /**
     * Draws plane to the screen.
     *
     * @param g2d Graphics2D
     */
    /*
    public void Draw(Graphics2D g2d)
    {
        enemyAnim.Draw(g2d);
    }
    */
}
