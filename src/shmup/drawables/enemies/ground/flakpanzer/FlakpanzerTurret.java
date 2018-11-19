package shmup.drawables.enemies.ground.flakpanzer;

import shmup.drawables.enemies.Turret;
import shmup.drawables.bullets.Bullet;
import shmup.drawables.bullets.BulletMedium;
import shmup.player.Player;

import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlakpanzerTurret extends Turret {
    public boolean spawn = false;
    private boolean isShooting = false;
    private long time;
    private int bulletNum = 0;
    private double ySpeed = 1;
    private boolean isUpperTurret;
    private boolean hitBottom = false;
    private boolean fired = false;
    private int clipSize = 2;
    private boolean engage = false;
    private AudioInputStream audio;
    private AudioFormat audioFormat;
    private DataLine.Info info;
    private Clip clip;

    public FlakpanzerTurret(int xCoordinate, int yCoordinate, double speed, double type, BufferedImage enemyImg,
                                     int frameWidth, int frameHeight, int noFrames)
    {
        super(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
        this.isUpperTurret = isUpperTurret;
        Initialize(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
        Initialize(xCoordinate, yCoordinate, speed);
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
            Logger.getLogger(FlakpanzerTurret.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Initialize enemy bomber
     *
     * @param xCoordinate Starting x coordinate of Turret
     * @param yCoordinate Starting y coordinate of Turret
     */
    public void Initialize(int xCoordinate, int yCoordinate, double speed)
    {
        health = 200;

        // Sets enemy position.
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        //ySpeed = 1;
        this.speed = speed;
        ySpeed = this.speed;

        setRotatePoint();
    }

    public void setRotatePoint()
    {
        locationX = objectImg.getWidth() / 2 + 0.5;
        locationY = objectImg.getHeight() * 24 / 60;
    }

    public boolean isShooting(long gameTime) {
        // Checks if it is the time for a new bullet.
        if (bulletNum >= 100)
        {
            bulletNum = 0;
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

    public ArrayList<Bullet> createBullet(Player player, double angleAdd) {
        double radianBull = Math.atan2(this.yCoordinate + 24 - player.yCoordinate - player.playerImg.getHeight() / 2,
                this.xCoordinate + 14 - player.xCoordinate - player.playerImg.getWidth() / 4);
        double angleBull = -(radianBull + Math.PI/2);

        double posX;
        double posY;

        posX = -objectImg.getWidth() * 84 / 60 * (float) Math.cos(angleBull + Math.PI/2);
        posY = objectImg.getHeight() * 42 / 60 * (float) Math.sin(angleBull + Math.PI/2);

        double leftBarrelX = (10) * (float)Math.cos(angle) + 0 * (float)Math.sin(angle);
        double leftBarrelY = -(-10) * (float)Math.sin(angle) + 0 * (float)Math.cos(angle);
        double rightBarrelX = (-7) * (float)Math.cos(angle) + 0 * (float)Math.sin(angle);
        double rightBarrelY = -(7) * (float)Math.sin(angle) + 0 * (float)Math.cos(angle);

        int locX = (int) this.xCoordinate + 14 - 6 + (int) posX;
        int locY = (int) this.yCoordinate + 24 - 6 + (int) posY;

        ArrayList bullets = new ArrayList<Bullet>();
        BulletMedium b = new BulletMedium(locX + (int) leftBarrelX, locY + (int) leftBarrelY, angleBull,
                0 * Math.PI / 180, 5, 0);
        bullets.add(b);
        b = new BulletMedium(locX + (int) rightBarrelX, locY + (int) rightBarrelY, angleBull,
                0 * Math.PI / 180, 5, 0);
        bullets.add(b);

        clip.stop();
        clip.setFramePosition(0);
        clip.start();

        return bullets;
    }

    public void Update(Player player, long gameTime) {
        this.radians = Math.atan2(this.yCoordinate + 24 - player.yCoordinate - player.playerImg.getHeight() / 2,
                this.xCoordinate + 14 - player.xCoordinate - player.playerImg.getWidth() / 4);
        this.angle = radians + Math.PI/2;
        hitbox = null;
        bulletNum++;
    }

    public void changeLoc(double xCoord, double yCoord) {
        xCoordinate = xCoord + 3;
        yCoordinate = yCoord + 20;
        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
    }
}
