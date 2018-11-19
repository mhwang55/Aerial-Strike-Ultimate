package shmup;

import java.net.URL;
import java.io.*;
import javax.sound.sampled.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Music implements Runnable {
    Thread runner;
    AudioInputStream ais;
    SourceDataLine line;
    public Music ()
    {
    }
    public Music (String name, AudioInputStream ais, SourceDataLine line)
    {
        this.ais = ais;
        this.line = line;
        runner = new Thread(this, name);
        runner.start();
    }
    public void run ()
    {
        try {
            line.start();
            FloatControl volume = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(-10.0F);
            byte[] bytesBuffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = ais.read(bytesBuffer)) != -1) {
                line.write(bytesBuffer, 0, bytesRead);
            }
            line.drain();
            line.close();
            ais.close();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
