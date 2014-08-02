/*
 * FeedbackQueueSimulator.java
 */
package gg1simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class FeedbackQueueSimulator {

    protected Distribution arrivalDistribution;
    protected Distribution serviceDistribution;
    protected Distribution midServiceDistribution;
    private double interval;
    private int bars;
    protected double p;
    protected double t, lastEventTime = 0, delta;
    protected int jobsInTheSystem;
    protected PriorityQueue<Event> eventQueue = new PriorityQueue<>();
    private PriorityQueue<Jobs> mainJobsQueue = new PriorityQueue<>();
    private Queue<Jobs> branchJobsQueue = new LinkedList<>();
    private Queue<Jobs> servedJobsQueue = new LinkedList<>();
    protected int customers, midCustomers;
    private Random r = new Random();
//    private ArrayList<Double> interArrivalTime = new ArrayList<>();
//    private ArrayList<Double> interTotalArrivalTime = new ArrayList<>();
//    private ArrayList<Double> interDepartureTime = new ArrayList<>();
//    private ArrayList<Double> interMidArrivalTime = new ArrayList<>();
//    private ArrayList<Double> interMidDepartureTime = new ArrayList<>();
//    private ArrayList<Double> busyTimeFirstServer = new ArrayList<>();
//    private ArrayList<Double> busyTimeSecondServer = new ArrayList<>();
//    private ArrayList<Double> responseTime = new ArrayList<>();
    private HashMap<Integer, Double> queueLength = new HashMap<>();
    private int numOfServicedJobs = 0, numOfMidArrivaledJobs = 0, numOfTotalArrivaledJobs = 0, numOfArrivaledJobs = 0,
            numOfMidServicedJobs = 0, numOfJobs = 0;
    private double maxInterArrivalTime = 0, maxTotalInterArrivalTime = 0, maxInterDepartureTime = 0,
            maxInterMidArrivalTime = 0, maxInterMidDepartureTime = 0, maxServiceTime1 = 0,
            maxServiceTime = 0, maxResponseTime = 0;
    private double sumInterArrivalTime, sumTotalInterArrivalTime, sumInterDepartureTime, sumMidArrivalTime,
            sumInterMidDepartureTime, sumServiceTime1, sumServiceTime, sumResponseTime, sumQueueLength;
    private double sdInterArrivalTime, sdTotalInterArrivalTime, sdInterDepartureTime, sdMidArrivalTime,
            sdInterMidDepartureTime, sdServiceTime1, sdServiceTime, sdResponseTime, sdQueueLength;
    private int[] interArrivalTimeList, totalInterArrivalTimeList, interDepartureTimeList, midArrivalTimeList,
            interMidDepartureTimeList, serviceTimeList1, serviceTimeList, responseTimeList;

    public FeedbackQueueSimulator(Distribution arrivalDistribution, Distribution serviceDistribution, Distribution midServiceDistribution, int bars, double interval, double p) {
        this.arrivalDistribution = arrivalDistribution;
        this.serviceDistribution = serviceDistribution;
        this.midServiceDistribution = midServiceDistribution;
        this.bars = bars;
        this.interval = interval;
        this.p = p;
    }


    public int getCustomers() {
        return customers;
    }

    public int getNumOfMidArrivalJobs() {
        return numOfMidArrivaledJobs;
    }

    public int getMidCustomers() {
        return midCustomers;
    }

    public double getDelta() {
        return delta;
    }

    public Queue<Jobs> getServerdJobsQueue() {
        return servedJobsQueue;
    }


    public void run(int servicedJobs) {
        double arrivalTime, serviceTime, midServiceTime, rTime;
        double tempArrivalTime = 0, tempTotalArrayTime = 0, tempDepartureTime = 0, tempMidArraialTime = 0, tempMidDepartureTime = 0;
        arrivalTime = arrivalDistribution.generateRandomTimes();
        eventQueue.add(new Event(t, t + arrivalTime, Event.ARRIVAL, 1));
        int jobId = 1, index;
        Jobs job;
        while (numOfServicedJobs < servicedJobs) {
            Event e = eventQueue.poll();
            t = e.endsTime;  //current simulation time            
            delta = t - lastEventTime;
            lastEventTime = t;
            jobsInTheSystem = customers + midCustomers;
            sumQueueLength += delta * jobsInTheSystem;
            sdQueueLength += delta *  jobsInTheSystem * jobsInTheSystem;
            Double length = queueLength.get(jobsInTheSystem);
            if (length == null) {
                length = 0.0;
            }
            queueLength.put(jobsInTheSystem, length + delta);
            switch (e.code) {
                case Event.ARRIVAL:    // Next Arrival Time
                    customers++;
                    numOfTotalArrivaledJobs++;
                    //interTotalArrivalTime.add(t - tempTotalArrayTime);
                    sumTotalInterArrivalTime += t - tempTotalArrayTime;
                    sdTotalInterArrivalTime += (t - tempTotalArrayTime) * (t - tempTotalArrayTime);
                    index = (int) ((t - tempTotalArrayTime) / interval);
                    if (index < bars) {
                        totalInterArrivalTimeList[index]++;
                    }
                    tempTotalArrayTime = t;
                    if (e.getBranch() == 1) {
                        numOfArrivaledJobs++;
                        //interArrivalTime.add(t - tempArrivalTime);
                        sumInterArrivalTime += t - tempArrivalTime;
                        sdInterArrivalTime += (t - tempArrivalTime) * (t - tempArrivalTime);
                        tempArrivalTime = t;
                        job = new Jobs(jobId++, t, t);
                        mainJobsQueue.add(job);
                        arrivalTime = arrivalDistribution.generateRandomTimes();
                        //System.out.println("a: "  + arrivalTime);
                        index = (int) (arrivalTime  / interval);
                        if (index < bars) {
                            interArrivalTimeList[index]++;
                        }
                        eventQueue.add(new Event(t, t + arrivalTime, Event.ARRIVAL, 1));
                    }
                    if (e.getBranch() == 2) {
                        job = branchJobsQueue.poll();
                        job.setLastArrivaltime(t);
                        mainJobsQueue.add(job);
                    }
                    if (customers == 1) {       // the first customer arrives and will be served ASAP , so the queue is empty
                        serviceTime = serviceDistribution.generateRandomTimes();
                        sumServiceTime1 += serviceTime;
                        sdServiceTime1 += serviceTime * serviceTime;
                        index = (int) (serviceTime  / interval);
                        if (index < bars) {
                            serviceTimeList1[index]++;
                        }
                        //busyTimeFirstServer.add(serviceTime);
                        eventQueue.add(new Event(t, t + serviceTime, Event.DEPARTURE, 1));
                    }
                    break;
                case Event.DEPARTURE:
                    customers--;      
                    numOfJobs++;
                    double temp = r.nextDouble();
                    if (temp > p) {
                        numOfServicedJobs++;
                        sumInterDepartureTime += t - tempDepartureTime;
                        sdInterDepartureTime += (t - tempDepartureTime) * (t - tempDepartureTime);
                        index = (int)((t - tempDepartureTime) / interval);
                        if(index < bars){
                            interDepartureTimeList[index]++;
                        }
                        //interDepartureTime.add(t - tempDepartureTime);
                        tempDepartureTime = t;
                        job = mainJobsQueue.poll();
                        job.setDepartureTime(t);
                        rTime = t - job.getArrivalTime(); 
                        sumResponseTime += rTime;
                        sdResponseTime += rTime * rTime;
//                        responseTime.add(rTime);
                        index = (int) (rTime / interval);
                        if (index < bars) {
                            responseTimeList[index]++;
                        }
                    } else {
                        eventQueue.add(new Event(t, t, Event.MIDARRIVAL, 1));
                    }
                    if (customers > 0) {
                        serviceTime = serviceDistribution.generateRandomTimes();
                        sumServiceTime1 += serviceTime;
                        sdServiceTime1 += serviceTime * serviceTime;
                        index = (int) (serviceTime / interval);
                        if (index < bars) {
                            serviceTimeList1[index]++;
                        }
                        //busyTimeFirstServer.add(serviceTime);
                        eventQueue.add(new Event(t, t + serviceTime, Event.DEPARTURE, 1));
                    }
                    break;
                case Event.MIDARRIVAL:
                    numOfMidArrivaledJobs++;
                    midCustomers++;
                    sumMidArrivalTime += t - tempMidArraialTime;
                    sdMidArrivalTime += (t - tempMidArraialTime) * (t - tempMidArraialTime);
                    index = (int) ((t - tempMidArraialTime)  / interval);
                    if (index < bars) {
                        midArrivalTimeList[index]++;
                    }
                    //interMidArrivalTime.add(t - tempMidArraialTime);
                    tempMidArraialTime = t;
                    job = mainJobsQueue.poll();
                    job.setMidArrivalTime(t);
                    branchJobsQueue.add(job);
                    if (midCustomers == 1) {
                        midServiceTime = midServiceDistribution.generateRandomTimes();
                        sumServiceTime += midServiceTime;
                        sdServiceTime += midServiceTime * midServiceTime;
                        index = (int) (midServiceTime  / interval);
                        if (index < bars) {
                            serviceTimeList[index]++;
                        }
                        //busyTimeSecondServer.add(midServiceTime);
                        eventQueue.add(new Event(t, t + midServiceTime, Event.MIDDEPARTURE, 2));
                    }
                    break;
                case Event.MIDDEPARTURE:
                    midCustomers--;
                    numOfMidServicedJobs++;
                    sumInterMidDepartureTime += t - tempMidDepartureTime;
                    sdInterMidDepartureTime += (t - tempMidDepartureTime) * (t - tempMidDepartureTime);
                    index = (int) ((t - tempMidDepartureTime)  / interval);
                    if (index < bars) {
                        interMidDepartureTimeList[index]++;
                    }
                    //interMidDepartureTime.add(t - tempMidDepartureTime);
                    tempMidDepartureTime = t;
                    eventQueue.add(new Event(t, t, Event.ARRIVAL, 2));
                    if (midCustomers > 0) {
                        midServiceTime = midServiceDistribution.generateRandomTimes();
                        sumServiceTime += midServiceTime;
                        sdServiceTime += midServiceTime * midServiceTime;
                        index = (int) (midServiceTime  / interval);
                        if (index < bars) {
                            serviceTimeList[index]++;
                        }
                        //busyTimeSecondServer.add(midServiceTime);
                        eventQueue.add(new Event(t, t + midServiceTime, Event.MIDDEPARTURE, 2));
                    }
                    break;
            }
        }
    }
   

//    public ArrayList<Double> getInterArrivalTime() {
//        return interArrivalTime;
//    }

//    public ArrayList<Double> getInterTotalArrivalTime() {
//        return interTotalArrivalTime;
//    }

//    public ArrayList<Double> getInterDepartureTime() {
//        return interDepartureTime;
//    }

//    public ArrayList<Double> getInterMidArrivalTime() {
//        return interMidArrivalTime;
//    }
//
//    public ArrayList<Double> getInterMidDepartureTime() {
//        return interMidDepartureTime;
//    }
//
//    public ArrayList<Double> getBusyTimeFirstServer() {
//        return busyTimeFirstServer;
//    }
//    
//    public ArrayList<Double> getResponseTime(){
//        return responseTime;
//    }
//
//    public ArrayList<Double> getBusyTimeSecondServer() {
//        return busyTimeSecondServer;
//    }

    public HashMap<Integer, Double> getQueueLength() {
        return queueLength;
    }

    /**
     * get sum of the list
     *
     * @param list
     * @return
     */
    public double getSum(ArrayList<Double> list) {
        double sum = 0;
        for (Double d : list) {
            sum += d;
        }
        return sum;
    }

    /**
     * get mean value of the list
     *
     * @param list
     * @return double result
     */
    public double getMean(ArrayList<Double> list) {
        return getSum(list) / (list.size() * 1.0);
    }

    public double getAverageJobs(HashMap<Integer, Double> map) {
        double total = 0;
        double totalTime = 0;
        for (Entry<Integer, Double> e : map.entrySet()) {
            total += e.getKey() * e.getValue();
            totalTime += e.getValue();
        }
        return total / totalTime;
    }

    public double getFirstUtilization() {
        return sumServiceTime1 / t;
    }

    public double getSecondUtilization() {
        return sumServiceTime / t;
    }

    public double getMaxInterArrivalTime() {
        return maxInterArrivalTime;
    }

    public double getMaxTotalInterArrivalTime() {
        return maxTotalInterArrivalTime;
    }

    public double getMaxInterDepartureTime() {
        return maxInterDepartureTime;
    }

    public double getMaxInterMidArrivalTime() {
        return maxInterMidArrivalTime;
    }

    public double getMaxInterMidDepartureTime() {
        return maxInterMidDepartureTime;
    }

    public double getMaxServiceTime1() {
        return maxServiceTime1;
    }

    public double getMaxServiceTime() {
        return maxServiceTime;
    }

    public double getMaxResponseTime() {
        return maxResponseTime;
    }

    public int[] getInterArrivalTimeList() {
        return interArrivalTimeList;
    }

    public int[] getTotalInterArrivalTimeList() {
        return totalInterArrivalTimeList;
    }

    public int[] getInterDepartureTimeList() {
        return interDepartureTimeList;
    }

    public int[] getMidArrivalTimeList() {
        return midArrivalTimeList;
    }

    public int[] getInterMidDepartureTimeList() {
        return interMidDepartureTimeList;
    }

    public int[] getServiceTimeList1() {
        return serviceTimeList1;
    }

    public int[] getServiceTimeList() {
        return serviceTimeList;
    }

    public int[] getResponseTimeList() {
        return responseTimeList;
    }

    public double getAvgInterArrivalTime() {
        return sumInterArrivalTime / numOfArrivaledJobs;
    }

    public double getSDInterArrivalTime() {
        return Math.sqrt(sdInterArrivalTime / numOfArrivaledJobs - Math.pow(getAvgInterArrivalTime(), 2));
    }

    public double getAvgTotalInterArrivalTime() {
        return sumTotalInterArrivalTime / numOfTotalArrivaledJobs;
    }

    public double getSDTotalInterArrivalTime() {
        return Math.sqrt(sdTotalInterArrivalTime / numOfTotalArrivaledJobs - Math.pow(getAvgTotalInterArrivalTime(), 2));
    }

    public double getAvgInterDepartureTime() {
        return sumInterDepartureTime / numOfServicedJobs;
    }

    public double getSDInterDepartureTime() {
        return Math.sqrt(sdInterDepartureTime / numOfServicedJobs - Math.pow(getAvgInterDepartureTime(), 2));
    }

    public double getAvgMidArrivalTime() {
        return sumMidArrivalTime / numOfMidArrivaledJobs;
    }

    public double getSDMidArrivalTime() {
        return Math.sqrt(sdMidArrivalTime / numOfMidArrivaledJobs - Math.pow(getAvgMidArrivalTime(), 2));
    }

    public double getAvgInterMidDepartureTime() {
        return sumInterMidDepartureTime / numOfMidServicedJobs;
    }

    public double getSDInterMidDepartureTime() {
        return Math.sqrt(sdInterMidDepartureTime / numOfMidServicedJobs - Math.pow(getAvgInterMidDepartureTime(), 2));
    }

    public double getAvgServiceTime1() {
        return sumServiceTime1 / (numOfMidArrivaledJobs + numOfServicedJobs);
    }

    public double getSDServiceTime1() {
        double result = sdServiceTime1 / (numOfMidArrivaledJobs + numOfServicedJobs) - Math.pow(getAvgServiceTime1(), 2);
        if(result < 0){
            return 0.0000;
        }
        return Math.sqrt(result);
    }

    public double getAvgServiceTime() {
        return sumServiceTime / numOfMidServicedJobs;
    }

    public double getSDServiceTime() {
        double result = sdServiceTime / numOfMidServicedJobs - Math.pow(getAvgServiceTime(), 2);
        if(result < 0){
            return 0.0000;
        }
        return Math.sqrt(result);
    }

    public double getAvgResponseTime() {
        return sumResponseTime / numOfServicedJobs;
    }

    public double getSDResponseTime() {
        return Math.sqrt(sdResponseTime / numOfServicedJobs - Math.pow(getAvgResponseTime(), 2));
    }

    public double getAvgQueueLength() {
        return sumQueueLength / t;
    }

    public double getSDQueueLength() {
        return Math.sqrt(sdQueueLength / t - Math.pow(getAvgQueueLength(), 2));
    }
    
    public int getTotalArrials(){
        return numOfTotalArrivaledJobs;
    }
    
    public int getArrivals1(){
        return numOfArrivaledJobs;
    }
    
    public int getArrivals2(){
        return midCustomers;
    }
    
    public int getTotalDepartures(){
        return numOfServicedJobs;
    }
    
    public int getServicedJobs1(){
        return numOfJobs;
    }
    
    public int getServicedJobs2(){
        return numOfMidServicedJobs;
    }
    

}
