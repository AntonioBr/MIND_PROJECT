package it.unimib.kriging.rLearning;

import burlap.mdp.core.state.State;

import java.util.ArrayList;
import java.util.List;

public class RunnerResult {
    public RunnerOptions runnerOptions;

    public ArrayList<List<State>> stateSequences;

    public RunnerResult() {
        stateSequences = new ArrayList<>();
    }
}
