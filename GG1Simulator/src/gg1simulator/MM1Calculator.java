/*
 * MM1Calculator.java - Calculator for M/M/1 Model
 * and open the template in the editor.
 */
package gg1simulator;


public class MM1Calculator extends Calculator {
    double meanArrivalTime, meanServiceTime;

    public MM1Calculator(double meanArrivalTime, double meanServiceTime) {
        this.meanArrivalTime = meanArrivalTime;
        this.meanServiceTime = meanServiceTime;
    }

    @Override
    public double getUtilization() {
        return meanServiceTime / meanArrivalTime;
    }

    @Override
    public double getResponseTime() {
        return meanServiceTime / (1 - getUtilization());
    }
    
    @Override
    public double getQueueLength() {
        double u = getUtilization();
        return u / (1 - u);
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
