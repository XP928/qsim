/*
 * MultipleServersModel.java
 */
package gg1simulator.GUI;

import gg1simulator.Distribution;
import gg1simulator.MultipleServersCalculator;
import gg1simulator.MultipleServersSimulator;
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

public class MultipleServersModel extends Simulator {

    private JPanel myChartPanel;
    private JScrollPane mainChartPanel;
    private boolean flag = false;
    private int arrivals, departures;
    private int[] preInterArrivalTime, preCycleTime, preResponseTime;
    private double preInterval;
    private HashMap<Integer, Double> preQueueMap = new HashMap<>();

    @Override
    public void init() {
        super.setImageName("MultipleServersModel.jpg");
        super.setNumOfServers(1);
        super.setModelType(4);
        super.init();
        ((JTextField) getColtrolsMap().get(getNumOfServers())).setText("2");
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

                if (checkErrors()) {
                    return;
                }

                int steps, bars, m;
                double interval;
                String thinkTimeStr, serviceStr;
                Distribution thinkTimeDistribution, serviceDistribution;
                steps = Integer.parseInt(((JTextField) getColtrolsMap().get(getSTEPS())).getText());
                bars = Integer.parseInt(((JComboBox) getColtrolsMap().get(getNUMBEROFBARS())).getSelectedItem().toString());
                m = Integer.parseInt(((JTextField) getColtrolsMap().get(getNumOfServers())).getText());
                thinkTimeStr = ((JComboBox) getColtrolsMap().get(getARRIVAL())).getSelectedItem().toString();
                serviceStr = ((JComboBox) getColtrolsMap().get(getSERVICEONE())).getSelectedItem().toString();
                interval = Double.parseDouble(((JTextField) getColtrolsMap().get(getInterval())).getText());

                thinkTimeDistribution = getDistribution(thinkTimeStr, getARRIVAL());
                serviceDistribution = getDistribution(serviceStr, getSERVICEONE());

                MultipleServersSimulator sim = new MultipleServersSimulator(thinkTimeDistribution, serviceDistribution, m, bars, interval);
                sim.run(steps);

                mainResultPanel.removeAll();
                mainResultPanel.add(createTitle());

                MultipleServersCalculator c = new MultipleServersCalculator(m, thinkTimeDistribution.getMean(), serviceDistribution.getMean());
                c.Calculate();
                int temp = 1;
                if (thinkTimeStr.equals("Exponential (M)") && serviceStr.equals("Exponential (M)")) {
                    temp = 0;
                }
                double avg, sdv, realAvg, error;
                String realValue, errorStr;
                avg = sim.getUtilization();
                realAvg = c.getUtilization();
                realValue = Utility.formatOutput(realAvg);
                error = 100 * (avg - realAvg) / realAvg;
                if (temp == 1) {
                    error *= -1;
                }
                errorStr = Utility.formatOutput(error);
                mainResultPanel.add(createRow("Server Utilization", avg, realValue, errorStr));

                avg = sim.getAvgInterArrivalTime();
                sdv = sim.getSDInterArrivalTime();
                realAvg = c.getArrivalMean();
                realValue = Utility.formatOutput(realAvg);
                error = 100 * (avg - realAvg) / realAvg;
                if (temp == 1) {
                    error *= -1;
                }
                errorStr = Utility.formatOutput(error);
                mainResultPanel.add(createRow("Interarrival time", avg, sdv, realValue, errorStr));

                avg = sim.getAvgResponseTime();
                sdv = sim.getSDResponseTime();
                realAvg = c.getResponseTime();
                realValue = Utility.formatOutput(realAvg);
                error = 100 * (avg - realAvg) / realAvg;
                if (temp == 1) {
                    error *= -1;
                }
                errorStr = Utility.formatOutput(error);
                mainResultPanel.add(createRow("Response time", avg, sdv, realValue, errorStr));
                
                avg = sim.getAvgInterDeparutreTime();
                sdv = sim.getSDInterDeparutreTime();
                realValue = "";
                mainResultPanel.add(createRow("Interdeparture time", avg, sdv, realValue, errorStr));
                
                avg = 1 / avg;
                realValue = "";
                mainResultPanel.add(createRow("Throughput", avg, realValue, errorStr));

                avg = sim.getAvgWaitTime();
                sdv = sim.getSDWaitTime();
                realAvg = c.getWaitTime();
                realValue = Utility.formatOutput(realAvg);
                error = 100 * (avg - realAvg) / realAvg;
                if (temp == 1) {
                    error *= -1;
                }
                errorStr = Utility.formatOutput(error);
                mainResultPanel.add(createRow("Queue wait time", avg, sdv, realValue, errorStr));

                avg = sim.getAvgJobsIntheSystem();
                sdv = sim.getSDJobsIntheSystem();
                realAvg = c.getQueueLength();
                realValue = Utility.formatOutput(realAvg);
                error = 100 * (avg - realAvg) / realAvg;
                if (temp == 1) {
                    error *= -1;
                }
                errorStr = Utility.formatOutput(error);
                mainResultPanel.add(createRow("Number of jobs:", avg, sdv, realValue, errorStr));

                mainResultPanel.revalidate();
                mainResultPanel.repaint();

                myChartPanel.removeAll();

                if (flag) {
                    myChartPanel.add(createChartPanel(preInterArrivalTime, arrivals, preInterval, "Interarrival time (Previous Exp)", 0));
                }
                myChartPanel.add(createChartPanel(sim.getInterArrivalList(), sim.getArrivals(), interval, "Interarrival time", 1));

                if (flag) {
                    myChartPanel.add(createChartPanel(preResponseTime, departures, preInterval, "Response time  (Previous Exp)", 0));
                }
                myChartPanel.add(createChartPanel(sim.getResponseTimeList(), sim.getDepartures(), interval, "Response time", 1));

                if (flag) {
                    myChartPanel.add(createChartPanel(preCycleTime, departures, preInterval, "Interdeparture time (Previous Exp)", 0));
                }
                myChartPanel.add(createChartPanel(sim.getInterDepartureList(), sim.getDepartures(), interval, "Interdeparture time", 1));

                if (flag) {
                    myChartPanel.add(createChartPanel(preQueueMap, "Number of jobs (Previous Exp)", bars, 0));
                }
                preQueueMap = (HashMap) sim.getQueueMap().clone();
                myChartPanel.add(createChartPanel(sim.getQueueMap(), "Number of jobs", bars, 1));

                myChartPanel.revalidate();
                myChartPanel.repaint();

                flag = true;
                preInterArrivalTime = new int[bars];
                preResponseTime = new int[bars];
                preCycleTime = new int[bars];

                System.arraycopy(sim.getInterArrivalList(), 0, preInterArrivalTime, 0, bars);
                System.arraycopy(sim.getResponseTimeList(), 0, preResponseTime, 0, bars);
                System.arraycopy(sim.getInterDepartureList(), 0, preCycleTime, 0, bars);

                arrivals = sim.getArrivals();
                preInterval = interval;
                departures = sim.getDepartures();
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
        int arrival, service, n = 1;
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
            errorName = "the number of servers ";
            n = Integer.parseInt(((JTextField) getColtrolsMap().get(getNumOfServers())).getText());
            if (n <= 0) {
                errorName += "must be a postive integer";
                JOptionPane.showMessageDialog(null, errorName);
                return true;
            }
            if (n > 16) {
                errorName += "must be lest than 16";
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
                errorName = "Mean think time ";
                tmp1 = Double.parseDouble(((JTextField) getColtrolsMap().get(getARRIVAL() + "-" + getLAMBDA())).getText());
                if (tmp1 <= 0) {
                    errorName += "must be a postive number";
                    JOptionPane.showMessageDialog(null, errorName);
                    return true;
                }
                arrivalMean = tmp1;
            }
            if (arrival == 1) {
                errorName = "Lower bound for think time distribution ";
                tmp1 = Double.parseDouble(((JTextField) getColtrolsMap().get(getARRIVAL() + "-" + getUNIFORMLOWER())).getText());
                if (tmp1 < 0) {
                    errorName += "must be a postive number ";
                    JOptionPane.showMessageDialog(null, errorName);
                    return true;
                }
                errorName = "Upper bound for think time distribution ";
                tmp2 = Double.parseDouble(((JTextField) getColtrolsMap().get(getARRIVAL() + "-" + getUNIFORMUPPER())).getText());
                if (tmp2 <= tmp1) {
                    errorName += "must greater than lower bound";
                    JOptionPane.showMessageDialog(null, errorName);
                    return true;
                }
                arrivalMean = (tmp1 + tmp2) / 2;
            }
            if (arrival == 2) {
                errorName = "Constant of think time distribution ";
                tmp1 = Double.parseDouble(((JTextField) getColtrolsMap().get(getARRIVAL() + "-" + getCONSTANT())).getText());
                if (tmp1 <= 0) {
                    errorName += "must be a postive number";
                    JOptionPane.showMessageDialog(null, errorName);
                    return true;
                }
                arrivalMean = tmp1;
            }
            if (service == 0) {
                errorName = "Mean service time";
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
                if (tmp2 <= tmp1) {
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
            if(arrivalMean < serviceMean / n){
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
