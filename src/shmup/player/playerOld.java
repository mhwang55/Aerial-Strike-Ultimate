package shmup.player;

import shmup.Animation;
import shmup.Canvas;
import shmup.Framework;
import shmup.Rocket;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Fighter which is managed by player.
 * 
 */

public class playerOld {

    // Health of the helicopter.
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

    // Images of helicopter and its propellers.
    public BufferedImage playerImg;
    private BufferedImage helicopterFrontPropellerAnimImg;
    private BufferedImage helicopterRearPropellerAnimImg;

    // Animation of the player's plane
    private Animation playerAnim;

    private Animation helicopterFrontPropellerAnim;
    private Animation helicopterRearPropellerAnim;
    // Offset for the propeler. We add offset to the position of the position of helicopter.
    private int offsetXFrontPropeller;
    private int offsetYFrontPropeller;
    private int offsetXRearPropeller;
    private int offsetYRearPropeller;

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
    public playerOld(int xCoordinate, int yCoordinate)
    {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        
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

        this.offsetXFrontPropeller = 70;
        this.offsetYFrontPropeller = -23;        
        this.offsetXRearPropeller = -6;
        this.offsetYRearPropeller = -21;
        
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
            /*
            URL helicopterBodyImgUrl = this.getClass().getResource("resources/images/1_helicopter_body.png");
            helicopterBodyImg = ImageIO.read(helicopterBodyImgUrl);
            
            URL helicopterFrontPropellerAnimImgUrl = this.getClass().getResource("resources/images/1_front_propeller_anim.png");
            helicopterFrontPropellerAnimImg = ImageIO.read(helicopterFrontPropellerAnimImgUrl);
            
            URL helicopterRearPropellerAnimImgUrl = this.getClass().getResource("resources/images/1_rear_propeller_anim_blur.png");
            helicopterRearPropellerAnimImg = ImageIO.read(helicopterRearPropellerAnimImgUrl);
            */
            URL playerImgUrl = this.getClass().getResource("../resources/pics/player/dragonflyAnimationStrip.png");
            playerImg = ImageIO.read(playerImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(playerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Now that we have images of propeller animation we initialize animation object.
        playerAnim= new Animation(playerImg, 46, 32, 2, 20, true, xCoordinate, yCoordinate, 0);
        /*
        helicopterFrontPropellerAnim = new Animation(helicopterFrontPropellerAnimImg, 204, 34, 3, 20, true, xCoordinate + offsetXFrontPropeller, yCoordinate + offsetYFrontPropeller, 0);
        helicopterRearPropellerAnim = new Animation(helicopterRearPropellerAnimImg, 54, 54, 4, 20, true, xCoordinate + offsetXRearPropeller, yCoordinate + offsetYRearPropeller, 0);
        */
    }
    
    
    /**
     * Resets the player.
     * 
     * @param xCoordinate Starting x coordinate of helicopter.
     * @param yCoordinate Starting y coordinate of helicopter.
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
        /*
        helicopterFrontPropellerAnim.changeCoordinates(xCoordinate + offsetXFrontPropeller, yCoordinate + offsetYFrontPropeller);
        helicopterRearPropellerAnim.changeCoordinates(xCoordinate + offsetXRearPropeller, yCoordinate + offsetYRearPropeller);
        */

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
        /*
        helicopterFrontPropellerAnim.Draw(g2d);
        helicopterRearPropellerAnim.Draw(g2d);
        g2d.drawImage(helicopterBodyImg, xCoordinate, yCoordinate, null);
        */
        playerAnim.Draw(g2d);
    }
    
}
