/*
 * FeedbackQueueCalculator.java - Calculator for feedback queuing model
 */
package gg1simulator;

public class FeedbackQueueCalculator {

    double arrivalTime;
    double firstServiceTime;
    double secondServiceTime;
    double p;
    
    public double getMeanArrivalTime(){
        return arrivalTime;
    }
    
    public double getMeanFirstServiceTime(){
        return firstServiceTime;
    }
    
    public double getMeanSecondServiceTime(){
        return secondServiceTime;
    }

    public FeedbackQueueCalculator(double arrivalTime, double firstServiceTime, double secondServiceTime, double p) {
        this.arrivalTime = arrivalTime;
        this.firstServiceTime = firstServiceTime;
        this.secondServiceTime = secondServiceTime;
        this.p = p;
    }

    public double getFirstUtilization() {
        return firstServiceTime / (arrivalTime * (1 - p));
    }

    public double getSecondUtilization() {
        return p * secondServiceTime / (arrivalTime * (1 - p));
    }

    public double getJobsInTheSystem() {
        double j1 = firstServiceTime / (arrivalTime * (1 - p) - firstServiceTime);
        double j2 = p * secondServiceTime / (arrivalTime * (1 - p) - p * secondServiceTime);
        return j1 + j2;
    }
    
    public double getResponseTime() {
        double j1 = firstServiceTime / (arrivalTime * (1 - p) - firstServiceTime);
        double j2 = p * secondServiceTime / (arrivalTime * (1 - p) - p * secondServiceTime);
        return (j1 + j2) * arrivalTime;
    }   
}
