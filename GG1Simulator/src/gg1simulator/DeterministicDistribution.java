/*
 * DeterministicDistribution.java
 */
package gg1simulator;

public class DeterministicDistribution extends Distribution {
    private double constrant;
    
    public DeterministicDistribution(double constrant){
        this.constrant = constrant;
    }

    @Override
    public double generateRandomTimes() {
        return constrant;
    }

    @Override
    public double getMean() {
        return constrant;
    }

}
