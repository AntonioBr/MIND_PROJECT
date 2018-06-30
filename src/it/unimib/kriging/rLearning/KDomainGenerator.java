package it.unimib.kriging.rLearning;

import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.UniversalActionType;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.visualizer.StateRenderLayer;
import burlap.visualizer.Visualizer;
import it.unimib.kriging.logic.ShotValueFunction;

public class KDomainGenerator implements DomainGenerator {

    private ShotValueFunction valueFunction;

    public KDomainGenerator(ShotValueFunction valueFunction) {
        this.valueFunction = valueFunction;
    }

    @Override
    public OOSADomain generateDomain() {

        OOSADomain domain = new OOSADomain();

        domain.addActionTypes(
                /*new UniversalActionType(KActions.MOVE_NORTH_1),
                new UniversalActionType(KActions.MOVE_SOUTH_1),
                new UniversalActionType(KActions.MOVE_EAST_1),
                new UniversalActionType(KActions.MOVE_WEST_1),*/

                new UniversalActionType(KActions.MOVE_NORTH_2),
                new UniversalActionType(KActions.MOVE_SOUTH_2),
                new UniversalActionType(KActions.MOVE_EAST_2),
                new UniversalActionType(KActions.MOVE_WEST_2)

                /*new UniversalActionType(KActions.MOVE_NORTH_3),
                new UniversalActionType(KActions.MOVE_SOUTH_3),
                new UniversalActionType(KActions.MOVE_EAST_3),
                new UniversalActionType(KActions.MOVE_WEST_3)*/

                //new UniversalActionType(KActions.STOP)
        );

        KStateModel stateModel = new KStateModel(this.valueFunction);
        RewardFunction rewardFunction = new KRewardFunction(this.valueFunction);
        TerminalFunction terminalFunction = new KTerminalFunction();

        domain.setModel(new FactoredModel(stateModel, rewardFunction, terminalFunction));

        return domain;
    }

    public Visualizer getVisualizer() {

        StateRenderLayer rl = new StateRenderLayer();
        rl.addStatePainter(new KPainter(this.valueFunction));
        return new Visualizer(rl);
    }
}
