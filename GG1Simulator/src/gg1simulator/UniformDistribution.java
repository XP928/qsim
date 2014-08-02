/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg1simulator;

import java.util.Random;

/**
 *
 * @author Xiao Ping
 */
public class UniformDistribution extends Distribution {

    double a, b;
    Random generator = new Random();

    public UniformDistribution(double a, double b) {
        this.a = Math.min(a, b);
        this.b = Math.max(a, b);
    }

    @Override
    public double generateRandomTimes() {
        return a + generator.nextDouble() * (b - a);
    }

    @Override
    public double getMean() {
        return (a + b) /2;
    }
    
    public double getMin(){
        return Math.min(a, b);
    }
    
    public double getMax(){
        return Math.max(a, b);
    }

}
