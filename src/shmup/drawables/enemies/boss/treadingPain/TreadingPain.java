package shmup.drawables.enemies.boss.treadingPain;

import shmup.drawables.Drawable;
import shmup.drawables.enemies.boss.treadingPain.TreadingPainBody;

import java.awt.*;
import java.util.ArrayList;

public class TreadingPain extends Drawable {

    private TreadingPainBody body;
    private ArrayList<Drawable> appendages;
    public TreadingPain(TreadingPainBody body)
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
        body.Update(gameTime);
    }

    @Override
    public void Draw(Graphics2D g2d){
        body.Draw(g2d);
    }
}
