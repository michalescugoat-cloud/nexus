package com.nexus.module;

public class Setting {
    public final String name;
    public final double min, max;
    private double value;

    public Setting(String name, double def, double min, double max) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.value = def;
    }

    public double get() { return value; }
    public boolean isBool() { return min == 0 && max == 1; }
    public boolean bool() { return value >= 0.5; }
    public void set(double v) { value = Math.max(min, Math.min(max, v)); }
}
