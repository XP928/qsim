/*
 * FeedbackModel.java
 */
package gg1simulator.GUI;

import gg1simulator.Distribution;
import gg1simulator.FeedbackQueueCalculator;
import gg1simulator.FeedbackQueueSimulator;
import gg1simulator.Utility;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


public class FeedbackModel extends Simulator {

    private JPanel myChartPanel;
    private JScrollPane mainChartPanel;
    private boolean flag = false;
    private int[] preInterArrivalTimeList, preTotalInterArrivalTimeList, preInterDepartureTimeList, preMidArrivalTimeList,
            preInterMidDepartureTimeList, preServiceTimeList1, preServiceTimeList, preResponseTimeList;
    private int preTotalArrivals, preArrivals1, preMidArrivals, preDepartures;
    private double preInterval;
    private HashMap<Integer, Double> preQueueMap = new HashMap<>();

    @Override
    public void init() {
        super.setImageName("FeedbackSingleServer.jpg");
        super.setModelType(1);
        super.setNumOfServers(2);
        super.init();
        ((JTextField) getColtrolsMap().get(getP())).setText("50");
        ((JTextField) getColtrolsMap().get(getARRIVAL() + "-" + getLAMBDA())).setText("4");
        ((JTextField) getColtrolsMap().get(getSERVICEONE() + "-" + getLAMBDA())).setText("1");
        ((JTextField) getColtrolsMap().get(getSERVICETWO() + "-" + getLAMBDA())).setText("1");
        this.setVisible(true);
    }

    @Override
    public JPanel createSimButtons() {
        JPanel btnPanel = new JPanel();

        JButton btnSim = new JButton("Run Simulator");
        btnSim.setPreferredSize(new Dimension(100, 24));
        btnSim.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (checkErrors()) {
                    return;
                }
                int steps, bars;
                double p, interval;
                String arrivalStr, serviceOneStr, serviceTwoStr;
                Distribution arrivalDistribution, serviceOneDistribution, serviceTwoDistribution;
                steps = Integer.parseInt(((JTextField) getColtrolsMap().get(getSTEPS())).getText());
                bars = Integer.parseInt(((JComboBox) getColtrolsMap().get(getNUMBEROFBARS())).getSelectedItem().toString());
                p = Double.parseDouble(((JTextField) getColtrolsMap().get(getP())).getText()) / 100;
                arrivalStr = ((JComboBox) getColtrolsMap().get(getARRIVAL())).getSelectedItem().toString();
                serviceOneStr = ((JComboBox) getColtrolsMap().get(getSERVICEONE())).getSelectedItem().toString();
                serviceTwoStr = ((JComboBox) getColtrolsMap().get(getSERVICETWO())).getSelectedItem().toString();
                interval = Double.parseDouble(((JTextField) getColtrolsMap().get(getInterval())).getText());

                arrivalDistribution = getDistribution(arrivalStr, getARRIVAL());
                serviceOneDistribution = getDistribution(serviceOneStr, getSERVICEONE());
                serviceTwoDistribution = getDistribution(serviceTwoStr, getSERVICETWO());

                FeedbackQueueSimulator simulator = new FeedbackQueueSimulator(arrivalDistribution, serviceOneDistribution, serviceTwoDistribution, bars, interval, p);
                //simulator.getMaxValues();
                simulator.run(steps);

                FeedbackQueueCalculator c = new FeedbackQueueCalculator(arrivalDistribution.getMean(), serviceOneDistribution.getMean(), serviceTwoDistribution.getMean(), p);
                mainResultPanel.removeAll();
                mainResultPanel.add(createTitle());

                int temp = 1;
                if (arrivalDistribution.equals("Exponential (M)") && serviceOneDistribution.equals("Exponential (M)") && serviceTwoDistribution.equals("Exponential (M)")) {
                    temp = 0;
                }

                double avg, sdv, realAvg, error;
                String realValue, errorStr;
                avg = simulator.getFirstUtilization();
                realAvg = c.getFirstUtilization();
                realValue = Utility.formatOutput(realAvg);
                error = 100 * (avg - realAvg) / realAvg;
                if (temp == 1) {
                    error *= -1;
                }
                errorStr = Utility.formatOutput(error);
                mainResultPanel.add(createRow("#1 Utilization", avg, realValue, errorStr));
                
                avg = simulator.getSecondUtilization();
                realAvg = c.getSecondUtilization();
                realValue = Utility.formatOutput(realAvg);
                error = 100 * (avg - realAvg) / realAvg;
                if (temp == 1) {
                    error *= -1;
                }
                errorStr = Utility.formatOutput(error);
                mainResultPanel.add(createRow("#2 Utilization", avg, realValue, errorStr));

                avg = simulator.getAvgInterArrivalTime();
                sdv = simulator.getSDInterArrivalTime();
                realAvg = c.getMeanArrivalTime();
                realValue = Utility.formatOutput(realAvg);
                error = 100 * (avg - realAvg) / realAvg;
                if (temp == 1) {
                    error *= -1;
                }
                errorStr = Utility.formatOutput(error);
                mainResultPanel.add(createRow("Interarrival time", avg, sdv, realValue, errorStr));

                avg = simulator.getAvgTotalInterArrivalTime();
                sdv = simulator.getSDTotalInterArrivalTime();
                errorStr = "";
                mainResultPanel.add(createRow("#1 Interarrival time", avg, sdv, "" , errorStr));

                avg = simulator.getAvgMidArrivalTime();
                sdv = simulator.getSDMidArrivalTime();
                errorStr = "";
                mainResultPanel.add(createRow("#2 Interarrival time", avg, sdv, "", errorStr));

                avg = simulator.getAvgServiceTime1();
                sdv = simulator.getSDServiceTime1();
                realAvg = c.getMeanFirstServiceTime();
                realValue = Utility.formatOutput(realAvg);
                error = 100 * (avg - realAvg) / realAvg;
                if (temp == 1) {
                    error *= -1;
                }
                errorStr = Utility.formatOutput(error);
                mainResultPanel.add(createRow("#1 Service time", avg, sdv, realValue, errorStr));

                avg = simulator.getAvgServiceTime();
                sdv = simulator.getSDServiceTime();
                realAvg = c.getMeanSecondServiceTime();
                realValue = Utility.formatOutput(realAvg);
                error = 100 * (avg - realAvg) / realAvg;
                if (temp == 1) {
                    error *= -1;
                }
                errorStr = Utility.formatOutput(error);
                mainResultPanel.add(createRow("#2 Service time", avg, sdv, realValue, errorStr));

                avg = simulator.getAvgInterDepartureTime();
                sdv = simulator.getSDInterDepartureTime();
                errorStr = "";
                mainResultPanel.add(createRow("Interdeparture time", avg, sdv, "", errorStr));
                
                avg = 1 /avg;
                mainResultPanel.add(createRow("Throughput", avg, "", errorStr));

                avg = simulator.getAvgResponseTime();
                sdv = simulator.getSDResponseTime();
                realAvg = c.getResponseTime();
                realValue = Utility.formatOutput(realAvg);
                error = 100 * (avg - realAvg) / realAvg;
                if (temp == 1) {
                    error *= -1;
                }
                errorStr = Utility.formatOutput(error);
                mainResultPanel.add(createRow("Response time", avg, sdv, realValue, errorStr));

                avg = simulator.getAvgQueueLength();
                sdv = simulator.getSDQueueLength();
                realAvg = c.getJobsInTheSystem();
                realValue = Utility.formatOutput(realAvg);
                error = 100 * (avg - realAvg) / realAvg;
                if (temp == 1) {
                    error *= -1;
                }
                errorStr = Utility.formatOutput(error);
                mainResultPanel.add(createRow("Number of jobs", avg, sdv, realValue, errorStr));

                mainResultPanel.revalidate();
                mainResultPanel.repaint();

                myChartPanel.removeAll();

                if (flag) {
                    myChartPanel.add(createChartPanel(preInterArrivalTimeList, preArrivals1, preInterval, "System Interarrival Time  (Previous Exp)", 0));
                }

                myChartPanel.add(createChartPanel(simulator.getInterArrivalTimeList(), simulator.getArrivals1(), interval, "System Interarrival Time", 1));

                if (flag) {
                    myChartPanel.add(createChartPanel(preTotalInterArrivalTimeList, preTotalArrivals, preInterval, "Interarrival Time-Queue #1 (Previous Exp)", 0));
                }

                //myChartPanel.add(createChartPanel(simulator.getTotalInterArrivalTimeList(), simulator.getMaxTotalInterArrivalTime()/10, "Interarrival Time-Server #1", 1));

                myChartPanel.add(createChartPanel(simulator.getTotalInterArrivalTimeList(), simulator.getTotalArrials(), interval, "Interarrival Time-Queue #1", 1));

                if (flag) {
                    myChartPanel.add(createChartPanel(preMidArrivalTimeList, preMidArrivals, preInterval, "Interarrival Time-Queue #2 (Previous Exp)", 0));
                }

                //myChartPanel.add(createChartPanel(simulator.getTotalInterArrivalTimeList(), simulator.getMaxTotalInterArrivalTime(), "Interarrival Time-Server #2", 1));

                myChartPanel.add(createChartPanel(simulator.getMidArrivalTimeList(), simulator.getNumOfMidArrivalJobs(), interval, "Interarrival Time-Queue #2", 1));

                if (flag) {
                    myChartPanel.add(createChartPanel(preInterDepartureTimeList, preDepartures, preInterval, "Interdeparture time (Previous Exp)", 0));
                }

                //myChartPanel.add(createChartPanel(simulator.getInterDepartureTimeList(), simulator.getMaxInterDepartureTime(), "Interdeparture time", 1));
                myChartPanel.add(createChartPanel(simulator.getInterDepartureTimeList(), simulator.getTotalDepartures(), interval, "Interdeparture time", 1));

                if (flag) {
                    myChartPanel.add(createChartPanel(preResponseTimeList, preDepartures, preInterval, "Response time (Previous Exp)", 0));
                }

                //myChartPanel.add(createChartPanel(simulator.getResponseTimeList(), simulator.getMaxResponseTime(), "Response time", 1));

                myChartPanel.add(createChartPanel(simulator.getResponseTimeList(), simulator.getTotalDepartures(), interval, "Response time", 1));

                if (flag) {
                    myChartPanel.add(createChartPanel(preQueueMap, "Number of Jobs (Previous Exp)", bars, 0));
                }
                preQueueMap = (HashMap) simulator.getQueueLength().clone();
                myChartPanel.add(createChartPanel(simulator.getQueueLength(), "Number of Jobs", bars, 1));



                myChartPanel.revalidate();
                myChartPanel.repaint();

                flag = true;
                preInterArrivalTimeList = new int[bars];
                preTotalInterArrivalTimeList = new int[bars];
                preInterDepartureTimeList = new int[bars];
                preMidArrivalTimeList = new int[bars];
                preInterMidDepartureTimeList = new int[bars];
                preServiceTimeList1 = new int[bars];
                preServiceTimeList = new int[bars];
                preResponseTimeList = new int[bars];
                System.arraycopy(simulator.getInterArrivalTimeList(), 0, preInterArrivalTimeList, 0, bars);
                System.arraycopy(simulator.getTotalInterArrivalTimeList(), 0, preTotalInterArrivalTimeList, 0, bars);
                System.arraycopy(simulator.getInterDepartureTimeList(), 0, preInterDepartureTimeList, 0, bars);
                System.arraycopy(simulator.getMidArrivalTimeList(), 0, preMidArrivalTimeList, 0, bars);
                System.arraycopy(simulator.getInterMidDepartureTimeList(), 0, preInterMidDepartureTimeList, 0, bars);
                System.arraycopy(simulator.getServiceTimeList1(), 0, preServiceTimeList1, 0, bars);
                System.arraycopy(simulator.getServiceTimeList(), 0, preServiceTimeList, 0, bars);
                System.arraycopy(simulator.getResponseTimeList(), 0, preResponseTimeList, 0, bars);


                preArrivals1 = simulator.getArrivals1();
                preTotalArrivals = simulator.getTotalArrials();
                preMidArrivals = simulator.getNumOfMidArrivalJobs();
                preDepartures = simulator.getTotalDepartures();
                preInterval = interval;

            }
        });
        btnPanel.add(btnSim);
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
        rightPanel.add(mainChartPanel);
        return rightPanel;
    }

    @Override
    public JPanel createServiceDistributionPanel() {
        JPanel serviceDistributionPanel = new JPanel(new GridLayout(0, 1));
        serviceDistributionPanel.setBorder(BorderFactory.createTitledBorder("Service Distribution (ST = Service Time)"));
        serviceDistributionPanel.add(createDistributionControl(getSERVICEONE(), "#1 ", 1));
        serviceDistributionPanel.add(createDistributionControl(getSERVICETWO(), "#2 ", 0));
        return serviceDistributionPanel;
    }

    @Override
    public boolean checkErrors() {
        int arrival, service1, service2;
        double arrivalMean = 0, serviceMean1 = 0, serviceMean2 = 0;
        double tmp1, tmp2;
        arrival = ((JComboBox) getColtrolsMap().get(getARRIVAL())).getSelectedIndex();
        service1 = ((JComboBox) getColtrolsMap().get(getSERVICEONE())).getSelectedIndex();
        service2 = ((JComboBox) getColtrolsMap().get(getSERVICETWO())).getSelectedIndex();
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
            errorName = "p ";
            double p = Double.parseDouble(((JTextField) getColtrolsMap().get(getP())).getText());
            if (p < 0 || p >= 100) {
                errorName += "must between 0 and 100";
                JOptionPane.showMessageDialog(null, errorName);
                return true;
            }
            p = p / 100;
            if (arrival == 0) {
                errorName = "Mean arrival time ";
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
            if (service1 == 0) {
                errorName = "Mean service time ";
                tmp1 = Double.parseDouble(((JTextField) getColtrolsMap().get(getSERVICEONE() + "-" + getLAMBDA())).getText());
                if (tmp1 < 0) {
                    errorName += "must be a postive number";
                    JOptionPane.showMessageDialog(null, errorName);
                    return true;
                }
                serviceMean1 = tmp1;
            }
            if (service1 == 1) {
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
                serviceMean1 = (tmp1 + tmp2) / 2;
            }
            if (service1 == 2) {
                errorName = "Constant of service distribution ";
                tmp1 = Double.parseDouble(((JTextField) getColtrolsMap().get(getSERVICEONE() + "-" + getCONSTANT())).getText());
                if (tmp1 < 0) {
                    errorName += "must be a postive number";
                    JOptionPane.showMessageDialog(null, errorName);
                    return true;
                }
                serviceMean1 = tmp1;
            }

            if (service2 == 0) {
                errorName = "Service Rate ";
                tmp1 = Double.parseDouble(((JTextField) getColtrolsMap().get(getSERVICETWO() + "-" + getLAMBDA())).getText());
                if (tmp1 < 0) {
                    errorName += "must be a postive number";
                    JOptionPane.showMessageDialog(null, errorName);
                    return true;
                }
                serviceMean2 = tmp1;
            }
            if (service2 == 1) {
                errorName = "Lower bound for service distribution ";
                tmp1 = Double.parseDouble(((JTextField) getColtrolsMap().get(getSERVICETWO() + "-" + getUNIFORMLOWER())).getText());
                if (tmp1 < 0) {
                    errorName += "must be a postive number";
                    JOptionPane.showMessageDialog(null, errorName);
                    return true;
                }
                errorName = "Upper bound for service distribution ";
                tmp2 = Double.parseDouble(((JTextField) getColtrolsMap().get(getSERVICETWO() + "-" + getUNIFORMUPPER())).getText());
                if (tmp2 < tmp1) {
                    errorName += "must greater than lower bound";
                    JOptionPane.showMessageDialog(null, errorName);
                    return true;
                }
                serviceMean2 = (tmp1 + tmp2) / 2;
            }
            if (service2 == 2) {
                errorName = "Constant of service distribution ";
                tmp1 = Double.parseDouble(((JTextField) getColtrolsMap().get(getSERVICETWO() + "-" + getCONSTANT())).getText());
                if (tmp1 < 0) {
                    errorName += "must be a postive number";
                    JOptionPane.showMessageDialog(null, errorName);
                    return true;
                }
                serviceMean2 = tmp1;
            }
            if ((serviceMean1 > (1 - p) * arrivalMean) || (p * serviceMean2 > (1 - p) * arrivalMean)) {
                JOptionPane.showMessageDialog(null, "Please increase the mean interarrival time or decrease the mean service time");
                return true;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, errorName + "is not a invalid number");
            return true;
        }
        return false;
    }
}
