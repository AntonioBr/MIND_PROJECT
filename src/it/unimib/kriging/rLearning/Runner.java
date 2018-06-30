package it.unimib.kriging.rLearning;

import burlap.behavior.learningrate.ConstantLR;
import burlap.behavior.policy.EpsilonGreedy;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.auxiliary.EpisodeSequenceVisualizer;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.LearningAgentFactory;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.behavior.singleagent.learning.tdmethods.SarsaLam;
import burlap.debugtools.RandomFactory;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.discretized.DiscretizingHashableStateFactory;
import burlap.visualizer.Visualizer;
import it.unimib.kriging.gui.KrigingUtils;
import it.unimib.kriging.logic.ShotValueFunction;
import it.unimib.kriging.logic.shotValueFunctions.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Runner {

    /*
        Ampliato i movimenti
        100 epoche su 150 episodi
    */

    public static final int RUNS = 1;
    public static final boolean FORCE_SEED = true;

    public static final int[][] START_COORDS = { { 300, 300 } };
    public static final boolean RANDOM_POSITION_BETWEEN_EPISODES = false;

    public static final int[] EPOCHS = { 100 };
    public static final ShotValueFunction valueFunction = new StyblinskiFunction();

    public static final boolean[][] HEURISTICS = { {false, false} };

    public static final int[] N_OF_EPISODES = { 151 };
    public static final double[] STOP_AT_PRECISION = { 99 };
    public static final double EPSILON = 0.1;
    public static final double LAMBDA = 0.1;

    public static final boolean LINEAR_MOVEMENT = true;
    public static final boolean DEBUG = false;
    public static final boolean WRITE_OUTPUT = false;
    public static final String OUTPUT_PATH = "./output/";
    private String BASE_FILENAME;

    private static KDomainGenerator domainGenerator;
    private static OOSADomain domain;
    private KState initialState;
    private DiscretizingHashableStateFactory hashableStateFactory;
    private SimulatedEnvironment environment;

    private RunnerOptions options;

    public Runner(RunnerOptions options) {
        this.options = options;
        this.BASE_FILENAME = String.format("%.1b%.1b", options.heuristics[0], options.heuristics[1]);
    }

    public double decreasingFunction(double probability) {
        probability -= probability / (N_OF_EPISODES[0]);
        return probability;
    }

    public RunnerResult run() {


        domainGenerator = new KDomainGenerator(options.valueFunction);
        domain = domainGenerator.generateDomain();
        hashableStateFactory = new DiscretizingHashableStateFactory(1);
        hashableStateFactory.addFloorDiscretizingMultipleFor(KStateVariables.COORD_X, 20);
        hashableStateFactory.addFloorDiscretizingMultipleFor(KStateVariables.COORD_Y, 20);
        hashableStateFactory.addFloorDiscretizingMultipleFor(KStateVariables.DELTA, 10);

        double[] coords = KrigingUtils.fromPixelsToRealValue(options.startCoords[0], options.startCoords[1], options.valueFunction, 600, 600);
        double startingValue = options.valueFunction.getValue(coords[0], coords[1]);
        initialState = new KState(options.startCoords[0], options.startCoords[1], startingValue, options.epochs, 0, startingValue, 0, 0, 0, true, "0##0", "null##null");

        environment = new SimulatedEnvironment(domain, initialState);
        //QLearning qLearningAgent = new QLearning(domain, 0.99, hashableStateFactory, 0, 1);
        SarsaLam sarsaLearningAgent = new SarsaLam(domain, 0.99, hashableStateFactory, 0., 1, LAMBDA);
        //qLearningAgent.setLearningPolicy(new EpsilonGreedy(qLearningAgent, EPSILON));
        sarsaLearningAgent.setLearningPolicy(new EpsilonGreedy(sarsaLearningAgent, EPSILON));

        //qLearningAgent.loadQTable("./qTable.txt");
        //sarsaLearningAgent.loadQTable("./sarsaTable.txt");
        //LearningAgent learningAgent = qLearningAgent;
        LearningAgent learningAgent = sarsaLearningAgent;

        RunnerResult results = new RunnerResult();
        results.runnerOptions = options;

        for (int i = 0; i < options.simulationLength; i++) {
            Episode episode = learningAgent.runLearningEpisode(environment);
            if (WRITE_OUTPUT) episode.write(OUTPUT_PATH + BASE_FILENAME + i);

            List<State> states = episode.stateSequence;
            results.stateSequences.add(states);

            int startX = options.startCoords[0];
            int startY = options.startCoords[1];

            if (RANDOM_POSITION_BETWEEN_EPISODES) {
                Random r = new Random();
                startX = r.nextInt(600);
                startY = r.nextInt(600);
            }

            //KState lastState = (KState) states.get(states.size() - 1);
            coords = KrigingUtils.fromPixelsToRealValue(startX, startY, options.valueFunction, 600, 600);
            startingValue = options.valueFunction.getValue(coords[0], coords[1]);
            KState newState = new KState(startX, startY, startingValue, options.epochs, 0, startingValue, 0, 0, 0, true, "0##0", "null##null");
            environment.resetEnvironment();
            environment.setCurStateTo(newState);
        }

        //qLearningAgent.writeQTable("./qTable.txt");
        sarsaLearningAgent.writeQTable("./sarsaTable.txt");

        //qLearningAgent.setLearningPolicy(new EpsilonGreedy(qLearningAgent, 0));
        sarsaLearningAgent.setLearningPolicy(new EpsilonGreedy(sarsaLearningAgent, 0));
        sarsaLearningAgent.setLearningRateFunction(new ConstantLR(0d)); // PRIMA MANCAVA!!!!!
        //learningAgent = qLearningAgent;
        learningAgent = sarsaLearningAgent;
        Episode episode = learningAgent.runLearningEpisode(environment);
        episode.write(OUTPUT_PATH + "greedy");

        List<State> states = episode.stateSequence;
        results.stateSequences.add(states);

        return results;
    }

    public static void main(String args[]) throws IOException {

        if (FORCE_SEED) RandomFactory.seedMapped(0, 1);

        for (int[] startCoords : START_COORDS) {
            for (double stopAtPrecision : STOP_AT_PRECISION) {
                System.out.println("");
                for (int simulations : N_OF_EPISODES) {
                    System.out.println("");
                    for (boolean[] heuristics : HEURISTICS) {
                        for (int epochs : EPOCHS) {
                            RunnerOptions runOptions = new RunnerOptions();
                            runOptions.startCoords = startCoords;
                            runOptions.epochs = epochs;
                            runOptions.heuristics = heuristics;
                            runOptions.simulationLength = simulations;
                            runOptions.stopAtPrecision = stopAtPrecision;
                            runOptions.valueFunction = valueFunction;


                            //System.out.println("Running " + RUNS + " runs with parameters: " + runOptions.toString());
                            ArrayList<RunnerResult> results = new ArrayList<>();
                            for (int n = 0; n < RUNS; n++) {
                                //if (n != 0 && n % (RUNS / 10 + 1) == 0) System.out.print((n + 1) + ", ");
                                runOptions.runNumber = n;
                                Runner run = new Runner(runOptions);
                                RunnerResult result = run.run();
                                results.add(result);
                            }
                            //System.out.println();
                            analyzeRunsResults(results);
                            //System.out.println();

                        }
                    }
                }
            }
        }

        if (WRITE_OUTPUT) {
            Visualizer v = domainGenerator.getVisualizer();
            new EpisodeSequenceVisualizer(v, domain, OUTPUT_PATH);
        }
    }

    private static void analyzeRunsResults(ArrayList<RunnerResult> results) throws IOException {

        //String filename = "dataAngles" + (LINEAR_MOVEMENT ? "Linear" : "Parametric") + valueFunction.getClass().getSimpleName() + ".txt";
        //BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        BufferedWriter writer = new BufferedWriter(new FileWriter("data.csv" ));
        writer.append("EPSIODE;EPOCH;GAP_VALUE;VALUE_FUNCTION;COORD_X;COORD_Y" + "\n");

        RunnerOptions option = results.get(0).runnerOptions;

        RunnerResult result = results.get(0);

        Set<State> stateSet = new HashSet<>();
        int totalStates = 0;

        int episode = 0;
        for (List<State> stateSequence: result.stateSequences) {
            KState firstState = (KState) stateSequence.get(0);

            if (true || (episode + 1) % 50 == 0 || episode == 0 || episode == result.stateSequences.size() - 1) {
                //System.out.println("\n\n" + episode);
                if (episode == result.stateSequences.size() -1 ) {
                    System.out.println("Greedy");
                }

                totalStates += stateSequence.size();
                stateSet.addAll(stateSequence);

                double lastValue = -1;

                for (int epoch = 0; epoch < stateSequence.size(); epoch++) {
                    KState currentState = (KState) stateSequence.get(epoch);

                    double gapValue = (currentState.currentMax - firstState.currentValue) /
                            (option.valueFunction.getMax() - firstState.currentValue);

                    writer.append(episode + ";" + epoch + ";" + gapValue + ";" + currentState.currentValue + ";" +
                            + currentState.coordX + ";" + currentState.coordY + "\n");

                }
            }

            episode++;

        }

        System.out.println("Stati totali visitati: " + totalStates);
        System.out.println("Stati diversi visitati: " + stateSet.size());

        writer.close();
    }

    private static double getPercentage(KState state) {
        double coords[] = KrigingUtils.fromPixelsToRealValue(state.coordX,
                state.coordY,
                valueFunction, 600, 600);
        double value = valueFunction.getValue(coords[0], coords[1]);
        double range = Math.abs(valueFunction.getMax() - valueFunction.getMin());
        double globalPercentage = (1 - Math.abs(value - valueFunction.getMax()) / range) * 100;
        return globalPercentage;
    }

}