package it.unimib.kriging.logic;

public class FunctionDomain {

    public double minX;
    public double maxX;
    public double minY;
    public double maxY;

    public FunctionDomain(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }
}
