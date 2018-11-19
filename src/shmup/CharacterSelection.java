package shmup;

import shmup.drawables.Background;
import shmup.drawables.Drawable;
import shmup.drawables.PowerUp;
import shmup.drawables.bullets.*;
import shmup.drawables.enemies.Enemy;
import shmup.drawables.enemies.air.B24;
import shmup.drawables.enemies.air.LockheedConstellation;
import shmup.drawables.enemies.air.P51;
import shmup.drawables.enemies.air.YB35;
import shmup.drawables.enemies.boss.ambulatamuere.*;
import shmup.drawables.enemies.midboss.Ecaep;
import shmup.player.Missile;
import shmup.player.Player;
import shmup.player.Shot;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * selection screen for planes
 *
 * @author Max Wang
 */

public class CharacterSelection {

    // Use this to generate a random number.
    private Random random;

    // We will use this for setting mouse position.
    private Robot robot;


    // selection screen images
    private BufferedImage dragonflyImg;
    private BufferedImage marauderImg;
    private BufferedImage bf109Img;
    private BufferedImage zeroImg;
    private BufferedImage spitfireImg;
    private BufferedImage me110Img;

    // Animation of dragonfly
    private Animation dragonflyAnim;
    private Animation marauderAnim;
    private Animation bf109Anim;
    private Animation zeroAnim;
    private Animation spitfireAnim;
    private Animation me110Anim;


    // selection screen images
    private BufferedImage dragonflyScreenImg;
    private BufferedImage marauderScreenImg;
    private BufferedImage bf109ScreenImg;
    private BufferedImage zeroScreenImg;
    private BufferedImage spitfireScreenImg;
    private BufferedImage me110ScreenImg;
    private BufferedImage menuPointerImg;

    // selection screen object
    private SelectionScreen selectionScreen;

    // menu pointer
    private MenuPointer menuPointer;
    private int menuPointerCenterX;
    private int menuPointerCenterY;

    // background screen type
    private int type = 0;


    // selection screen music
    private Music music;

    public CharacterSelection() {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

        Thread threadForInitGame = new Thread() {
            @Override
            public void run() {
                // Selection screen content
                loadSelectionScreenContent();

                // Sets variables and objects for the game.
                Initialize();
                // Load game files (images, sounds, ...)
                LoadContent();

                Framework.gameState = Framework.GameState.CHARACTER_SELECTION;
            }
        };
        threadForInitGame.start();
    }


    /**
     * Set variables and objects for the game.
     */
    private void Initialize() {
        random = new Random();

        // selection screen init
        ArrayList<BufferedImage> selectionScreenImgs = new ArrayList<>(Arrays.asList(dragonflyScreenImg,
                marauderScreenImg, bf109ScreenImg, zeroScreenImg, spitfireScreenImg, me110ScreenImg));

        selectionScreen = new SelectionScreen(selectionScreenImgs);
        menuPointer = new MenuPointer(100, 800, menuPointerImg, 90, 56, 1);
        menuPointerCenterX = menuPointerImg.getWidth() / 2;
        menuPointerCenterY = menuPointerImg.getHeight() / 2;

        try {
            robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(CharacterSelection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Load game files (images).
     */
    private void loadSelectionScreenContent() {
        try {
            // selection screens
            URL dragonflyImgUrl = this.getClass().getResource("resources/pics/background/selectionScreen/dragonfly.png");
            dragonflyScreenImg = ImageIO.read(dragonflyImgUrl);

            URL marauderImgUrl = this.getClass().getResource("resources/pics/background/selectionScreen/marauder.png");
            marauderScreenImg = ImageIO.read(marauderImgUrl);

            URL bf109ImgUrl = this.getClass().getResource("resources/pics/background/selectionScreen/bf109.png");
            bf109ScreenImg = ImageIO.read(bf109ImgUrl);

            URL zeroImgUrl = this.getClass().getResource("resources/pics/background/selectionScreen/zero.png");
            zeroScreenImg = ImageIO.read(zeroImgUrl);

            URL spitfireImgUrl = this.getClass().getResource("resources/pics/background/selectionScreen/spitfire.png");
            spitfireScreenImg = ImageIO.read(spitfireImgUrl);

            URL me110ImgUrl = this.getClass().getResource("resources/pics/background/selectionScreen/me110.png");
            me110ScreenImg = ImageIO.read(me110ImgUrl);

            URL menuPointerImgUrl = this.getClass().getResource("resources/pics/background/selectionScreen/menuPointer.png");
            menuPointerImg = ImageIO.read(menuPointerImgUrl);
        } catch (IOException ex) {
            Logger.getLogger(CharacterSelection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Load game files (images).
     */
    private void LoadContent() {
        try
        {
            // dragonfly img
            URL dragonflyImgUrl = this.getClass().getResource("./resources/pics/player/dragonflyAnimationStrip.png");
            dragonflyImg = ImageIO.read(dragonflyImgUrl);

            // marauder img
            URL marauderImgUrl = this.getClass().getResource("./resources/pics/player/marauderAnimationStrip.png");
            marauderImg = ImageIO.read(marauderImgUrl);

            // bf-109 img
            URL bf109ImgUrl = this.getClass().getResource("./resources/pics/player/bf109AnimationStrip.png");
            bf109Img = ImageIO.read(bf109ImgUrl);

            // zero img
            URL zeroImgUrl = this.getClass().getResource("./resources/pics/player/zeroAnimationStrip.png");
            zeroImg = ImageIO.read(zeroImgUrl);

            // spitfire img
            URL spitfireImgUrl = this.getClass().getResource("./resources/pics/player/spitfireAnimationStrip.png");
            spitfireImg = ImageIO.read(spitfireImgUrl);

            // me-110 img
            URL me110ImgUrl = this.getClass().getResource("./resources/pics/player/me110AnimationStrip.png");
            me110Img = ImageIO.read(me110ImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(CharacterSelection.class.getName()).log(Level.SEVERE, null, ex);
        }

        int posX = dragonflyImg.getWidth() / 4;
        int posY = dragonflyImg.getHeight() / 2;
        int posYFinal = 800 + (Math.abs(menuPointerCenterY - posY));

        dragonflyAnim =
                new Animation(dragonflyImg,
                        46, 37, 2, 20, true,
                        100 + (Math.abs(menuPointerCenterX - posX)), posYFinal, 0);

        posX = marauderImg.getWidth() / 4;
        posY = marauderImg.getHeight() / 2;
        posYFinal = 800 + (Math.abs(menuPointerCenterY - posY));
        marauderAnim =
                new Animation(marauderImg,
                        65, 48, 2, 20, true,
                        200 + (Math.abs(menuPointerCenterX - posX)), posYFinal, 0);

        posX = bf109Img.getWidth() / 4;
        posY = bf109Img.getHeight() / 2;
        posYFinal = 800 + (Math.abs(menuPointerCenterY - posY));
        bf109Anim =
                new Animation(bf109Img,
                        43, 37, 2, 20, true,
                        300 + (Math.abs(menuPointerCenterX - posX)), posYFinal, 0);

        posX = zeroImg.getWidth() / 4;
        posY = zeroImg.getHeight() / 2;
        posYFinal = 800 + (Math.abs(menuPointerCenterY - posY));
        zeroAnim =
                new Animation(zeroImg,
                        50, 38, 2, 20, true,
                        400 + (Math.abs(menuPointerCenterX - posX)), posYFinal, 0);

        posX = spitfireImg.getWidth() / 4;
        posY = spitfireImg.getHeight() / 2;
        posYFinal = 800 + (Math.abs(menuPointerCenterY - posY));
        spitfireAnim =
                new Animation(spitfireImg,
                        49, 39, 2, 20, true,
                        500 + (Math.abs(menuPointerCenterX - posX)), posYFinal, 0);

        posX = me110Img.getWidth() / 4;
        posY = me110Img.getHeight() / 2;
        posYFinal = 800 + (Math.abs(menuPointerCenterY - posY));
        me110Anim =
                new Animation(me110Img,
                        68, 54, 2, 20, true,
                        600 + (Math.abs(menuPointerCenterX - posX)), posYFinal, 0);

        loadSelectionScreenContent();
        loadSoundContent();
        //music = new Music("bgMusic", aisBoss, lineBoss);
    }

    private void loadSoundContent() {
        try {
        } catch (Exception ex) {
            Logger.getLogger(CharacterSelection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /**
     * Update game logic.
     *
     */
    public int updateSelection()
    {
        ///*
        if(menuPointer.xCoordinate == 100)
            type = 0;
        else if(menuPointer.xCoordinate == 200)
            type = 1;
        else if(menuPointer.xCoordinate == 300)
            type = 2;
        else if(menuPointer.xCoordinate == 400)
            type = 3;
        else if(menuPointer.xCoordinate == 500)
            type = 4;
        else if(menuPointer.xCoordinate == 600)
            type = 5;
        //*/

        menuPointer.Update(0);
        return type;
    }

    /**
     * Draw the game to the screen.
     *
     * @param g2d           Graphics2D
     * @param gameTime      time of game
     */
    public void draw(Graphics2D g2d, long gameTime)
    {
        selectionScreen.draw(g2d, type);
        menuPointer.Draw(g2d);
        dragonflyAnim.Draw(g2d);
        marauderAnim.Draw(g2d);
        bf109Anim.Draw(g2d);
        zeroAnim.Draw(g2d);
        spitfireAnim.Draw(g2d);
        me110Anim.Draw(g2d);
    }

    public void getType()
    {
        // gets type of plane
    }
}
