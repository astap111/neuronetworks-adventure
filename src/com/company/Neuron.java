package com.company;

import java.util.LinkedList;

public class Neuron {
    private double value;
    private double sigmoidValue;
    private LinkedList<Synapse> inputSynapses = new LinkedList<>();
    private LinkedList<Synapse> outputSynapses = new LinkedList<>();

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getSigmoidValue() {
        return sigmoidValue;
    }

    public void setSigmoidValue(double sigmoidValue) {
        this.sigmoidValue = sigmoidValue;
    }

    public LinkedList<Synapse> getInputSynapses() {
        return inputSynapses;
    }

    public void setInputSynapses(LinkedList<Synapse> inputSynapses) {
        this.inputSynapses = inputSynapses;
    }

    public LinkedList<Synapse> getOutputSynapses() {
        return outputSynapses;
    }

    public void setOutputSynapses(LinkedList<Synapse> outputSynapses) {
        this.outputSynapses = outputSynapses;
    }
}
