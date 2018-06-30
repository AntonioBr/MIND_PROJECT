package it.unimib.kriging.logic.shotValueFunctions;

import it.unimib.kriging.logic.FunctionDomain;
import it.unimib.kriging.logic.ShotValueFunction;

public class HimmelblauValueFunction implements ShotValueFunction {


    @Override
    public double getValue(double x, double y) {
        return -(Math.pow(Math.pow(x, 2) + y - 11, 2) + Math.pow(x + Math.pow(y, 2) - 7, 2)) + getMax();
    }

    @Override
    public double getMax() {
        return 2500;
    }

    @Override
    public double getMin() {
        return 1500;
    }

    @Override
    public FunctionDomain getDomain() {

        return new FunctionDomain(-5, 5, -5, 5);
    }

    @Override
    public String getPlotFilename() {
        return "himmelblau.png";
    }
}
