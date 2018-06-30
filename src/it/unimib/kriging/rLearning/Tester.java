package it.unimib.kriging.rLearning;

import it.unimib.kriging.gui.KrigingUtils;
import it.unimib.kriging.logic.ShotValueFunction;
import it.unimib.kriging.logic.shotValueFunctions.BealeFunction;
import it.unimib.kriging.logic.shotValueFunctions.HimmelblauValueFunction;
import it.unimib.kriging.logic.shotValueFunctions.SimpleShotValueFunction;
import it.unimib.kriging.logic.shotValueFunctions.StyblinskiFunction;

public class Tester {

    public static void main(String[] args) {

        System.out.println(KStateModel.round(0, 5));
        ShotValueFunction function = new BealeFunction();
        System.out.println(KrigingUtils.fromRealValueToPixels(3, 0.5, function, 600, 600)[0]);
        System.out.println(KrigingUtils.fromRealValueToPixels(3, 0.5, function, 600, 600)[1]);

       // System.out.println(function.getValue(3, 0.5));

    }

    public static double getValue(double x) {
        return Math.pow(x, 2);
    }
}
