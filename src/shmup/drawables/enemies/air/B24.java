package shmup.drawables.enemies.air;

import shmup.drawables.enemies.Enemy;
import shmup.drawables.bullets.Bullet;
import shmup.drawables.bullets.BulletMedium;
import shmup.player.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class B24 extends Enemy {
    // For creating new enemies.
    public boolean spawn = false;
    private boolean isShooting = false;
    private int bulletNum2 = 0;
    private int clipSize = 8;
    private int coolDown = 0;
    private boolean spreadFire = false;
    private int hardPoint = 0;

    public B24 (int xCoordinate, int yCoordinate, int type,
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
        health = 120;

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
        B24.timeBetweenNewEnemies = timeBetweenNewEnemiesInit;
        B24.timeOfLastCreatedEnemy = 0;
    }

    public boolean isShooting(long gameTime)
    {
        int timeDiff1 = timer - bulletNum;
        int timeDiff2 = timer - bulletNum2;
        // Checks if it is the time for a new bullet.
        if ((timeDiff1 + 20) == 150)
        {
            hardPoint = 1;
            bulletNum = timer;
        }
        else
            hardPoint = 0;

        if (timeDiff2 == 150 && clipSize == 8)
        {
            bulletNum2 = timer;
            if (hardPoint == 1)
                hardPoint = 3;
            else
                hardPoint = 2;
            clipSize--;
            spreadFire = true;
        }
        else if (clipSize > 0 && coolDown < 20 && spreadFire)
        {
            coolDown+=2;
            if (hardPoint != 1)
                hardPoint = 0;
        }
        else if (clipSize > 0 && coolDown == 20 && spreadFire)
        {
            coolDown = 0;
            clipSize--;
            if (hardPoint == 1)
                hardPoint = 3;
            else
                hardPoint = 2;
        }
        else
        {
            spreadFire = false;
            clipSize = 8;
            if (hardPoint != 1)
                hardPoint = 0;
        }
        return hardPoint != 0;
    }


    public ArrayList<Bullet> createBullet(Player player, double angleAdd) {
        ArrayList bullets = new ArrayList<Bullet>();
        BulletMedium b;
        switch (hardPoint)
        {
            case 3:
            case 1:
                double radianBull = Math.atan2(this.yCoordinate + objectImg.getHeight() - player.yCoordinate - player.playerImg.getHeight() / 2,
                        this.xCoordinate + objectImg.getWidth()/noFrames/2 - player.xCoordinate - player.playerImg.getWidth() / 4);
                double angleBull = -(radianBull + Math.PI/2);

                int locX = (int) this.xCoordinate + objectImg.getWidth() / noFrames / 2 - 4;
                int locY = (int) this.yCoordinate + objectImg.getHeight() - 4;


                b = new BulletMedium(locX, locY,
                        angleBull, 5 * Math.PI / 180, 6, 0);
                bullets.add(b);
                b = new BulletMedium(locX, locY,
                        angleBull, -5 * Math.PI / 180, 6, 0);
                bullets.add(b);
                if (hardPoint == 1)
                    break;
            case 2:
                locX = (int) this.xCoordinate + objectImg.getWidth() / noFrames / 2 - 4;
                locY = (int) this.yCoordinate + objectImg.getHeight()/2 - 4;
                b = new BulletMedium(locX - objectImg.getWidth()/noFrames/4, locY,
                        0, (clipSize - 5) * 10 * Math.PI / 180, 7, 0);
                bullets.add(b);
                b = new BulletMedium(locX + objectImg.getWidth()/noFrames/4, locY,
                        0, -(clipSize - 5) * 10 * Math.PI / 180, 7, 0);
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
                if (timer >= 300 && timer <= 800)
                    ySpeed = 0;
                else if (timer >= 800 && timer <= 900) {
                    ySpeed = 0.75;
                    xSpeed = -2;
                }
                else
                {
                    xSpeed = 0;
                    ySpeed = 0.75;
                }
                break;
            case 2:
                if (timer >= 300 && timer <= 800)
                    ySpeed = 0;
                else if (timer >= 800 && timer <= 900) {
                    ySpeed = 0.75;
                    xSpeed = 2;
                }
                else
                {
                    xSpeed = 0;
                    ySpeed = 0.75;
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
