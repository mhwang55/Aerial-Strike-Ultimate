package shmup.drawables.enemies.boss.treadingPain;

import shmup.drawables.bullets.Bullet;
import shmup.drawables.bullets.BulletStarburst;
import shmup.drawables.bullets.BulletStarburstShell;
import shmup.drawables.enemies.Turret;
import shmup.player.Player;

import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TreadingPainTripleTurret extends Turret {

    public boolean spawn = false;
    private boolean isShooting = false;
    private long time;
    private int bulletNum = 0;
    private double ySpeed = 1;
    private boolean hitBottom = false;
    private boolean fired = false;

    private double angleTarget;
    private double traverseSpeed = 0.5 * Math.PI / 180;


    private int bulletNum1 = 0;
    private int coolDown1 = 0;
    private int clipSize1 = 1;
    private boolean spreadFire1 = false;
    private boolean firing1 = false;
    private int hardPoint = 0;

    private boolean engage = false;
    private AudioInputStream audio;
    private AudioFormat audioFormat;
    private DataLine.Info info;
    private Clip clip;

    public TreadingPainTripleTurret(int xCoordinate, int yCoordinate, BufferedImage enemyImg,
                                    int frameWidth, int frameHeight, int noFrames, long gameTime)
    {
        super(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
        time = gameTime;
        this.timer = 200;
        this.angle = 0;
        Initialize(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
        try {
                URL audioURL = this.getClass().getResource("../../../../resources/soundtracks/enemyFire/cannonBig.wav");
                audio = AudioSystem.getAudioInputStream(audioURL);
                audioFormat = audio.getFormat();

                info= new DataLine.Info(Clip.class, audioFormat);

                clip = (Clip) AudioSystem.getLine(info);
                clip.open(audio);
                audio.close();
        }
        catch (Exception ex)
        {
            Logger.getLogger(TreadingPainTripleTurret.class.getName()).log(Level.SEVERE, null, ex);
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
        locationY = objectImg.getHeight() * 98 / 262;
    }

    public boolean isShooting(long gameTime) {
        int timeDiff1 = timer - bulletNum1;
        // Checks if it is the time for a new bullet.

        if (timeDiff1 == 500 && clipSize1 == 1)
        {
            bulletNum1 = timer;
            clipSize1--;
            spreadFire1 = true;
            firing1 = true;
        }
        else if (clipSize1 > 0 && coolDown1 < 10 && spreadFire1)
        {
            coolDown1++;
            firing1 = false;
        }
        else if (clipSize1 > 0 && coolDown1 == 10 && spreadFire1)
        {
            coolDown1 = 0;
            clipSize1--;
            firing1 = true;
        }
        else
        {
            spreadFire1 = false;
            firing1 = false;
            clipSize1 = 1;
        }

        return firing1;
    }

    @Override
    public boolean isLeftScreen() {
        return false;
    }

    public ArrayList<Bullet> createBullet(Player player, double angleAdd) {
        /*
        double radianBull = Math.atan2(this.yCoordinate + 98 - player.yCoordinate - player.playerImg.getHeight() / 2,
                this.xCoordinate + 72 - player.xCoordinate - player.playerImg.getWidth() / 4);
        */
        //this.angle = radians + Math.PI/2;
        //this.angle = -radians - Math.PI/2;
        double angleBull = -this.angle;

        double posX;
        double posY;

        posX = -objectImg.getWidth() * 350 / 262 * (float) Math.cos(angleBull + Math.PI/2);
        posY = objectImg.getHeight() * 180 / 262 * (float) Math.sin(angleBull + Math.PI/2);

        double leftBarrelX = (-30) * (float)Math.cos(this.angle) + 0 * (float)Math.sin(this.angle);
        double leftBarrelY = -(30) * (float)Math.sin(this.angle) + 0 * (float)Math.cos(this.angle);

        double midBarrelX = (-0) * (float)Math.cos(this.angle) + 0 * (float)Math.sin(this.angle);
        double midBarrelY = -(0) * (float)Math.sin(this.angle) + 0 * (float)Math.cos(this.angle);

        double rightBarrelX = (30) * (float)Math.cos(this.angle) + 0 * (float)Math.sin(this.angle);
        double rightBarrelY = -(-30) * (float)Math.sin(this.angle) + 0 * (float)Math.cos(this.angle);

        int locX = (int) this.xCoordinate + 72 - 16 + (int) posX;
        int locY = (int) this.yCoordinate + 98 - 16 + (int) posY;

        ArrayList bullets = new ArrayList<Bullet>();
        BulletStarburstShell b;
        ///*
        b = new BulletStarburstShell(locX + (int) leftBarrelX, locY + (int) leftBarrelY, angleBull,
                -45 * Math.PI / 180, 10.5, 1, 0);
        bullets.add(b);
        //*/

        b = new BulletStarburstShell(locX + (int) midBarrelX, locY + (int) midBarrelY, angleBull,
                0 * Math.PI / 180, 10.5, 1, 0);
        bullets.add(b);

        ///*
        b = new BulletStarburstShell(locX + (int) rightBarrelX, locY + (int) rightBarrelY, angleBull,
                45 * Math.PI / 180, 10.5, 1, 0);
        bullets.add(b);
        //*/

        clip.stop();
        clip.setFramePosition(0);
        clip.start();

        return bullets;
    }

    public void Update(Player player, long gameTime) {
        this.radians = Math.atan2(this.yCoordinate + 98 - player.yCoordinate - player.playerImg.getHeight() / 2,
                this.xCoordinate + 72 - player.xCoordinate - player.playerImg.getWidth() / 4);
        //this.angle = -(radians + Math.PI/2);
        this.angleTarget = radians + Math.PI/2;
        double angleTargetMod = this.angleTarget;
        /*
        if (angleTargetMod < 0)
            angleTargetMod += 2 * Math.PI;
        */
        if (angleTargetMod > this.angle && angleTargetMod - this.angle > this.traverseSpeed)
            this.angle += this.traverseSpeed;
        else if (angleTargetMod < this.angle && this.angle - angleTargetMod > this.traverseSpeed)
            this.angle -= this.traverseSpeed;
        else
            this.angle = this.angleTarget;
        /*
        if (this.angle < 0)
            this.angle += 2 * Math.PI;
        */
        System.out.println("angle: " + this.angle + ", angleTarget: " + this.angleTarget);

        //this.angle = radians + Math.PI/2;

        //isShooting(gameTime);
        ySpeed = 0;
        /*
        yCoordinate += ySpeed;
        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
        */
        //engage = true;
        timer++;
        if (engage)
            hitbox = new Rectangle((int) this.xCoordinate, (int) this.yCoordinate, this.objectImg.getWidth(), this.objectImg.getHeight() * 98 / 262);
    }

    public void changeLoc(double xCoord, double yCoord) {
        xCoordinate = xCoord + 25;
        yCoordinate = yCoord + 25;
        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
    }

    public Rectangle getHitbox()
    {
        return hitbox;
    }
}