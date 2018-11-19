package shmup.drawables.enemies;

import shmup.Framework;
import shmup.player.Player;
import shmup.drawables.Drawable;
import shmup.drawables.bullets.Bullet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Enemy extends Drawable{
    // For creating new enemies.
    private boolean spawn = false;
    public static final long timeBetweenNewEnemiesInit = Framework.secInNanosec * 2;
    public static long timeBetweenNewEnemies = timeBetweenNewEnemiesInit;
    public static long timeOfLastCreatedEnemy = 0;
    protected double radians;
    protected double angle;
    protected boolean enemyRotate = false;
    protected double enemyAngle;
    protected int type;

    protected boolean enteredScreen = false;

    // internal clock that enemy uses to keep track of when to do stuff
    protected int timer = 0;

    // internal clock that enemy uses to keep track of when to fire
    protected int bulletNum = 0;

    // Health of the enemy
    public int health;

    // whether to destroy the enemy when hp reaches 0
    public boolean destroy = true;

    /**
     * Initialize enemy
     *
     * @param xCoordinate Starting x coordinate of enemy
     * @param yCoordinate Starting y coordinate of enemy
     */
    public Enemy (int xCoordinate, int yCoordinate, BufferedImage enemyImg, int frameWidth, int frameHeight, int noFrames)
    {
        super(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
        // Sets enemy position.
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.hitbox = new Rectangle(-10, -10, 1,1);
        setRotatePoint();
    }

    /**
     * It sets speed and time between enemies to the initial properties.
     */
    public void restartDrawable(){
        Enemy.timeBetweenNewEnemies = timeBetweenNewEnemiesInit;
        Enemy.timeOfLastCreatedEnemy = 0;
    }

    public abstract boolean isShooting(long gameTime);

    public ArrayList<Bullet> createBullet(Player player, double angleAdd){
        return null;
    }

    public void setRotatePoint()
    {
        locationX = objectImg.getWidth()/noFrames/2;
        locationY = objectImg.getHeight();
    }


    protected boolean isEnteredScreen() {
        if (xCoordinate + objectImg.getWidth() / noFrames > 0 && xCoordinate < Framework.frameWidth &&
                yCoordinate + objectImg.getHeight() > 0 && yCoordinate < Framework.frameHeight)
            enteredScreen = true;
        return enteredScreen;
    }

    /**
     * Checks if the enemy is left the screen.
     *
     * @return true if the enemy is left the screen, false otherwise.
     */
    public boolean isLeftScreen()
    {
        return isEnteredScreen() && !(xCoordinate + objectImg.getWidth() / noFrames >= 0 && xCoordinate <= Framework.frameWidth &&
                yCoordinate + objectImg.getHeight() >= 0 && yCoordinate <= Framework.frameHeight);
    }


    /**
     * Updates position of enemy
     */
    public void Update()
    {
        // Move enemy on y coordinate.
        yCoordinate += ySpeed;

        // Moves enemy animation
        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
        hitbox = new Rectangle((int) xCoordinate, (int) yCoordinate, objectImg.getWidth(), objectImg.getHeight());
    }

    /**
     * Updates position of enemy
     */
    public void Update(long gameTime)
    {
        isShooting(gameTime);
        // Move enemy on y coordinate.
        yCoordinate += ySpeed;

        // Moves enemy animation
        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
    }

    public void Update(Player player, long gameTime) {
        this.radians = Math.atan2(this.yCoordinate - player.xCoordinate, this.xCoordinate - player.yCoordinate);
        this.angle = radians + Math.PI/2;
        isShooting(gameTime);

        yCoordinate += ySpeed;
        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
    }

    public BufferedImage getEnemyImg(){
        return objectImg;
    }

    public Rectangle getEnemyHitbox(){
        return hitbox;
    }

    public int getNoFrames() {return noFrames; }

    public void updateDmg(int dmg)
    {
        this.health -= dmg;
    }

    public boolean destroy()
    {
        return destroy;
    }

    public void removal()
    {
        ;
    }
    /*
    public boolean destroy()
    {
        //
        Animation expAnim = new Animation(explosionAnimImg, 134, 134, 12,
                        45, false,
                        eh.xCoordinate + eh.getRotateX() - explosionAnimImg.getWidth()/12/2,
                        eh.yCoordinate + eh.getRotateY() - explosionAnimImg.getHeight()/2, 0);
        explosionsList.add(expAnim);

        clipExplosion.stop();
        clipExplosion.setFramePosition(0);
        clipExplosion.start();
    }
    */
}
