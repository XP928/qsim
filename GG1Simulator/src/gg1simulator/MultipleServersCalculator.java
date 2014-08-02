/*
 * MultipleServersCalculator.java - Calculator for multiple servers model
 */
package gg1simulator;

public class MultipleServersCalculator extends Calculator {
    
    private int numOfServers;
    private double avgArrivalTime, avgServiceTime;
    private double waitTime, responseTime, jobs;
    
    public MultipleServersCalculator(int numOfServers, double avgArrivalTime, double avgServiceTime){
        this.numOfServers = numOfServers;
        this.avgArrivalTime = avgArrivalTime;
        this.avgServiceTime = avgServiceTime;
    }

    @Override
    public double getArrivalMean() {
        return avgArrivalTime;
    }

    @Override
    public double getServiceMean() {
        return avgServiceTime;
    }

    @Override
    public double getUtilization() {
        return avgServiceTime /(numOfServers * avgArrivalTime);
    }
    
    
    public void Calculate(){
        double u = avgServiceTime / avgArrivalTime;
        double sum = 1;
        for(int i = 2; i <= numOfServers; i++){
            sum += Math.pow(u, i - 1) / factorial(i - 1);
        }
        sum += Math.pow(u, numOfServers) / (factorial(numOfServers) * (1 - u / numOfServers));
        sum *= factorial(numOfServers);
        sum *= numOfServers;
        sum *= Math.pow(1 - u / numOfServers, 2);
        waitTime = avgServiceTime * Math.pow(u, numOfServers) / sum;
        responseTime = avgServiceTime + waitTime;
        jobs = responseTime / avgArrivalTime;
    }
    
    public static int factorial(int x) {
        if (x < 0) {
            throw new IllegalArgumentException("x must be>=0");
        }
        int fact = 1;
        for (int i = 2; i <= x; i++) {
            fact *= i;
        }
        return fact;
    }
    
    public double getWaitTime(){
        return waitTime;
    }

    @Override
    public double getResponseTime() {
        return responseTime;
    }

    @Override
    public double getQueueLength() {
        return jobs;
    }    
    
}
