package it.unimib.kriging.logic.shotValueFunctions;

import it.unimib.kriging.logic.FunctionDomain;
import it.unimib.kriging.logic.ShotValueFunction;

public class GriewankFunction implements ShotValueFunction {

    @Override
    public double getValue(double x, double y) {
        //x = x+4;
        //y = y-2;
        return -((Math.pow(x, 2) / 4000 + Math.pow(y, 2) / 4000) -
                (Math.cos(x / Math.sqrt(1)) * Math.cos(y / Math.sqrt(2)))) + 1000;
    }

    @Override
    public double getMax() {
        return getValue(0, 0);
    }

    @Override
    public double getMin() {
        return 0;
    }

    @Override
    public FunctionDomain getDomain() {
        return new FunctionDomain(-5, 5, -5, 5);
    }

    @Override
    public String getPlotFilename() {
        return "griewank.png";
    }
}
