package shmup;

import shmup.drawables.Drawable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SelectionScreen {

    public ArrayList<BufferedImage> selectionScreenImgs;

    public SelectionScreen(ArrayList<BufferedImage> selectionScreenImgs)
    {
        this.selectionScreenImgs = selectionScreenImgs;
    }

    public void draw(Graphics2D g2d, int typeOfScreen)
    {
        g2d.drawImage(selectionScreenImgs.get(typeOfScreen), null, 0, 0);
    }
}
