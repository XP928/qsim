/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg1simulator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Administrator
 */
public class RunSimulator {

    public static void print(String s, int length) {
        System.out.print(s);
        if (s.length() < length) {
            for (int i = 0; i < length - s.length(); i++) {
                System.out.print(" ");
            }
        }
    }

    public static String formatOutput(double d) {
        DecimalFormat df = new DecimalFormat("##0.000");
        return df.format(d);
    }

    public static void main(String[] args) {

//        FeedbackQueueSimulator fbSimulator = new FeedbackQueueSimulator(new ExponentialDistribution(4), new ExponentialDistribution(1), new ExponentialDistribution(1), 20, 0.1);
//
//
//        fbSimulator.run(100000);
//        System.out.println("waiting jobs:\t" + fbSimulator.getCustomers());
//        System.out.println("served jobs:\t" + fbSimulator.getNumOfMidArrivalJobs());
//
//        System.out.println("Average response time: " + fbSimulator.getMean(fbSimulator.getResponseTime()));
//        System.out.println("Frist server utilization: " + fbSimulator.getSum(fbSimulator.getBusyTimeFirstServer()) / fbSimulator.t);
//        System.out.println("Second server utilization: " + fbSimulator.getSum(fbSimulator.getBusyTimeSecondServer()) / fbSimulator.t);
//        System.out.println("Jobs in the system: " + fbSimulator.getAverageJobs(fbSimulator.getQueueLength()));

//        PlotGenerator pg = new PlotGenerator();
//        pg.generateHistogram(fbSimulator.getInterArrivalTime(), "Inter-Arrival");
//        pg.generateHistogram(fbSimulator.getInterTotalArrivalTime(), "Total Inter-Arrival");
//        pg.generateHistogram(fbSimulator.getInterDepartureTime(), "Inter-Departure");
//        pg.generateHistogram(fbSimulator.getInterMidArrivalTime(), "Inter-MidArrival");
//        pg.generateHistogram(fbSimulator.getInterMidDepartureTime(), "Inter-MidDeparture");
//        pg.generateHistogram(fbSimulator.getResponseTime(), "Response Time");



//        pg.generateData(fbSimulator.getInterTotalArrivalTime(), 100);
//        System.out.println("\nAnalytical Result:");
//        GG1Calculator c = new GG1Calculator(0, 10, 0, 4, 0, 4, 0.5);
//        System.out.println("Frist Server Utilization :" + c.getFirstUtilization());
//        System.out.println("Second Server Utilization :" + c.getSecondUtilization());
//        System.out.println("Response Time: " + c.getResponseTime());
//
//
//        FeedbackQueueCalculator fbC = new FeedbackQueueCalculator(0.1, 0.4, 0.2, 0.5);
//        System.out.println("Response Time: " + fbC.getResponseTime());
//        System.out.println("Frist Server Utilization: " + fbC.getFirstUtilization());
//        System.out.println("Second Server Utilization: " + fbC.getSecondUtilization());
//
//
//
//        ArrayList<Double> rSList = new ArrayList<>();
//        ArrayList<Double> u1SList = new ArrayList<>();
//        ArrayList<Double> u2SList = new ArrayList<>();
//        ArrayList<Double> jSList = new ArrayList<>();
//        ArrayList<Double> rCList = new ArrayList<>();
//        ArrayList<Double> u1CList = new ArrayList<>();
//        ArrayList<Double> u2CList = new ArrayList<>();
//        ArrayList<Double> jCList = new ArrayList<>();
//        
//        
//        for (double p = 0; p < 0.95; p += 0.1) {
//            FeedbackQueueSimulator fbSimulator = new FeedbackQueueSimulator(new ExponentialDistribution(100), new ExponentialDistribution(1), new ExponentialDistribution(1), 20, 1, p);
//            FeedbackQueueCalculator fbCalculator = new FeedbackQueueCalculator(100, 1, 1, p);
//            fbSimulator.run(100000);
//
//
//            rSList.add(fbSimulator.getAvgResponseTime());
//            u1SList.add(fbSimulator.getFirstUtilization());
//            u2SList.add(fbSimulator.getSecondUtilization());
//            jSList.add(fbSimulator.getAvgQueueLength());
//
//            rCList.add(fbCalculator.getResponseTime());
//            u1CList.add(fbCalculator.getFirstUtilization());
//            u2CList.add(fbCalculator.getSecondUtilization());
//            jCList.add(fbCalculator.getJobsInTheSystem());
//        }
//
//
//        System.out.println("Arrival Jobs: " + 100000);
//        System.out.println("First Server Rate:" + 2 / 4.0);
//        System.out.println("Second Server Rate:" + 2 / 4.0);
//
//        print("  P", 10);
//        print("AU1", 10);
//        print("SU1", 10);
//        print("E[%]", 10);
//        print("AU2", 10);
//        print("SU2", 10);
//        print("E[%]", 10);
//        print("AJobs", 10);
//        print("SJobs", 10);
//        print("E[%]", 10);
//        print("AR", 10);
//        print("SR", 10);
//        print("E[%]", 10);
//        System.out.println();
//
//        int index = 0;
//        for (double p = 0; p < 0.95; p += 0.1) {
//            print(formatOutput(p), 10);
//            print(formatOutput(u1CList.get(index)), 10);
//            print(formatOutput(u1SList.get(index)), 10);
//            print(formatOutput(100 * (u1SList.get(index) - u1CList.get(index)) / u1CList.get(index)), 10);
//            print(formatOutput(u2CList.get(index)), 10);
//            print(formatOutput(u2SList.get(index)), 10);
//            print(formatOutput(100 * (u2SList.get(index) - u2CList.get(index)) / u2CList.get(index)), 10);
//            print(formatOutput(jCList.get(index)), 10);
//            print(formatOutput(jSList.get(index)), 10);
//            print(formatOutput(100 * (jSList.get(index) - jCList.get(index)) / jCList.get(index)), 10);
//            print(formatOutput(rCList.get(index)), 10);
//            print(formatOutput(rSList.get(index)), 10);
//            print(formatOutput(100 * (rSList.get(index) - rCList.get(index)) / rCList.get(index)), 10);
//            System.out.println();
//            index++;
//        }
        
        ArrayList<Double> rSList = new ArrayList<>();
        ArrayList<Double> u1SList = new ArrayList<>();
        ArrayList<Double> u2SList = new ArrayList<>();
        ArrayList<Double> jSList = new ArrayList<>();
        ArrayList<Double> rCList = new ArrayList<>();
        ArrayList<Double> u1CList = new ArrayList<>();
        ArrayList<Double> u2CList = new ArrayList<>();
        ArrayList<Double> jCList = new ArrayList<>();
        
        
        for (double d = 1; d < 10; d += 1) {
            InteractiveSystemSimuator fbSimulator = new InteractiveSystemSimuator(new ExponentialDistribution(8), new ExponentialDistribution(d), 6, 3, 15, 1);
            InteractiveSystemCalculator fbCalculator = new InteractiveSystemCalculator(8, d, 6, 3);
            fbCalculator.Calculate();
            fbSimulator.run(100000);


            rSList.add(fbSimulator.getAvgResponseTime());
            u1SList.add(fbSimulator.getUtilization());
            u2SList.add(fbSimulator.getAvgCycleTime());
            jSList.add(fbSimulator.getAvgJobsIntheSystem());
            

            rCList.add(fbCalculator.getResponseTime());
            u1CList.add(fbCalculator.getUtilization());
            u2CList.add(fbCalculator.getCycleTime());
            jCList.add(fbCalculator.getQueueLength());
        }


        System.out.println("Arrival Jobs: " + 100000);
        System.out.println("First Server Rate:" + 2 / 4.0);
        System.out.println("Second Server Rate:" + 2 / 4.0);

        print("AU1", 10);
        print("SU1", 10);
        print("E[%]", 10);
        print("AU2", 10);
        print("SU2", 10);
        print("E[%]", 10);
        print("AJobs", 10);
        print("SJobs", 10);
        print("E[%]", 10);
        print("AR", 10);
        print("SR", 10);
        print("E[%]", 10);
        System.out.println();

        int index = 0;
        for (double d = 1; d < 10; d += 1) {
            print(formatOutput(u1CList.get(index)), 10);
            print(formatOutput(u1SList.get(index)), 10);
            print(formatOutput(100 * (u1SList.get(index) - u1CList.get(index)) / u1CList.get(index)), 10);
            print(formatOutput(u2CList.get(index)), 10);
            print(formatOutput(u2SList.get(index)), 10);
            print(formatOutput(100 * (u2SList.get(index) - u2CList.get(index)) / u2CList.get(index)), 10);
            print(formatOutput(jCList.get(index)), 10);
            print(formatOutput(jSList.get(index)), 10);
            print(formatOutput(100 * (jSList.get(index) - jCList.get(index)) / jCList.get(index)), 10);
            print(formatOutput(rCList.get(index)), 10);
            print(formatOutput(rSList.get(index)), 10);
            print(formatOutput(100 * (rSList.get(index) - rCList.get(index)) / rCList.get(index)), 10);
            System.out.println();
            index++;
        }

     

    }
}
