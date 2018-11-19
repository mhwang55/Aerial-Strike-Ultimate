package shmup.drawables;

import shmup.drawables.Drawable;

import java.awt.image.BufferedImage;

public class Background extends Drawable {

    private int startPosition;

    public Background(int xCoordinate, int yCoordinate, BufferedImage enemyImg, int frameWidth, int frameHeight, int noFrames)
    {
        super(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
        ySpeed = 1;
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
        this.yCoordinate = startPosition = yCoordinate;
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
        yCoordinate += ySpeed;
        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
    }

    /**
     * returns start position of background
     */
    public int getPosition()
    {
        return this.startPosition;
    }
}
