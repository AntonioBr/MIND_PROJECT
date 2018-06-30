package it.unimib.kriging.logic;

import java.util.ArrayList;

public class SimulationState {

    public int remainingShots;
    public Shot bestShot;
    public Shot lastShot;
    public ArrayList<Shot> shots;

    public SimulationState(int remainingShots) {
        this.remainingShots = remainingShots;
        this.bestShot = null;
        this.lastShot = new Shot(0, 0, -1);
        shots = new ArrayList<>();

    }

    public void addShot(Shot newShot) {
        shots.add(newShot);
        remainingShots--;
        lastShot = newShot;
        if (bestShot == null || newShot.value > bestShot.value) {
            bestShot = newShot;
        }
    }

    @Override
    public String toString() {
        return "SimulationState{" +
                "remainingShots=" + remainingShots +
                ", bestShot=" + bestShot +
                ", lastShot=" + lastShot +
                ", epochs=" + shots +
                '}';
    }

}
