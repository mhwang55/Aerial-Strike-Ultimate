package shmup.drawables.enemies.boss.treadingPain;

import shmup.drawables.bullets.Bullet;
import shmup.drawables.bullets.BulletSmall;
import shmup.drawables.enemies.Turret;
import shmup.player.Player;

import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

//import static shmup.drawables.enemies.boss.treadingPain.QuadTurretType.TurretTypes.QUADTURRETLEFT;

public class TreadingPainQuadTurret extends Turret {

    public boolean spawn = false;
    private boolean isShooting = false;
    private long time;
    private int bulletNum = 0;
    private double ySpeed = 1;
    private boolean hitBottom = false;


    private int bulletNum1 = 0;
    private int coolDown1 = 0;
    private int clipSize1 = 15;
    private static int CLIPSIZE1T = 15;
    private static int CLIPSIZE1B = 40;
    private int timeDiff1;
    private int turn = 1;

    private boolean fired1 = false;

    private boolean spreadFire1 = false;
    private boolean firing1 = false;
    private int hardPoint = 0;
    private QuadTurretType.TurretTypes turretTypesX;
    private QuadTurretType.TurretTypes turretTypesY;

    private boolean engage = false;
    private AudioInputStream audio;
    private AudioFormat audioFormat;
    private DataLine.Info info;
    private Clip clip;

    public TreadingPainQuadTurret(int xCoordinate, int yCoordinate, BufferedImage enemyImg,
                                  int frameWidth, int frameHeight, int noFrames, long gameTime,
                                  QuadTurretType.TurretTypes typeOfX, QuadTurretType.TurretTypes typeOfY)
    {
        super(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
        time = gameTime;
        this.timer = 450;
        this.turretTypesX = typeOfX;
        this.turretTypesY = typeOfY;
        switch (this.turretTypesY)
        {
            case TOP:
                this.angle = 0;
                break;
            case BOTTOM:
                switch (this.turretTypesX)
                {
                    case LEFT:
                        this.angle = 0 * Math.PI / 180;
                        break;
                    case RIGHT:
                        this.angle = -0 * Math.PI / 180;
                        break;
                }
        }
        Initialize(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
        try {
            URL audioURL = this.getClass().getResource("../../../../resources/soundtracks/enemyFire/cannon.wav");
            audio = AudioSystem.getAudioInputStream(audioURL);
            audioFormat = audio.getFormat();

            info= new DataLine.Info(Clip.class, audioFormat);

            clip = (Clip) AudioSystem.getLine(info);
            clip.open(audio);
            audio.close();
        }
        catch (Exception ex)
        {
            Logger.getLogger(TreadingPainQuadTurret.class.getName()).log(Level.SEVERE, null, ex);
        }

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

        setRotatePoint();
    }

    public void setRotatePoint()
    {
        locationX = objectImg.getWidth() / 2;
        locationY = objectImg.getHeight() * 20 / 56;
    }

    public boolean isShooting(long gameTime) {
        switch (turretTypesY)
        {
            case TOP:
                return topFiring();
            case BOTTOM:
                return bottomFiring();
        }
        return false;
    }

    public boolean topFiring() {
        timeDiff1 = timer - bulletNum1;

        // Checks if it is the time for a new bullet.
        if (timeDiff1 == 500 && clipSize1 == CLIPSIZE1T)
        {
            bulletNum1 = timer;
            clipSize1--;
            spreadFire1 = true;
            firing1 = true;
        }
        else if (clipSize1 > 0 && coolDown1 < 8 && spreadFire1)
        {
            coolDown1++;
            firing1 = false;
        }
        else if (clipSize1 > 0 && coolDown1 == 8 && spreadFire1)
        {
            coolDown1 = 0;
            clipSize1--;
            firing1 = true;
        }
        else
        {
            spreadFire1 = false;
            firing1 = false;
            clipSize1 = CLIPSIZE1T;
        }

        return firing1;
    }

    public boolean bottomFiring() {
        timeDiff1 = timer - bulletNum1;
        // Checks if it is the time for a new bullet.

        if (timeDiff1 == 500 && clipSize1 == CLIPSIZE1B)
        {
            bulletNum1 = timer;
            clipSize1--;
            spreadFire1 = true;
            firing1 = true;
        }
        else if (clipSize1 > 0 && coolDown1 < 4 && spreadFire1)
        {
            coolDown1++;
            firing1 = false;
        }
        else if (clipSize1 > 0 && coolDown1 == 4 && spreadFire1)
        {
            coolDown1 = 0;
            clipSize1--;
            firing1 = true;
        }
        else
        {
            spreadFire1 = false;
            firing1 = false;
            clipSize1 = CLIPSIZE1B;
        }

        return firing1;
    }

    @Override
    public boolean isLeftScreen() {
        return false;
    }

    public ArrayList<Bullet> createBullet(Player player, double angleAdd) {
        double radianBull = Math.atan2(this.yCoordinate + 20 - player.yCoordinate - player.playerImg.getHeight() / 2,
                this.xCoordinate + 25 - player.xCoordinate - player.playerImg.getWidth() / 4);
        //double angleBull = -(radianBull + Math.PI/2);
        double angleBull = -this.angle;

        double posX;
        double posY;

        posX = -objectImg.getWidth() * 42 / 50 * (float) Math.cos(angleBull + Math.PI/2);
        posY = objectImg.getHeight() * 42 / 56 * (float) Math.sin(angleBull + Math.PI/2);

        double leftLeftBarrelX = (-14) * (float)Math.cos(angle) + 0 * (float)Math.sin(angle);
        double leftLeftBarrelY = -(14) * (float)Math.sin(angle) + 0 * (float)Math.cos(angle);
        double leftBarrelX = (-7) * (float)Math.cos(angle) + 0 * (float)Math.sin(angle);
        double leftBarrelY = -(7) * (float)Math.sin(angle) + 0 * (float)Math.cos(angle);
        double rightBarrelX = (8) * (float)Math.cos(angle) + 0 * (float)Math.sin(angle);
        double rightBarrelY = -(-8) * (float)Math.sin(angle) + 0 * (float)Math.cos(angle);
        double rightRightBarrelX = (15) * (float)Math.cos(angle) + 0 * (float)Math.sin(angle);
        double rightRightBarrelY = -(-15) * (float)Math.sin(angle) + 0 * (float)Math.cos(angle);

        int locX = (int) this.xCoordinate + 25 - 2 + (int) posX;
        int locY = (int) this.yCoordinate + 20 - 2 + (int) posY;

        double speed = 6;

        ArrayList bullets = new ArrayList<Bullet>();
        BulletSmall b;
        switch (turretTypesY) {
            case TOP:
                b = new BulletSmall(locX + (int) leftLeftBarrelX, locY + (int) leftLeftBarrelY, angleBull,
                        -10 * Math.PI / 180, speed, 0);
                bullets.add(b);
                b = new BulletSmall(locX + (int) leftBarrelX, locY + (int) leftBarrelY, angleBull,
                        -3 * Math.PI / 180, speed, 0);
                bullets.add(b);
                b = new BulletSmall(locX + (int) rightBarrelX, locY + (int) rightBarrelY, angleBull,
                        3 * Math.PI / 180, speed, 0);
                bullets.add(b);
                b = new BulletSmall(locX + (int) rightRightBarrelX, locY + (int) rightRightBarrelY, angleBull,
                        10 * Math.PI / 180, speed, 0);
                bullets.add(b);
                break;
            case BOTTOM:
                b = new BulletSmall(locX + (int) leftLeftBarrelX, locY + (int) leftLeftBarrelY, angleBull,
                        -0 * Math.PI / 180, speed, 0);
                bullets.add(b);
                b = new BulletSmall(locX + (int) leftBarrelX, locY + (int) leftBarrelY, angleBull,
                        -0 * Math.PI / 180, speed, 0);
                bullets.add(b);
                b = new BulletSmall(locX + (int) rightBarrelX, locY + (int) rightBarrelY, angleBull,
                        0 * Math.PI / 180, speed, 0);
                bullets.add(b);
                b = new BulletSmall(locX + (int) rightRightBarrelX, locY + (int) rightRightBarrelY, angleBull,
                        0 * Math.PI / 180, speed, 0);
                bullets.add(b);
                break;
        }

        clip.stop();
        clip.setFramePosition(0);
        clip.start();

        return bullets;
    }

    public void Update(Player player, long gameTime) {
        /*
        this.radians = Math.atan2(this.yCoordinate + 20 - player.yCoordinate - player.playerImg.getHeight() / 2,
                this.xCoordinate + 25 - player.xCoordinate - player.playerImg.getWidth() / 4);
        */
        //this.angle = -(radians + Math.PI/2);
        //this.angle = 0;
        //isShooting(gameTime);
        ySpeed = 0;
        double angleChange;

        if (spreadFire1) {
            switch (turretTypesY) {
                case TOP:
                    angleChange = 1.0;
                    if (Math.abs(this.angle) >= 30 * Math.PI / 180)
                        this.turn *= -1;
                    /*                    angleChange = 1.0;
                    if (Math.abs(this.angle) <= 0 * Math.PI / 180)
                        this.turn *= -1;
                    //*/
                    switch (turretTypesX) {
                        case LEFT:
                            this.angle += (this.turn) * angleChange * Math.PI / 180;
                            break;
                        case RIGHT:
                            this.angle += (-this.turn) * angleChange * Math.PI / 180;
                            break;
                    }
                    break;
                case BOTTOM:
                    angleChange = 1.5;
                    switch (turretTypesX) {
                        case LEFT:
                            if (this.angle >= 50 * Math.PI / 180)
                                this.turn *= -1;
                            if (this.angle < 0 * Math.PI / 180)
                                this.turn *= -1;
                            this.angle += (this.turn) * angleChange * Math.PI / 180;
                            break;
                        case RIGHT:
                            if (this.angle <= -50 * Math.PI / 180)
                                this.turn *= -1;
                            if (this.angle > -0 * Math.PI / 180)
                                this.turn *= -1;
                            this.angle += (-this.turn) * angleChange * Math.PI / 180;
                            break;
                    }
                    break;
            }
        }
        else
        {
            switch (turretTypesY) {
                case TOP:
                    angleChange = 1.0;
                    if (this.angle > 0 * Math.PI / 180 && this.angle > angleChange * Math.PI / 180) {
                        this.turn = 1;
                        switch (turretTypesX) {
                            case LEFT:
                                this.angle -= (this.turn) * angleChange * Math.PI / 180;
                                break;
                            case RIGHT:
                                this.angle -= (-this.turn) * angleChange * Math.PI / 180;
                                break;
                        }
                    } else if (this.angle < 0 * Math.PI / 180 && Math.abs(this.angle) > angleChange * Math.PI / 180) {
                        this.turn = 1;
                        switch (turretTypesX) {
                            case LEFT:
                                this.angle -= (this.turn) * angleChange * Math.PI / 180;
                                break;
                            case RIGHT:
                                this.angle -= (-this.turn) * angleChange * Math.PI / 180;
                                break;
                        }
                    } else
                        this.angle = 0;
                    break;
                case BOTTOM:
                    angleChange = 2.0;
            }

        }

        //this.angle = 0;

        /*
        yCoordinate += ySpeed;
        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
        */
        //engage = true;
        timer++;
        if (engage)
            hitbox = new Rectangle((int) this.xCoordinate, (int) this.yCoordinate, this.objectImg.getWidth(), this.objectImg.getHeight() * 20 / 56);
    }

    public Rectangle getHitbox()
    {
        return hitbox;
    }
}
