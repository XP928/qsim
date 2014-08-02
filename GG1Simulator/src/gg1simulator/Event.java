/**
 * Event.java 
 * 
 */
package gg1simulator;

public class Event implements Comparable<Event> {

    public static final int ARRIVAL = 0;
    public static final int DEPARTURE = 1;
    public static final int MIDARRIVAL = 2;
    public static final int MIDDEPARTURE = 3;
    public static final int DEPARTURE2 = 4;
    public static final int FIRSTBRANCHARRIVAL = 5;
    public static final int SECONDBRANCHARRIVAL = 6;
    protected double startsTime;
    protected double endsTime;      // next arrival or departure time
    protected int code;
    protected int branch;
    protected int id;

    public Event(double startsTime, double endsTime, int code, int branch) {
        this.startsTime = startsTime;
        this.endsTime = endsTime;
        this.code = code;
        this.branch = branch;
    }
    
    public Event(int id, double startsTime, double endsTime, int code, int branch) {
        this.id = id;
        this.startsTime = startsTime;
        this.endsTime = endsTime;
        this.code = code;
        this.branch = branch;
    }

    public double getStartsTime() {
        return startsTime;
    }

    public double getEndsTime() {
        return endsTime;
    }

    public int getCode() {
        return code;
    }

    public int getBranch() {
        return branch;
    }

    @Override
    public int compareTo(Event e) {
        return Double.compare(endsTime, e.endsTime);
    }
}
