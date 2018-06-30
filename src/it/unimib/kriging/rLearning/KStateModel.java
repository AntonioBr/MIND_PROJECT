package it.unimib.kriging.rLearning;

import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;
import it.unimib.kriging.gui.KrigingUtils;
import it.unimib.kriging.logic.ShotValueFunction;

import java.util.List;

public class KStateModel implements FullStateModel {

    private final ShotValueFunction valueFunction;

    public KStateModel(ShotValueFunction valueFunction) {
        this.valueFunction = valueFunction;
    }

    @Override
    public List<StateTransitionProb> stateTransitions(State state, Action action) {
        return Helper.deterministicTransition(this, state, action);
    }

    @Override
    public State sample(State oldState, Action action) {

        KState oldKState = (KState) oldState;
        KState kState = (KState) oldState.copy();
        kState.epoch++;

        double movementAmount = 20;

        Integer newXStraight = oldKState.coordX, newYStraight = oldKState.coordY;

        int sign = 1;

        switch (action.actionName()) {

            /*case KActions.STOP:
                kState.running = false;
                return kState;

            case KActions.MOVE_NORTH_1:
                newYStraight = (int) (kState.coordY - movementAmount);
                sign = -1;
                break;

            case KActions.MOVE_SOUTH_1:
                newYStraight = (int) (kState.coordY + movementAmount);
                break;

            case KActions.MOVE_EAST_1:
                newXStraight = (int) (kState.coordX + movementAmount);
                break;

            case KActions.MOVE_WEST_1:
                newXStraight = (int) (kState.coordX - movementAmount);
                sign = -1;

                break;*/


            case KActions.MOVE_NORTH_2:
                movementAmount *= 2;
                newYStraight = (int) (kState.coordY - movementAmount);
                sign = -1;

                break;

            case KActions.MOVE_SOUTH_2:
                movementAmount *= 2;
                newYStraight = (int) (kState.coordY + movementAmount);
                break;

            case KActions.MOVE_EAST_2:
                movementAmount *= 2;
                newXStraight = (int) (kState.coordX + movementAmount);
                break;

            case KActions.MOVE_WEST_2:
                movementAmount *= 2;
                newXStraight = (int) (kState.coordX - movementAmount);
                sign = -1;

                break;


            /*case KActions.MOVE_NORTH_3:
                movementAmount *= 4;
                newYStraight = (int) (kState.coordY - movementAmount);
                sign = -1;

                break;

            case KActions.MOVE_SOUTH_3:
                movementAmount *= 4;
                newYStraight = (int) (kState.coordY + movementAmount);
                break;

            case KActions.MOVE_EAST_3:
                movementAmount *= 4;
                newXStraight = (int) (kState.coordX + movementAmount);
                break;

            case KActions.MOVE_WEST_3:
                movementAmount *= 4;
                newXStraight = (int) (kState.coordX - movementAmount);
                sign = -1;

                break;*/
        }

        kState.straightX = newXStraight;
        kState.straightY = newYStraight;

        double[] newStraightCoords = KrigingUtils.fromPixelsToRealValue(newXStraight, newYStraight, this.valueFunction, 600, 600);
        double newStraightValue = this.valueFunction.getValue(newStraightCoords[0], newStraightCoords[1]);

        double[] oldCoords = KrigingUtils.fromPixelsToRealValue(oldKState.coordX, oldKState.coordY, this.valueFunction, 600, 600);
        double oldValue = this.valueFunction.getValue(oldCoords[0], oldCoords[1]);

        double delta = newStraightValue - oldValue;

        double angle = Math.atan(delta/movementAmount);

        double hypotenuse = (movementAmount/Math.cos(angle));

        double projectionOnHypotenuse = ((movementAmount * movementAmount)/hypotenuse);

        double realMovementAmount = projectionOnHypotenuse * Math.cos(angle) * sign;

        if (!Runner.LINEAR_MOVEMENT) {
            if (newXStraight != oldKState.coordX) {
                kState.coordX += (realMovementAmount);
            }

            if (newYStraight != oldKState.coordY) {
                kState.coordY += (realMovementAmount);
            }

        } else {
            kState.coordX = newXStraight;
            kState.coordY = newYStraight;

        }

        kState.coordX = Math.max(0, kState.coordX);
        kState.coordY = Math.min(600, kState.coordY);
        kState.coordY = Math.max(0, kState.coordY);
        kState.coordX = Math.min(600, kState.coordX);

        double[] coords = KrigingUtils.fromPixelsToRealValue(kState.coordX, kState.coordY, this.valueFunction, 600, 600);
        double value = this.valueFunction.getValue(coords[0], coords[1]);
        kState.currentValue = value;

        kState.delta = (int) ((value - kState.currentMax) * 1000);

        if (value > kState.currentMax) {
           kState.currentMax = value;
        }

        kState.remainingShots--;

        String[] angles = kState.angles.split("##");
        System.arraycopy(angles, 1, angles, 0, angles.length - 1);
        angles[angles.length - 1] = String.format("%d", round((int) Math.toDegrees(angle), 5));
        kState.angles = String.join("##", angles);

        String[] actions = kState.actions.split("##");
        System.arraycopy(actions, 1, actions, 0, actions.length - 1);
        actions[actions.length - 1] = action.actionName();
        kState.actions = String.join("##", actions);

        return kState;
    }

    public static int round (int number,int multiple){

        int result = multiple;

        //If not already multiple of given number

        if (number % multiple != 0){

            int division = (number / multiple)+1;

            result = division * multiple;

        }

        return result;

    }
}
