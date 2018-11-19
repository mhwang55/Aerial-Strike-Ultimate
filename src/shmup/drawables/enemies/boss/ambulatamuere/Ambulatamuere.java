package shmup.drawables.enemies.boss.ambulatamuere;

import shmup.Animation;
import shmup.drawables.Drawable;
import shmup.drawables.enemies.Enemy;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class Ambulatamuere extends Drawable {

    private AmbulatamuereBody body;
    private ArrayList<Drawable> appendages;
    public Ambulatamuere(ArrayList<Drawable> appendages, AmbulatamuereBody body)
    {
        super();
        this.body = body;
        this.appendages = appendages;
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
        for (int i = 0; i < appendages.size(); i++)
        {
            if (appendages.get(i) instanceof AmbulatamuereLimb)
            {
                AmbulatamuereLimb limb = (AmbulatamuereLimb) appendages.get(i);
                limb.Update(gameTime, (int) body.xCoordinate, (int) body.yCoordinate);
            }
            else {
                Drawable dr = appendages.get(i);
                dr.Update(gameTime);
            }
        }
        for (Drawable appendage : appendages)
        {
        }
        body.Update(gameTime);
    }

    @Override
    public void Draw(Graphics2D g2d){
        for (Drawable appendage : appendages)
            appendage.Draw(g2d);

        body.Draw(g2d);
    }
}
