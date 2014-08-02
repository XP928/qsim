/*
 * Utility.java
 */
package gg1simulator;

import java.text.DecimalFormat;
import java.util.*;

public class Utility {

    public static double getSum(ArrayList<Double> list) {
        double sum = 0;
        for (Double d : list) {
            sum += d;
        }
        return sum;
    }

    public static int getSum(int[] list) {
        int sum = 0;
        for (int i : list) {
            sum += i;
        }
        return sum;
    }

    public static double getSum(HashMap<Integer, Double> map) {
        double sum = 0;
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            sum += (double)pairs.getValue();
            it.remove(); 
        }
        return sum;
    }

    /**
     * get mean value of the list
     *
     * @param list
     * @return double result
     */
    public static double getMean(ArrayList<Double> list) {

        return getSum(list) / (list.size() * 1.0);
    }

    /**
     * get standard deviation of the list
     *
     * @param list
     * @return double result
     */
    public static double getSD(ArrayList<Double> list) {
        double mean = getMean(list), sum = 0;
        for (double d : list) {
            sum += Math.pow(d - mean, 2);
        }
        return Math.sqrt(sum / (list.size() - 1));
    }

    public static double getAverage(HashMap<Integer, Double> map) {
        double total = 0;
        double totalTime = 0;
        for (Map.Entry<Integer, Double> e : map.entrySet()) {
            total += e.getKey() * e.getValue();
            totalTime += e.getValue();
        }
        return total / totalTime;
    }

    public static double getStandardDeviation(HashMap<Integer, Double> map, double average) {
        double total = 0;
        double totalTime = 0;
        for (Map.Entry<Integer, Double> e : map.entrySet()) {
            total += e.getKey() * e.getKey() * e.getValue();
            totalTime += e.getValue();
        }
        total /= totalTime;
        total -= average * average;
        return Math.sqrt(total);
    }

    public static String formatOutput(double d) {
        DecimalFormat df = new DecimalFormat("##0.000");
        return df.format(d);
    }

    public static void print(String s, int length) {
        System.out.print(s);
        if (s.length() < length) {
            for (int i = 0; i < length - s.length(); i++) {
                System.out.print(" ");
            }
        }
    }
}
