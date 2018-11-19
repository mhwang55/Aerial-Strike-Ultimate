package shmup.drawables.enemies.ground.flakpanzer;

import shmup.drawables.enemies.Enemy;
import shmup.drawables.bullets.Bullet;
import shmup.player.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Flakpanzer extends Enemy {
    private boolean beginTurn3 = false;
    private boolean beginTurn4 = false;
    private FlakpanzerTurret turret;

    public Flakpanzer(int xCoordinate, int yCoordinate, FlakpanzerTurret turret, double angle, double speed, int type,
               BufferedImage enemyImg, int frameWidth, int frameHeight, int noFrames)
    {
        super(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
        this.turret = turret;

        if (angle != 0)
            enemyRotate = true;
        enemyAngle = angle;

        this.type = type;

        Initialize(xCoordinate, yCoordinate, enemyAngle, speed);
    }

    /**
     * Initialize enemy tank
     *
     * @param xCoordinate Starting x coordinate of Turret
     * @param yCoordinate Starting y coordinate of Turret
     */
    public void Initialize(int xCoordinate, int yCoordinate, double angle, double speed)
    {
        health = 20;

        // Sets enemy position.
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;

        // Moving speed and direction of enemy.
        this.speed = speed;
        setDirectionAndSpeed(angle, speed);
        setRotatePoint();
    }

    @Override
    public boolean isShooting(long gameTime) {
        return this.turret.isShooting(gameTime);
    }

    @Override
    public ArrayList<Bullet> createBullet(Player player, double angleAdd) {
        return this.turret.createBullet(player, angleAdd);
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
        locationY = objectImg.getHeight() / 2;
    }

    private void setAngle(int tankType)
    {
        switch (tankType)
        {
            case 1:
                break;
            case 2:
                if (enemyAngle > 0 && xCoordinate < 500)
                    enemyAngle -= 0.7;
                break;
            case 3:
                if (enemyAngle < 0 && xCoordinate > 200)
                    enemyAngle += 1.0;
                break;
            case 4:
                if (enemyAngle > -80 && xCoordinate < 150)
                {
                    enemyAngle -= 2;
                    beginTurn3 = true;
                }
                if (enemyAngle > -80 && beginTurn3)
                    enemyAngle -= 2;
                break;
            case 5:
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

        this.turret.changeLoc(xCoordinate, yCoordinate);
        this.turret.Update(player, gameTime);
    }

    public void Draw(Graphics2D g2d)
    {
        objectAnim.Draw(g2d);
        this.turret.Draw(g2d);
    }
}
