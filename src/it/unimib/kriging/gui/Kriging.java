package it.unimib.kriging.gui;

import burlap.mdp.core.state.State;
import it.unimib.kriging.logic.*;
import it.unimib.kriging.logic.shotValueFunctions.*;
import it.unimib.kriging.rLearning.KState;
import it.unimib.kriging.rLearning.Runner;
import it.unimib.kriging.rLearning.RunnerOptions;
import it.unimib.kriging.rLearning.RunnerResult;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Kriging {

    private static final String SHOTS_CSV = "./epochs.csv";

    private JPanel mainPanel;
    private JLabel remainingShots;
    private JLabel bestShotValue;
    private JLabel lastShotValue;
    private JButton restartButton;
    private JComboBox selectedFunctionComboBox;
    private JLabel messageLabel;
    private JPanel map;
    private JLabel colorScale;
    private SimulationManager simulationManager;
    private CSVPrinter csvPrinter;
    private Date startDate;

    private ShotValueFunction selectedFunction;
    private String selectedFunctionString;
    private BufferedImage image;

    private RunnerResult runResults;

    public Kriging(SimulationManager sm, CSVPrinter csvPrinter) throws IOException {
        this.csvPrinter = csvPrinter;
        this.startDate = new Date();
        JComboBox jComboBox = this.selectedFunctionComboBox;

        String name;
        do {
            name = JOptionPane.showInputDialog("Nome:  ");
        } while (name==null);

        String sex;
        do {
            sex = JOptionPane.showInputDialog("Sesso:  ");
        } while (sex == null || (!sex.toLowerCase().equals("m") && !sex.toLowerCase().equals("f")));

        String age;
        do {
            age = JOptionPane.showInputDialog("Età:  ");
        } while (age == null);

        final String finalName = name;
        final String finalSex = sex;
        final String finalAge = age;

        map.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {

                if (simulationManager.canShoot()) {

                    try {
                        image = ImageIO.read(new File("./plots/" + selectFunction(jComboBox).get(0).toString() + ".png"));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    int x = event.getX();
                    int y = event.getY();

                    /*// first click
                    if (simulationManager.state.remainingShots == 10) {
                        RunnerOptions options = new RunnerOptions();
                        options.stopAtPrecision = 95;
                        options.startCoords = new int[]{x, y};
                        options.epochs = simulationManager.state.remainingShots;
                        options.simulationLength = 150;
                        options.heuristics = new boolean[]{true, false};
                        options.valueFunction = selectedFunction;
                        Runner runner = new Runner(options);
                        runResults = runner.run();
                    }*/

                    double coords[] = KrigingUtils.fromPixelsToRealValue(x, y, simulationManager.shotValueFunction, map.getWidth(), map.getHeight());
                    double value = simulationManager.makeShot(coords[0], coords[1]);
                    updateGui(simulationManager.state, image);
                    try {
                        String formattedDate = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(startDate);
                        csvPrinter.printRecord(formattedDate, finalName, finalSex, finalAge, simulationManager.shotValueFunction.getPlotFilename(), simulationManager.state.shots.size(), coords[0], coords[1], value);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                selectedFunction = (ShotValueFunction) selectFunction(jComboBox).get(1);
                selectedFunctionString = selectFunction(jComboBox).get(0).toString();
                simulationManager = new SimulationManager(selectedFunction);

                try {
                    image = ImageIO.read(new File("./plots/" + selectedFunctionString + ".png"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                startDate = new Date();
                restartButton.setEnabled(false);
                jComboBox.setEnabled(false);

                updateGui(simulationManager.state, image);
            }
        });
    }


    public void updateGui(SimulationState s, Image image) {

        Image importedImage = image;
        if (s.bestShot != null) {
            lastShotValue.setText(String.format("%.2f", s.lastShot.value));
            bestShotValue.setText(String.format("%.2f", s.bestShot.value));
        }

        remainingShots.setText(String.valueOf(s.remainingShots));

        if (s.remainingShots == 0) {
            restartButton.setEnabled(true);
            selectedFunctionComboBox.setEnabled(true);
        }

        drawMap(s, importedImage);
    }

    public void drawMap(SimulationState s, Image image) {
        Graphics g = map.getGraphics();

        // repaint the background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, map.getWidth(), map.getHeight());

        g.setColor(Color.BLACK);
        /*
        //y axis
        g.drawLine(map.getWidth() / 2, 15, map.getWidth() / 2, map.getHeight() - 15);
        // x axis
        g.drawLine(15, map.getHeight() / 2, map.getWidth() - 15, map.getHeight() / 2);*/

        FunctionDomain domain = simulationManager.shotValueFunction.getDomain();
        //g.drawString(String.valueOf(domain.minX), 15, map.getHeight() / 2 + 15);
        //g.drawString(String.valueOf(domain.maxX), map.getWidth() - 30, map.getHeight() / 2 + 15);
        //g.drawString(String.valueOf(domain.minY), map.getWidth() / 2, 15);
        //g.drawString(String.valueOf(domain.maxY), map.getWidth() / 2, map.getHeight() - 15);
        //g.drawImage(image, 0, 0, null);

        g.setColor(Color.RED);
        g.drawRect(0, 0, map.getWidth(), map.getHeight());

        int radius = 30;

        int[] coords;
        // draw the best
        if (s.bestShot != null) {
            coords = KrigingUtils.fromRealValueToPixels(s.bestShot.x, s.bestShot.y, simulationManager.shotValueFunction, map.getWidth(), map.getHeight());
            g.setColor(Color.MAGENTA);
            g.fillOval(coords[0] - (radius / 2), coords[1] - (radius / 2), radius, radius);
        }

        radius /= 2;

        for (Shot shot : s.shots) {
            double range = Math.abs(simulationManager.shotValueFunction.getMax() - simulationManager.shotValueFunction.getMin());
            double percentage = (1 - Math.abs(shot.value - simulationManager.shotValueFunction.getMax()) / range) * 100;

            if (percentage < 15) {
                g.setColor(new Color(10, 50, 120));
            } else if (percentage < 30) {
                g.setColor(new Color(30, 110, 200));
            } else if (percentage < 45) {
                g.setColor(new Color(160, 240, 255));
            } else if (percentage < 60) {
                g.setColor(new Color(255, 220, 220));
            } else if (percentage < 78) {
                g.setColor(new Color(255, 250, 60));
            } else if (percentage < 95) {
                g.setColor(new Color(255, 160, 0));
            } else {
                g.setColor(new Color(0, 255, 50));
            }

            coords = KrigingUtils.fromRealValueToPixels(shot.x, shot.y, simulationManager.shotValueFunction, map.getWidth(), map.getHeight());
            g.fillOval(coords[0] - (radius / 2), coords[1] - (radius / 2), radius, radius);

            // draw the value of the shot
            String prettyValue = String.format("%.0f", shot.value);
            g.setColor(Color.BLACK);
            g.drawString(prettyValue, coords[0] + radius, coords[1] + radius / 2);
        }


        /*List<State> states = runResults.stateSequences.get(runResults.stateSequences.size() - 1);
        for (int i = 0; i < s.shots.size(); i++) {
            KState state = (KState) states.get(i);
            g.setColor(Color.BLACK);
            g.fillOval(state.coordX - radius/2, state.coordY - radius/2, radius, radius);
        }*/
    }

    public static boolean checkPresence(String name, String password) throws IOException {


        Reader in = new FileReader("./epochs.csv");

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
        for (CSVRecord record : records) {
            String userName = record.get(1);
            String p = record.get(2);
            if (name.equals(userName) && password.equals(p)) {
                return true;
            }
        }
        return false;

    }

    public static int wantToRegister() {

        Object[] options1 = {
                "Sign In",
                "Register",
        };

        int selectedOption = JOptionPane.showOptionDialog(null,
                "Select an action",
                "First page",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options1,
                null);


        return selectedOption;


    }

    public static String[] getUserInfo() throws IOException {

        String[] userInfo = new String[2];
        JTextField username = new JTextField();
        JTextField password = new JPasswordField();

        int loginOrRegister = wantToRegister();
        String title = loginOrRegister == 0 ? "Login" : "Register";
        switch (loginOrRegister) {
            case 0: // login
            case 1: // register
                Object[] message = {
                        "Username:", username,
                        "Password:", password
                };
                int returnValue = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.OK_CANCEL_OPTION);
                userInfo[0] = username.getText().toString();
                userInfo[1] = password.getText().toString();
                if (returnValue > 0 || userInfo[0].length() == 0 || userInfo[1].length() == 0) return getUserInfo();

                break;
            case -1:
                return null;

        }

        if (loginOrRegister == 1) { // register
            if (checkPresence(username.getText().toString(), password.getText().toString())) {
                // there is already an user with that username and password
                return getUserInfo();
            }
            return userInfo;
        } else {
            if (!checkPresence(username.getText().toString(), password.getText().toString())) {
                // there is no one with that login info
                return getUserInfo();
            }
            return userInfo;
        }


    }

    public static void main(String[] args) throws IOException {
        // create a new simulation

        StandardOpenOption standardOpenOption;
        CSVFormat csvFormat;

        if (Files.exists(Paths.get(SHOTS_CSV))) {
            standardOpenOption = StandardOpenOption.APPEND;
            csvFormat = CSVFormat.DEFAULT;
        } else {
            standardOpenOption = StandardOpenOption.CREATE;
            csvFormat = CSVFormat.DEFAULT.withHeader("ID", "NAME", "SEX", "AGE", "FUNCTION", "N SHOT", "X", "Y", "VALUE");
        }

        BufferedWriter writer = Files.newBufferedWriter(Paths.get(SHOTS_CSV), standardOpenOption);
        CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat);

        JFrame frame = new JFrame("Kriging");


        SimulationManager simulationManager = new SimulationManager(null);

        Kriging k = new Kriging(simulationManager, csvPrinter);
        frame.setContentPane(k.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);


        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    csvPrinter.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private ArrayList selectFunction(JComboBox comboBox) {

        selectedFunctionString = comboBox.getSelectedItem().toString();
        String functionPlotString = null;

        ArrayList functionOptions = new ArrayList();

        switch (selectedFunctionString) {

            case ("Simple Function"):

                selectedFunction = new SimpleShotValueFunction();
                functionPlotString = "simple";
                functionOptions.add(functionPlotString);
                functionOptions.add(selectedFunction);
                return functionOptions;

            case ("Parabolic Function"):

                selectedFunction = new ParabolicValueFunction();
                functionPlotString = "parabolic";
                functionOptions.add(functionPlotString);
                functionOptions.add(selectedFunction);
                return functionOptions;

            case ("Himmelblau Function"):

                selectedFunction = new HimmelblauValueFunction();
                functionPlotString = "himmelblau";
                functionOptions.add(functionPlotString);
                functionOptions.add(selectedFunction);
                return functionOptions;

            case ("Griewank Function"):

                selectedFunction = new GriewankFunction();
                functionPlotString = "griewank";
                functionOptions.add(functionPlotString);
                functionOptions.add(selectedFunction);
                return functionOptions;

            case ("Beale Function"):

                selectedFunction = new BealeFunction();
                functionPlotString = "beale";
                functionOptions.add(functionPlotString);
                functionOptions.add(selectedFunction);
                return functionOptions;

            case ("Corner Function"):

                selectedFunction = new CornerCaseTestingFunction();
                functionPlotString = "corner";
                functionOptions.add(functionPlotString);
                functionOptions.add(selectedFunction);
                return functionOptions;


            case ("Styblinski Function"):

                selectedFunction = new StyblinskiFunction();
                functionPlotString = "styblinski";
                functionOptions.add(functionPlotString);
                functionOptions.add(selectedFunction);
                return functionOptions;
        }

        return null;
    }
}
