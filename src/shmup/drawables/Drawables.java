/*
 * wrapper around a collection of drawable objects
 */

package shmup.drawables;

import java.util.List;

public class Drawables {
    protected List <Drawable> drawables;

    /**
     * Initialize collection
     *
     * @param drawables list of drawable objects
     */
    public Drawables(List<Drawable> drawables)
    {
        this.drawables = drawables;
    }

    /**
     * It sets speed and time between objects to the initial properties.
     */
    public void restartDrawables()
    {
        for (Drawable drawable: drawables)
            drawable.restartDrawable();
    }

    /**
     * Checks if the drawable in drawable list left the screen.
     */
    public void isLeftScreen()
    {
        for (Drawable drawable: drawables)
        {
            drawable.isLeftScreen();
        }
    }

    /**
     * Updates position of drawable in drawable list
     */
    public void Update(long gameTime)
    {
        for (Drawable drawable: drawables)
        {
            drawable.Update(gameTime);
        }
    }
}
