/**
 * GG1Calculator.java - Calculator for G/G/1 Model
 * @author  Xiao Ping
 */
package gg1simulator;



public class GG1Calculator extends Calculator {

    double aMin, aMax, sMin, sMax;

    public GG1Calculator(double aMin, double aMax, double sMin, double sMax) {
        this.aMin = aMin;
        this.aMax = aMax;
        this.sMin = sMin;
        this.sMax = sMax;
    }

    @Override
    public double getUtilization() {
        double meanArrivalTime = (aMin + aMax)/2;
        double meanServiceTime = (sMin + sMax)/2;
        return meanServiceTime/meanArrivalTime;        
    }

    @Override
    public double getResponseTime() {
        double S = (sMin + sMax)/2;
        double va = (aMax - aMin) / ((aMax + aMin) * Math.sqrt(3.));
        double vs = (sMax - sMin) / ((sMax + sMin) * Math.sqrt(3.));
        double U = getUtilization();        
        double R = (S / (1. - U)) * (1 - 0.5 * U * (1. - vs * vs - ((vs * vs + 1.) * (va * va - 1.) / (U * U * vs * vs + 1))));
        return R;
    }

    @Override
    public double getQueueLength() {        
        return 0;
    }

    @Override
    public double getArrivalMean() {
        return (aMin + aMax)/2;
    }
    
    @Override
    public double getServiceMean() {
        return (sMin + sMax)/2;
    }
}
