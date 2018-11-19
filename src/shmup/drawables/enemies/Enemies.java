/*
 * wrapper around a collection of enemies
 */

package shmup.drawables.enemies;

import java.util.List;

public class Enemies{
    protected List<Enemy> enemies;

    /**
     * Initialize collection
     *
     * @param enemies list of enemies
     */
    public Enemies(List<Enemy> enemies)
    {
        this.enemies = enemies;
    }

    /**
     * It sets speed and time between enemies to the initial properties.
     */
    public void restartEnemies(){
        for (Enemy enemy: enemies)
            enemy.restartDrawable();
    }

    public void isShooting(long gameTime)
    {
        for (Enemy enemy: enemies)
            enemy.isShooting(gameTime);
    }

    /**
     * Checks if an enemy in collection left the screen.
     *
     * @return true if an enemy in collection left the screen, false otherwise.
     */
    public void isLeftScreen()
    {
        for (Enemy enemy: enemies)
            enemy.isLeftScreen();
    }


    /**
     * Updates position of each enemy in collection
     */
    public void Update(long gameTime)
    {
        for (Enemy enemy: enemies)
            enemy.Update(gameTime);
    }
}
