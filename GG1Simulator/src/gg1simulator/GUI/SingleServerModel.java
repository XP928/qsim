/*
 * SingleServerModel.java
 */
package gg1simulator.GUI;

import gg1simulator.Calculator;
import gg1simulator.Distribution;
import gg1simulator.GG1Calculator;
import gg1simulator.GG1Simulator;
import gg1simulator.MD1Calculator;
import gg1simulator.MG1Calculator;
import gg1simulator.MM1Calculator;
import gg1simulator.UniformDistribution;
import gg1simulator.Utility;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class SingleServerModel extends Simulator {

    private JPanel myChartPanel;
    private JScrollPane mainChartPanel;
    private boolean flag = false;
    private int preArrivals, preDepartures;
    private double preInterval;
    private int[] preInterArrivalTime, preServiceTime, preInterDepartureTime, preResponseTime;
    private HashMap<Integer, Double> preQueueMap;

    @Override
    public void init() {
        super.setModelType(2);
        super.setImageName("SingleServer.jpg");
        super.setNumOfServers(1);
        super.init();
    }

    @Override
    public JPanel createSimButtons() {

        JPanel btnPanel = new JPanel(new BorderLayout());
        JLabel msgLabel = new JLabel();
        msgLabel.setPreferredSize(new Dimension(580, 18));
        JPanel buttonPanel = new JPanel();
        JButton btnSim = new JButton("Run Simulator");
        btnSim.setPreferredSize(new Dimension(100, 24));


        btnSim.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(flag);
                if (checkErrors()) {
                    return;
                }
                int steps, bars;
                double interval;
                String arrivalStr, serviceOneStr;
                Distribution arrivalDistribution, serviceOneDistribution;
                steps = Integer.parseInt(((JTextField) getColtrolsMap().get(getSTEPS())).getText());
                bars = Integer.parseInt(((JComboBox) getColtrolsMap().get(getNUMBEROFBARS())).getSelectedItem().toString());
                arrivalStr = ((JComboBox) getColtrolsMap().get(getARRIVAL())).getSelectedItem().toString();
                serviceOneStr = ((JComboBox) getColtrolsMap().get(getSERVICEONE())).getSelectedItem().toString();
                interval = Double.parseDouble(((JTextField) getColtrolsMap().get(getInterval())).getText());

                arrivalDistribution = getDistribution(arrivalStr, getARRIVAL());
                serviceOneDistribution = getDistribution(serviceOneStr, getSERVICEONE());



                GG1Simulator simulator = new GG1Simulator(arrivalDistribution, serviceOneDistribution, bars, interval);
                Calculator c = new MM1Calculator(arrivalDistribution.getMean(), serviceOneDistribution.getMean());;
                int temp = 1;
                if (arrivalStr.equals("Exponential (M)")) {
                    temp = 0;
                    switch (serviceOneStr) {
                        case "Exponential (M)":
                            c = new MM1Calculator(arrivalDistribution.getMean(), serviceOneDistribution.getMean());
                            break;
                        case "Uniform (G)":
                            c = new MG1Calculator(arrivalDistribution.getMean(), ((UniformDistribution) serviceOneDistribution).getMin(), ((UniformDistribution) serviceOneDistribution).getMax());
                            break;
                        case "Deterministic (D)":
                            c = new MD1Calculator(arrivalDistribution.getMean(), serviceOneDistribution.getMean());
                            break;
                    }
                }
                if (arrivalStr.equals("Uniform (G)") && serviceOneStr.equals("Uniform (G)")) {
                    temp = 0;
                    c = new GG1Calculator(((UniformDistribution) arrivalDistribution).getMin(), ((UniformDistribution) arrivalDistribution).getMax(),
                            ((UniformDistribution) serviceOneDistribution).getMin(), ((UniformDistribution) serviceOneDistribution).getMax());
                }
                simulator.run(steps);
                mainResultPanel.removeAll();
                mainResultPanel.add(createTitle());


                double avg, sdv, realAvg, error;
                String realValue, errorStr;
                
                avg = simulator.getUtilization();
                realAvg = c.getUtilization();
                realValue = Utility.formatOutput(realAvg);
                error = 100 * (avg - realAvg) / realAvg;
                if(temp == 1){
                    error *= -1;
                }
                errorStr = Utility.formatOutput(error);
                mainResultPanel.add(createRow("Server Utilization", avg, realValue, errorStr));
                
                avg = simulator.getAvgInterArrivalTime();
                sdv = simulator.getSDInterArrivalTime();
                realAvg = c.getArrivalMean();
                realValue = Utility.formatOutput(realAvg);
                error = 100 * (avg - realAvg) / realAvg;
                if(temp == 1){
                    error *= -1;
                }
                errorStr = Utility.formatOutput(error);
                mainResultPanel.add(createRow("Interarrival time", avg, sdv, realValue, errorStr));


                avg = simulator.getAvgServiceTime();
                sdv = simulator.getSDServiceTime();
                realAvg = c.getServiceMean();
                realValue = Utility.formatOutput(realAvg);
                error = 100 * (avg - realAvg) / realAvg;
                if(temp == 1){
                    error *= -1;
                }
                errorStr = Utility.formatOutput(error);
                mainResultPanel.add(createRow("Service time", avg, sdv, realValue, errorStr));
                
                avg = simulator.getAvgInterDeparutreTime();
                sdv = simulator.getSDInterDeparutreTime();
                realValue = "";
                mainResultPanel.add(createRow("Interdeparture time", avg, sdv, realValue, errorStr));
                
                avg = 1 / avg;
                realValue = "";
                mainResultPanel.add(createRow("Throughput", avg, realValue, errorStr));

                avg = simulator.getAvgResponseTime();
                sdv = simulator.getSDResponseTime();
                realAvg = c.getResponseTime();

                realValue = Utility.formatOutput(realAvg);
                error = 100 * (avg - realAvg) / realAvg;
                if(temp == 1){
                    error *= -1;
                }
                errorStr = Utility.formatOutput(error);
                mainResultPanel.add(createRow("Response time", avg, sdv, realValue, errorStr));


                avg = Utility.getAverage(simulator.getQueueMap());
                sdv = Utility.getStandardDeviation(simulator.getQueueMap(), avg);
                realAvg = c.getQueueLength();
                if (realAvg == 0) {
                    realValue = "";
                } else {
                    realValue = Utility.formatOutput(realAvg);
                }
                error = 100 * (avg - realAvg) / realAvg;
                if(temp == 1){
                    error *= -1;
                }
                errorStr = Utility.formatOutput(error);
                mainResultPanel.add(createRow("Number of jobs", avg, sdv, realValue, errorStr));

                mainResultPanel.revalidate();
                mainResultPanel.repaint();

                myChartPanel.removeAll();

                if (flag == true) {
                    myChartPanel.add(createChartPanel(preInterArrivalTime, preArrivals, preInterval, "Interarrival Time (Previous Exp)", 0));
                }

                myChartPanel.add(createChartPanel(simulator.getInterArrivalList(), simulator.getArrivals(), interval, "Interarrival Time", 1));

                if (flag == true) {
                    myChartPanel.add(createChartPanel(preServiceTime, preDepartures, preInterval, "Service Time (Previous Exp)", 0));
                }

                myChartPanel.add(createChartPanel(simulator.getServiceList(), simulator.getDepartures(), interval, "Service Time", 1));

                if (flag == true) {
                    myChartPanel.add(createChartPanel(preInterDepartureTime, preDepartures, preInterval, "Interdeparture Time (Previous Exp)", 0));
                }

                myChartPanel.add(createChartPanel(simulator.getInterDepartureList(), simulator.getDepartures(), interval, "Interdeparture Time", 1));

                if (flag == true) {
                    myChartPanel.add(createChartPanel(preResponseTime, preDepartures, preInterval, "Response Time (Previous Exp)", 0));
                }

                myChartPanel.add(createChartPanel(simulator.getResponseTimeList(), simulator.getDepartures(), interval, "Response Time", 1));

                if (flag == true) {
                    myChartPanel.add(createChartPanel(preQueueMap, "Number of jobs (Previous Exp)", bars, 0));
                }

                preQueueMap = (HashMap) simulator.getQueueMap().clone();
                myChartPanel.add(createChartPanel(simulator.getQueueMap(), "Number of jobs", bars, 1));


                myChartPanel.revalidate();
                myChartPanel.repaint();

                flag = true;
                preInterArrivalTime = new int[bars];
                System.arraycopy(simulator.getInterArrivalList(), 0, preInterArrivalTime, 0, bars);
                preServiceTime = new int[bars];
                System.arraycopy(simulator.getServiceList(), 0, preServiceTime, 0, bars);
                preInterDepartureTime = new int[bars];
                System.arraycopy(simulator.getInterDepartureList(), 0, preInterDepartureTime, 0, bars);
                preResponseTime = new int[bars];
                System.arraycopy(simulator.getResponseTimeList(), 0, preResponseTime, 0, bars);

                preArrivals = simulator.getArrivals();
                preDepartures = simulator.getDepartures();
                preInterval = interval;



            }
        });
        buttonPanel.add(btnSim);
        btnPanel.add(msgLabel, BorderLayout.NORTH);
        btnPanel.add(buttonPanel, BorderLayout.CENTER);
        return btnPanel;
    }

    @Override
    public JPanel createChartPanel() {
        JPanel rightPanel = new JPanel();

        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Simulatoion Charts (The results of previous experiment are shown in blue)"));
        myChartPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        mainChartPanel = new JScrollPane(myChartPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainChartPanel.getVerticalScrollBar().setUnitIncrement(16);
        rightPanel.add(mainChartPanel);
        return rightPanel;
    }

    @Override
    public JPanel createServiceDistributionPanel() {
        JPanel serviceDistributionPanel = new JPanel(new GridLayout(0, 1));
        serviceDistributionPanel.setBorder(BorderFactory.createTitledBorder("Service Distribution (ST = Service Time)"));
        serviceDistributionPanel.add(createDistributionControl(getSERVICEONE(), "", 0));
        return serviceDistributionPanel;
    }

    @Override
    public boolean checkErrors() {
        int arrival, service;
        double arrivalMean = 0, serviceMean = 0;
        double tmp1, tmp2;
        arrival = ((JComboBox) getColtrolsMap().get(getARRIVAL())).getSelectedIndex();
        service = ((JComboBox) getColtrolsMap().get(getSERVICEONE())).getSelectedIndex();
        String errorName = "";
        try {
            errorName = "the number of jobs ";
            int jobs = Integer.parseInt(((JTextField) getColtrolsMap().get(getSTEPS())).getText());
            if (jobs < 0) {
                errorName += "must be a postive integer";
                JOptionPane.showMessageDialog(null, errorName);
                return true;
            }
            errorName = "the interval of histogram ";
            double inerval = Double.parseDouble(((JTextField) getColtrolsMap().get(getInterval())).getText());
            if (inerval <= 0) {
                errorName += "must be a postive number";
                JOptionPane.showMessageDialog(null, errorName);
                return true;
            }
            if (arrival == 0) {
                errorName = "Arrival Rate ";
                tmp1 = Double.parseDouble(((JTextField) getColtrolsMap().get(getARRIVAL() + "-" + getLAMBDA())).getText());
                if (tmp1 < 0) {
                    errorName += "must be a postive number";
                    JOptionPane.showMessageDialog(null, errorName);
                    return true;
                }
                arrivalMean = tmp1;
            }
            if (arrival == 1) {
                errorName = "Lower bound for arrival distribution ";
                tmp1 = Double.parseDouble(((JTextField) getColtrolsMap().get(getARRIVAL() + "-" + getUNIFORMLOWER())).getText());
                if (tmp1 < 0) {
                    errorName += "must be a postive number";
                    JOptionPane.showMessageDialog(null, errorName);
                    return true;
                }
                errorName = "Upper bound for arrival distribution ";
                tmp2 = Double.parseDouble(((JTextField) getColtrolsMap().get(getARRIVAL() + "-" + getUNIFORMUPPER())).getText());
                if (tmp2 < tmp1) {
                    errorName += "must greater than lower bound";
                    JOptionPane.showMessageDialog(null, errorName);
                    return true;
                }
                arrivalMean = (tmp1 + tmp2) / 2;
            }
            if (arrival == 2) {
                errorName = "Constant of arrival distribution ";
                tmp1 = Double.parseDouble(((JTextField) getColtrolsMap().get(getARRIVAL() + "-" + getCONSTANT())).getText());
                if (tmp1 < 0) {
                    errorName += "must be a postive number";
                    JOptionPane.showMessageDialog(null, errorName);
                    return true;
                }
                arrivalMean = tmp1;
            }
            if (service == 0) {
                errorName = "Service Rate ";
                tmp1 = Double.parseDouble(((JTextField) getColtrolsMap().get(getSERVICEONE() + "-" + getLAMBDA())).getText());
                if (tmp1 < 0) {
                    errorName += "must be a postive number";
                    JOptionPane.showMessageDialog(null, errorName);
                    return true;
                }
                serviceMean = tmp1;
            }
            if (service == 1) {
                errorName = "Lower bound for service distribution ";
                tmp1 = Double.parseDouble(((JTextField) getColtrolsMap().get(getSERVICEONE() + "-" + getUNIFORMLOWER())).getText());
                if (tmp1 < 0) {
                    errorName += "must be a postive number";
                    JOptionPane.showMessageDialog(null, errorName);
                    return true;
                }
                errorName = "Upper bound for service distribution ";
                tmp2 = Double.parseDouble(((JTextField) getColtrolsMap().get(getSERVICEONE() + "-" + getUNIFORMUPPER())).getText());
                if (tmp2 < tmp1) {
                    errorName += "must greater than lower bound";
                    JOptionPane.showMessageDialog(null, errorName);
                    return true;
                }
                serviceMean = (tmp1 + tmp2) / 2;
            }
            if (service == 2) {
                errorName = "Constant of service distribution ";
                tmp1 = Double.parseDouble(((JTextField) getColtrolsMap().get(getSERVICEONE() + "-" + getCONSTANT())).getText());
                if (tmp1 < 0) {
                    errorName += "must be a postive number";
                    JOptionPane.showMessageDialog(null, errorName);
                    return true;
                }
                serviceMean = tmp1;
            }
            if (arrivalMean - serviceMean < 0) {
                JOptionPane.showMessageDialog(null, "Mean service time must less than mean arrival time");
                return true;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, errorName + "is not a invalid number");
            return true;
        }

        return false;
    }
}
