package com.company;

import java.util.LinkedList;

public class NeuronsLayer {
    private double deltaSum;

    public NeuronsLayer() {
    }

    public NeuronsLayer(int neuronsNumber) {
        neurons = new LinkedList<>();
        for (int i = 0; i < neuronsNumber; i++) {
            neurons.add(new Neuron());
        }
    }

    private LinkedList<Neuron> neurons;

    public LinkedList<Neuron> getNeurons() {
        return neurons;
    }

    public void setNeurons(LinkedList<Neuron> neurons) {
        this.neurons = neurons;
    }

    public void setDeltaSum(double deltaSum) {
        this.deltaSum = deltaSum;
    }

    public double getDeltaSum() {
        return deltaSum;
    }
}
