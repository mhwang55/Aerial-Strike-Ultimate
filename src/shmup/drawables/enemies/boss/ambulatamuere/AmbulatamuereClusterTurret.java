package shmup.drawables.enemies.boss.ambulatamuere;

import shmup.player.Player;
import shmup.drawables.bullets.Bullet;
import shmup.drawables.enemies.Turret;
import shmup.drawables.bullets.BulletStarburst;

import java.awt.image.BufferedImage;
import java.net.URL;
import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AmbulatamuereClusterTurret extends Turret {
    // For creating new enemies.
    public boolean spawn = false;
    private boolean isShooting = false;
    private long time;
    private int bulletNum = 0;
    private int ySpeed = 1;
    private boolean isUpperTurret;
    private boolean hitBottom = false;
    private boolean engage = false;
    private long timeDiff;
    private AudioInputStream audio;
    private AudioFormat audioFormat;
    private DataLine.Info info;
    private Clip clip;

    public AmbulatamuereClusterTurret(int xCoordinate, int yCoordinate, BufferedImage enemyImg,
                                      int frameWidth, int frameHeight, int noFrames, long gameTime)
    {
        super(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
        time = gameTime;
        Initialize(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
        try {
                URL laserURL = this.getClass().getResource("../../../../resources/soundtracks/enemyFire/cannonBig.wav");
                audio = AudioSystem.getAudioInputStream(laserURL);
                audioFormat = audio.getFormat();

                info= new DataLine.Info(Clip.class, audioFormat);

                clip = (Clip) AudioSystem.getLine(info);
                clip.open(audio);
                audio.close();
        }
        catch (Exception ex)
        {
            Logger.getLogger(AmbulatamuereClusterTurret.class.getName()).log(Level.SEVERE, null, ex);
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
        locationY = objectImg.getHeight() * 50 / 270;
    }

    public boolean isShooting(long gameTime) {
        timeDiff = gameTime - time;
        if (hitBottom)
            engage = true;
        if (!engage)
            return false;
        if (timeDiff > 5000000000L)
        {
            time = gameTime;
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
        int locX = (int) this.xCoordinate + 57 - 16;
        int locY = (int) this.yCoordinate + objectImg.getHeight();

        ArrayList bullets = new ArrayList<Bullet>();
        BulletStarburst b = new BulletStarburst(locX, locY, 0, 0 * Math.PI / 180, 1, 0, 0);
        bullets.add(b);

        clip.stop();
        clip.setFramePosition(0);
        clip.start();

        return bullets;
    }

    public void Update(Player player, long gameTime) {
        this.radians = Math.atan2(this.yCoordinate + 50 - player.yCoordinate - player.playerImg.getHeight() / 2,
                this.xCoordinate + 57 - player.xCoordinate - player.playerImg.getWidth() / 4);
        //this.angle = -(radians + Math.PI/2);
        this.angle = radians + Math.PI/2;
        this.angle = 0;
        isShooting(gameTime);
        if (yCoordinate < 149 + 153 && !hitBottom) {
            ySpeed = 1;
        }
        else if (yCoordinate >= 149 + 153 && !hitBottom) {
            hitBottom = true;
        }
        else if (yCoordinate >= 101 + 153 && hitBottom) {
            ySpeed = -1;
        }
        else
            hitBottom = false;

        yCoordinate += ySpeed;
        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
    }
}
