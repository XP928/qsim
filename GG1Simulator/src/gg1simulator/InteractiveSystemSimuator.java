/*
 * InteractiveSystemSimuator.java
 */
package gg1simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class InteractiveSystemSimuator {

    protected int bars;
    protected double interval;
    protected Distribution thinkTimeDistribution;
    protected Distribution serviceDistribution;
    protected int numOfTerminals, numOfServers;
    protected double t = 0, lastEventTime = 0, delta;
    protected PriorityQueue<Event> eventQueue = new PriorityQueue<>();
    protected int customers;
    
    protected int midCustomers;
    private int numOfArrivaledJobs = 0, numOfServicedJobs = 0;
//    private ArrayList<Double> interArrival = new ArrayList<>();
//    private ArrayList<Double> interDeparture = new ArrayList<>();
//    private ArrayList<Double> busyTime = new ArrayList<>();
//    private ArrayList<Double> responseTime = new ArrayList<>();
    private HashMap<Integer, Double> queueMap = new HashMap<>();
    private Queue<Jobs> jobsQueue = new LinkedList<>();
    private boolean[] isTerminalBusy, isServerBusy;
    private double maxInterArrivalTime = 0, maxServiceTime = 0, maxInterDepartureTime = 0,
            maxResponseTime = 0;
    private double sumInterArrivalTime, sumServiceTime, sumInterDepartureTime,
            sumResponseTime, sumQueueLength, sumBusyTime, sumCycleTime, sumWaitTime;
    private double sdInterArrivalTime, sdServiceTime, sdInterDepartureTime,
            sdResponseTime, sdQueueLength, sdCycleTime, sdWaitTime;
    private int[] interArrivalList, serviceList, interDepartureList, responseTimeList, cycleTimeList;

    public InteractiveSystemSimuator(Distribution thinkTimeDistribution, Distribution serviceDistribution, int numOfTerminals, int numOfServers, int bars, double interval) {
        this.thinkTimeDistribution = thinkTimeDistribution;
        this.serviceDistribution = serviceDistribution;
        this.numOfTerminals = numOfTerminals;
        this.numOfServers = numOfServers;
        this.bars = bars;
        this.interval = interval;
    }

    public int getNumOfServers() {
        return numOfServers;
    }


    public int getAFreeServer() {
        Random r = new Random();
        int num;
        do {
            num = r.nextInt(numOfServers);
        } while (isServerBusy[num]);
        return num;
    }

    public int getAFreeTerminal() {
        Random r = new Random();
        int num;
        do {
            num = r.nextInt(numOfTerminals);
        } while (isTerminalBusy[num]);
        return num;
    }

    public int getLength() {
        return customers;
    }

    public double getDelta() {
        return delta;
    }

    public int getBars() {
        return bars;
    }

    public void run(int servicedJobs) {
        double arrivalTime, serviceTime, rTime, cTime, wTime;
        double tempArrival = 0, tempDeparture = 0;
        int index;
        for (int i = 0; i < numOfTerminals; i++) {
            eventQueue.add(new Event(i, t, t + thinkTimeDistribution.generateRandomTimes(), Event.ARRIVAL, i));
            isTerminalBusy[i] = true;
        }
        while (numOfServicedJobs < servicedJobs) {
            Event e = eventQueue.poll();
            t = e.endsTime;  //current simulation time            
            delta = t - lastEventTime; //
            sumQueueLength += delta * customers;
            sdQueueLength += delta * customers * customers;
            lastEventTime = t;
            Double length = queueMap.get(customers);
            if (length == null) {
                length = 0.0;
            }
            queueMap.put(customers, length + delta);
            switch (e.code) {
                case Event.ARRIVAL:
                    customers++;
                    numOfArrivaledJobs++;
                    isTerminalBusy[e.id] = false;
                    Jobs jobA = new Jobs(e.id, e.startsTime, t, t);
                    jobsQueue.add(jobA);
                    sumInterArrivalTime += t - tempArrival;
                    sdInterArrivalTime += (t - tempArrival) * (t - tempArrival);
                    index = (int) ((t - tempArrival) / interval);
                    if (index < bars) {
                        interArrivalList[index]++;
                    }
                    tempArrival = t;
                    if (customers <= numOfServers) {
                        serviceTime = serviceDistribution.generateRandomTimes();
                        index = (int) (serviceTime / interval);
                        if (index < bars) {
                            serviceList[index]++;
                        }
                        sumServiceTime += serviceTime;
                        sdServiceTime += serviceTime * serviceTime;
                        int branch = getAFreeServer();
                        eventQueue.add(new Event(jobA.getJobId(), t, t + serviceTime, Event.DEPARTURE, branch + 1));
                        isServerBusy[branch] = true;
                    }
                    break;
                case Event.DEPARTURE:
                    customers--;
                    numOfServicedJobs++;
                    eventQueue.add(new Event(e.id, t, t + thinkTimeDistribution.generateRandomTimes(), Event.ARRIVAL, 1));
                    isServerBusy[e.branch - 1] = false;
                    tempDeparture = t;
                    Jobs jobD = jobsQueue.poll();
                    jobD.setDepartureTime(t);
                    rTime = jobD.getDepartureTime() - jobD.getLastArrivalTime();
                    cTime = jobD.getDepartureTime() - jobD.getStartTime();
                    wTime = rTime - (e.getEndsTime() - e.getStartsTime());
                    
                    sumCycleTime += cTime;
                    sdCycleTime += cTime * cTime;
                    index = (int) (cTime  / interval);
                    if (index < bars) {
                        cycleTimeList[index]++;
                    }
                    sumWaitTime += wTime;
                    sdWaitTime += wTime * wTime;
                    //responseTime.add(rTime);
                    sumResponseTime += rTime;
                    sdResponseTime += rTime * rTime;
                    index = (int) (rTime / interval);
                    if (index < bars) {
                        responseTimeList[index]++;
                    }

                    if (customers > numOfServers - 1) {
                        serviceTime = serviceDistribution.generateRandomTimes();
                        index = (int) (serviceTime * bars / maxServiceTime);
                        if (index < bars) {
                            serviceList[index]++;
                        }
                        sumServiceTime += serviceTime;
                        sdServiceTime += serviceTime * serviceTime;
                        int id = jobsQueue.peek().getJobId();
                        eventQueue.add(new Event(id, t, t + serviceTime, Event.DEPARTURE, e.branch));
                    }
                    break;
            }
        }

    }

    public double getMaxInterArrivalTime() {
        return maxInterArrivalTime;
    }

    public double getMaxInterDepartureTime() {
        return maxInterDepartureTime;
    }

    public double getMaxServiceTime() {
        return maxServiceTime;
    }

    public double getMaxResponseTime() {
        return maxResponseTime;
    }
    
    public int getArrivals(){
        return numOfArrivaledJobs;
    }
    
    public int getDepartures(){
        return numOfServicedJobs;
    }
        

    public int[] getInterArrivalList() {
        return interArrivalList;
    }

    public int[] getServiceList() {
        return serviceList;
    }

    public int[] getInterDepartureList() {
        return interDepartureList;
    }

    public int[] getResponseTimeList() {
        return responseTimeList;
    }
    
    public int[] getCycleTimeList(){
        return cycleTimeList;
    }

//    public ArrayList<Double> getInterArrival() {
//        return interArrival;
//    }
//
//    public ArrayList<Double> getInterDeparture() {
//        return interDeparture;
//    }
//
//    public ArrayList<Double> getBusyTime() {
//        return busyTime;
//    }
//
//    public ArrayList<Double> getResponseTime() {
//        return responseTime;
//    }

    public HashMap<Integer, Double> getQueueMap() {
        return queueMap;
    }

    public double getUtilization() {
        return sumServiceTime / (numOfServers * t);
    }

    public double getAvgInterArrivalTime() {
        return sumInterArrivalTime / numOfArrivaledJobs;
    }

    public double getSDInterArrivalTime() {
        return Math.sqrt(sdInterArrivalTime / numOfArrivaledJobs - Math.pow(getAvgInterArrivalTime(), 2));
    }

    public double getAvgCycleTime() {
        return sumCycleTime / numOfServicedJobs;
    }

    public double getSDCycleTime() {
        return Math.sqrt(sdCycleTime / numOfServicedJobs - Math.pow(getAvgCycleTime(), 2));
    }

    public double getAvgWaitTime() {
        return sumWaitTime / numOfServicedJobs;
    }

    public double getSDWaitTime() {
        return sumWaitTime / numOfServicedJobs;
    }

    public double getAvgResponseTime() {
        return sumResponseTime / numOfServicedJobs;
    }

    public double getSDResponseTime() {
        return Math.sqrt(sdResponseTime / numOfServicedJobs - Math.pow(getAvgResponseTime(), 2));
    }

    public double getAvgServiceTime() {
        return sumServiceTime / numOfServicedJobs;
    }

    public double getSDServiceTime() {
        return Math.sqrt(sdServiceTime / numOfServicedJobs - Math.pow(getAvgServiceTime(), 2));
    }

    public double getAvgInterDeparutreTime() {
        return sumInterDepartureTime / numOfServicedJobs;
    }

    public double getSDInterDeparutreTime() {
        return Math.sqrt(sdInterDepartureTime / numOfServicedJobs - Math.pow(getAvgInterDeparutreTime(), 2));
    }

    public double getAvgJobsIntheSystem() {
        return sumQueueLength / t;
    }

    public double getSDJobsIntheSystem() {
        return Math.sqrt(sdQueueLength / t - Math.pow(getAvgJobsIntheSystem(), 2));
    }

    public static void main(String args[]) {
        InteractiveSystemSimuator sim = new InteractiveSystemSimuator(new ExponentialDistribution(4), new ExponentialDistribution(0.1), 80, 1, 15, 1);
        sim.run(100000);
        System.out.println("Average Response time: " + sim.getAvgResponseTime());
        System.out.println("Average Cycle Time: " + sim.getAvgCycleTime());
        System.out.println("Average Wait Time: " + sim.getAvgWaitTime());
        System.out.println("Average jobs: " + sim.getAvgJobsIntheSystem());
        System.out.println("Utilization: " + sim.getUtilization() / sim.getNumOfServers());
    }
}
