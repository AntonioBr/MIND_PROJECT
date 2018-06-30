package it.unimib.kriging.logic.shotValueFunctions;

import it.unimib.kriging.logic.FunctionDomain;
import it.unimib.kriging.logic.ShotValueFunction;

public class BealeFunction implements ShotValueFunction {
    @Override
    public double getValue(double x, double y) {
        return -((Math.pow(1.5 - x + (x * y), 2)) +
                (Math.pow(2.25 - x + x * (Math.pow(y, 2)), 2)) +
                (Math.pow(2.625 - x + x * (Math.pow(y, 3)), 2))) + 2000;
    }

    @Override
    public double getMax() {
        return 2000;
    }

    @Override
    public double getMin() {
        return 1000;
    }

    @Override
    public FunctionDomain getDomain() {
        return new FunctionDomain(-3, 3, -3, 3);
    }

    @Override
    public String getPlotFilename() {
        return "beale.png";
    }
}
