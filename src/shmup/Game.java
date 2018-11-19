package shmup;

import shmup.drawables.Background;
import shmup.drawables.Drawable;
import shmup.drawables.PowerUp;
import shmup.drawables.enemies.*;
import shmup.drawables.enemies.air.B24;
import shmup.drawables.enemies.air.LockheedConstellation;
import shmup.drawables.enemies.air.P51;
import shmup.drawables.enemies.air.YB35;
import shmup.drawables.enemies.boss.ambulatamuere.*;
import shmup.drawables.bullets.*;
import shmup.drawables.enemies.boss.treadingPain.*;
import shmup.drawables.enemies.midboss.Ecaep;
import shmup.player.Missile;
import shmup.player.Player;
import shmup.player.Shot;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

/**
 * Actual game.
 *
 * @author Max Wang
 */

public class Game {

    // type of plane
    private int type;
    
    // Use this to generate a random number.
    private Random random;
    
    // We will use this for setting mouse position.
    private Robot robot;

    // selection screen object
    private SelectionScreen selectionScreen;

    // Player - plane that is managed by player.
    private Player player;
    
    // Enemies
    private ArrayList<Enemy> enemyList = new ArrayList<Enemy>();

    // Sound files
    private AudioInputStream aisBoss;
    private AudioFormat formatBoss;
    private SourceDataLine lineBoss;
    private DataLine.Info infoBoss;
    byte[] dataBoss;

    private AudioInputStream aisHitEnemy;
    private AudioFormat formatHitEnemy;
    private Clip clipHitEnemy;
    private DataLine.Info infoHitEnemy;

    private AudioInputStream aisLaserFire;
    private AudioFormat formatLaserFire = null;
    private int BUFSIZE;
    private Clip clipLaser;
    private DataLine.Info infoLaser;
    private Music music;

    private Clip clipExplosion;

    private boolean playBossMusic = false;



    // Drawables
    private ArrayList<Drawable> drawableList = new ArrayList<>();
    // enemy pics
    // air
    private BufferedImage imgC47;
    private BufferedImage imgB17;
    private BufferedImage imgB24;
    private BufferedImage imgB25;
    private BufferedImage imgB29;
    private BufferedImage imgF4U;
    private BufferedImage imgLockheedConstellation;
    private BufferedImage imgP38;
    private BufferedImage imgP51;
    private BufferedImage imgYB35;

    // ground
    private BufferedImage imgFlakpanzerBody;
    private BufferedImage imgFlakpanzerTurret;
    private BufferedImage imgJagdpanther;

    // midboss
    private BufferedImage imgB36;

    /*
     * bosses
     */
    // command center
    private BufferedImage imgCommandCenterMachineGun;
    // ambulatamuere
    private BufferedImage imgAmbulatamuereDoubleTurret;
    private BufferedImage imgAmbulatamuereMachineGun;
    private BufferedImage imgAmbulatamuereClusterTurret;
    private BufferedImage imgAmbulatamuereBody;
    private BufferedImage imgAmbulatamuereFoot;
    private BufferedImage imgAmbulatamuereLimbLeft;
    private BufferedImage imgAmbulatamuereLimbRight;
    private boolean spawn= true;

    private boolean spawnAmbulatamuere = true;

    // treading pain
    private BufferedImage imgTreadingPainMachineGun;
    private BufferedImage imgTreadingPainQuadTurret;
    private BufferedImage imgTreadingPainDoubleTurret;
    private BufferedImage imgTreadingPainTripleTurret;
    private BufferedImage imgTreadingPainBody;

    private boolean spawnTreadingPain = true;

    //private ArrayList<P51> p51List = new ArrayList<P51>();

    // Enemy bomber
    //private ArrayList<B24> b24List = new ArrayList<B24>();

    // Explosions
    private ArrayList<Animation> explosionsList;
    private BufferedImage explosionAnimImg;

    // List of all the enemy bullets
    private ArrayList<Bullet> enemyBulletsList;

    // List of all the lasers
    private ArrayList<Shot> bulletsList;

    // List of all the secondary weapon shots
    private ArrayList<Missile> secondaryList;

    // List of all the rockets.
    private ArrayList<Rocket> rocketsList;
    // List of all the rockets smoke.
    private ArrayList<RocketSmoke> rocketSmokeList;


    // background (TEMP)
    private BufferedImage backgroundImg;
    private Background background;

    // Image for the sky color.
    private BufferedImage skyColorImg;
    
    // Images for white spot on the sky.
    private BufferedImage cloudLayer1Img;
    private BufferedImage cloudLayer2Img;
    // Images for mountains and ground.

    // Objects of moving images.
    private MovingBackground cloudLayer1Moving;
    private MovingBackground cloudLayer2Moving;
    private MovingBackground mountainsMoving;
    private MovingBackground groundMoving;
    
    // Image of mouse cursor.
    private BufferedImage mouseCursorImg;
    
    // Font that we will use to write statistic to the screen.
    private Font font;
    
    // Statistics (destroyed enemies, run away enemies)
    private int runAwayEnemies;
    private int destroyedEnemies;
    

    public Game(int type)
    {
        this.type = type;
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        
        Thread threadForInitGame = new Thread() {
            @Override
            public void run(){
                // Selection screen content
                loadSelectionScreenContent();

                // Sets variables and objects for the game.
                Initialize();
                // Load game files (images, sounds, ...)
                LoadContent();

                Framework.gameState = Framework.GameState.PLAYING;
                //Framework.gameState = Framework.GameState.CHARACTER_SELECTION;
            }
        };
        threadForInitGame.start();
    }
    
    
    /**
     * Set variables and objects for the game.
     */
    private void Initialize()
    {
        random = new Random();
        
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //player = new Player(Framework.frameWidth / 4, Framework.frameHeight / 4);
        player = new Player(250, 800, this.type);

        enemyList = new ArrayList<Enemy>();
        //p51List = new ArrayList<P51>();
        //b24List = new ArrayList<B24>();

        explosionsList = new ArrayList<Animation>();

        enemyBulletsList = new ArrayList<Bullet>();
        bulletsList = new ArrayList<Shot>();
        secondaryList = new ArrayList<Missile>();

        rocketsList = new ArrayList<Rocket>();
        rocketSmokeList = new ArrayList<RocketSmoke>();
        
        // Moving images.
        cloudLayer1Moving = new MovingBackground();
        cloudLayer2Moving = new MovingBackground();
        mountainsMoving = new MovingBackground();
        groundMoving = new MovingBackground();

        font = new Font("monospaced", Font.BOLD, 18);
        
        runAwayEnemies = 0;
        destroyedEnemies = 0;
    }

    /**
     * Load game files (images).
     */
    private void loadSelectionScreenContent() {
        //
    }

    /**
     * Load boss game files (images).
     */
    private void loadBossContent() {
        loadAmbulatamuereContent();
        loadTreadingPainContent();
    }

    /**
     * Load Ambulatamuere game files (images).
     */
    private void loadAmbulatamuereContent() {
        try
        {
            // Load image for ambulatamuere double Turret
            URL aDoubleTurretUrl = this.getClass().getResource("resources/pics/enemies/boss/2ambulatamuere/doubleTurret.png");
            imgAmbulatamuereDoubleTurret = ImageIO.read(aDoubleTurretUrl);

            // Load image for ambulatamuere machine gun
            URL aMachineGunUrl = this.getClass().getResource("resources/pics/enemies/boss/2ambulatamuere/machineGun.png");
            imgAmbulatamuereMachineGun = ImageIO.read(aMachineGunUrl);

            // Load image for ambulatamuere cluster Turret
            URL aClusterTurretUrl = this.getClass().getResource("resources/pics/enemies/boss/2ambulatamuere/clusterTurret.png");
            imgAmbulatamuereClusterTurret = ImageIO.read(aClusterTurretUrl);

            // Load image for ambulatamuere body
            URL aBodyUrl = this.getClass().getResource("resources/pics/enemies/boss/2ambulatamuere/body.png");
            imgAmbulatamuereBody = ImageIO.read(aBodyUrl);

            // Load image for ambulatamuere foot
            URL aFootUrl = this.getClass().getResource("resources/pics/enemies/boss/2ambulatamuere/foot.png");
            imgAmbulatamuereFoot = ImageIO.read(aFootUrl);

            // Load image for ambulatamuere left limb
            URL aLimbLeftUrl = this.getClass().getResource("resources/pics/enemies/boss/2ambulatamuere/limbLeft.png");
            imgAmbulatamuereLimbLeft = ImageIO.read(aLimbLeftUrl);

            // Load image for ambulatamuere right limb
            URL aLimbRightUrl = this.getClass().getResource("resources/pics/enemies/boss/2ambulatamuere/limbRight.png");
            imgAmbulatamuereLimbRight = ImageIO.read(aLimbRightUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Load Treading Pain game files (images).
     */
    private void loadTreadingPainContent() {
        try
        {
            // Load image for treading pain double turret
            URL aDoubleTurretUrl = this.getClass().getResource("resources/pics/enemies/boss/5treadingPain/doubleTurret.png");
            imgTreadingPainDoubleTurret = ImageIO.read(aDoubleTurretUrl);

            // Load image for treading pain machine gun
            /*
            URL tMachineGunUrl = this.getClass().getResource("resources/pics/enemies/boss/5treadingPain/machineGun.png");
            imgTreadingPainMachineGun = ImageIO.read(tMachineGunUrl);
            */

            // Load image for treading pain quad turret
            URL tQuadTurretUrl = this.getClass().getResource("resources/pics/enemies/boss/5treadingPain/quadTurret.png");
            imgTreadingPainQuadTurret = ImageIO.read(tQuadTurretUrl);

            // Load image for treading pain triple turret
            URL tTripleTurretUrl = this.getClass().getResource("resources/pics/enemies/boss/5treadingPain/tripleTurret.png");
            imgTreadingPainTripleTurret = ImageIO.read(tTripleTurretUrl);

            // Load image for treading pain body
            URL tBodyUrl = this.getClass().getResource("resources/pics/enemies/boss/5treadingPain/body.png");
            imgTreadingPainBody = ImageIO.read(tBodyUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Load game files (images).
     */
    private void LoadContent()
    {
        try 
        {
            // Images of environment
            URL skyColorImgUrl = this.getClass().getResource("resources/old/images/sky_color.jpg");
            skyColorImg = ImageIO.read(skyColorImgUrl);
            URL cloudLayer1ImgUrl = this.getClass().getResource("resources/pics/background/backgrounds/bgLayer21.png");
            cloudLayer1Img = ImageIO.read(cloudLayer1ImgUrl);
            URL cloudLayer2ImgUrl = this.getClass().getResource("resources/pics/background/backgrounds/bgLayer11.png");
            cloudLayer2Img = ImageIO.read(cloudLayer2ImgUrl);

            // background image
            URL backgroundUrl = this.getClass().getResource("resources/pics/background/background.png");
            backgroundImg = ImageIO.read(backgroundUrl);

            // Load images for enemy c17
            URL c47Url = this.getClass().getResource("resources/pics/enemies/air/c47DarkAnimationStrip.png");
            imgC47 = ImageIO.read(c47Url);

            // Load images for enemy b17
            URL b17Url = this.getClass().getResource("resources/pics/enemies/air/b17AnimationStrip.png");
            imgB17 = ImageIO.read(b17Url);

            // Load images for enemy b24
            URL b24Url = this.getClass().getResource("resources/pics/enemies/air/b24AnimationStrip.png");
            imgB24 = ImageIO.read(b24Url);

            // Load images for enemy b25
            URL b25Url = this.getClass().getResource("resources/pics/enemies/air/b25AnimationStrip.png");
            imgB25 = ImageIO.read(b25Url);

            // Load images for enemy b29
            URL b29Url = this.getClass().getResource("resources/pics/enemies/air/b29AnimationStrip.png");
            imgB29 = ImageIO.read(b29Url);

            // Load images for enemy b36
            URL b36Url = this.getClass().getResource("resources/pics/enemies/midboss/ecaep.png");
            imgB36 = ImageIO.read(b36Url);

            // Load images for enemy f4u
            URL f4uUrl = this.getClass().getResource("resources/pics/enemies/air/f4uAnimationStrip.png");
            imgF4U = ImageIO.read(f4uUrl);

            // Load images for enemy lockheed constellation
            URL lockheedConstellationUrl = this.getClass().getResource("resources/pics/enemies/air/lockheedConstellationAnimationStrip.png");
            imgLockheedConstellation = ImageIO.read(lockheedConstellationUrl);

            // Load images for enemy p38
            URL p38Url = this.getClass().getResource("resources/pics/enemies/air/p38AnimationStripDown.png");
            imgP38 = ImageIO.read(p38Url);

            // Load images for enemy p51
            URL p51Url = this.getClass().getResource("resources/pics/enemies/air/p51AnimationStripDown.png");
            imgP51 = ImageIO.read(p51Url);

            // Load images for enemy yb35
            URL yb35Url = this.getClass().getResource("resources/pics/enemies/air/yb35AnimationStrip.png");
            imgYB35 = ImageIO.read(yb35Url);

            // ground
            // Load images for enemy flakpanzer body
            URL flakpanzerUrl = this.getClass().getResource("resources/pics/enemies/ground/flakpanzer/body.png");
            imgFlakpanzerBody = ImageIO.read(flakpanzerUrl);
            // Load images for enemy flakpanzer turret
            URL flakpanzerTurretUrl = this.getClass().getResource("resources/pics/enemies/ground/flakpanzer/turret.png");
            imgFlakpanzerTurret = ImageIO.read(flakpanzerTurretUrl);

            // boss
            loadBossContent();

            // Images of rocket and its smoke.
            URL rocketImgUrl = this.getClass().getResource("resources/old/images/rocket.png");
            Rocket.rocketImg = ImageIO.read(rocketImgUrl);
            URL rocketSmokeImgUrl = this.getClass().getResource("resources/old/images/rocket_smoke.png");
            RocketSmoke.smokeImg = ImageIO.read(rocketSmokeImgUrl);
            
            // Image of explosion animation.
            URL explosionAnimImgUrl = this.getClass().getResource("resources/old/images/explosion_anim.png");
            explosionAnimImg = ImageIO.read(explosionAnimImgUrl);
            
            // Image of mouse cursor.
            URL mouseCursorImgUrl = this.getClass().getResource("resources/old/images/mouse_cursor.png");
            mouseCursorImg = ImageIO.read(mouseCursorImgUrl);
            
            // Plane bullet.
            URL bulletImgUrl = this.getClass().getResource("resources/pics/player/primary/laser.png");
            Shot.bulletImg = ImageIO.read(bulletImgUrl);

            // Plane bullet.
            URL missileImgUrl = this.getClass().getResource("resources/pics/player/secondary/rocket.png");
            Missile.missileImg= ImageIO.read(missileImgUrl);

            // Enemy bullet
            bulletImgUrl = this.getClass().getResource("resources/pics/enemies/bullets/bullet.png");
            BulletMedium.bulletImg = ImageIO.read(bulletImgUrl);
            bulletImgUrl = this.getClass().getResource("resources/pics/enemies/bullets/bulletSmall.png");
            BulletSmall.bulletImg = ImageIO.read(bulletImgUrl);
            bulletImgUrl = this.getClass().getResource("resources/pics/enemies/bullets/bulletLarge.png");
            BulletLarge.bulletImg = ImageIO.read(bulletImgUrl);
            bulletImgUrl = this.getClass().getResource("resources/pics/enemies/bullets/bulletStarburst.png");
            BulletStarburst.bulletImg = ImageIO.read(bulletImgUrl);
            bulletImgUrl = this.getClass().getResource("resources/pics/enemies/bullets/bulletStarburst.png");
            BulletStarburstShell.bulletImg = ImageIO.read(bulletImgUrl);

            // Powerup
            URL powerUpImgUrl = this.getClass().getResource("resources/pics/player/powerUp.bmp");
            PowerUp.powerUpImg = ImageIO.read(powerUpImgUrl);

            // sounds
            loadSoundContent();
        }
        catch (IOException ex) 
        {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        background = new Background(0, -9000, backgroundImg, 560, 6200, 1);

        // Now that we have images we initialize moving images.
        cloudLayer1Moving.Initialize(cloudLayer1Img, 6, 0);
        cloudLayer2Moving.Initialize(cloudLayer2Img, 2, 0);
        music = new Music("bgMusic", aisBoss, lineBoss);
    }

    private void loadSoundContent()
    {
        try
        {
            URL bossURL = this.getClass().getResource("resources/soundtracks/levels/stage2.wav");
            aisBoss = AudioSystem.getAudioInputStream(bossURL);
            formatBoss = aisBoss.getFormat();
            infoBoss = new DataLine.Info(SourceDataLine.class, formatBoss);
            lineBoss = (SourceDataLine) AudioSystem.getLine(infoBoss);
            lineBoss.open(formatBoss);
            BUFSIZE = 4096;


            URL laserURL = this.getClass().getResource("resources/soundtracks/laserFire.wav");
            aisLaserFire = AudioSystem.getAudioInputStream(laserURL);
            formatLaserFire = aisLaserFire.getFormat();

            infoLaser = new DataLine.Info(Clip.class, formatLaserFire);

            clipLaser = (Clip) AudioSystem.getLine(infoLaser);
            clipLaser.open(aisLaserFire);
            aisLaserFire.close();

            URL hitEnemyURL = this.getClass().getResource("resources/soundtracks/enemyFire/machineGun.wav");
            aisHitEnemy = AudioSystem.getAudioInputStream(hitEnemyURL);
            formatHitEnemy = aisHitEnemy.getFormat();

            infoHitEnemy = new DataLine.Info(Clip.class, formatHitEnemy);

            clipHitEnemy = (Clip) AudioSystem.getLine(infoHitEnemy);
            clipHitEnemy.open(aisHitEnemy);
            aisHitEnemy.close();

            URL explosionURL = this.getClass().getResource("resources/soundtracks/explosion.wav");
            AudioInputStream aisExplosion = AudioSystem.getAudioInputStream(explosionURL);
            AudioFormat formatExplosion = aisExplosion.getFormat();
            DataLine.Info infoExplosion = new DataLine.Info(Clip.class, formatExplosion);
            clipExplosion = (Clip) AudioSystem.getLine(infoExplosion);
            clipExplosion.open(aisExplosion);
            aisExplosion.close();
        } catch (Exception ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    /**
     * Restart game - reset some variables.
     */
    public void RestartGame()
    {
        player.Reset(250, 800);
        
        P51.restartEnemy();
        
        Shot.timeOfLastCreatedBullet = 0;
        Rocket.timeOfLastCreatedRocket = 0;
        
        // Empty all the lists.
        enemyList.clear();
        bulletsList.clear();
        enemyBulletsList.clear();
        rocketsList.clear();
        rocketSmokeList.clear();
        explosionsList.clear();
        
        // Statistics
        runAwayEnemies = 0;
        destroyedEnemies = 0;
    }
    
    
    /**
     * Update game logic.
     * 
     * @param gameTime The elapsed game time in nanoseconds.
     * @param mousePosition current mouse position.
     */
    public void UpdateGame(long gameTime, Point mousePosition)
    {
        /* Player */
        // When player is destroyed and all explosions are finished showing we change game status.
        if( !isPlayerAlive() && explosionsList.isEmpty() ){
            Framework.gameState = Framework.GameState.GAMEOVER;
            return; // If player is destroyed, we don't need to do thing below.
        }
        // When a player is out of rockets and machine gun bullets, and all lists 
        // of bullets, rockets and explosions are empyt(end showing) we finish the game.
        if(player.numberOfAmmo <= 0 && 
           player.numberOfRockets <= 0 && 
           bulletsList.isEmpty() && 
           rocketsList.isEmpty() && 
           explosionsList.isEmpty())
        {
            Framework.gameState = Framework.GameState.GAMEOVER;
            return;
        }
        // If player is alive we update him.
        if(isPlayerAlive()){
            isPlayerShooting(gameTime);
            didPlayerFiredRocket(gameTime);
            player.isMoving();
            player.Update();
        }
        
        /* Mouse */
        //limitMousePosition(mousePosition);

        /* Music */
        //updateMusic();
        
        /* Bullets */
        updateBullets();
        updateSecondary();
        updateEnemyBullets(gameTime);

        /* Rockets */
        updateRockets(gameTime); // It also checks for collisions (if any of the rockets hit any of the enemy helicopter).
        updateRocketSmoke(gameTime);
        
        /* Enemies */
        updateStage(gameTime);
        updateEnemies(gameTime);
        updateDrawables(gameTime);

        /* Explosions */
        updateExplosions();
        /*
        try
        {
            Thread.sleep(1);
        }
        catch (Exception ex)
        {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
    }
    
    /**
     * Draw the game to the screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition current mouse position.
     */
    public void Draw(Graphics2D g2d, Point mousePosition, long gameTime)
    {
        // Image for background sky color.
        //g2d.drawImage(skyColorImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        
        // Moving images.
        //cloudLayer2Moving.Draw(g2d);
        // Moving images.
        //cloudLayer1Moving.Draw(g2d);

        // Draws all the drawables
        background.Draw(g2d);
        for(int i = 0; i < drawableList.size(); i++)
        {
            drawableList.get(i).Draw(g2d);
        }

        // Draws all the enemies.
        for(int i = 0; i < enemyList.size(); i++)
        {
            Enemy eh = enemyList.get(i);
            enemyList.get(i).Draw(g2d);
            /*
            if (eh instanceof AmbulatamuereDoubleTurret)
            {
                eh.Draw(g2d);
            }
            else {
                enemyList.get(i).Draw(g2d);
            }
            */
        }

        if(isPlayerAlive())
            player.Draw(g2d);

        // Draws all the player secondary weapons.
        for(int i = 0; i < secondaryList.size(); i++)
        {
            if (secondaryList.get(i) != null)
                secondaryList.get(i).Draw(g2d);
        }

        // Draws all the player bullets.
        for(int i = 0; i < bulletsList.size(); i++)
        {
            bulletsList.get(i).Draw(g2d);
        }

        // Draws all the rockets.
        for(int i = 0; i < rocketsList.size(); i++)
        {
            rocketsList.get(i).Draw(g2d);
        }
        // Draws smoke of all the rockets.
        for(int i = 0; i < rocketSmokeList.size(); i++)
        {
            rocketSmokeList.get(i).Draw(g2d);
        }
        
        // Draw all explosions.
        for(int i = 0; i < explosionsList.size(); i++)
        {
            explosionsList.get(i).Draw(g2d);
        }

        // Draws all the enemy bullets.
        for(int i = 0; i < enemyBulletsList.size(); i++)
        {
            if (enemyBulletsList.get(i) != null)
                enemyBulletsList.get(i).Draw(g2d);
        }

        // Draw statistics
        g2d.setFont(font);
        g2d.setColor(Color.darkGray);
        
        g2d.drawString(formatTime(gameTime), Framework.frameWidth/2 - 45, 21);
        g2d.drawString("DESTROYED: " + destroyedEnemies, 10, 21);
        g2d.drawString("RUNAWAY: "   + runAwayEnemies,   10, 41);
        g2d.drawString("ROCKETS: "   + player.numberOfRockets, 10, 81);
        g2d.drawString("AMMO: "      + player.numberOfAmmo, 10, 101);
        

        /*
        // Mouse cursor
        if(isPlayerAlive())
            drawRotatedMouseCursor(g2d, mousePosition);
        */
    }

    public void selectionDraw(Graphics2D g2d, long time)
    {
        //
    }

    /**
     * Draws some game statistics when game is over.
     * 
     * @param g2d Graphics2D
     * @param gameTime Elapsed game time.
     */
    public void DrawStatistic(Graphics2D g2d, long gameTime){
        g2d.drawString("Time: " + formatTime(gameTime),                Framework.frameWidth/2 - 50, Framework.frameHeight/3 + 80);
        g2d.drawString("Rockets left: "      + player.numberOfRockets, Framework.frameWidth/2 - 55, Framework.frameHeight/3 + 105);
        g2d.drawString("Ammo left: "         + player.numberOfAmmo,    Framework.frameWidth/2 - 55, Framework.frameHeight/3 + 125);
        g2d.drawString("Destroyed enemies: " + destroyedEnemies,       Framework.frameWidth/2 - 65, Framework.frameHeight/3 + 150);
        g2d.drawString("Runaway enemies: "   + runAwayEnemies,         Framework.frameWidth/2 - 65, Framework.frameHeight/3 + 170);
        g2d.setFont(font);
        g2d.drawString("Statistics: ",                                 Framework.frameWidth/2 - 75, Framework.frameHeight/3 + 60);
    }
    
    /**
     * Draws rotated mouse cursor.
     * It rotates the cursor image on the basis of the player helicopter machine gun.
     * 
     * @param g2d Graphics2D
     * @param mousePosition Position of the mouse.
     */
    private void drawRotatedMouseCursor(Graphics2D g2d, Point mousePosition)
    {
        double RIGHT_ANGLE_RADIANS = Math.PI / 2;
        
        // Positon of the player helicopter machine gun.
        int pivotX = player.machineGunXcoordinate;
        int pivotY = player.machineGunYcoordinate;
        
        int a = pivotX - mousePosition.x;
        int b = pivotY - mousePosition.y;
        double ab = (double)a / (double)b;
        double alfaAngleRadians = Math.atan(ab);

        if(mousePosition.y < pivotY) // Above the helicopter.
            alfaAngleRadians = RIGHT_ANGLE_RADIANS - alfaAngleRadians - RIGHT_ANGLE_RADIANS*2;
        else if (mousePosition.y > pivotY) // Under the helicopter.
            alfaAngleRadians = RIGHT_ANGLE_RADIANS - alfaAngleRadians;
        else
            alfaAngleRadians = 0;

        AffineTransform origXform = g2d.getTransform();
        AffineTransform newXform = (AffineTransform)(origXform.clone());

        newXform.rotate(alfaAngleRadians, mousePosition.x, mousePosition.y);
        g2d.setTransform(newXform);
        
        g2d.drawImage(mouseCursorImg, mousePosition.x, mousePosition.y - mouseCursorImg.getHeight()/2, null); // We substract half of the cursor image so that will be drawn in center of the y mouse coordinate.
        
        g2d.setTransform(origXform);
    }
    
    /**
     * Format given time into 00:00 format.
     * 
     * @param time Time that is in nanoseconds.
     * @return Time in 00:00 format.
     */
    private static String formatTime(long time){
            // Given time in seconds.
            int sec = (int)(time / Framework.milisecInNanosec / 1000);

            // Given time in minutes and seconds.
            int min = sec / 60;
            sec = sec - (min * 60);

            String minString, secString;

            if(min <= 9)
                minString = "0" + Integer.toString(min);
            else
                minString = "" + Integer.toString(min);

            if(sec <= 9)
                secString = "0" + Integer.toString(sec);
            else
                secString = "" + Integer.toString(sec);

            return minString + ":" + secString;
    }
    

    private void updateStage(long gameTime)
    {
        int xCoordinate = Framework.frameWidth + imgP51.getWidth() / 2;
        int yCoordinate = 100;
        double angle = 70;
        double rads = angle * Math.PI / 180;
        double speed = 4;
        double numEnemiesInWave = 5;

        double xIncrement = -speed * 15 * (float)Math.cos(rads + Math.PI/2);
        double yIncrement = -speed * 15 * (float)Math.sin(rads + Math.PI/2);

        /*
        if (background.yCoordinate == background.getPosition() + 00)
        {
            int xPos = Framework.frameWidth / 2;
            int yPosB;
            int yPosT;
            yPosB = -imgFlakpanzerBody.getHeight();
            yPosT = -imgFlakpanzerBody.getHeight() + 20;

            FlakpanzerTurret eh = new FlakpanzerTurret(xPos + 3, yPosT, 1, 1, imgFlakpanzerTurret,
                    27, 60, 1);
            Flakpanzer body = new Flakpanzer(xPos, yPosB, eh, 0, 1, 1, imgFlakpanzerBody,
                    33, 72, 1);
            // Add created enemy to the list of enemies.
            enemyList.add(body);
        }
        //*/
        ///*
        if (background.yCoordinate == background.getPosition() + 100)
        {
            for (int i = 0; i < numEnemiesInWave; i++) {
                P51 eh = new P51(xCoordinate + (int) xIncrement * i,
                        yCoordinate + (int) yIncrement * i, angle, speed, 1,
                        imgP51, 48, 43, 2);
                // Add created enemy to the list of enemies.
                enemyList.add(eh);
            }
        }

        if (background.yCoordinate == background.getPosition() + 200)
        {
            LockheedConstellation eh = new LockheedConstellation(imgLockheedConstellation.getWidth()/2,
                    -imgLockheedConstellation.getHeight(), 1,
                    imgLockheedConstellation, 163, 155, 2);
            // Add created enemy to the list of enemies.
            enemyList.add(eh);
        }

        if (background.yCoordinate == background.getPosition() + 300)
        {
            xCoordinate = -imgP51.getWidth() / 2;
            yCoordinate = 100;
            angle = -70;
            rads = angle * Math.PI / 180;
            xIncrement = -speed * 15 * (float)Math.cos(rads + Math.PI/2);
            yIncrement = -speed * 15 * (float)Math.sin(rads + Math.PI/2);

            for (int i = 0; i < numEnemiesInWave; i++) {
                P51 eh = new P51(xCoordinate + (int) xIncrement * i,
                        yCoordinate + (int) yIncrement * i, angle, speed, 2,
                        imgP51, 48, 43, 2);
                // Add created enemy to the list of enemies.
                enemyList.add(eh);
            }
        }

        ///
        if (background.yCoordinate == background.getPosition() + 400)
        {
            numEnemiesInWave = 7;
            xCoordinate = Framework.frameWidth + imgP51.getWidth() / 2;
            yCoordinate = 150;
            angle = 90;
            rads = angle * Math.PI / 180;
            xIncrement = -speed * 15 * (float)Math.cos(rads + Math.PI/2);
            yIncrement = -speed * 15 * (float)Math.sin(rads + Math.PI/2);
            for (int i = 0; i < numEnemiesInWave; i++) {
                P51 eh = new P51(xCoordinate + (int) xIncrement * i,
                        yCoordinate + (int) yIncrement * i, angle, speed, 3,
                        imgP51, 48, 43, 2);
                // Add created enemy to the list of enemies.
                enemyList.add(eh);
            }
        }

        if (background.yCoordinate == background.getPosition() + 500)
        {
            int xPos = Framework.frameWidth - imgB24.getWidth()/4 * 4;
            B24 eh = new B24(xPos, -imgB24.getHeight(), 1,
                    imgB24, 146, 93, 2);
            // Add created enemy to the list of enemies.
            enemyList.add(eh);
        }

        if (background.yCoordinate == background.getPosition() + 650)
        {
            int xPos = Framework.frameWidth/2 - imgLockheedConstellation.getWidth()/4;
            LockheedConstellation eh = new LockheedConstellation(xPos,
                    -imgLockheedConstellation.getHeight(), 1,
                    imgLockheedConstellation, 163, 155, 2);
            // Add created enemy to the list of enemies.
            enemyList.add(eh);
        }

        if (background.yCoordinate == background.getPosition() + 600)
        {
            B24 eh = new B24(imgB24.getWidth()/4, -imgB24.getHeight(), 1,
                    imgB24, 146, 93, 2);
            // Add created enemy to the list of enemies.
            enemyList.add(eh);
        }

        if (background.yCoordinate == background.getPosition() + 800)
        {
            numEnemiesInWave = 10;
            xCoordinate = Framework.frameWidth + imgP51.getWidth() / 2;
            yCoordinate = -100;
            angle = 60;
            rads = angle * Math.PI / 180;
            xIncrement = -speed * 15 * (float)Math.cos(rads + Math.PI/2);
            yIncrement = -speed * 15 * (float)Math.sin(rads + Math.PI/2);
            for (int i = 0; i < numEnemiesInWave; i++) {
                P51 eh = new P51(xCoordinate + (int) xIncrement * i,
                        yCoordinate + (int) yIncrement * i, angle, speed, 3,
                        imgP51, 48, 43, 2);
                // Add created enemy to the list of enemies.
                enemyList.add(eh);
            }
            xCoordinate = -imgP51.getWidth() / 2;
            angle = -60;
            rads = angle * Math.PI / 180;
            xIncrement = -speed * 15 * (float)Math.cos(rads + Math.PI/2);
            yIncrement = -speed * 15 * (float)Math.sin(rads + Math.PI/2);
            for (int i = 0; i < numEnemiesInWave; i++) {
                P51 eh = new P51(xCoordinate + (int) xIncrement * i,
                        yCoordinate + (int) yIncrement * i, angle, speed, 4,
                        imgP51, 48, 43, 2);
                // Add created enemy to the list of enemies.
                enemyList.add(eh);
            }
        }

        if (background.yCoordinate == background.getPosition() + 900)
        {
            YB35 eh = new YB35(Framework.frameWidth + imgYB35.getWidth()/2, 5 * imgYB35.getHeight(), 1,
                    imgYB35, 226, 70, 2);
            // Add created enemy to the list of enemies.
            enemyList.add(eh);
        }

        if (background.yCoordinate == background.getPosition() + 1400)
        {
            YB35 eh = new YB35(-imgYB35.getWidth()/2, (int) (3.5 * imgYB35.getHeight()), 2,
                    imgYB35, 226, 70, 2);
            // Add created enemy to the list of enemies.
            enemyList.add(eh);
        }

        if (background.yCoordinate == background.getPosition() + 2800)
        {
            Ecaep eh = new Ecaep(Framework.frameWidth/2 - imgB36.getWidth()/4, -imgB36.getHeight(), 1,
                    imgB36, 313, 227, 2);
            // Add created enemy to the list of enemies.
            enemyList.add(eh);
        }
        //*/

        //if (background.yCoordinate == background.getPosition() + 00)
        ///*
        if (background.yCoordinate == background.getPosition() + 3800)
            createEnemyAmbulatamuere(gameTime);
        //*/
        /*
        if (background.yCoordinate == background.getPosition() + 00)
            createEnemyTreadingPain(gameTime);
        //*/

        /*
        if (background.yCoordinate == background.getPosition() + 00)
            createEnemyAmbulatamuere(gameTime);
        //*/
    }
    
    
    
    /*
     * 
     * Methods for updating the game. 
     * 
     */
    
     
    /**
     * Check if player is alive. If not, set game over status.
     * 
     * @return True if player is alive, false otherwise.
     */
    private boolean isPlayerAlive()
    {
        if(player.health <= 0)
            return false;

        return true;
    }
    
    /**
     * Checks if the player is shooting with the machine gun and creates bullets if he shooting.
     * 
     * @param gameTime Game time.
     */
    private void isPlayerShooting(long gameTime)
    {
        if(player.isShooting(gameTime))
        {
            Shot.timeOfLastCreatedBullet = gameTime;
            player.numberOfAmmo--;

            Shot b;
            switch (player.powerLvl)
            {
                case 2:
                    b = new Shot(player.machineGunXcoordinate - 12 * Shot.bulletImg.getWidth()/8, player.machineGunYcoordinate);
                    bulletsList.add(b);
                    b = new Shot(player.machineGunXcoordinate + 4 * Shot.bulletImg.getWidth()/8, player.machineGunYcoordinate);
                    bulletsList.add(b);
                case 1:
                    b = new Shot(player.machineGunXcoordinate - 9 * Shot.bulletImg.getWidth()/8, player.machineGunYcoordinate);
                    bulletsList.add(b);
                    b = new Shot(player.machineGunXcoordinate + 1 * Shot.bulletImg.getWidth()/8, player.machineGunYcoordinate);
                    bulletsList.add(b);
                case 0:
                    b = new Shot(player.machineGunXcoordinate - 6 * Shot.bulletImg.getWidth()/8, player.machineGunYcoordinate);
                    bulletsList.add(b);
                    b = new Shot(player.machineGunXcoordinate - 2 * Shot.bulletImg.getWidth()/8, player.machineGunYcoordinate);
                    bulletsList.add(b);
            }

            /*
            clipLaser.stop();
            clipLaser.setFramePosition(0);
            clipLaser.start();
            */
        }
        if(player.isShootingSecondary(gameTime))
        {
            Missile.timeOfLastCreatedBullet = gameTime;
            player.numberOfAmmo--;

            Missile m;
            switch (player.powerLvl)
            {
                case 2:
                    m = new Missile(player.machineGunXcoordinate - 12 * Missile.missileImg.getWidth()/8,
                            player.machineGunYcoordinate + Missile.missileImg.getHeight() / 2, Missile.missileImg, 2);
                    secondaryList.add(m);
                    m = new Missile(player.machineGunXcoordinate + 9 * Missile.missileImg.getWidth()/8,
                            player.machineGunYcoordinate + Missile.missileImg.getHeight() / 2, Missile.missileImg, 2);
                    secondaryList.add(m);
                case 1:
                    m = new Missile(player.machineGunXcoordinate - 8 * Missile.missileImg.getWidth()/8,
                            player.machineGunYcoordinate, Missile.missileImg, 2);
                    secondaryList.add(m);
                    m = new Missile(player.machineGunXcoordinate + 5 * Missile.missileImg.getWidth()/8,
                            player.machineGunYcoordinate, Missile.missileImg, 2);
                    secondaryList.add(m);
            }
        }
    }

    /**
     * Checks if the enemy is shooting
     *
     * @param gameTime Game time, enemy enemy
     */
    private void isEnemyShooting(long gameTime, Enemy enemy, Player player)
    {
        if(enemy.isShooting(gameTime))
        {
            ArrayList<Bullet> bullets;
            bullets = enemy.createBullet(player, 0);
            Bullet.timeOfLastCreatedBullet = gameTime;
            //System.out.println(bullets.get(0));
            if(bullets != null)
                enemyBulletsList.addAll(bullets);
        }
    }

    /**
     * Checks if the player is fired the rocket and creates it if he did.
     * It also checks if player can fire the rocket.
     * 
     * @param gameTime Game time.
     */
    private void didPlayerFiredRocket(long gameTime)
    {
        if(player.isFiredRocket(gameTime))
        {
            Rocket.timeOfLastCreatedRocket = gameTime;
            player.numberOfRockets--;
            
            Rocket r = new Rocket();
            r.Initialize(player.rocketHolderXcoordinate, player.rocketHolderYcoordinate);
            rocketsList.add(r);
        }
    }
    
    /**
     * Creates the ambulatamuere if it's time.
     *
     * @param gameTime Game time.
     */
    private void createEnemyAmbulatamuere(long gameTime)
    {
        if(spawnAmbulatamuere)
        {
            spawnAmbulatamuere = false;
            int xCoordinate, yCoordinate, initX, initY;
            ///*
            initX = 200;
            initY = -600;
            //*/
            /*
            initX = 200;
            initY = 100;
            //*/
            xCoordinate = initX;
            yCoordinate = initY;
            ArrayList<AmbulatamuereDoubleTurret> doubleTurrets = new ArrayList<>();
            ArrayList<AmbulatamuereMachineGun> machineGuns = new ArrayList<>();
            AmbulatamuereDoubleTurret eh;
            AmbulatamuereMachineGun gun;

            AmbulatamuereClusterTurret clusterTurret;
            AmbulatamuereFoot dr;
            AmbulatamuereBody body;
            AmbulatamuereLimb limb;
            Ambulatamuere ambulatamuere;
            ArrayList<Drawable> appendages = new ArrayList<>();

            eh = new AmbulatamuereDoubleTurret(xCoordinate - 17, yCoordinate + 24,
                    imgAmbulatamuereDoubleTurret, 60, 60, 1, true, gameTime);
            //enemyList.add(eh);
            dr = new AmbulatamuereFoot(xCoordinate - 42, yCoordinate, eh, imgAmbulatamuereFoot, 84,
                    83, 1, true);
            enemyList.add(dr);
            limb = new AmbulatamuereLimb(xCoordinate + 18, yCoordinate + 52,
                    imgAmbulatamuereLimbLeft, 130, 130, 1, true);
            appendages.add(limb);

            xCoordinate = initX + 300;
            yCoordinate = initY - 100;
            eh = new AmbulatamuereDoubleTurret(xCoordinate - 17, yCoordinate + 24,
                    imgAmbulatamuereDoubleTurret, 60, 60, 1, true, gameTime);
            //enemyList.add(eh);
            dr = new AmbulatamuereFoot(xCoordinate - 42, yCoordinate, eh, imgAmbulatamuereFoot, 84,
                    83, 1, true);
            enemyList.add(dr);
            //appendages.add(dr);
            limb = new AmbulatamuereLimb(xCoordinate - 18, yCoordinate + 52,
                    imgAmbulatamuereLimbLeft, 130, 130, 1, true);
            appendages.add(limb);

            xCoordinate = initX;
            yCoordinate = initY + 330;
            eh = new AmbulatamuereDoubleTurret(xCoordinate - 17, yCoordinate + 24,
                    imgAmbulatamuereDoubleTurret, 60, 60, 1, false, gameTime);
            //enemyList.add(eh);
            dr = new AmbulatamuereFoot(xCoordinate - 42, yCoordinate, eh, imgAmbulatamuereFoot, 84,
                    83, 1, false);
            enemyList.add(dr);
            //appendages.add(dr);
            limb = new AmbulatamuereLimb(xCoordinate + 18, yCoordinate + 32,
                    imgAmbulatamuereLimbLeft, 130, 130, 1, false);
            appendages.add(limb);

            xCoordinate = initX + 300;
            yCoordinate = initY + 430;
            eh = new AmbulatamuereDoubleTurret(xCoordinate - 17, yCoordinate + 24,
                    imgAmbulatamuereDoubleTurret, 60, 60, 1, false, gameTime);
            //enemyList.add(eh);
            dr = new AmbulatamuereFoot(xCoordinate - 42, yCoordinate, eh, imgAmbulatamuereFoot, 84,
                    83, 1, false);
            enemyList.add(dr);
            //appendages.add(dr);
            limb = new AmbulatamuereLimb(xCoordinate - 18, yCoordinate + 32,
                    imgAmbulatamuereLimbLeft, 130, 130, 1, false);
            appendages.add(limb);

            xCoordinate = initX;
            yCoordinate = initY;
            body = new AmbulatamuereBody(xCoordinate - 17, yCoordinate + 37,
                    imgAmbulatamuereBody, 333, 333, 1);
            ambulatamuere = new Ambulatamuere(appendages, body);
            drawableList.add(ambulatamuere);
            gun = new AmbulatamuereMachineGun(xCoordinate + 55, yCoordinate + 281,
                    imgAmbulatamuereMachineGun, 18, 38, 1, 1, gameTime);
            enemyList.add(gun);
            gun = new AmbulatamuereMachineGun(xCoordinate + 18, yCoordinate + 194,
                    imgAmbulatamuereMachineGun, 18, 38, 1, 2, gameTime);
            enemyList.add(gun);
            gun = new AmbulatamuereMachineGun(xCoordinate + 55, yCoordinate + 109,
                    imgAmbulatamuereMachineGun, 18, 38, 1, 3, gameTime);
            enemyList.add(gun);
            gun = new AmbulatamuereMachineGun(xCoordinate + 141, yCoordinate + 71,
                    imgAmbulatamuereMachineGun, 18, 38, 1, 4, gameTime);
            enemyList.add(gun);
            gun = new AmbulatamuereMachineGun(xCoordinate + 225, yCoordinate + 109,
                    imgAmbulatamuereMachineGun, 18, 38, 1, 3, gameTime);
            enemyList.add(gun);
            gun = new AmbulatamuereMachineGun(xCoordinate + 263, yCoordinate + 194,
                    imgAmbulatamuereMachineGun, 18, 38, 1, 2, gameTime);
            enemyList.add(gun);
            gun = new AmbulatamuereMachineGun(xCoordinate + 225, yCoordinate + 281,
                    imgAmbulatamuereMachineGun, 18, 38, 1, 1, gameTime);
            enemyList.add(gun);
            clusterTurret = new AmbulatamuereClusterTurret(xCoordinate + 93, yCoordinate + 153,
                    imgAmbulatamuereClusterTurret, 113, 270, 1, gameTime);
            enemyList.add(clusterTurret);

            // Sets new time for last created enemy.
            spawn = false;


            playBossMusic = true;
            //music = new Music("bgMusic", aisBoss, lineBoss);
            //lineBoss.start();

            /*
            while (total < totalToRead && !stopped){
                numBytesRead = stream.read(myData, 0, numBytesToRead);
                if (numBytesRead == -1) break;
                total += numBytesRead;
                lineBoss.write(myData, 0, numBytesRead);
            }
            */
        }
    }

    /**
     * Creates the treading pain if it's time.
     *
     * @param gameTime Game time.
     */
    private void createEnemyTreadingPain(long gameTime)
    {
        if(spawnTreadingPain)
        {
            spawnTreadingPain = false;
            int xCoordinate, yCoordinate, initX, initY;
            ///*
            initX = 250;
            initY = -37;
            //*/
            /*
            initX = 200;
            initY = 100;
            //*/
            xCoordinate = initX;
            yCoordinate = initY;
            ArrayList<TreadingPainDoubleTurret> doubleTurrets = new ArrayList<>();
            ArrayList<TreadingPainQuadTurret> quadTurrets = new ArrayList<>();
            ArrayList<TreadingPainMachineGun> machineGuns = new ArrayList<>();
            TreadingPainDoubleTurret eh;
            TreadingPainQuadTurret ehQ;
            TreadingPainMachineGun gun;

            TreadingPainTripleTurret tripleTurret;
            TreadingPainBody body;
            TreadingPain treadingPain;

            ///*
            ehQ = new TreadingPainQuadTurret(xCoordinate + 10, yCoordinate + 180,
                    imgTreadingPainQuadTurret, 50, 56, 1, gameTime,
                    QuadTurretType.TurretTypes.LEFT, QuadTurretType.TurretTypes.TOP);
            enemyList.add(ehQ);
            ehQ = new TreadingPainQuadTurret(xCoordinate + 160, yCoordinate + 180,
                    imgTreadingPainQuadTurret, 50, 56, 1, gameTime,
                    QuadTurretType.TurretTypes.RIGHT, QuadTurretType.TurretTypes.TOP);
            enemyList.add(ehQ);
            //*/

            ///*
            eh = new TreadingPainDoubleTurret(xCoordinate + 5, yCoordinate + 90,
                    imgTreadingPainDoubleTurret, 42, 94, 1, gameTime);
            enemyList.add(eh);

            eh = new TreadingPainDoubleTurret(xCoordinate + 170, yCoordinate + 90,
                    imgTreadingPainDoubleTurret, 42, 94, 1, gameTime);
            enemyList.add(eh);
            //*/

            xCoordinate = initX;
            yCoordinate = initY + 330;

            ///*
            ehQ = new TreadingPainQuadTurret(xCoordinate + 10, yCoordinate + 110,
                    imgTreadingPainQuadTurret, 50, 56, 1, gameTime,
                    QuadTurretType.TurretTypes.LEFT, QuadTurretType.TurretTypes.BOTTOM);
            enemyList.add(ehQ);
            ehQ = new TreadingPainQuadTurret(xCoordinate + 160, yCoordinate + 110,
                    imgTreadingPainQuadTurret, 50, 56, 1, gameTime,
                    QuadTurretType.TurretTypes.RIGHT, QuadTurretType.TurretTypes.BOTTOM);
            enemyList.add(ehQ);
            //*/

            ///*
            eh = new TreadingPainDoubleTurret(xCoordinate + 5, yCoordinate + 180,
                    imgTreadingPainDoubleTurret, 42, 94, 1, gameTime);
            enemyList.add(eh);

            eh = new TreadingPainDoubleTurret(xCoordinate + 170, yCoordinate + 180,
                    imgTreadingPainDoubleTurret, 42, 94, 1, gameTime);
            enemyList.add(eh);
            //*/


            xCoordinate = initX;
            yCoordinate = initY;
            body = new TreadingPainBody(xCoordinate - 17, yCoordinate + 37,
                    imgTreadingPainBody, 255, 584, 1);
            treadingPain = new TreadingPain(body);
            drawableList.add(treadingPain);
            ///*
            tripleTurret = new TreadingPainTripleTurret(xCoordinate + 37, yCoordinate + 250,
                    imgTreadingPainTripleTurret, 144, 262, 1, gameTime);
            enemyList.add(tripleTurret);
            //*/


            /*
            gun = new TreadingPainMachineGun(xCoordinate + 55, yCoordinate + 281,
                    imgTreadingPainMachineGun, 18, 38, 1, 1, gameTime);
            enemyList.add(gun);
            */

            // Sets new time for last created enemy.
            spawn = false;


            playBossMusic = true;
            //music = new Music("bgMusic", aisBoss, lineBoss);
            //lineBoss.start();

            /*
            while (total < totalToRead && !stopped){
                numBytesRead = stream.read(myData, 0, numBytesToRead);
                if (numBytesRead == -1) break;
                total += numBytesRead;
                lineBoss.write(myData, 0, numBytesRead);
            }
            */
        }
    }

    private void updateDrawables(long gameTime)
    {
        for (int i = 0; i < drawableList.size(); i++)
        {
            Drawable dr = drawableList.get(i);
            dr.Update(gameTime);

            if (dr instanceof PowerUp)
            {
                Rectangle playerRectangle = new Rectangle(player.xCoordinate, player.yCoordinate,
                        player.playerImg.getWidth()/2, player.playerImg.getHeight());
                Rectangle drawableRectangle = dr.getHitbox();
                if (playerRectangle.intersects(drawableRectangle))
                {
                    player.powerLvl++;
                    drawableList.remove(i);
                }
            }
        }
        background.Update(gameTime);
    }

    /**
     * Updates all enemies.
     * Move the helicopter and checks if he left the screen.
     * Updates helicopter animations.
     * Checks if enemy was destroyed.
     * Checks if any enemy collision with player.
     */
    private void updateEnemies(long gameTime)
    {
        for(int i = 0; i < enemyList.size(); i++)
        {
            Enemy eh = enemyList.get(i);
            isEnemyShooting(gameTime, eh, player);
            eh.Update(player, gameTime);

            // Crashed with player?
            Rectangle playerRectangle = new Rectangle(player.xCoordinate, player.yCoordinate, player.playerImg.getWidth(), player.playerImg.getHeight());
            Rectangle enemyRectangle = eh.getEnemyHitbox();
            /*
            if(playerRectangle.intersects(enemyRectangle)){
                player.health = 0;
                
                // Remove enemy from the list.
                enemyList.remove(i);
                
                // Add explosion of player.
                for(int exNum = 0; exNum < 3; exNum++){
                    Animation expAnim = new Animation(explosionAnimImg, 134, 134, 12, 45, false, player.xCoordinate + exNum*60, player.yCoordinate - random.nextInt(100), exNum * 200 +random.nextInt(100));
                    explosionsList.add(expAnim);
                }
                // Add explosion of enemy.
                for(int exNum = 0; exNum < 3; exNum++){
                    Animation expAnim = new Animation(explosionAnimImg, 134, 134, 12, 45, false, eh.xCoordinate + exNum*60, eh.yCoordinate - random.nextInt(100), exNum * 200 +random.nextInt(100));
                    explosionsList.add(expAnim);
                }
                
                // Because player crashed with enemy the game will be over so we don't need to check other enemies.
                break;
            }
            */

            // Check health.
            if(eh.health <= 0){
                //eh.destroy()
                // Add explosion of enemy.
                ///*
                Animation expAnim = new Animation(explosionAnimImg, 134, 134, 12,
                        45, false,
                        eh.xCoordinate + eh.getRotateX() - explosionAnimImg.getWidth()/12/2,
                        eh.yCoordinate + eh.getRotateY() - explosionAnimImg.getHeight()/2, 0);
                explosionsList.add(expAnim);

                clipExplosion.stop();
                clipExplosion.setFramePosition(0);
                clipExplosion.start();

                // Increase the destroyed enemies counter.
                destroyedEnemies++;

                // Remove enemy from the list.
                if (eh.destroy()) {
                    enemyList.remove(i);
                }
                else
                {
                    enemyList.get(i).removal();
                }

                // if a b-24 was destroyed, give a powerup
                if (eh instanceof LockheedConstellation)
                {
                    LockheedConstellation lockheedConstellation = (LockheedConstellation) eh;
                    PowerUp powerUp = lockheedConstellation.createPowerUp();
                    drawableList.add(powerUp);
                }
                //*/

                // Fighter was destroyed so we can move to next enemy.
                continue;
            }
            
            // If the current enemy is left the screen we remove him from the list and update the runAwayEnemies variable.
            if(eh.isLeftScreen())
            {
                enemyList.remove(i);
                runAwayEnemies++;
            }
        }
    }
    
    /**
     * Update bullets. 
     * It moves bullets.
     * Checks if the bullet is left the screen.
     * Checks if any bullets is hit any enemy.
     */
    private void updateBullets()
    {
        for(int i = 0; i < bulletsList.size(); i++)
        {
            Shot shot = bulletsList.get(i);
            
            // Move the shot.
            shot.Update();
            
            // Is left the screen?
            if(shot.isItLeftScreen()){
                bulletsList.remove(i);
                // Shot have left the screen so we removed it from the list and now we can continue to the next shot.
                continue;
            }
            
            // Did hit any enemy?
            // Rectangle of the shot image.
            Rectangle bulletRectangle = new Rectangle((int) shot.xCoordinate, (int) shot.yCoordinate, Shot.bulletImg.getWidth(), Shot.bulletImg.getHeight());
            // Go through all enemies.
            for(int j = 0; j < enemyList.size(); j++)
            {
                Enemy eh = enemyList.get(j);

                // Current enemy rectangle.
                Rectangle enemyRectangle = eh.getEnemyHitbox();

                // Is current shot over current enemy?
                if(bulletRectangle.intersects(enemyRectangle))
                {
                    eh.updateDmg(Shot.damagePower);
                    // Shot hit the enemy so we reduce his health.
                    //eh.health -= Shot.damagePower;
                    
                    // Shot was also destroyed so we remove it.
                    bulletsList.remove(i);

                    clipLaser.stop();
                    clipLaser.setFramePosition(0);
                    clipLaser.start();

                    // That shot hit enemy so we don't need to check other enemies.
                    break;
                }
            }
        }
    }


    /**
     * Update secondary weapons.
     * It moves secondary weapons.
     * Checks if the secondary weapon is left the screen.
     * Checks if any secondary weapon is hit any enemy.
     */
    private void updateSecondary()
    {
        for(int i = 0; i < secondaryList.size(); i++)
        {
            Missile missile = secondaryList.get(i);

            // Move the shot.
            missile.Update();

            // Is left the screen?
            if(missile.isItLeftScreen()){
                secondaryList.remove(i);
                // Shot have left the screen so we removed it from the list and now we can continue to the next shot.
                continue;
            }

            // Did hit any enemy?
            // Rectangle of the shot image.
            Rectangle missleRectangle = new Rectangle((int) missile.xCoordinate, (int) missile.yCoordinate,
                    Missile.missileImg.getWidth(), Missile.missileImg.getHeight());
            // Go through all enemies.
            for(int j = 0; j < enemyList.size(); j++)
            {
                Enemy eh = enemyList.get(j);

                // Current enemy rectangle.
                Rectangle enemyRectangle = eh.getEnemyHitbox();

                // Is current shot over current enemy?
                if(missleRectangle.intersects(enemyRectangle))
                {
                    // Shot hit the enemy so we reduce his health.
                    eh.health -= Missile.damagePower;

                    // Shot was also destroyed so we remove it.
                    secondaryList.remove(i);

                    clipLaser.stop();
                    clipLaser.setFramePosition(0);
                    clipLaser.start();

                    // That shot hit enemy so we don't need to check other enemies.
                    break;
                }
            }
        }
    }

    /**
     * Update enemy bullets.
     * It moves bullets.
     * Checks if the bullet is left the screen.
     * Checks if any bullets is hit any enemy.
     */
    private void updateEnemyBullets(long gameTime)
    {
        for(int i = 0; i < enemyBulletsList.size(); i++)
        {
            if (enemyBulletsList.get(i) instanceof BulletStarburst)
            {
                BulletStarburst bulletStarburst = (BulletStarburst) enemyBulletsList.get(i);
                //bulletStarburst.Update(gameTime);
                if (bulletStarburst.isExploded())
                {
                    ArrayList<Bullet> bullets;
                    bullets = bulletStarburst.explode();
                    enemyBulletsList.remove(i);
                    enemyBulletsList.addAll(bullets);
                }
            }
            if (enemyBulletsList.get(i) instanceof BulletStarburstShell)
            {
                BulletStarburstShell bulletStarburstShell = (BulletStarburstShell) enemyBulletsList.get(i);
                //bulletStarburstShell.Update(gameTime);
                if (bulletStarburstShell.isExploded())
                {
                    ArrayList<Bullet> bullets;
                    bullets = bulletStarburstShell.explode();
                    enemyBulletsList.addAll(bullets);
                }
            }
            Bullet bullet = enemyBulletsList.get(i);

            // Move the shot.
            if (bullet != null)
                bullet.Update(gameTime);
            else
                continue;

            // Is left the screen?
            if(bullet.isLeftScreen()){
                enemyBulletsList.remove(i);
                // Shot have left the screen so we removed it from the list and now we can continue to the next shot.
                continue;
            }

            // Did hit player
            // Rectangle of the shot image.
            /*
            Rectangle bulletRectangle = new Rectangle((int) bullet.xCoordinate + Shot.bulletImg.getWidth() / 4,
                    (int) bullet.yCoordinate + Shot.bulletImg.getHeight() / 4, Shot.bulletImg.getWidth() / 2,
                    Shot.bulletImg.getHeight() / 2);
            */
            Rectangle bulletRectangle = bullet.getHitbox();
            // Get player hitbox
            Rectangle playerRectangle = new Rectangle(player.xCoordinate + player.playerImg.getWidth()/8,
                    player.yCoordinate + player.playerImg.getHeight() * 3 / 8, player.playerImg.getWidth() * 2 / 8,
                    player.playerImg.getHeight()/4);

            // Is current shot over current enemy?
            if(bulletRectangle.intersects(playerRectangle))
            {
                // Shot hit the enemy so we reduce his health.
                player.health -= bullet.getDamagePower();

                // Shot was also destroyed so we remove it.
                enemyBulletsList.remove(i);
            }
        }
    }


    /**
     * Update rockets. 
     * It moves rocket and add smoke behind it.
     * Checks if the rocket is left the screen.
     * Checks if any rocket is hit any enemy.
     * 
     * @param gameTime Game time.
     */
    private void updateRockets(long gameTime)
    {
        for(int i = 0; i < rocketsList.size(); i++)
        {
            Rocket rocket = rocketsList.get(i);
            
            // Moves the rocket.
            rocket.Update();
            
            // Checks if it is left the screen.
            if(rocket.isItLeftScreen())
            {
                rocketsList.remove(i);
                // Rocket left the screen so we removed it from the list and now we can continue to the next rocket.
                continue;
            }
            
            // Creates a rocket smoke.
            RocketSmoke rs = new RocketSmoke();
            int xCoordinate = rocket.xCoordinate - RocketSmoke.smokeImg.getWidth(); // Subtract the size of the rocket smoke image (rocketSmokeImg.getWidth()) so that smoke isn't drawn under/behind the image of rocket.
            int yCoordinte = rocket.yCoordinate - 5 + random.nextInt(6); // Subtract 5 so that smok will be at the middle of the rocket on y coordinate. We rendomly add a number between 0 and 6 so that the smoke line isn't straight line.
            rs.Initialize(xCoordinate, yCoordinte, gameTime, rocket.currentSmokeLifeTime);
            rocketSmokeList.add(rs);
            
            // Because the rocket is fast we get empty space between smokes so we need to add more smoke. 
            // The higher is the speed of rockets, the bigger are empty spaces.
            int smokePositionX = 5 + random.nextInt(8); // We will draw this smoke a little bit ahead of the one we draw before.
            rs = new RocketSmoke();
            xCoordinate = rocket.xCoordinate - RocketSmoke.smokeImg.getWidth() + smokePositionX; // Here we need to add so that the smoke will not be on the same x coordinate as previous smoke. First we need to add 5 because we add random number from 0 to 8 and if the random number is 0 it would be on the same coordinate as smoke before.
            yCoordinte = rocket.yCoordinate - 5 + random.nextInt(6); // Subtract 5 so that smok will be at the middle of the rocket on y coordinate. We rendomly add a number between 0 and 6 so that the smoke line isn't straight line.
            rs.Initialize(xCoordinate, yCoordinte, gameTime, rocket.currentSmokeLifeTime);
            rocketSmokeList.add(rs);
            
            // Increase the life time for the next piece of rocket smoke.
            rocket.currentSmokeLifeTime *= 1.02;
            
            // Checks if current rocket hit any enemy.
            if( checkIfRocketHitEnemy(rocket) )
                // Rocket was also destroyed so we remove it.
                rocketsList.remove(i);
        }
    }
    
    /**
     * Checks if the given rocket is hit any of enemy helicopters.
     * 
     * @param rocket Rocket to check.
     * @return True if it hit any of enemy helicopters, false otherwise.
     */
    private boolean checkIfRocketHitEnemy(Rocket rocket)
    {
        boolean didItHitEnemy = false;
        
        // Current rocket rectangle. // I inserted number 2 insted of rocketImg.getWidth() because I wanted that rocket 
        // is over helicopter when collision is detected, because actual image of helicopter isn't a rectangle shape. (We could calculate/make 3 areas where helicopter can be hit and checks these areas, but this is easier.)
        Rectangle rocketRectangle = new Rectangle(rocket.xCoordinate, rocket.yCoordinate, 2, Rocket.rocketImg.getHeight());
        
        // Go through all enemies.
        for(int j = 0; j < enemyList.size(); j++)
        {
            Enemy eh = enemyList.get(j);

            // Current enemy rectangle.
            Rectangle enemyRectangle = eh.getEnemyHitbox();

            // Is current rocket over current enemy?
            if(rocketRectangle.intersects(enemyRectangle))
            {
                didItHitEnemy = true;
                
                // Rocket hit the enemy so we reduce his health.
                eh.health -= Rocket.damagePower;
                
                // Rocket hit enemy so we don't need to check other enemies.
                break;
            }
        }
        
        return didItHitEnemy;
    }
    
    /**
     * Updates smoke of all the rockets.
     * If the life time of the smoke is over then we delete it from list.
     * It also changes a transparency of a smoke image, so that smoke slowly disappear.
     * 
     * @param gameTime Game time.
     */
    private void updateRocketSmoke(long gameTime)
    {
        for(int i = 0; i < rocketSmokeList.size(); i++)
        {
            RocketSmoke rs = rocketSmokeList.get(i);
            
            // Is it time to remove the smoke.
            if(rs.didSmokeDisapper(gameTime))
                rocketSmokeList.remove(i);
            
            // Set new transparency of rocket smoke image.
            rs.updateTransparency(gameTime);
        }
    }
    
    /**
     * Updates all the animations of an explosion and remove the animation when is over.
     */
    private void updateExplosions()
    {
        for(int i = 0; i < explosionsList.size(); i++)
        {
            // If the animation is over we remove it from the list.
            if(!explosionsList.get(i).active)
                explosionsList.remove(i);
        }
    }
    
    /**
     * It limits the distance of the mouse from the player.
     * 
     * @param mousePosition Position of the mouse.
     */
    private void limitMousePosition(Point mousePosition)
    {
        // Max distance from the player on y coordinate above player helicopter.
        int maxYcoordinateDistanceFromPlayer_top = 30;
        // Max distance from the player on y coordinate under player helicopter.
        int maxYcoordinateDistanceFromPlayer_bottom = 120;
        
        // Mouse cursor will always be the same distance from the player helicopter machine gun on the x coordinate.
        int mouseXcoordinate = player.machineGunXcoordinate + 250;
        
        // Here we will limit the distance of mouse cursor on the y coordinate.
        int mouseYcoordinate = mousePosition.y;
        if(mousePosition.y < player.machineGunYcoordinate){ // Above the helicopter machine gun.
            if(mousePosition.y < player.machineGunYcoordinate - maxYcoordinateDistanceFromPlayer_top)
                mouseYcoordinate = player.machineGunYcoordinate - maxYcoordinateDistanceFromPlayer_top;
        } else { // Under the helicopter.
            if(mousePosition.y > player.machineGunYcoordinate + maxYcoordinateDistanceFromPlayer_bottom)
                mouseYcoordinate = player.machineGunYcoordinate + maxYcoordinateDistanceFromPlayer_bottom;
        }
        
        // We move mouse on y coordinate with helicopter. That makes shooting easier.
        mouseYcoordinate += player.movingYspeed;
        
        // Move the mouse.
        robot.mouseMove(mouseXcoordinate, mouseYcoordinate);
    }
}
