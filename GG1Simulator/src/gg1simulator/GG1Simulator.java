/*
 * GG1Simulator.java
 */
package gg1simulator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class GG1Simulator {

    protected int bars;
    protected double interval;
    protected Distribution arrivalDistribution;
    protected Distribution serviceDistribution;
    protected double t, lastEventTime = 0, delta;
    protected PriorityQueue<Event> eventQueue = new PriorityQueue<>();
    protected int customers;
    protected int midCustomers;
    private int numOfArrivaledJobs = 0, numOfServicedJobs = 0;
    private HashMap<Integer, Double> queueMap = new HashMap<>();
    private Queue<Jobs> jobsQueue = new LinkedList<>();
    private double maxInterArrivalTime = 0, maxServiceTime = 0, maxInterDepartureTime = 0,
            maxResponseTime = 0;
    private double sumInterArrivalTime, sumServiceTime, sumInterDepartureTime,
            sumResponseTime, sumQueueLength;
    private double sdInterArrivalTime, sdServiceTime, sdInterDepartureTime,
            sdResponseTime, sdQueueLength;
    private int[] interArrivalList, serviceList, interDepartureList, responseTimeList;

    public GG1Simulator(Distribution arrivalDistribution, Distribution serviceDistribution, int bars, double interval) {
        this.arrivalDistribution = arrivalDistribution;
        this.serviceDistribution = serviceDistribution;
        this.bars = bars;
        this.interval = interval;
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
        double arrivalTime, serviceTime, rTime;
        double tempArraival = 0, tempDeparture = 0;
        int jobArrivalId = 1, index;
        arrivalTime = arrivalDistribution.generateRandomTimes();
        eventQueue.add(new Event(t, t + arrivalTime, Event.ARRIVAL, 1));
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
//                    interArrival.add(t - tempArraival);
                    sumInterArrivalTime += t - tempArraival;
                    sdInterArrivalTime += (t - tempArraival) * (t - tempArraival);
                    tempArraival = t;
                    Jobs jobA = new Jobs(jobArrivalId++, t, t);
                    jobsQueue.add(jobA);
                    arrivalTime = arrivalDistribution.generateRandomTimes();
                    index = (int) (arrivalTime / interval);
                    if (index < bars) {
                        interArrivalList[index]++;
                    }
//                    sumArrivalTime += arrivalTime;
//                    sdArrivalTime += arrivalTime * arrivalTime;
                    eventQueue.add(new Event(t, t + arrivalTime, Event.ARRIVAL, 1));
                    if (customers == 1) {
                        serviceTime = serviceDistribution.generateRandomTimes();
                        index = (int) (serviceTime / interval);
                        if (index < bars) {
                            serviceList[index]++;
                        }
                        sumServiceTime += serviceTime;
                        sdServiceTime += serviceTime * serviceTime;
                        eventQueue.add(new Event(t, t + serviceTime, Event.DEPARTURE, 1));
                    }
                    break;
                case Event.DEPARTURE:
                    customers--;
                    numOfServicedJobs++;
                    //interDeparture.add(t - tempDeparture);
                    index = (int) ((t - tempDeparture) / interval);
                    if (index < bars) {
                        interDepartureList[index]++;
                    }
                    sumInterDepartureTime += t - tempDeparture;
                    sdInterDepartureTime += (t - tempDeparture) * (t - tempDeparture);
                    tempDeparture = t;
                    Jobs jobD = jobsQueue.poll();
                    jobD.setDepartureTime(t);
                    rTime = jobD.getDepartureTime() - jobD.getArrivalTime();
                    index = (int) (rTime / interval);
                    if (index < bars) {
                        responseTimeList[index]++;
                    }
//                    responseTime.add(rTime);
                    sumResponseTime += rTime;
                    sdResponseTime += rTime * rTime;
                    if (customers > 0) {
                        serviceTime = serviceDistribution.generateRandomTimes();
                        index = (int) (serviceTime / interval);
                        if (index < bars) {
                            serviceList[index]++;
                        }
                        sumServiceTime += serviceTime;
                        sdServiceTime += serviceTime * serviceTime;
                        eventQueue.add(new Event(t, t + serviceTime, Event.DEPARTURE, 1));
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

//    public ArrayList<Double> getInterArrival() {
//        return interArrival;
//    }

//    public ArrayList<Double> getInterDeparture() {
//        return interDeparture;
//    }

//    public ArrayList<Double> getBusyTime() {
//        return busyTime;
//    }

//    public ArrayList<Double> getResponseTime() {
//        return responseTime;
//    }

    public HashMap<Integer, Double> getQueueMap() {
        return queueMap;
    }
    
    public int getArrivals(){
        return numOfArrivaledJobs;
    }
    
    public int getDepartures(){
        return numOfServicedJobs;
    }

    public double getUtilization() {
        return sumServiceTime / t;
    }

    public double getAvgInterArrivalTime() {
        return sumInterArrivalTime / numOfArrivaledJobs;
    }

    public double getSDInterArrivalTime() {
        return Math.sqrt(sdInterArrivalTime / numOfArrivaledJobs - Math.pow(getAvgInterArrivalTime(), 2));
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
}
