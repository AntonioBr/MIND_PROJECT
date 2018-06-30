package it.unimib.kriging.gui;

import it.unimib.kriging.logic.FunctionDomain;
import it.unimib.kriging.logic.ShotValueFunction;

public class KrigingUtils {

    public static double[] fromPixelsToRealValue(int pixelX, int pixelY, ShotValueFunction shotValueFunction, int pixelXRange, int pixelYRange) {
        FunctionDomain domain = shotValueFunction.getDomain();
        double xRange = domain.maxX - domain.minX;
        double yRange = domain.maxY - domain.minY;

        double xReal = domain.minX + (pixelX * xRange) / pixelXRange;
        double yReal = domain.minY + (pixelY * yRange) / pixelYRange;

        return new double[]{xReal, yReal};
    }

    public static int[] fromRealValueToPixels(double realX, double realY, ShotValueFunction shotValueFunction, int pixelXRange, int pixelYRange) {
        FunctionDomain domain = shotValueFunction.getDomain();
        realX = realX - domain.minX;
        realY = realY - domain.minY;

        double xRange = domain.maxX - domain.minX;
        double yRange = domain.maxY - domain.minY;

        double xPixel = (realX * pixelXRange) / xRange;
        double yPixel = (realY * pixelYRange) / yRange;

        return new int[]{(int) xPixel, (int) yPixel};
    }

    public static double fromPixelToRealValue(int pixel, ShotValueFunction shotValueFunction, int pixelRange) {
        FunctionDomain domain = shotValueFunction.getDomain();
        double range = domain.maxX - domain.minX;

        double real = domain.minX + (pixel * range) / pixelRange;

        return real;
    }
}
