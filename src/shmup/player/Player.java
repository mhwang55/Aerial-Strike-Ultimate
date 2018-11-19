package shmup.player;

import shmup.Animation;
import shmup.Canvas;
import shmup.Framework;
import shmup.Rocket;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Fighter which is managed by player.
 * 
 */

public class Player {

    // type of plane
    private int type;
    
    // Health of the plane.
    private final int healthInit = 100;
    //private final int healthInit = 10000000;
    public int health;
    
    // Position of the helicopter on the screen.
    public int xCoordinate;
    public int yCoordinate;
    
    // Moving speed and also direction.
    private double speed = 5;
    private double movingXspeed;
    public double movingYspeed;
    private double acceleratingXspeed;
    private double acceleratingYspeed;
    private double stoppingXspeed;
    private double stoppingYspeed;
    
    // Helicopter rockets.
    private final int numberOfRocketsInit = 80;
    public int numberOfRockets;
    
    // Helicopter machinegun ammo.
    private final int numberOfAmmoInit = 1400;
    public int numberOfAmmo;
    
    // Image of plane
    public BufferedImage playerImg;

    // Animation of the player's plane
    private Animation playerAnim;

    // Offset of the helicopter rocket holder.
    private int offsetXRocketHolder;
    private int offsetYRocketHolder;
    // Position on the frame/window of the helicopter rocket holder.
    public int rocketHolderXcoordinate;
    public int rocketHolderYcoordinate;

    // Offset of the helicopter machine gun. We add offset to the position of the position of helicopter.
    private int offsetXMachineGun;
    private int offsetYMachineGun;
    // Position on the frame/window of the helicopter machine gun.
    public int machineGunXcoordinate;
    public int machineGunYcoordinate;

    // firepower level of player
    public int powerLvl = 0;

    // number of bombs of player
    public int bombNum = 2;

    
    /**
     * Creates object of player.
     * 
     * @param xCoordinate Starting x coordinate of helicopter.
     * @param yCoordinate Starting y coordinate of helicopter.
     */
    public Player(int xCoordinate, int yCoordinate, int type)
    {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.type = type;
        
        LoadContent();
        Initialize();
    }
    
    
    /**
     * Set variables and objects for this class.
     */
    private void Initialize()
    {
        this.health = healthInit;
        
        this.numberOfRockets = numberOfRocketsInit;
        this.numberOfAmmo = numberOfAmmoInit;
        
        this.movingXspeed = 0;
        this.movingYspeed = 0;
        this.acceleratingXspeed = 0.2;
        this.acceleratingYspeed = 0.2;
        this.stoppingXspeed = 0.1;
        this.stoppingYspeed = 0.1;

        this.offsetXRocketHolder = 138;
        this.offsetYRocketHolder = 40;
        this.rocketHolderXcoordinate = this.xCoordinate + this.offsetXRocketHolder;
        this.rocketHolderYcoordinate = this.yCoordinate + this.offsetYRocketHolder;
        
        this.offsetXMachineGun = 0;
        this.offsetYMachineGun = 0;
        this.machineGunXcoordinate = this.xCoordinate + this.playerImg.getWidth()/4;
        this.machineGunYcoordinate = this.yCoordinate;
    }
    
    /**
     * Load files for this class.
     */
    private void LoadContent()
    {
        try 
        {
            URL  playerImgUrl;
            System.out.println(type);
            if (type == 0) {
                playerImgUrl = this.getClass().getResource("../resources/pics/player/dragonflyAnimationStrip.png");
                playerImg = ImageIO.read(playerImgUrl);
                // Now that we have images of plane animation we initialize animation object.
                playerAnim =
                        new Animation(playerImg, 46, 37, 2, 20, true,
                                xCoordinate, yCoordinate, 0);
            }
            else if (type == 1)
            {
                playerImgUrl = this.getClass().getResource("../resources/pics/player/marauderAnimationStrip.png");
                playerImg = ImageIO.read(playerImgUrl);
                playerAnim =
                        new Animation(playerImg,
                                65, 48, 2, 20, true,
                                xCoordinate, yCoordinate, 0);
            }
            else if (type == 2)
            {
                playerImgUrl = this.getClass().getResource("../resources/pics/player/bf109AnimationStrip.png");
                playerImg = ImageIO.read(playerImgUrl);
                playerAnim =
                        new Animation(playerImg,
                                43, 37, 2, 20, true,
                                xCoordinate, yCoordinate, 0);
            }
            else if (type == 3)
            {
                playerImgUrl = this.getClass().getResource("../resources/pics/player/zeroAnimationStrip.png");
                playerImg = ImageIO.read(playerImgUrl);
                playerAnim =
                        new Animation(playerImg,
                                50, 38, 2, 20, true,
                                xCoordinate, yCoordinate, 0);
            }
            else if (type == 4)
            {
                playerImgUrl = this.getClass().getResource("../resources/pics/player/spitfireAnimationStrip.png");
                playerImg = ImageIO.read(playerImgUrl);
                playerAnim =
                        new Animation(playerImg,
                                49, 39, 2, 20, true,
                                xCoordinate, yCoordinate, 0);
            }
            else
            {
                playerImgUrl = this.getClass().getResource("../resources/pics/player/me110AnimationStrip.png");
                playerImg = ImageIO.read(playerImgUrl);
                playerAnim =
                        new Animation(playerImg,
                                68, 54, 2, 20, true,
                                xCoordinate, yCoordinate, 0);
            }
        }
        catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * Resets the player.
     * 
     * @param xCoordinate Starting x coordinate of plane.
     * @param yCoordinate Starting y coordinate of plane.
     */
    public void Reset(int xCoordinate, int yCoordinate)
    {
        this.health = healthInit;
        
        this.numberOfRockets = numberOfRocketsInit;
        this.numberOfAmmo = numberOfAmmoInit;
        
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        
        this.machineGunXcoordinate = this.xCoordinate;
        this.machineGunYcoordinate = this.yCoordinate;
        
        this.movingXspeed = 0;
        this.movingYspeed = 0;
    }
    
    
    /**
     * Checks if player is shooting. It also checks if player can 
     * shoot (time between bullets, does a player have any bullet left).
     * 
     * @param gameTime The current elapsed game time in nanoseconds.
     * @return true if player is shooting.
     */
    public boolean isShooting(long gameTime)
    {
        // Checks if j key is down && if it is the time for a new bullet.
        if(Canvas.keyboardKeyState(KeyEvent.VK_J) &&
            ((gameTime - Shot.timeOfLastCreatedBullet) >= Shot.timeBetweenNewBullets))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Checks if player is shooting. It also checks if player can
     * shoot (time between bullets, does a player have any bullet left).
     *
     * @param gameTime The current elapsed game time in nanoseconds.
     * @return true if player is shooting.
     */
    public boolean isShootingSecondary(long gameTime)
    {
        // Checks if j key is down && if it is the time for a new bullet.
        if(Canvas.keyboardKeyState(KeyEvent.VK_J) &&
            ((gameTime - Missile.timeOfLastCreatedBullet) >= Missile.timeBetweenNewBullets))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    
    /**
     * Checks if player is fired a rocket. It also checks if player can 
     * fire a rocket (time between rockets, does a player have any rocket left).
     * 
     * @param gameTime The current elapsed game time in nanoseconds.
     * @return true if player is fired a rocket.
     */
    public boolean isFiredRocket(long gameTime)
    {
        // Checks if k key is down && if it is the time for new bomb && if there are bombs left
        if(Canvas.keyboardKeyState(KeyEvent.VK_K) &&
            ((gameTime - Rocket.timeOfLastCreatedRocket) >= Rocket.timeBetweenNewRockets) &&
            this.numberOfRockets > 0 ) 
        {
            return true;
        } else
            return false;
    }
    
    
    /**
     * Checks if player moving helicopter and sets its moving speed if player is moving.
     */
    public void isMoving()
    {
        // Moving on the x coordinate.
        if((Canvas.keyboardKeyState(KeyEvent.VK_D) || Canvas.keyboardKeyState(KeyEvent.VK_RIGHT)) &&
                xCoordinate + playerImg.getWidth() / 4 <= Framework.frameWidth)
            movingXspeed = speed;
        else if((Canvas.keyboardKeyState(KeyEvent.VK_A) || Canvas.keyboardKeyState(KeyEvent.VK_LEFT)) &&
                xCoordinate + playerImg.getWidth() / 4 >= 0)
            movingXspeed = -speed;
        else    // Stop
            movingXspeed = 0;

        // Moving on the y coordinate.
        if((Canvas.keyboardKeyState(KeyEvent.VK_W) || Canvas.keyboardKeyState(KeyEvent.VK_UP)) &&
                yCoordinate + playerImg.getHeight() / 2 >= 0)
            movingYspeed = -speed;
        else if((Canvas.keyboardKeyState(KeyEvent.VK_S) || Canvas.keyboardKeyState(KeyEvent.VK_DOWN)) &&
                yCoordinate + playerImg.getHeight() / 2 <= Framework.frameHeight)
            movingYspeed = speed;
        else    // Stop
            movingYspeed = 0;
    }
    
    
    /**
     * Updates position of helicopter, animations.
     */
    public void Update()
    {
        // Move player
        xCoordinate += movingXspeed;
        yCoordinate += movingYspeed;
        playerAnim.changeCoordinates(xCoordinate, yCoordinate);

        // Change position of the rocket holder.
        this.rocketHolderXcoordinate = this.xCoordinate + this.offsetXRocketHolder;
        this.rocketHolderYcoordinate = this.yCoordinate + this.offsetYRocketHolder;
        
        // Move the machine gun with helicopter.
        this.machineGunXcoordinate = this.xCoordinate + playerImg.getWidth()/4;
        this.machineGunYcoordinate = this.yCoordinate + this.offsetYMachineGun;
    }
    
    
    /**
     * Draws helicopter to the screen.
     * 
     * @param g2d Graphics2D
     */
    public void Draw(Graphics2D g2d)
    {
        playerAnim.Draw(g2d);
    }
    
}
