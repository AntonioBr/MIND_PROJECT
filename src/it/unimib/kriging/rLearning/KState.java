package it.unimib.kriging.rLearning;

import burlap.mdp.core.state.MutableState;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.StateUtilities;
import burlap.mdp.core.state.UnknownKeyException;
import burlap.mdp.core.state.annotations.DeepCopyState;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@DeepCopyState
public class KState implements MutableState {

    public int epoch;

    public int remainingShots;
    public int coordX, coordY, delta;
    public int straightX, straightY;
    public double currentMax;
    public double currentValue;
    public boolean running;
    public String angles;
    public String actions;

    private final static List<Object> keys = Arrays.asList(
            // KStateVariables.COORD_X,
            // KStateVariables.COORD_Y,
            //KStateVariables.REMAINING_SHOTS
            // KStateVariables.DELTA
            KStateVariables.ANGLES,
            KStateVariables.ACTIONS
    );

    public KState() {
    }

    public KState(int coordX, int coordY, double currentValue, int remainingShots, int delta, double currentMax, int straightX, int straightY, int epoch, boolean running, String angles, String actions) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.currentValue = currentValue;
        this.remainingShots = remainingShots;
        this.delta = delta;
        this.currentMax = currentMax;
        this.straightX = straightX;
        this.straightY = straightY;

        this.angles = angles;
        this.actions = actions;

        this.epoch = epoch;
        this.running = running;


    }

    @Override
    public MutableState set(Object key, Object value) {

        if (key.equals(KStateVariables.COORD_X)) {

            this.coordX = StateUtilities.stringOrNumber(value).intValue();
            return this;
        }

        if (key.equals(KStateVariables.COORD_Y)) {

            this.coordY = StateUtilities.stringOrNumber(value).intValue();
            return this;
        }

        if (key.equals(KStateVariables.REMAINING_SHOTS)) {

            this.remainingShots = StateUtilities.stringOrNumber(value).intValue();
            return this;
        }

        if (key.equals(KStateVariables.DELTA)) {

            this.delta = StateUtilities.stringOrNumber(value).intValue();
            return this;
        }

        throw new UnknownKeyException(key);

    }

    @Override
    public List<Object> variableKeys() {
        return this.keys;
    }

    @Override
    public Object get(Object key) {

        if (key.equals(KStateVariables.COORD_X)) {

            return this.coordX;
        }

        if (key.equals(KStateVariables.COORD_Y)) {

            return this.coordY;
        }

        if (key.equals(KStateVariables.REMAINING_SHOTS)) {

            return this.remainingShots;
        }

        if (key.equals(KStateVariables.DELTA)) {

            return this.delta;
        }

        if (key.equals(KStateVariables.ANGLES)) {
            return this.angles;
        }

        if (key.equals(KStateVariables.ACTIONS)) {
            return this.actions;
        }

        throw new UnknownKeyException(key);
    }

    @Override
    public State copy() {
        return new KState(this.coordX, this.coordY, this.currentValue, this.remainingShots, this.delta, this.currentMax, this.straightX, this.straightY, this.epoch, this.running, this.angles, this.actions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KState kState = (KState) o;
        return Objects.equals(angles, kState.angles) &&
                Objects.equals(actions, kState.actions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(angles, actions);
    }

    @Override
    public String toString() {
        return "KState{" +
                "coordX=" + coordX +
                ", coordY=" + coordY +
                ", delta=" + delta+
                '}';
    }
}
