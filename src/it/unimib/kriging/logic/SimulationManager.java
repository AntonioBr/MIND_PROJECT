package it.unimib.kriging.logic;

public class SimulationManager {

    public ShotValueFunction shotValueFunction;
    public SimulationState state;

    public SimulationManager(ShotValueFunction shotValueFunction) {
        this.shotValueFunction = shotValueFunction;
        this.state = new SimulationState(15);
    }

    public boolean canShoot() {
        return state.remainingShots > 0;
    }

    public double makeShot(double x, double y) {
        double value = shotValueFunction.getValue(x, y);
        state.addShot(new Shot(x, y, value));

        return value;
    }
}
