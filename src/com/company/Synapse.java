package com.company;

public class Synapse {
    private Neuron from;
    private Neuron to;
    private double weight;
    private double calculatedWeight;
    private double deltaWeight;

    public Neuron getFrom() {
        return from;
    }

    public void setFrom(Neuron from) {
        this.from = from;
    }

    public Neuron getTo() {
        return to;
    }

    public void setTo(Neuron to) {
        this.to = to;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getCalculatedWeight() {
        return calculatedWeight;
    }

    public void setCalculatedWeight(double calculatedWeight) {
        this.calculatedWeight = calculatedWeight;
    }

    public double getDeltaWeight() {
        return deltaWeight;
    }

    public void setDeltaWeight(double deltaWeight) {
        this.deltaWeight = deltaWeight;
    }
}
