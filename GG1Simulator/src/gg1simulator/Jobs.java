/*
 * Jobs.java
 */
package gg1simulator;

public class Jobs implements Comparable<Jobs> {

    private int jobId;
    private double startTime;
    private double arrivalTime;
    private double lastArrivalTime;
    private double startServiceTime;
    private double midArrivalTime;
    private double midDepartureTime;
    private double departureTime;


    public Jobs(int jobId, double arrivalTime, double lastArrivalTime) {
        this.jobId = jobId;
        this.arrivalTime = arrivalTime;
        this.lastArrivalTime = lastArrivalTime;
    }
    
    public Jobs(int jobId, double startTime, double lastArrivalTime, double departureTime){
        this.jobId = jobId;
        this.startTime = startTime;
        this.lastArrivalTime = lastArrivalTime;
        this.departureTime = departureTime;
    }
    
    

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }
    
    public double getStartTime(){
        return startTime;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public double getMidArrivalTime() {
        return midArrivalTime;
    }

    public double getLastArrivalTime() {
        return lastArrivalTime;
    }

    public void setLastArrivaltime(double lastArrivalTime) {
        this.lastArrivalTime = lastArrivalTime;
    }

    public void setMidArrivalTime(double midArrivalTime) {
        this.midArrivalTime = midArrivalTime;
    }

    public double getMidDepartureTime() {
        return midDepartureTime;
    }

    public void setMidDepartureTime(double midDepartureTime) {
        this.midDepartureTime = midDepartureTime;
    }

    public double getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(double departureTime) {
        this.departureTime = departureTime;
    }

    public double getStartServiceTime() {
        return startServiceTime;
    }

    public void setStartServiceTime(double startServiceTime) {
        this.startServiceTime = startServiceTime;
    }

    @Override
    public int compareTo(Jobs jobs) {
        return Double.compare(lastArrivalTime, jobs.lastArrivalTime);
    }
}
