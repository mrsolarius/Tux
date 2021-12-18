/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.litopia.tux.game.utils;

/**
 *
 * @author gladen
 */
public class Random {

    private double min;
    private double max;
    private double range;

    public Random(double min, double max) {
        setMinMax(min, max);
        if (range == 0.0) {
            range = 1.0;
        }
    }

    public void setMin(double min) {
        setMinMax(min, max);
    }

    public void setMax(double max) {
        setMinMax(min, max);
    }
    
    public void setMinMax(double min, double max) {
        if (min <= max) {
            this.min = min;
            this.max = max;
        } else {
            this.min = max;
            this.max = min;
        }
        range = (max - min);
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getRange() {
        return range;
    }

   
    public double get() {
        return (Math.random() * range) + min;
    }

}
