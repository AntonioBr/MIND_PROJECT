package it.unimib.kriging.rLearning;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;
import it.unimib.kriging.gui.KrigingUtils;
import it.unimib.kriging.logic.ShotValueFunction;

public class KRewardFunction implements RewardFunction {

    private ShotValueFunction valueFunction;

    public KRewardFunction(ShotValueFunction valueFunction) {
        this.valueFunction = valueFunction;
    }

    @Override
    public double reward(State oldState, Action action, State newState) {

        KState kState = (KState) newState;

        double coords[] = KrigingUtils.fromPixelsToRealValue(kState.coordX,
                kState.coordY,
                this.valueFunction, 600, 600);

        double value = this.valueFunction.getValue(coords[0], coords[1]);
        double range = Math.abs(valueFunction.getMax() - valueFunction.getMin());
        double globalPercentage = (1 - Math.abs(value - valueFunction.getMax()) / range) * 100;

        double percentage = Math.abs((value / kState.currentMax)) * 100;

        return kState.delta;
    }
}
