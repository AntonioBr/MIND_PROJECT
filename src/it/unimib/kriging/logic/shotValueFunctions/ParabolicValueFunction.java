package it.unimib.kriging.logic.shotValueFunctions;

import it.unimib.kriging.logic.FunctionDomain;
import it.unimib.kriging.logic.ShotValueFunction;

public class ParabolicValueFunction implements ShotValueFunction {
    @Override
    public double getValue(double x, double y) {
        return -(x * x + y * y) + 3560;
    }

    @Override
    public double getMax() {
        return 3560;
    }

    @Override
    public double getMin() {
        return 2560;
    }

    @Override
    public FunctionDomain getDomain() {
        return new FunctionDomain(-10, 10, -10, 10);
    }

    @Override
    public String getPlotFilename() {
        return "parabolic.png";
    }
}
