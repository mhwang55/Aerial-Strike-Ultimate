package shmup.drawables.enemies.boss.ambulatamuere;

import shmup.player.Player;
import shmup.drawables.enemies.Turret;
import shmup.drawables.bullets.Bullet;
import shmup.drawables.bullets.BulletMedium;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AmbulatamuereMachineGun extends Turret {
    // For creating new enemies.
    public boolean spawn = false;
    private boolean isShooting = false;
    private long time;
    private int bulletNum = 0;
    private boolean hitBottom = false;
    // gunNo is the number of the machine gun; starts at 1 going clockwise
    private int gunNo;
    private boolean engage = false;
    private boolean beginFire = false;
    private boolean fired = false;
    private int offset = 0;
    private int engageTimer = 600;
    private boolean startEngageTimer = false;
    private int clipSize = 4;
    private AudioInputStream audio;
    private AudioFormat audioFormat;
    private DataLine.Info info;
    private Clip clip;

    public AmbulatamuereMachineGun(int xCoordinate, int yCoordinate, BufferedImage enemyImg,
                                   int frameWidth, int frameHeight, int noFrames, int gunNo, long gameTime)
    {
        super(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
        this.gunNo = gunNo;
        time = gameTime;
        switch (gunNo)
        {
            case 1:
                offset = 281;
                break;
            case 2:
                offset = 194;
                break;
            case 3:
                offset = 109;
                break;
            case 4:
                offset = 71;
                break;
        }
        Initialize(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
        try {
                URL laserURL = this.getClass().getResource("../../../../resources/soundtracks/enemyFire/machineGun.wav");
                audio = AudioSystem.getAudioInputStream(laserURL);
                audioFormat = audio.getFormat();

                info= new DataLine.Info(Clip.class, audioFormat);

                clip = (Clip) AudioSystem.getLine(info);
                clip.open(audio);
        }
        catch (Exception ex)
        {
            Logger.getLogger(AmbulatamuereMachineGun.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initialize enemy machine gun
     *
     * @param xCoordinate Starting x coordinate of fighter
     * @param yCoordinate Starting y coordinate of fighter
     */
    public void Initialize(int xCoordinate, int yCoordinate, BufferedImage enemyImg, int frameWidth, int frameHeight, int noFrames)
    {
        health = 100;

        // Sets enemy position.
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;

        setRotatePoint();
    }

    public void setRotatePoint()
    {
        locationX = objectImg.getWidth() / 2;
        locationY = objectImg.getHeight() * 10 / 38;
    }

    public boolean isShooting(long gameTime) {
        long timeDiff = gameTime - time;
        if (yCoordinate >= 150 && engageTimer >= 0)
            startEngageTimer = true;
        if (startEngageTimer && engageTimer >= 0)
            engageTimer--;

        if (engageTimer <= 0)
            engage = true;
        if (!engage)
            return false;

        if (timeDiff > 7500000000L && clipSize == 4)
        {
            clipSize--;
            time = gameTime;
            return true;
        }
        else if (timeDiff > 50000000L && clipSize > 0 && clipSize < 4)
        {
            clipSize--;
            if (clipSize == 0)
                fired = true;
            time = gameTime;
            return true;
        }
        else {
            if (fired)
            {
                time = gameTime;
                fired = false;
                clipSize = 4;
            }
            return false;
        }
    }

    public ArrayList<Bullet> createBullet(Player player, double angleAdd) {
        double radianBull = Math.atan2(this.yCoordinate + 10 - player.yCoordinate - player.playerImg.getHeight() / 2,
                this.xCoordinate + 9 - player.xCoordinate - player.playerImg.getWidth() / 4);
        double angleBull = -(radianBull + Math.PI/2);

        double posX;
        double posY;

        posX = -objectImg.getWidth() * 56 / 38 * (float) Math.cos(angleBull + Math.PI/2);
        posY = objectImg.getHeight() * 28 / 38 * (float) Math.sin(angleBull + Math.PI/2);
        int randomNum1 = ThreadLocalRandom.current().nextInt(-7, 7 + 1);

        int locX = (int) this.xCoordinate + 9 - 4 + (int) posX;
        int locY = (int) this.yCoordinate + 10 - 4 + (int) posY;

        ArrayList bullets = new ArrayList<Bullet>();
        BulletMedium b = new BulletMedium(locX, locY, angleBull, randomNum1 * Math.PI / 180, 5, 0);
        bullets.add(b);

        /*
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
        */

        return bullets;
    }

    public void Update(Player player, long gameTime) {
        this.radians = Math.atan2(this.yCoordinate + 10 - player.yCoordinate - player.playerImg.getHeight() / 2,
                this.xCoordinate + 9 - player.xCoordinate - player.playerImg.getWidth() / 4);
        this.angle = radians + Math.PI/2;
        isShooting(gameTime);

        if (yCoordinate < 149 + offset && !hitBottom) {
            ySpeed = 1;
        }
        else if (yCoordinate >= 149 + offset && !hitBottom) {
            hitBottom = true;
        }
        else if (yCoordinate >= 101 + offset && hitBottom) {
            ySpeed = -1;
        }
        else
            hitBottom = false;

        yCoordinate += ySpeed;
        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
        if (engage)
            hitbox = new Rectangle((int) this.xCoordinate, (int) this.yCoordinate, this.objectImg.getWidth(), this.objectImg.getHeight() * 10 / 60);
    }
}
