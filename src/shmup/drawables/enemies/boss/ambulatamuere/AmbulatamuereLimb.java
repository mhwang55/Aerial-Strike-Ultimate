package shmup.drawables.enemies.boss.ambulatamuere;

import shmup.drawables.Drawable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;

public class AmbulatamuereLimb extends Drawable {
    private boolean isUpperLimb;
    private boolean hitBottom = false;
    private double radians;
    private double angle;

    public AmbulatamuereLimb(int xCoordinate, int yCoordinate, BufferedImage enemyImg, int frameWidth, int frameHeight, int noFrames, boolean isUpperLimb)
    {
        super(xCoordinate, yCoordinate, enemyImg, frameWidth, frameHeight, noFrames);
        this.isUpperLimb = isUpperLimb;
        Initialize(xCoordinate, yCoordinate);
    }

    /**
     * Initialize enemy bomber
     *
     * @param xCoordinate Starting x coordinate of fighter
     * @param yCoordinate Starting y coordinate of fighter
     */
    public void Initialize(int xCoordinate, int yCoordinate)
    {
        // Sets enemy position.
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
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
    public void Update(long gameTime) { int x = 0; }

    @Override
    public boolean isLeftScreen() {
        return false;
    }

    public void Update(long gameTime, int bodyXCoord, int bodyYCoord) {
        this.radians = Math.atan2(this.yCoordinate - (bodyYCoord + 167), this.xCoordinate - (bodyXCoord + 167));
        this.angle = radians - Math.PI * 5 / 4;
        if (isUpperLimb) {
            if (yCoordinate < 149 + 52 && !hitBottom) {
                ySpeed = 1;
            }
            else if (yCoordinate >= 149 + 52 && !hitBottom) {
                hitBottom = true;
            }
            else if (yCoordinate >= 3 + 52 && hitBottom) {
                ySpeed = -3;
            }
            else
                hitBottom = false;
        }
        else
        {
            if (yCoordinate < 579 + 32 && !hitBottom) {
                ySpeed = 1;
            }
            else if (yCoordinate >= 579 + 32 && !hitBottom) {
                hitBottom = true;
            }
            else if (yCoordinate >= 433 + 32 && hitBottom) {
                ySpeed = -3;
            }
            else
                hitBottom = false;
        }

        yCoordinate += ySpeed;
        objectAnim.changeCoordinates(xCoordinate, yCoordinate);
    }
    @Override
    public void Draw(Graphics2D g2d) {
        double rotationRequired = angle;
        double locationX = 0;
        double locationY = 0;

        AffineTransform backup = g2d.getTransform();
        AffineTransform trans = new AffineTransform();
        trans.rotate( rotationRequired, locationX + xCoordinate, locationY + yCoordinate); // the points to rotate around

        g2d.transform( trans );
        g2d.drawImage( objectImg, null, (int) xCoordinate, (int) yCoordinate );  // the actual location of the sprite

        g2d.setTransform( backup ); // restore previous transform
    }
}
