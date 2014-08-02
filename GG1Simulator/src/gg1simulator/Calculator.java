/**
 * Calculator.java - Calculator for analytical models 
 * @author  Xiao Ping
 */
package gg1simulator;


public abstract class Calculator {
    public abstract double getArrivalMean();
    public abstract double getServiceMean();
    public abstract double getUtilization();
    public abstract double getResponseTime();
    public abstract double getQueueLength();
}
