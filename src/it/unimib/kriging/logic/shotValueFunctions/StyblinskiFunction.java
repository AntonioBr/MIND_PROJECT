package it.unimib.kriging.logic.shotValueFunctions;

import it.unimib.kriging.logic.FunctionDomain;
import it.unimib.kriging.logic.ShotValueFunction;

public class StyblinskiFunction implements ShotValueFunction {

    @Override
    public double getValue(double x, double y) {
        return -((Math.pow(x, 4) - 16 * Math.pow(x, 2) + 5 * x) + (Math.pow(y, 4) - 16 * Math.pow(y, 2) + 5 * y)) + 5000;
    }

    @Override
    public double getMax() {
        return getValue(-2.90, -2.90);
    }

    @Override
    public double getMin() {
        return 4500;
    }

    @Override
    public FunctionDomain getDomain() {
        return new FunctionDomain(-5, 5, -5 ,5);
    }

    @Override
    public String getPlotFilename() {
        return "styblinski.png";
    }
}
