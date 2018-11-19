package shmup;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Moving background image.
 * Move the image left or right in a loop.
 */

public class MovingBackground {

    // Image of background
    private BufferedImage image;

    // Speed of background
    private double speed;

    // Positions of background
    private double yPositions[];
    private int xPosition;


    /**
     * Initialize object for moving image.
     * 
     * @param image Image that will be moving.
     * @param speed How fast and into which direction should the image move? If number is negative, image will move into left. If you use decimal number, in some cases image can cover one over another at the end point, or could be spaces between images, so try another number.
     * @param yPosition y coordinate of the image.
     */
    //public void Initialize(BufferedImage image, double speed, int xPosition)
    public void Initialize(BufferedImage image, double speed, int xPosition)
    {
        this.image = image;
        this.speed = speed;

        this.xPosition = xPosition;

        // We divide frame size with image size do that we get how many times we need to draw image to screen.
        int numberOfPositions = (Framework.frameHeight / this.image.getHeight()) + 2; // We need to add 2 so that we don't get blank spaces between images.
        yPositions = new double[numberOfPositions];

        // Set y coordinate for each image that we need to draw.
        for (int i = 0; i < yPositions.length; i++)
        {
            yPositions[i] = i * image.getHeight();
        }
    }


    /**
     * Moves images.
     */
    private void Update()
    {
        for (int i = 0; i < yPositions.length; i++)
        {
            // Move image
            yPositions[i] += speed;

            // If image moving down
            if (speed < 0)
            {
                // If image is out of the screen, it moves it to the end of line of images.
                if (yPositions[i] <= -image.getHeight())
                {
                    yPositions[i] = image.getHeight() * (yPositions.length - 1);
                }
            }
            // If image moving down
            else
            {
                // If image is out of the screen, it moves it to the end of line of images.
                if (yPositions[i] >= image.getHeight() * (yPositions.length - 1))
                {
                    yPositions[i] = -image.getHeight();
                }
            }
        }
    }

    /**
     * Draw images to the screen.
     * 
     * @param g2d Graphics2D
     */
    public void Draw(Graphics2D g2d)
    {
        this.Update();
        
        for (int i = 0; i < yPositions.length; i++)
        {
            g2d.drawImage(image, xPosition, (int)yPositions[i], null);
        }
    }
}