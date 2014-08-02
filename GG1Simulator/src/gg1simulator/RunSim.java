/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg1simulator;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class RunSim {

    public static void main(String[] args) {
//        GG1Simulator gg1Sim = new GG1Simulator(new PoissonDistribution(5), new DeterministicDistribution(2));
//        gg1Sim.run(1000000);
//        System.out.println("Simulation Result");
//        System.out.println("Utilization: " + gg1Sim.getUtilization());
//        System.out.println("Response time:" + gg1Sim.getAverageResponseTime());
//        System.out.println("Inter-arrival time:" + gg1Sim.getAverageInterArrivalTime());
//        System.out.println("Service time:" + gg1Sim.getAverageServiceTime());
//        System.out.println("Queue length:" + gg1Sim.getAverageJobsIntheSystem());
//
//        PlotGenerator pg = new PlotGenerator();
//        pg.generateHistogram(gg1Sim.getInterArrival(), "MD1.2 Inter-Arrival");
//        pg.generateHistogram(gg1Sim.getInterDeparture(), "MD1.2 Inter-Departure");
//        pg.generateHistogram(gg1Sim.getQueueMap(), "MD1.2 Queue Length");
//        pg.generateHistogram(gg1Sim.getResponseTime(), "MD1.2 Response Time");

//        System.out.println("\nAnalytical Result");
//        Calculator c = new MD1Calculator(8, 4);
//        System.out.println("Utilization: " + c.getUtilization());
//        System.out.println("Queue Length: " + c.getQueueLength());
//        System.out.println("Response Time: " + c.getResponseTime());

//        Calculator c = new GG1Calculator(0, 10, 0, 10);
//        System.out.println("Utilization: " + c.getUtilization());
//        System.out.println("Queue Length: " + c.getQueueLength());
//        System.out.println("Response Time: " + c.getResponseTime());

//        Calculator c1 = new MM1Calculator(1, 0.25);
//        System.out.println("Utilization: " + c1.getUtilization());
//        System.out.println("Queue Length: " + c1.getQueueLength());
//        System.out.println("Response Time: " + c1.getResponseTime());

//        PlotGenerator pg = new PlotGenerator();
//        ArrayList<Double> aUlist = new ArrayList<>();
//        ArrayList<Double> aRlist = new ArrayList<>();
//        ArrayList<Double> aQlist = new ArrayList<>();
//        ArrayList<Double> sUlist = new ArrayList<>();
//        ArrayList<Double> sRlist = new ArrayList<>();
//        ArrayList<Double> sQlist = new ArrayList<>();
//
//        for (double d = 0.3; d < 4; d += 0.4) {
//            GG1Simulator sim = new GG1Simulator(new ExponentialDistribution(4), new DeterministicDistribution(d), 15, 1);
//            Calculator c = new MD1Calculator(4, d);
//            sim.run(100000);
//            aUlist.add(c.getUtilization());
//            aRlist.add(c.getResponseTime());
//            aQlist.add(c.getQueueLength());
//
//            sUlist.add(sim.getUtilization());
//            sRlist.add(sim.getAvgResponseTime());
//            sQlist.add(sim.getAvgJobsIntheSystem());
//        }
//
//        System.out.println("Served Jobs: " + 100000);
//        System.out.println("Service Rate: " + 2);
//
//        Utility.print("X", 10);
//        Utility.print("AU", 10);
//        Utility.print("SU", 10);
//        Utility.print("E[%]", 10);
//        Utility.print("AR", 10);
//        Utility.print("SR", 10);
//        Utility.print("E[%]", 10);
//        Utility.print("AQ", 10);
//        Utility.print("SQ", 10);
//        Utility.print("E[%]", 10);
//        System.out.println();
//
//        int index = 0;
//        for (double d = 0.3; d < 4; d += 0.4) {
//            Utility.print(Utility.formatOutput(d), 10);
//            Utility.print(Utility.formatOutput(aUlist.get(index)), 10);
//            Utility.print(Utility.formatOutput(sUlist.get(index)), 10);
//            Utility.print(Utility.formatOutput(100 * (aUlist.get(index) - sUlist.get(index)) / aUlist.get(index)), 10);
//            Utility.print(Utility.formatOutput(aRlist.get(index)), 10);
//            Utility.print(Utility.formatOutput(sRlist.get(index)), 10);
//            Utility.print(Utility.formatOutput(100 * (aRlist.get(index) - sRlist.get(index)) / aRlist.get(index)), 10);
//            Utility.print(Utility.formatOutput(aQlist.get(index)), 10);
//            Utility.print(Utility.formatOutput(sQlist.get(index)), 10);
//            Utility.print(Utility.formatOutput(100 * (aQlist.get(index) - sQlist.get(index)) / sQlist.get(index)), 10);
//            System.out.println();
//            index++;
//        }
//        pg.generateResponseTimeLineChart(aRlist, sRlist, "MM1 Response Time");
//        pg.generateUtilizationLineChart(aUlist, sUlist, "MM1 Utilization");



//        ArrayList<Double> aUlist = new ArrayList<>();
//        ArrayList<Double> aRlist = new ArrayList<>();
//        ArrayList<Double> aQlist = new ArrayList<>();
//        ArrayList<Double> sUlist = new ArrayList<>();
//        ArrayList<Double> sRlist = new ArrayList<>();
//        ArrayList<Double> sQlist = new ArrayList<>();
//
//        for (double d = 0.1; d < 4; d += 0.4) {
//            MultipleServersSimulator sim = new MultipleServersSimulator(new ExponentialDistribution(1), new ExponentialDistribution(d), 4, 15, 1);
//            MultipleServersCalculator c = new MultipleServersCalculator(4, 1, d);
//            c.Calculate();
//            sim.run(100000);
//            aUlist.add(c.getUtilization());
//            aRlist.add(c.getResponseTime());
//            aQlist.add(c.getQueueLength());
//
//            sUlist.add(sim.getUtilization());
//            sRlist.add(sim.getAvgResponseTime());
//            sQlist.add(sim.getAvgJobsIntheSystem());
//        }
//
//        System.out.println("Served Jobs: " + 100000);
//        System.out.println("Service Rate: " + 0.5);
//
//        Utility.print("X", 10);
//        Utility.print("AU", 10);
//        Utility.print("SU", 10);
//        Utility.print("E[%]", 10);
//        Utility.print("AR", 10);
//        Utility.print("SR", 10);
//        Utility.print("E[%]", 10);
//        Utility.print("AQ", 10);
//        Utility.print("SQ", 10);
//        Utility.print("E[%]", 10);
//        System.out.println();
//
//        int index = 0;
//        for (double d = 0.1; d < 4; d += 0.4) {
//            Utility.print(Utility.formatOutput((3 + d) / 2), 10);
//            Utility.print(Utility.formatOutput(aUlist.get(index)), 10);
//            Utility.print(Utility.formatOutput(sUlist.get(index)), 10);
//            Utility.print(Utility.formatOutput(100*(aUlist.get(index) - sUlist.get(index)) / sUlist.get(index)), 10);
//            Utility.print(Utility.formatOutput(aRlist.get(index)), 10);
//            Utility.print(Utility.formatOutput(sRlist.get(index)), 10);
//            Utility.print(Utility.formatOutput(100*(aRlist.get(index) - sRlist.get(index)) / sRlist.get(index)), 10);
//            Utility.print(Utility.formatOutput(aQlist.get(index)), 10);
//            Utility.print(Utility.formatOutput(sQlist.get(index)), 10);
//            Utility.print(Utility.formatOutput(100 * (aQlist.get(index) - sQlist.get(index)) / sQlist.get(index)), 10);
//            System.out.println();
//            index++;
//        }

//        double aMin = 0, aMax = 100;
//        double sMin = 0, sMax = 90;
//        double error = 0;
//        
//        GG1Calculator c = new GG1Calculator(aMin, aMax, sMin, sMax);        
//        for (int i = 0; i < 100; i++) {
//            GG1Simulator sim = new GG1Simulator(new UniformDistribution(aMin, aMax), new UniformDistribution(sMin, sMax), 15, 1);
//            sim.run(100000);
//            double R = sim.getAvgResponseTime();
//            error += 100 * Math.abs((c.getResponseTime() - sim.getAvgResponseTime())) / R;
//        }
//        error /= 100;
//        System.out.print(error + "\t");

        ArrayList<Double> aUlist = new ArrayList<>();
        ArrayList<Double> aRlist = new ArrayList<>();
        ArrayList<Double> aQlist = new ArrayList<>();
        ArrayList<Double> sUlist = new ArrayList<>();
        ArrayList<Double> sRlist = new ArrayList<>();
        ArrayList<Double> sQlist = new ArrayList<>();

        for (double d = 0.1; d < 4; d += 0.4) {
            MultipleServersSimulator sim = new MultipleServersSimulator(new ExponentialDistribution(1), new ExponentialDistribution(d), 4, 15, 1);
            MultipleServersCalculator c = new MultipleServersCalculator(4, 1, d);
            c.Calculate();
            sim.run(1000000);
            aUlist.add(c.getUtilization());
            aRlist.add(c.getResponseTime());
            aQlist.add(c.getQueueLength());

            sUlist.add(sim.getUtilization());
            sRlist.add(sim.getAvgResponseTime());
            sQlist.add(sim.getAvgJobsIntheSystem());
        }

        System.out.println("Served Jobs: " + 100000);
        System.out.println("Service Rate: " + 0.5);

        Utility.print("X", 10);
        Utility.print("AU", 10);
        Utility.print("SU", 10);
        Utility.print("E[%]", 10);
        Utility.print("AR", 10);
        Utility.print("SR", 10);
        Utility.print("E[%]", 10);
        Utility.print("AQ", 10);
        Utility.print("SQ", 10);
        Utility.print("E[%]", 10);
        System.out.println();

        int index = 0;
        for (double d = 0.1; d < 4; d += 0.4) {
            Utility.print(Utility.formatOutput((3 + d) / 2), 10);
            Utility.print(Utility.formatOutput(aUlist.get(index)), 10);
            Utility.print(Utility.formatOutput(sUlist.get(index)), 10);
            Utility.print(Utility.formatOutput(100*(aUlist.get(index) - sUlist.get(index)) / sUlist.get(index)), 10);
            Utility.print(Utility.formatOutput(aRlist.get(index)), 10);
            Utility.print(Utility.formatOutput(sRlist.get(index)), 10);
            Utility.print(Utility.formatOutput(100*(aRlist.get(index) - sRlist.get(index)) / sRlist.get(index)), 10);
            Utility.print(Utility.formatOutput(aQlist.get(index)), 10);
            Utility.print(Utility.formatOutput(sQlist.get(index)), 10);
            Utility.print(Utility.formatOutput(100 * (aQlist.get(index) - sQlist.get(index)) / sQlist.get(index)), 10);
            System.out.println();
            index++;
        }

    }
}
