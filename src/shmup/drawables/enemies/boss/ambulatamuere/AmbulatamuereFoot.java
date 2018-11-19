package shmup.drawables.enemies.boss.ambulatamuere;

import shmup.drawables.enemies.Enemy;
import shmup.drawables.bullets.Bullet;
import shmup.player.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class AmbulatamuereFoot extends Enemy {
    // For creating new enemies.
    public boolean spawn = false;
    private boolean isShooting = false;
    private double time;
    private int bulletNum = 0;
    public boolean isUpperFoot;
    private boolean hitBottom = false;
    private AmbulatamuereDoubleTurret turret;
    private boolean turretDestroyed = false;

    public AmbulatamuereFoot(int xCoordinate, int yCoordinate, AmbulatamuereDoubleTurret turret,
                             BufferedImage enemyImg, int frameWidth, int frameHeight, int noFrames, boolean isUpperFoot)
    {
        super(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
        this.turret = turret;
        this.isUpperFoot = isUpperFoot;
        health = turret.health;
        destroy = false;
        Initialize(xCoordinate, yCoordinate);
        setRotatePoint();
        //hitbox = new Rectangle(-10, -10, 0,0);
    }

    /**
     * Initialize enemy bomber
     *
     * @param xCoordinate Starting x coordinate of Turret
     * @param yCoordinate Starting y coordinate of Turret
     */
    public void Initialize(int xCoordinate, int yCoordinate)
    {
        // Sets enemy position.
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;

        // Moving speed and direction of enemy.
        time = System.nanoTime();
    }

    public void setRotatePoint()
    {
        ///*
        if (this.turret != null) {
            locationX = this.turret.getRotateX() * 10 / 4;
            locationY = this.turret.getRotateY() * 8 / 4;
        }
        //*/
    }

    @Override
    public boolean isShooting(long gameTime) {
        if (!this.turretDestroyed)
            return this.turret.isShooting(gameTime);
        else
            return false;
    }

    @Override
    public ArrayList<Bullet> createBullet(Player player, double angleAdd) {
        return this.turret.createBullet(player, angleAdd);
    }

    public void removal ()
    {
        this.turretDestroyed = true;
        this.turret = null;
        this.health = 20;
    }

    @Override
    public void restartDrawable() {
        int x = 0;
    }

    @Override
    public boolean isLeftScreen() {
        return false;
    }

    public void Update() {
        int x = 0;
    }

    public void Update(Player player, long gameTime) {
        if (isUpperFoot) {
            if (yCoordinate < 149 && !hitBottom) {
                ySpeed = 1;
            }
            else if (yCoordinate >= 149 && !hitBottom) {
                hitBottom = true;
            }
            else if (yCoordinate >= 3 && hitBottom) {
                ySpeed = -3;
            }
            else
                hitBottom = false;
        }
        else
        {
            if (yCoordinate < 579 && !hitBottom) {
                ySpeed = 1;
            }
            else if (yCoordinate >= 579 && !hitBottom) {
                hitBottom = true;
            }
            else if (yCoordinate >= 433 && hitBottom) {
                ySpeed = -3;
            }
            else
                hitBottom = false;
        }

        //ySpeed = 0;
        yCoordinate += ySpeed;
        objectAnim.changeCoordinates(xCoordinate, yCoordinate);

        if (!turretDestroyed) {
            this.turret.changeLoc(xCoordinate, yCoordinate);
            this.turret.Update(player, gameTime);
            hitbox = this.turret.getHitbox();
        }
        else
        {
            hitbox = new Rectangle(-10, -10, 0, 0);
        }
    }

    public void Draw(Graphics2D g2d)
    {
        objectAnim.Draw(g2d);
        if (!turretDestroyed)
            this.turret.Draw(g2d);
    }
}
