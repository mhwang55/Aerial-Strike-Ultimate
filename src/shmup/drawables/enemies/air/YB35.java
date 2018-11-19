package shmup.drawables.enemies.air;

import shmup.drawables.enemies.Enemy;
import shmup.drawables.bullets.Bullet;
import shmup.drawables.bullets.BulletLarge;
import shmup.drawables.bullets.BulletMedium;
import shmup.player.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class YB35 extends Enemy {
    // For creating new enemies.
    public boolean spawn = false;
    private boolean isShooting = false;
    private int bulletNum2 = 0;
    private int bulletNum3 = 0;
    private int clipSize1 = 6;
    private int clipSize2 = 6;
    private int clipSize3 = 14;
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

    public YB35(int xCoordinate, int yCoordinate, int type,
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
        health = 600;

        // Sets enemy position.
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;

        // Initialize animation object.
        //Animation anim = new Animation(enemyImg, 146, 93, 2, 20, true, xCoordinate, yCoordinate, 0);
        //this.setEnemyAnim(anim);

        // Moving speed and direction of enemy.
        ySpeed = 1;
    }

    /**
     * It sets speed and time between enemies to the initial properties.
     */
    public static void restartEnemy(){
        YB35.timeBetweenNewEnemies = timeBetweenNewEnemiesInit;
        YB35.timeOfLastCreatedEnemy = 0;
    }

    public boolean isShooting(long gameTime)
    {
        int timeDiff1 = timer - bulletNum;
        int timeDiff2 = timer - bulletNum2;
        int timeDiff3 = timer - bulletNum3;
        // Checks if it is the time for a new bullet.

        if (timeDiff1 == 200 && clipSize1 == 6)
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
            clipSize1 = 6;
        }

        if (timeDiff2 == 150 && clipSize2 == 6)
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
            clipSize2 = 6;
        }

        if (timeDiff3 == 250 && clipSize3 == 14)
        {
            bulletNum3 = timer;
            clipSize3--;
            spreadFire3 = true;
            firing3 = true;
        }
        else if (clipSize3 > 0 && coolDown3 < 4 && spreadFire3)
        {
            coolDown3+=2;
            firing3 = false;
        }
        else if (clipSize3 > 0 && coolDown3 == 4 && spreadFire3)
        {
            coolDown3 = 0;
            clipSize3--;
            firing3 = true;
        }
        else
        {
            spreadFire3 = false;
            firing3 = false;
            clipSize3 = 14;
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

        if (firing1) {
            locX = (int) this.xCoordinate + objectImg.getWidth() / noFrames / 2 - 6;
            locY = (int) this.yCoordinate + objectImg.getHeight() / noFrames / 4 - 6;
            b2 = new BulletLarge(locX, locY,
                    -80, (clipSize1 - 1) * 90 * Math.PI / 180, 3, 0);
            bullets.add(b2);
            b2 = new BulletLarge(locX, locY,
                    80, (clipSize1 - 3) * 90 * Math.PI / 180,3, 0);
            bullets.add(b2);
            b2 = new BulletLarge(locX, locY,
                    -80, (clipSize1 - 3) * 90 * Math.PI / 180, 3, 0);
            bullets.add(b2);
            b2 = new BulletLarge(locX, locY,
                    80, (clipSize1 - 5) * 90 * Math.PI / 180,3, 0);
            bullets.add(b2);
        }

        if (firing2) {
            double radianBull = Math.atan2(this.yCoordinate + objectImg.getHeight() / 2 - 4 - player.yCoordinate - player.playerImg.getHeight() / 2,
                    this.xCoordinate + objectImg.getWidth() / noFrames / 2 - 4 - player.xCoordinate - player.playerImg.getWidth() / 4);
            double angleBull = -(radianBull + Math.PI / 2);
            int randomNum1 = ThreadLocalRandom.current().nextInt(-7, 7 + 1);
            //int randomNum2 = -ThreadLocalRandom.current().nextInt(1, 7 + 1);

            locX = (int) this.xCoordinate + objectImg.getWidth() / noFrames / 2 - 4;
            locY = (int) this.yCoordinate + objectImg.getHeight() / 2 - 4;

            switch (clipSize2 % 2) {
                case 0:
                    b = new BulletMedium(locX, locY,
                            angleBull, randomNum1 * Math.PI / 180, 6, 0);
                    bullets.add(b);
                    break;
                case 1:
                    b = new BulletMedium(locX, locY,
                            angleBull, randomNum1 * Math.PI / 180, 6, 0);
                    bullets.add(b);
            }
        }

        if (firing3) {
            locX = (int) this.xCoordinate + objectImg.getWidth() / noFrames / 2 - 4;
            locY = (int) this.yCoordinate + objectImg.getHeight() - 4;
            b = new BulletMedium(locX - objectImg.getWidth() / noFrames / 2, locY,
                    0, 0, 7, 0);
            bullets.add(b);
            b = new BulletMedium(locX + objectImg.getWidth() / noFrames / 2, locY,
                    0, 0, 7, 0);
            bullets.add(b);
        }

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
                if (timer <= 800)
                {
                    xSpeed = -1;
                    ySpeed = -0.05;
                }
                else if ((timer + 100) % 200 == 0)
                {
                    xSpeed = 1;
                    ySpeed = 0;
                }
                else if ((timer + 200) % 200 == 0)
                {
                    xSpeed = -1;
                    ySpeed = 0;
                }
                break;
            case 2:
                if (timer <= 600)
                {
                    xSpeed = 1;
                    ySpeed = -0.05;
                }
                else if ((timer + 100) % 200 == 0)
                {
                    xSpeed = -1;
                    ySpeed = 0;
                }
                else if ((timer + 200) % 200 == 0)
                {
                    xSpeed = 1;
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
     * Draws helicopter to the screen.
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
