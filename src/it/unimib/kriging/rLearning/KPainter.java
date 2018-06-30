package it.unimib.kriging.rLearning;

import burlap.mdp.core.state.State;
import burlap.visualizer.StatePainter;
import it.unimib.kriging.logic.ShotValueFunction;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class KPainter implements StatePainter {
    private ShotValueFunction valueFunction;
    private BufferedImage image;

    public KPainter(ShotValueFunction valueFunction) {
        this.valueFunction = valueFunction;
        try {
            this.image = ImageIO.read(new File("plots/" + valueFunction.getPlotFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics2D graphics2D, State state, float v, float v1) {

        KState kState = (KState) state;

        graphics2D.drawImage(this.image, 0, 0, null);

        int radius = 20;

        graphics2D.setColor(Color.GREEN);
        graphics2D.fillOval(kState.straightX - radius / 2, kState.straightY - radius / 2, radius, radius);

        radius = 10;
        graphics2D.setColor(Color.RED);
        graphics2D.fillOval(kState.coordX - radius / 2, kState.coordY - radius / 2, radius, radius);

        graphics2D.setColor(Color.BLACK);
        graphics2D.drawRect(0, 0, 600, 600);

        graphics2D.drawString(Integer.toString(kState.delta), 10, 10);

        graphics2D.drawString(Integer.toString(kState.epoch), 10, 100);

        graphics2D.drawString(kState.angles, 10, 400);
        graphics2D.drawString(kState.actions, 10, 450);

        graphics2D.drawString(Integer.toString(kState.coordX), 10, 250);

        graphics2D.drawString(Integer.toString(kState.coordY), 35, 250);


    }

}
