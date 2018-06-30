package it.unimib.kriging.logic;

public interface ShotValueFunction {
    double getValue(double x, double y);

    double getMax();

    double getMin();

    FunctionDomain getDomain();

    String getPlotFilename();
}
