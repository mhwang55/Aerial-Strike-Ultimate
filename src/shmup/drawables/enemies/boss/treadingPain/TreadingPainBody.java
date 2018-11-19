package shmup.drawables.enemies.boss.treadingPain;

import shmup.drawables.Drawable;

import java.awt.image.BufferedImage;

public class TreadingPainBody extends Drawable {
    // For creating new enemies.
    public boolean spawn = false;
    private boolean isShooting = false;
    private double time;
    private int bulletNum = 0;
    private boolean hitBottom = false;

    public TreadingPainBody(int xCoordinate, int yCoordinate, BufferedImage enemyImg, int frameWidth, int frameHeight, int noFrames)
    {
        super(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
        Initialize(xCoordinate, yCoordinate);
    }

    /**
     * Initialize ambulatamuere body
     *
     * @param xCoordinate Starting x coordinate of body
     * @param yCoordinate Starting y coordinate of body
     */
    public void Initialize(int xCoordinate, int yCoordinate)
    {
        // Sets enemy position.
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;

        time = System.nanoTime();
    }

    @Override
    public void restartDrawable() {
        int x = 0;
    }

    //@Override
    public void Update() {
        int x = 0;
    }

    @Override
    public boolean isLeftScreen() {
        return false;
    }

    public void Update(long gameTime) {
        ySpeed = 0;
        /*
        if (yCoordinate < 149 + 37 && !hitBottom) {
            ySpeed = 1;
        }
        else if (yCoordinate >= 149 + 37 && !hitBottom) {
            hitBottom = true;
        }
        else if (yCoordinate >= 101 + 37 && hitBottom) {
            ySpeed = -1;
        }
        else
            hitBottom = false;
        */

        yCoordinate += ySpeed;
        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
    }
}
