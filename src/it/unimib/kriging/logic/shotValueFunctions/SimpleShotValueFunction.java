package it.unimib.kriging.logic.shotValueFunctions;

import it.unimib.kriging.logic.FunctionDomain;
import it.unimib.kriging.logic.ShotValueFunction;

public class SimpleShotValueFunction implements ShotValueFunction {

    public double getValue(double x, double y) {
        return x + y;
    }

    public FunctionDomain getDomain() {
        return new FunctionDomain(0, 100, 0, 100);
    }

    public double getMin() {
        return this.getValue(0, 0);
    }

    public double getMax() {
        return this.getValue(this.getDomain().maxX, this.getDomain().maxY);

    }

    @Override
    public String getPlotFilename() {
        return "simple.png";
    }
}
