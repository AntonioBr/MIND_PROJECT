package it.unimib.kriging.logic.shotValueFunctions;

import it.unimib.kriging.logic.FunctionDomain;
import it.unimib.kriging.logic.ShotValueFunction;

public class CornerCaseTestingFunction implements ShotValueFunction {
    @Override
    public double getValue(double x, double y) {

        if (x >= 0) {

            return (Math.pow(x, 2));
        }

        if (x < 0 && x > -9) {

            return 0;
        }

        return (Math.pow(x, 4));

    }

    @Override
    public double getMax() {
        return 10000;
    }

    @Override
    public double getMin() {
        return 0;
    }

    @Override
    public FunctionDomain getDomain() {

        return (new FunctionDomain(-10, 10, -10, 10));
    }

    @Override
    public String getPlotFilename() {
        return "corner.png";
    }
}
