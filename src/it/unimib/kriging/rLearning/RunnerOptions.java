package it.unimib.kriging.rLearning;

import it.unimib.kriging.logic.ShotValueFunction;

import java.util.Arrays;

public class RunnerOptions {
    public int[] startCoords;
    public int epochs;
    public boolean[] heuristics;
    public int simulationLength;
    public double stopAtPrecision;
    public int runNumber;
    public ShotValueFunction valueFunction;

    @Override
    public String toString() {
        return "RunnerOptions{" +
                "startCoords=" + Arrays.toString(startCoords) +
                ", epochs=" + epochs +
                ", heuristics=" + Arrays.toString(heuristics) +
                ", simulationLength=" + simulationLength +
                ", stopAtPrecision=" + stopAtPrecision +
                '}';
    }
}
