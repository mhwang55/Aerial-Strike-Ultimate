package shmup.drawables.enemies.air;

import shmup.drawables.enemies.Enemy;
import shmup.player.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Enemy P51
 */

public class P51 extends Enemy {
    private boolean beginTurn3 = false;
    private boolean beginTurn4 = false;

    public P51(int xCoordinate, int yCoordinate, double angle, double speed, int type,
               BufferedImage enemyImg, int frameWidth, int frameHeight, int noFrames)
    {
        super(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);

        if (angle != 0)
            enemyRotate = true;
        enemyAngle = angle;

        this.type = type;

        Initialize(xCoordinate, yCoordinate, enemyAngle, speed);
    }
    /**
     * Initialize enemy fighter
     * 
     * @param xCoordinate Starting x coordinate of fighter
     * @param yCoordinate Starting y coordinate of fighter
     */
    public void Initialize(int xCoordinate, int yCoordinate, double angle, double speed)
    {
        health = 2;
        
        // Sets enemy position.
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        
        // Initialize animation object.
        //Animation anim = new Animation(enemyImg, 48, 43, 2, 20, true, xCoordinate, yCoordinate, 0);
        //this.setEnemyAnim(anim);

        // Moving speed and direction of enemy.
        this.speed = speed;
        setDirectionAndSpeed(angle, speed);
        setRotatePoint();
    }

    /**
     * It sets speed and time between enemies to the initial properties.
     */
    public static void restartEnemy(){
        P51.timeBetweenNewEnemies = timeBetweenNewEnemiesInit;
        P51.timeOfLastCreatedEnemy = 0;
    }

    @Override
    public boolean isShooting(long gameTime) {
        return false;
    }

    private void setDirectionAndSpeed(double angle, double speed) {
        double rads = angle * Math.PI / 180;
        this.xSpeed = speed * (float)Math.cos(rads + Math.PI/2);
        this.ySpeed = speed * (float)Math.sin(rads + Math.PI/2);
    }

    @Override
    public void setRotatePoint()
    {
        locationX = objectImg.getWidth() / noFrames / 2;
        locationY = objectImg.getHeight() * 2 / 3;
    }

    private void setAngle(int planeType)
    {
        switch (planeType)
        {
            case 1:
                if (enemyAngle > 0 && xCoordinate < 500)
                    enemyAngle -= 0.75;
                break;
            case 2:
                if (enemyAngle < 0 && xCoordinate > 200)
                    enemyAngle += 1.0;
                break;
            case 3:
                if (enemyAngle > -80 && xCoordinate < 150)
                {
                    enemyAngle -= 2;
                    beginTurn3 = true;
                }
                if (enemyAngle > -80 && beginTurn3)
                    enemyAngle -= 2;
                break;
            case 4:
                if (enemyAngle < 80 && xCoordinate > 550)
                {
                    enemyAngle += 2;
                    beginTurn4 = true;
                }
                if (enemyAngle < 80 && beginTurn4)
                    enemyAngle += 2;
                break;

        }
    }

    public void Update(Player player, long gameTime)
    {
        xCoordinate += xSpeed;
        yCoordinate += ySpeed;
        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
        hitbox = new Rectangle((int) xCoordinate + (objectImg.getWidth()/noFrames)/8, (int) yCoordinate + (objectImg.getWidth()/noFrames)/8,
                (objectImg.getWidth()/noFrames)*3/4, (objectImg.getHeight()/noFrames)*3/4);
        setAngle(type);
        setDirectionAndSpeed(enemyAngle, speed);

    }

    /**
     * Checks if the enemy is left the screen.
     * 
     * @return true if the enemy is left the screen, false otherwise.
     */
    /*
    public boolean isLeftScreen()
    {
        if(yCoordinate > 900 + enemyImg.getHeight()) // When the entire helicopter is out of the screen.
            return true;
        else
            return false;
    }
    */
    
        
    /**
     * Updates position of helicopter, animations.
     */
    /*
    public void Update()
    {
        // Move enemy on x coordinate.
        yCoordinate += movingYSpeed;
        
        // Moves fighter animation
        enemyAnim.changeCoordinates(xCoordinate, yCoordinate);
    }
    */

    
    /**
     * Draws p51 to the screen.
     *
     * @param g2d Graphics2D
     */
    public void Draw(Graphics2D g2d)
    {
        objectAnim.Draw(g2d, enemyAngle * Math.PI/180, (int) locationX, (int) locationY);
    }

}
