/*
 * MD1Calculator.java - Calculator for M/D/1 Model
 */
package gg1simulator;


public class MD1Calculator extends Calculator {

    double meanArrivalTime, meanServiceTime;

    public MD1Calculator(double meanArrivalTime, double meanServiceTime) {
        this.meanArrivalTime = meanArrivalTime;
        this.meanServiceTime = meanServiceTime;
    }

    @Override
    public double getUtilization() {
        return meanServiceTime / meanArrivalTime;
    }

    @Override
    public double getResponseTime() {
        return meanServiceTime * (2 - getUtilization()) / (2 * (1 - getUtilization()));
    }

    @Override
    public double getQueueLength() {
        double u = getUtilization();
        return u * u / (2 * (1 - u)) + u; 
    }
    
    @Override
    public double getArrivalMean() {
        return meanArrivalTime;
    }
    
    @Override
    public double getServiceMean() {
        return meanServiceTime;
    }
}
