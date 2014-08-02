/*
 * InteractiveSystemCalculator.java - Calculator for interactive systems
 */
package gg1simulator;

public class InteractiveSystemCalculator {
    
    private int n, m;
    private double t, s;
    public double u, r, x, l;
    
    public InteractiveSystemCalculator(double t, double s, int n, int m) {        
        this.t = t;
        this.s = s;
        this.n = n;
        this.m = m;
    }
    
    public void Calculate() {
        double[] f = new double[500], p = new double[500];
        //critn = nproc * (serve + think) / serve;
        double rho = s / t;
        f[0] = 1.0;
        double sum = 1.0;
        int j = 1;
        while (j <= n) {
            f[j] = f[j - 1] * (n + 1. - j) * rho / Math.min(m, j);
            sum += f[j];
            j++;
        }
        j = 0;
        while (j <= m) {
            p[j] = f[j] / sum;
            j++;
        }
        double pidle = 0.;
        j = 0;
        while (j < n) {
            pidle += p[j] * (float) (m - j) / (float) (m);
            j++;
        }
        u = 1. - pidle;
        r = n * s / (u * m) - t;
        x = n / (t + r);
        l = r * x;
    }
    
    public double getUtilization() {        
        return u;
    }
    
    public double getResponseTime() {
        return r;
    }
    
    public double getThroughput() {
        return x;        
    }
    
    public double getCycleTime() {
        return t + r;
    }    

    public double getQueueLength() {
        return l;
    }

}
