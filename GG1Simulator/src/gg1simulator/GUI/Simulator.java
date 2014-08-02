/*
 * Simulator.java
 */
package gg1simulator.GUI;

import gg1simulator.DeterministicDistribution;
import gg1simulator.Distribution;
import gg1simulator.ExponentialDistribution;
import gg1simulator.PlotGenerator;
import gg1simulator.UniformDistribution;
import gg1simulator.Utility;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public abstract class Simulator extends JApplet {

    private HashMap<String, JComponent> coltrolsMap = new HashMap<>();
    private String M = "Exponential (M)";
    private String G = "Uniform (G)";
    private String D = "Deterministic (D)";
    private String[] distributions = new String[]{M, G, D};
    private String[] numbers = new String[]{"10", "15", "20"};
    private int m_width = 1200;
    private int m_height = 700;
    private static final String STEPS = "Steps";
    private static final String ARRIVAL = "Arrival";
    private static final String SERVICEONE = "ServiceOne";
    private static final String SERVICETWO = "ServiceTwo";
    private static final String SERVICETHREE = "ServiceThree";
    private static final String SERVICEFOUR = "ServiceFour";
    private static final String SERVICEFIVE = "ServiceFive";
    private static final String LAMBDA = "Lambda";
    private static final String CONSTANT = "Constant";
    private static final String UNIFORMLOWER = "Uniform lower";
    private static final String UNIFORMUPPER = "Uniform upper";
    private static final String PRECENTAGE = "Precentage";
    private static final String NUMBEROFBARS = "bars";
    private static final String INTERVAL = "Interval";
    private static final String P = "p";
    private static final String P1 = "p1";
    private static final String P2 = "p2";
    private static final String P3 = "p3";
    private static final String P4 = "p4";
    private static final String NUMOFTERMINALS = "numOfTerminals";
    private static final String NUMOFSERVERS = "numOfServers";
    private String imageName = null;
    private Image bgImage;
    protected JPanel mainResultPanel;
    protected JScrollPane resultScrollPanel;
    private int numOfServers;
    public int modelType; // 0: non feedback model; 1: feedback model

    public void setNumOfServers(int servers) {
        numOfServers = servers;
    }

    public void setModelType(int type) {
        modelType = type;
    }

    public void setImageName(String name) {
        imageName = name;
    }

    public abstract JPanel createSimButtons();

    public abstract JPanel createChartPanel();

    public abstract JPanel createServiceDistributionPanel();

    public abstract boolean checkErrors();

    public static String getARRIVAL() {
        return ARRIVAL;

    }

    public static String getSERVICEONE() {
        return SERVICEONE;
    }

    public static String getSERVICETWO() {
        return SERVICETWO;
    }

    public static String getSERVICETHREE() {
        return SERVICETHREE;
    }

    public static String getSERVICEFOUR() {
        return SERVICEFOUR;
    }

    public static String getSERVICEFIVE() {
        return SERVICEFIVE;
    }

    public static String getCONSTANT() {
        return CONSTANT;
    }

    public HashMap<String, JComponent> getColtrolsMap() {
        return coltrolsMap;
    }

    public static String getSTEPS() {
        return STEPS;
    }

    public static String getLAMBDA() {
        return LAMBDA;
    }

    public static String getUNIFORMLOWER() {
        return UNIFORMLOWER;
    }

    public static String getUNIFORMUPPER() {
        return UNIFORMUPPER;
    }

    public static String getPRECENTAGE() {
        return PRECENTAGE;
    }

    public static String getNUMBEROFBARS() {
        return NUMBEROFBARS;
    }

    public static String getP() {
        return P;
    }

    public static String getP1() {
        return P1;
    }

    public static String getP2() {
        return P2;
    }

    public static String getP3() {
        return P3;
    }

    public static String getP4() {
        return P4;
    }

    public static String getInterval() {
        return INTERVAL;
    }

    public static String getNumOfTerminals() {
        return NUMOFTERMINALS;
    }

    public static String getNumOfServers() {
        return NUMOFSERVERS;
    }

    @Override
    public void init() {
        this.setSize(m_width, m_height);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - m_width) / 2, (screenSize.height - m_height) / 2);
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Simulator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        JPanel leftUpPanel = new JPanel(new BorderLayout());
        leftUpPanel.setBorder(BorderFactory.createTitledBorder("Simulatoion controls"));

        JPanel distributionPanel = new JPanel();
        distributionPanel.setLayout(new BoxLayout(distributionPanel, BoxLayout.Y_AXIS));

        JPanel imagePanel = new JPanel();
        imagePanel.setBorder(BorderFactory.createCompoundBorder());
        try {
            bgImage = getImage(new URL(getCodeBase(), imageName));
        } catch (MalformedURLException ex) {
            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
        }

        JLabel imageLabel = new JLabel(new ImageIcon(bgImage));
        imageLabel.repaint();
        imagePanel.add(imageLabel);
        distributionPanel.add(imagePanel);

        JPanel placePanel = new JPanel(new GridLayout(0, 1));
        placePanel.setBorder(BorderFactory.createTitledBorder("Simulation Parameters"));
        JPanel simParametersPanel;


        simParametersPanel = new JPanel(new GridLayout(0, 3));
        simParametersPanel.add(createSimulationParameters());
        simParametersPanel.add(createBarsControlPanel());
        simParametersPanel.add(createBarsInterval());

        placePanel.add(simParametersPanel);
        if (modelType == 1) {
            JPanel simParameterPanel2 = new JPanel(new GridLayout(0, 1));
            simParameterPanel2.add(createPrecentagePanel(getP()));
            placePanel.add(simParameterPanel2);
        }
        if (modelType == 3) {
            JPanel simParameterPanel2 = new JPanel(new GridLayout(0, 2));
            simParameterPanel2.add(createTerminalsPanel());
            simParameterPanel2.add(createServersPanel());
            placePanel.add(simParameterPanel2);
        }
        if (modelType == 4) {
            JPanel simParameterPanel2 = new JPanel(new GridLayout(0, 1));
            simParameterPanel2.add(createServersPanel());
            placePanel.add(simParameterPanel2);
        }
        distributionPanel.add(placePanel);


        JPanel arrivalDistributionPanel = new JPanel(new GridLayout(0, 1));
        String title = "Arrival Distribution (IAT = Interarrival Time)";
        if (modelType == 3) {
            title = "Think Time Distribution (TT = Think Time)";
        }
        arrivalDistributionPanel.setBorder(BorderFactory.createTitledBorder(title));
        arrivalDistributionPanel.add(createDistributionControl(getARRIVAL(), "", 0));
        distributionPanel.add(arrivalDistributionPanel);


        distributionPanel.add(createServiceDistributionPanel());

        leftUpPanel.add(distributionPanel, BorderLayout.CENTER);
        leftUpPanel.add(createSimButtons(), BorderLayout.SOUTH);
        leftPanel.add(leftUpPanel);

        leftPanel.add(createResultPanel());

        mainPanel.add(leftPanel);
        mainPanel.add(createChartPanel());

        this.setContentPane(mainPanel);

        ((JTextField) coltrolsMap.get(STEPS)).setText("1000000");
        ((JTextField) coltrolsMap.get(ARRIVAL + "-" + LAMBDA)).setText("2");
        ((JTextField) coltrolsMap.get(SERVICEONE + "-" + LAMBDA)).setText("1");
        ((JTextField) coltrolsMap.get(SERVICEONE + "-" + LAMBDA)).setText("1");
        ((JTextField) coltrolsMap.get(INTERVAL)).setText("1");
        this.setVisible(true);
    }

    public JPanel createResultPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setPreferredSize(new Dimension(550, 400));
        String title;
        if(modelType != 2){
            title = "Simulation Results (Values in brackets are analytic results for exponential model)";
        }else{
            title = "Simulation Results (Values in brackets are analytic results)";
        }
        resultPanel.setBorder(BorderFactory.createTitledBorder(title));

        mainResultPanel = new JPanel(new GridLayout(0, 1));
        resultScrollPanel = new JScrollPane(mainResultPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        resultScrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        resultPanel.add(resultScrollPanel);
        return resultPanel;
    }

    public JPanel createSimulationParameters() {
        JPanel simParameters = new JPanel(new FlowLayout(FlowLayout.LEFT));
        simParameters.add(new JLabel("Served jobs: "));
        JTextField txtStep = new JTextField();
        txtStep.setPreferredSize(new Dimension(50, 24));
        simParameters.add(txtStep);
        coltrolsMap.put(STEPS, txtStep);
        return simParameters;
    }

    public JPanel createPrecentagePanel(String name) {
        JPanel precentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        precentPanel.add(new JLabel(name + ": "));
        JTextField txtPrecent = new JTextField();
        txtPrecent.setPreferredSize(new Dimension(50, 24));
        precentPanel.add(txtPrecent);
        coltrolsMap.put(name, txtPrecent);
        return precentPanel;
    }

    public JPanel createDistributionControl(final String name, final String number, final int type) {
        final JPanel disPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String title;
        if(name.contains("Arrival")){
            title = "Type:";
        }else{
            title = "Server " + number + "Type:";
        }
        disPanel.add(new JLabel(title));
        JComboBox distribution = new JComboBox(distributions);
        distribution.setSelectedIndex(0);


        distribution.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (e.getItem().equals(M)) {
                        disPanel.add(createExponentialFields(name, number, type));
                    } else if (e.getItem().equals(G)) {
                        disPanel.add(createUniformFields(name, number, type));
                    } else if (e.getItem().equals(D)) {
                        disPanel.add(createDeterministicFields(name, number, type));
                    }
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    disPanel.remove(2);
                }
                disPanel.validate();
                disPanel.repaint();
            }
        });
        coltrolsMap.put(name, distribution);
        disPanel.add(distribution);
        disPanel.add(createExponentialFields(name, number, type));
        return disPanel;
    }

    public JPanel createExponentialFields(String name, String number, int type) {
        JPanel eFieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

//        if (type == 0 && !number.equals("")) {
//            int counter = Integer.parseInt(number.substring(1, 2));
//            if (counter != numOfServers) {
//                if (number.equals("#1 ")) {
//                    eFieldPanel.add(createPrecentagePanel(getP1()));
//                }
//                if (number.equals("#2 ")) {
//                    eFieldPanel.add(createPrecentagePanel(getP2()));
//                }
//                if (number.equals("#3 ")) {
//                    eFieldPanel.add(createPrecentagePanel(getP3()));
//                }
//            }
//        }
        String labelname;
        if (name.contains("Arrival")) {
            if (modelType == 3) {
                labelname = "Mean TT:";
            } else {
                labelname = "Mean IAT:";
            }
        } else {
            labelname = "Mean ST:";
        }
        JLabel eFieldLabel = new JLabel(labelname);
        eFieldPanel.add(eFieldLabel);

        JTextField eField = new JTextField();
        eField.setPreferredSize(new Dimension(50, 24));
        eFieldPanel.add(eField);

        coltrolsMap.put(name + "-" + LAMBDA, eField);
        return eFieldPanel;
    }

    public JPanel createUniformFields(String name, String number, int type) {
        JPanel uFieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        if (type == 0 && !number.equals("")) {
//            int counter = Integer.parseInt(number.substring(1, 2));
//            if (counter != numOfServers) {
//                if (number.equals("#1 ")) {
//                    uFieldPanel.add(createPrecentagePanel(getP1()));
//                }
//                if (number.equals("#2 ")) {
//                    uFieldPanel.add(createPrecentagePanel(getP2()));
//                }
//                if (number.equals("#3 ")) {
//                    uFieldPanel.add(createPrecentagePanel(getP3()));
//                }
//            }
//        }
        String labelname;
        if (name.contains("Arrival")) {
            if (modelType == 3) {
                labelname = "TT:";
            } else {
                labelname = "IAT:";
            }
        } else {
            labelname = "ST:";
        }
        JLabel uFieldLowerLabel = new JLabel("Min " + labelname);
        uFieldPanel.add(uFieldLowerLabel);
        JTextField uLowerBoundField = new JTextField();
        uLowerBoundField.setPreferredSize(new Dimension(50, 24));
        uFieldPanel.add(uLowerBoundField);
        coltrolsMap.put(name + "-" + UNIFORMLOWER, uLowerBoundField);

        JLabel uFieldUpperLabekl = new JLabel("Max " + labelname);
        uFieldPanel.add(uFieldUpperLabekl);
        JTextField uUpperBoundField = new JTextField();
        uUpperBoundField.setPreferredSize(new Dimension(50, 24));
        uFieldPanel.add(uUpperBoundField);
        coltrolsMap.put(name + "-" + UNIFORMUPPER, uUpperBoundField);

        return uFieldPanel;
    }

    public JPanel createDeterministicFields(String name, String number, int type) {
        JPanel dFieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        if (type == 0 && !number.equals("")) {
//            int counter = Integer.parseInt(number.substring(1, 2));
//            if (counter != numOfServers) {
//                if (number.equals("#1 ")) {
//                    dFieldPanel.add(createPrecentagePanel(getP1()));
//                }
//                if (number.equals("#2 ")) {
//                    dFieldPanel.add(createPrecentagePanel(getP2()));
//                }
//                if (number.equals("#3 ")) {
//                    dFieldPanel.add(createPrecentagePanel(getP3()));
//                }
//            }
//        }
        String labelname;
        if (name.contains("Arrival")) {
            if (modelType == 3) {
                labelname = "TT:";
            } else {
                labelname = "IAT:";
            }
        } else {
            labelname = "ST:";
        }
        JLabel dFieldLabel = new JLabel("Constant " + labelname);
        dFieldPanel.add(dFieldLabel);
        JTextField dField = new JTextField();
        dField.setPreferredSize(new Dimension(50, 24));
        dFieldPanel.add(dField);
        coltrolsMap.put(name + "-" + CONSTANT, dField);

        return dFieldPanel;
    }

    public JPanel createBarsControlPanel() {
        JPanel barsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel barsLabel = new JLabel("Number of bars: ");
        barsPanel.add(barsLabel);
        JComboBox numberOfBars = new JComboBox(numbers);
        numberOfBars.setPreferredSize(new Dimension(50, 24));
        numberOfBars.setSelectedIndex(1);
        barsPanel.add(numberOfBars);
        coltrolsMap.put(NUMBEROFBARS, numberOfBars);
        return barsPanel;
    }

    public JPanel createBarsInterval() {
        JPanel intervalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel barsInterval = new JLabel("Interval between bars: ");
        intervalPanel.add(barsInterval);
        JTextField dField = new JTextField();
        dField.setPreferredSize(new Dimension(50, 24));
        intervalPanel.add(dField);
        coltrolsMap.put(INTERVAL, dField);

        return intervalPanel;
    }

    public JPanel createTerminalsPanel() {
        JPanel intervalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel barsInterval = new JLabel("Number of terminals: ");
        intervalPanel.add(barsInterval);
        JTextField dField = new JTextField();
        dField.setPreferredSize(new Dimension(50, 24));
        intervalPanel.add(dField);
        coltrolsMap.put(NUMOFTERMINALS, dField);

        return intervalPanel;
    }

    public JPanel createServersPanel() {
        JPanel intervalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel barsInterval = new JLabel("Number of servers: ");
        intervalPanel.add(barsInterval);
        JTextField dField = new JTextField();
        dField.setPreferredSize(new Dimension(50, 24));
        intervalPanel.add(dField);
        if (modelType == 4) {
            JLabel note = new JLabel("(Must be less than 16)");
            intervalPanel.add(note);
        }
        coltrolsMap.put(NUMOFSERVERS, dField);

        return intervalPanel;
    }

    public JPanel createTitle() {
        JPanel titlePanel = new JPanel(new GridLayout(1, 5));
        titlePanel.add(new JLabel(" Indictor"));        
        titlePanel.add(new JLabel("Mean value (μ)"));
        titlePanel.add(new JLabel("Error [%]"));
        titlePanel.add(new JLabel("SD (σ)"));
        titlePanel.add(new JLabel("CV (v=100*μ/σ)"));
        return titlePanel;
    }

    public JPanel createRow(String name, ArrayList<Double> values) {
        JPanel row = new JPanel(new GridLayout(1, 4));

        row.add(new JLabel(" " + name));

        double average = Utility.getMean(values);
        row.add(new JLabel(Utility.formatOutput(average)));

        double stddev = Utility.getSD(values);
        row.add(new JLabel(Utility.formatOutput(stddev)));

        row.add(new JLabel(Utility.formatOutput(100 * stddev / average)));
        return row;
    }

    public JPanel createRow(String name, double avg, double sdv, String real, String error) {
        double res;
        JPanel row = new JPanel(new GridLayout(1, 4));
        row.add(new JLabel(" " + name));
        if (!real.equals("")) {
            row.add(new JLabel(Utility.formatOutput(avg) + " [" + real + "]"));
            row.add(new JLabel(error));
        } else {
            row.add(new JLabel(Utility.formatOutput(avg)));
            row.add(new JLabel(""));
        }
        row.add(new JLabel(Utility.formatOutput(sdv)));
        if (avg == 0) {
            res = 0.000;
        } else {
            res = 100 * sdv / avg;
        }
        row.add(new JLabel(Utility.formatOutput(res)));
        return row;
    }

    public JPanel createRow(String name, double u, String real, String error) {
        JPanel row = new JPanel(new GridLayout(1, 4));
        row.add(new JLabel(" " + name));
        if (!real.equals("")) {
            row.add(new JLabel(Utility.formatOutput(u) + " [" + real + "]"));
            row.add(new JLabel(error));
        } else {
            row.add(new JLabel(Utility.formatOutput(u)));
            row.add(new JLabel(""));
        }
        row.add(new JLabel(""));
        row.add(new JLabel(""));
        return row;
    }

    public ChartPanel createChartPanel(int[] chartArray, double max, String name, int type) {
        JFreeChart chart = PlotGenerator.generateHistogramChart(chartArray, max, name, type);
        ChartPanel panel = new ChartPanel(chart, false);
        panel.setPreferredSize(new Dimension(560, 350));
        return panel;
    }

    public ChartPanel createChartPanel(int[] chartArray, int size, double interval, String name, int type) {
        JFreeChart chart = PlotGenerator.generateHistogramChart2(chartArray, size, interval, name, type);
        ChartPanel panel = new ChartPanel(chart, false);
        panel.setPreferredSize(new Dimension(560, 350));
        return panel;
    }

    public ChartPanel createChartPanel(HashMap<Integer, Double> map, String name, int bars, int type) {
        JFreeChart chart = PlotGenerator.generateHistogram(map, name, bars, type);
        ChartPanel panel = new ChartPanel(chart, false);
        panel.setPreferredSize(new Dimension(560, 350));
        return panel;
    }

    public ChartPanel createChartPanel(ArrayList list, int bars, String name) {
        JFreeChart chart = PlotGenerator.generateHistogramChart1(list, bars, name);
        ChartPanel panel = new ChartPanel(chart, false);
        panel.setPreferredSize(new Dimension(560, 350));
        return panel;
    }

    public double convertStringToDouble(String number) {
        try {
            return Double.parseDouble(number);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, number + " is not a valid number", "Number Error", JOptionPane.ERROR_MESSAGE);
            throw ex;
        }
    }

    public Distribution getDistribution(String source, String type) {
        Distribution distribution = null;
        if (source.equals(M)) {
            double lamdba = convertStringToDouble(((JTextField) coltrolsMap.get(type + "-" + LAMBDA)).getText());
            distribution = new ExponentialDistribution(lamdba);
        } else if (source.equals(G)) {
            double lowerBound = convertStringToDouble(((JTextField) coltrolsMap.get(type + "-" + UNIFORMLOWER)).getText());
            double upperBound = convertStringToDouble(((JTextField) coltrolsMap.get(type + "-" + UNIFORMUPPER)).getText());
            distribution = new UniformDistribution(lowerBound, upperBound);
        } else if (source.equals(D)) {
            double constant = convertStringToDouble(((JTextField) coltrolsMap.get(type + "-" + CONSTANT)).getText());
            distribution = new DeterministicDistribution(constant);
        }
        return distribution;
    }
}
