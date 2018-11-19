package shmup.drawables.enemies.air;

import shmup.drawables.PowerUp;
import shmup.drawables.enemies.Enemy;
import shmup.drawables.bullets.Bullet;
import shmup.drawables.bullets.BulletMedium;
import shmup.player.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LockheedConstellation extends Enemy {
    // For creating new enemies.
    public boolean spawn = false;
    private boolean isShooting = false;

    public LockheedConstellation(int xCoordinate, int yCoordinate, int type,
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
        health = 50;

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
        LockheedConstellation.timeBetweenNewEnemies = timeBetweenNewEnemiesInit;
        LockheedConstellation.timeOfLastCreatedEnemy = 0;
    }

    public boolean isShooting(long gameTime)
    {
        // Checks if it is the time for a new bullet.
        if (bulletNum >= 240)
        {
            bulletNum = 0;
            return true;
        }
        else
        {
            return false;
        }
    }


    public ArrayList<Bullet> createBullet(Player player, double angleAdd) {
        ArrayList bullets = new ArrayList<Bullet>();
        double radianBull = Math.atan2(this.yCoordinate + objectImg.getHeight() - player.yCoordinate - player.playerImg.getHeight() / 2,
                this.xCoordinate + objectImg.getWidth()/noFrames/2 - player.xCoordinate - player.playerImg.getWidth() / 4);
        double angleBull = -(radianBull + Math.PI/2);

        int locX = (int) this.xCoordinate + objectImg.getWidth() / noFrames / 2 - 4;
        int locY = (int) this.yCoordinate + objectImg.getHeight() - 4;


        BulletMedium b = new BulletMedium(locX, locY,
                angleBull, 0 * Math.PI / 180, 5, 0);
        bullets.add(b);
        b = new BulletMedium(locX, locY,
                angleBull, 15 * Math.PI / 180, 5, 0);
        bullets.add(b);
        b = new BulletMedium(locX, locY,
                angleBull, -15 * Math.PI / 180, 5, 0);
        bullets.add(b);

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
        double ySpeedT = 0.75;
        switch (planeType)
        {
            case 1:
                if (timer >= 600 && timer <= 900)
                    ySpeed = 0;
                else if (timer > 900)
                    ySpeed = -ySpeedT;
                else
                    ySpeed = ySpeedT;
        }
    }

    /**
     * Updates position of bomber
     */
    public void Update(Player player, long gameTime)
    {
        isShooting(gameTime);
        // Move enemy on x coordinate.
        yCoordinate += ySpeed;

        // Moves fighter animation
        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
        hitbox = new Rectangle((int) xCoordinate + (objectImg.getWidth()/noFrames)/8, (int) yCoordinate + objectImg.getHeight()/8,
                (objectImg.getWidth()/noFrames)*3/4, objectImg.getHeight()*1/4);
        bulletNum++;
        setSpeed(type);
        timer++;
    }

    public PowerUp createPowerUp()
    {
        return new PowerUp((int) locationX + (int) xCoordinate, (int) locationY + (int) yCoordinate);
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
