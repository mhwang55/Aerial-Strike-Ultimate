package shmup.drawables.enemies.boss.ambulatamuere;

import shmup.player.Player;
import shmup.drawables.bullets.BulletLarge;
import shmup.drawables.bullets.Bullet;
import shmup.drawables.enemies.Turret;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AmbulatamuereDoubleTurret extends Turret {

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

    public AmbulatamuereDoubleTurret(int xCoordinate, int yCoordinate, BufferedImage enemyImg,
                                     int frameWidth, int frameHeight, int noFrames, boolean isUpperTurret, long gameTime)
    {
        super(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
        this.isUpperTurret = isUpperTurret;
        time = gameTime;
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
            Logger.getLogger(AmbulatamuereDoubleTurret.class.getName()).log(Level.SEVERE, null, ex);
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
        locationY = objectImg.getHeight() * 18 / 60;
    }

    public boolean isShooting(long gameTime) {
        long timeDiff = gameTime - time;
        if (hitBottom)
            engage = true;
        if (!engage || hitBottom)
            return false;
        if (isUpperTurret) {
            if (yCoordinate >= 0 && timeDiff > 4000000000L && !fired && clipSize == 2)
            {
                clipSize--;
                time = gameTime;
                return true;
            }
            else if (yCoordinate >= 0 && timeDiff > 100000000L && clipSize == 1)
            {
                fired = true;
                clipSize--;
                time = gameTime;
                return true;
            }
        }
        else {
            if (yCoordinate >= 430 && timeDiff > 4000000000L && !fired && clipSize == 2)
            {
                clipSize--;
                time = gameTime;
                return true;
            }
            else if (yCoordinate >= 430 && timeDiff > 100000000L && clipSize == 1)
            {
                fired = true;
                clipSize--;
                time = gameTime;
                return true;
            }
        }
        if (fired)
        {
            fired = false;
            clipSize = 2;
            return false;
        }
        else
            return false;

    }

    @Override
    public boolean isLeftScreen() {
        return false;
    }

    public ArrayList<Bullet> createBullet(Player player, double angleAdd) {
        double radianBull = Math.atan2(this.yCoordinate + 18 - player.yCoordinate - player.playerImg.getHeight() / 2,
                this.xCoordinate + 17 - player.xCoordinate - player.playerImg.getWidth() / 4);
        double angleBull = -(radianBull + Math.PI/2);

        double posX;
        double posY;

        posX = -objectImg.getWidth() * 84 / 60 * (float) Math.cos(angleBull + Math.PI/2);
        posY = objectImg.getHeight() * 42 / 60 * (float) Math.sin(angleBull + Math.PI/2);

        double leftBarrelX = (9) * (float)Math.cos(angle) + 0 * (float)Math.sin(angle);
        double leftBarrelY = -(-9) * (float)Math.sin(angle) + 0 * (float)Math.cos(angle);
        double rightBarrelX = (-9) * (float)Math.cos(angle) + 0 * (float)Math.sin(angle);
        double rightBarrelY = -(9) * (float)Math.sin(angle) + 0 * (float)Math.cos(angle);

        int locX = (int) this.xCoordinate + 17 - 6 + (int) posX;
        int locY = (int) this.yCoordinate + 18 - 6 + (int) posY;

        ArrayList bullets = new ArrayList<Bullet>();
        BulletLarge b = new BulletLarge(locX + (int) leftBarrelX, locY + (int) leftBarrelY, angleBull,
                0 * Math.PI / 180, 4, 0);
        bullets.add(b);
        b = new BulletLarge(locX + (int) rightBarrelX, locY + (int) rightBarrelY, angleBull,
                0 * Math.PI / 180, 4, 0);
        bullets.add(b);

        clip.stop();
        clip.setFramePosition(0);
        clip.start();

        return bullets;
    }

    public void Update(Player player, long gameTime) {
        this.radians = Math.atan2(this.yCoordinate + 18 - player.yCoordinate - player.playerImg.getHeight() / 2,
                this.xCoordinate + 17 - player.xCoordinate - player.playerImg.getWidth() / 4);
        //this.angle = -(radians + Math.PI/2);
        this.angle = radians + Math.PI/2;
        //isShooting(gameTime);
        if (isUpperTurret) {
            if (yCoordinate < 149 + 24 && !hitBottom) {
                //ySpeed = 1;
            }
            else if (yCoordinate >= 149 + 24 && !hitBottom)
            {
                hitBottom = true;
            }
            else if (yCoordinate >= 3 + 24 && hitBottom) {
                //ySpeed = -3;
            }
            else
                hitBottom = false;
        }
        else
        {
            if (yCoordinate < 579 + 24 && !hitBottom) {
                //ySpeed = 1;
            }
            else if (yCoordinate >= 579 + 24 && !hitBottom) {
                hitBottom = true;
            }
            else if (yCoordinate >= 433 + 24 && hitBottom) {
                //ySpeed = -3;
            }
            else
                hitBottom = false;
        }
        /*
        yCoordinate += ySpeed;
        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
        */
        //engage = true;
        if (engage)
            hitbox = new Rectangle((int) this.xCoordinate, (int) this.yCoordinate, this.objectImg.getWidth(), this.objectImg.getHeight() * 18 / 60);
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
