package com.company;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Random;

public class NetworkTrainer {
    private NeuroNetwork net;
    private LinkedList<TrainingEntry> trainingSet;
    private NumberFormat formatter = new DecimalFormat("#0.00");
    private double learningRate = 0.7;
    private double momentum = 0.3;

    public void batchTrain(int times) {
        for (int i = 0; i < times; i++) {
            printWeights();
            double squareError = 0;
            for (TrainingEntry trainingEntry : trainingSet) {
                setInput(trainingEntry.getInput());
                net.calculateNetwork();
                printComparison(trainingEntry.getOutput()[0], net.getNeuronsLayers().getLast().getNeurons().getFirst().getSigmoidValue());
                squareError += calculateSquareError(trainingEntry);
                backPropagation(trainingEntry);
                setNewWeights();
            }
            printError(squareError);
        }
    }

    public void stochasticTrain(int times) {
        for (int i = 0; i < times; i++) {
            printWeights();
            TrainingEntry trainingEntry = trainingSet.get((int) Math.round(new Random().nextDouble() * (trainingSet.size() - 1)));
            setInput(trainingEntry.getInput());
            net.calculateNetwork();
            printComparison(trainingEntry.getOutput()[0], net.getNeuronsLayers().getLast().getNeurons().getFirst().getSigmoidValue());
            backPropagation(trainingEntry);
            printError(calculateSquareError(trainingEntry));
            setNewWeights();
        }
    }

    private void printError(double error) {
        System.out.println("Error:" + formatter.format(error));
        System.out.println();
    }

    private void printComparison(double expected, double result) {
        System.out.print("Exp:" + formatter.format(expected) + " ");
        System.out.println("Res:" + formatter.format(result));
    }

    private void printWeights() {
        for (NeuronsLayer neuronsLayer : net.getNeuronsLayers()) {
            for (Neuron neuron : neuronsLayer.getNeurons()) {
                if (neuron.getInputSynapses() != null && !neuron.getInputSynapses().isEmpty()) {
                    for (Synapse synapse : neuron.getInputSynapses()) {
                        System.out.print(formatter.format(synapse.getWeight()) + " ");
                    }
                    System.out.print(" ");
                }
            }
        }
        System.out.println();
    }

    private double calculateSquareError(TrainingEntry trainingEntry) {
        double realOutput = net.getNeuronsLayers().getLast().getNeurons().getFirst().getSigmoidValue();
        double expectedOutput = trainingEntry.getOutput()[0];
        return Math.pow(realOutput - expectedOutput, 2) / 2;
    }

    private void setInput(double[] input) {
        NeuronsLayer inputLayer = net.getNeuronsLayers().getFirst();
        for (int i = 0; i < input.length; i++) {
            Neuron inputNeuron = inputLayer.getNeurons().get(i);
            inputNeuron.setValue(input[i]);
        }
    }


    private void backPropagation(TrainingEntry trainingEntry) {
        //Result level
        LinkedList<Neuron> resultNeurons = net.getNeuronsLayers().getLast().getNeurons();
        double deltaOutputSum = deltaOutputSum(trainingEntry.getOutput());
        net.getNeuronsLayers().getLast().setDeltaSum(deltaOutputSum);
        for (Neuron neuron : resultNeurons) {
            for (Synapse synapse : neuron.getInputSynapses()) {
                double gradient = deltaOutputSum * synapse.getFrom().getSigmoidValue();
                double deltaWeight = learningRate * gradient + momentum * synapse.getDeltaWeight();
                synapse.setCalculatedWeight(synapse.getWeight() + deltaWeight);
                synapse.setDeltaWeight(deltaWeight);
            }
        }

        //Other levels
        for (int i = (net.getNeuronsLayers().size() - 2); i > 0; i--) {
            for (Neuron neuron : net.getNeuronsLayers().get(i).getNeurons()) {
                double deltaHiddenSum = deltaHiddenSum(net.getNeuronsLayers().get(i + 1), neuron);
                neuron.setDeltaHiddenSum(deltaHiddenSum);
                for (Synapse synapse : neuron.getInputSynapses()) {
                    double gradient = deltaHiddenSum * synapse.getFrom().getSigmoidValue();
                    double deltaWeight = learningRate * gradient + momentum * synapse.getDeltaWeight();
                    synapse.setCalculatedWeight(synapse.getWeight() + deltaWeight);
                    synapse.setDeltaWeight(deltaWeight);
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
        //to simplify -  for single-output network
        double error = resultNeurons.getFirst().getSigmoidValue() - trainingOutput[0];
        return -sigmoidPrime(resultNeurons.getFirst().getValue()) * error;
    }

    private double deltaHiddenSum(NeuronsLayer neuronsLayer, Neuron neuron) {
        double deltaHiddenSum = 0;
        for (Synapse synapse : neuron.getOutputSynapses()) {
            deltaHiddenSum += sigmoidPrime(neuron.getValue()) * synapse.getWeight() * neuronsLayer.getDeltaSum();
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
        System.out.println(sigmoidPrime(-0.16));
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
