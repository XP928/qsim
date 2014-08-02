/*
 * MG1Calculator.java - Calculator for M/G/1 model
 */
package gg1simulator;

public class MG1Calculator extends Calculator {

    double a, sMin, sMax;
    
    public double getMin(){
        return sMin;
    }
    
    public double getMax(){
        return sMax;
    }

    public MG1Calculator(double a, double sMin, double sMax) {
        this.a = a;
        this.sMin = sMin;
        this.sMax = sMax;
    }

    @Override
    public double getUtilization() {
        double s = (sMin + sMax) / 2;
        return s / a;
    }

    @Override
    public double getResponseTime() {
        double S = (sMin + sMax) / 2;
        double vs = (sMax - sMin) / ((sMax + sMin) * Math.sqrt(3.));
        double U = getUtilization();
        double R = (S / (1. - U)) * (1 - 0.5 * U * (1. - vs * vs));
        return R;
    }

    @Override
    public double getQueueLength() {
        double vs = (sMax - sMin) / ((sMax + sMin) * Math.sqrt(3.));
        double U = getUtilization();
        double aRate = 1.0 / a;
        return U + (U * U + aRate * aRate * vs) / (2 * (1 - U));
    }
    
    @Override
    public double getArrivalMean() {
        return a;
    }
    
    @Override
    public double getServiceMean() {
        return (sMin + sMax) / 2;
    }
}
