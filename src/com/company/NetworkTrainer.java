package com.company;

import java.util.LinkedList;

public class NetworkTrainer {
    private NeuroNetwork net;
    private LinkedList<TrainingEntry> trainingSet;

    public void train(int times) {
        for (int i = 0; i < times; i++) {
            double squareError = 0;
            for (TrainingEntry trainingEntry : trainingSet) {
                setInput(trainingEntry.getInput());
                calculateNetwork();
                System.out.print("Exp:" + trainingEntry.getOutput()[0] + " ");
                System.out.println("Calc:" + net.getNeuronsLayers().getLast().getNeurons().getFirst().getSigmoidValue() + " ");
                squareError += calculateSquareError(trainingEntry);
                backPropagation(trainingEntry);
                setNewWeights();
            }
            System.out.println("Error:" + squareError);
            System.out.println();
        }
    }

    private double calculateSquareError(TrainingEntry trainingEntry) {
        double realOutput = net.getNeuronsLayers().getLast().getNeurons().getFirst().getSigmoidValue();
        double expectedOutput = trainingEntry.getOutput()[0];
        return Math.pow(realOutput - expectedOutput, 2);
    }

    private void setInput(double[] input) {
        NeuronsLayer inputLayer = net.getNeuronsLayers().getFirst();
        for (int i = 0; i < inputLayer.getNeurons().size(); i++) {
            Neuron inputNeuron = inputLayer.getNeurons().get(i);
            inputNeuron.setValue(input[i]);
        }
    }

    private void calculateNetwork() {
        for (int i = 1; i < net.getNeuronsLayers().size(); i++) {
            NeuronsLayer neuronsLayer = net.getNeuronsLayers().get(i);
            for (Neuron neuron : neuronsLayer.getNeurons()) {
                double calculatedValue = 0;
                for (Synapse synapse : neuron.getInputSynapses()) {
                    calculatedValue += synapse.getFrom().getSigmoidValue() * synapse.getWeight();
                }
                neuron.setValue(calculatedValue);
                neuron.setSigmoidValue(sigmoid(calculatedValue));
            }
        }
    }

    private void backPropagation(TrainingEntry trainingEntry) {
        //Result level
        LinkedList<Neuron> resultNeurons = net.getNeuronsLayers().getLast().getNeurons();
        double deltaOutputSum = deltaOutputSum(trainingEntry.getOutput());
        net.getNeuronsLayers().getLast().setDeltaSum(deltaOutputSum);
        for (Neuron neuron : resultNeurons) {
            for (Synapse synapse : neuron.getInputSynapses()) {
                double deltaWeight = deltaOutputSum / synapse.getTo().getSigmoidValue();
                synapse.setCalculatedWeight(synapse.getWeight() + deltaWeight);
            }
        }

        //Other levels
        for (int i = (net.getNeuronsLayers().size() - 2); i >= 0; i--) {
            for (Neuron neuron : net.getNeuronsLayers().get(i).getNeurons()) {
                double deltaHiddenSum = deltaHiddenSum(net.getNeuronsLayers().get(i + 1), neuron);
                net.getNeuronsLayers().get(i).setDeltaSum(deltaHiddenSum);
                for (Synapse synapse : neuron.getInputSynapses()) {
                    synapse.setCalculatedWeight(synapse.getWeight() + deltaHiddenSum);
                }
            }
        }
    }

    private void setNewWeights() {
        for (NeuronsLayer neuronsLayer : net.getNeuronsLayers()) {
            for (Neuron neuron : neuronsLayer.getNeurons()) {
                if (neuron.getOutputSynapses() != null && neuron.getOutputSynapses().size() > 0) {
                    for (Synapse synapse : neuron.getOutputSynapses()) {
                        synapse.setWeight(synapse.getCalculatedWeight());
                    }
                }
            }
        }
    }

    private double deltaOutputSum(double[] trainingOutput) {
        LinkedList<Neuron> resultNeurons = net.getNeuronsLayers().getLast().getNeurons();
        //to simplify -  for one output network
        double error = trainingOutput[0] - resultNeurons.getFirst().getSigmoidValue();
        return sigmoidPrime(resultNeurons.getFirst().getValue()) * error;
    }

    private double deltaHiddenSum(NeuronsLayer neuronsLayer, Neuron neuron) {
        double deltaHiddenSum = 0;
        for (Synapse synapse : neuron.getOutputSynapses()) {
            deltaHiddenSum += (neuronsLayer.getDeltaSum() / synapse.getCalculatedWeight()) * sigmoidPrime(neuron.getSigmoidValue());
        }

        return deltaHiddenSum;
    }

    private static double sigmoid(double x) {
        return (1 / (1 + Math.pow(Math.E, (-x))));
    }

    private static double sigmoidPrime(double x) {
        return sigmoid(x) * (1 - sigmoid(x));
    }

    public static void main(String... args) {
        System.out.println(sigmoid(0.8365));
    }

    public NeuroNetwork getNet() {
        return net;
    }

    public void setNet(NeuroNetwork net) {
        this.net = net;
    }

    public LinkedList<TrainingEntry> getTrainingSet() {
        return trainingSet;
    }

    public void setTrainingSet(LinkedList<TrainingEntry> trainingSet) {
        this.trainingSet = trainingSet;
    }
}
