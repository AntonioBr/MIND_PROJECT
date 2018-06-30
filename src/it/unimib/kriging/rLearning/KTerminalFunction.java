package it.unimib.kriging.rLearning;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;

public class KTerminalFunction implements TerminalFunction {


    @Override
    public boolean isTerminal(State state) {

        KState kState = (KState) state;

        if (kState.remainingShots <= 0 || !kState.running) {
            return true;
        }

        return false;
    }
}
