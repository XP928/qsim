package gg1simulator;

import java.util.*;


public class ExponentialDistribution extends Distribution {

    double lambda;
    Random rand = new Random();

    public ExponentialDistribution(double lambda) {
        this.lambda = lambda;
    }

    @Override
    public double generateRandomTimes() {
        return - lambda * Math.log(rand.nextDouble());
    }

    @Override
    public double getMean() {
        return  lambda;
    }
}
