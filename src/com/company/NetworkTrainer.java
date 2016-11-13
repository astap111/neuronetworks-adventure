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
            double squareError = 0;
            for (TrainingEntry trainingEntry : trainingSet) {
                setInput(trainingEntry.getInput());
                net.calculateNetwork();
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

    public void stochasticTrain(int times) {
        for (int i = 0; i < times; i++) {
            TrainingEntry trainingEntry = trainingSet.get((int) Math.round(new Random().nextDouble() * (trainingSet.size() - 1)));
            setInput(trainingEntry.getInput());
            printWeights();
            net.calculateNetwork();
            System.out.print("Exp:" + trainingEntry.getOutput()[0] + " ");
            System.out.println("Calc:" + net.getNeuronsLayers().getLast().getNeurons().getFirst().getSigmoidValue() + " ");
            backPropagation(trainingEntry);
            setNewWeights();
            System.out.println("Error:" + calculateSquareError(trainingEntry));
            System.out.println();
        }
    }

    private void printWeights() {
        for (NeuronsLayer neuronsLayer : net.getNeuronsLayers()) {
            for (Neuron neuron : neuronsLayer.getNeurons()) {
                if (neuron.getOutputSynapses() != null && !neuron.getOutputSynapses().isEmpty()) {
                    for (Synapse synapse : neuron.getOutputSynapses()) {
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
        for (int i = 0; i < inputLayer.getNeurons().size(); i++) {
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
                double deltaWeight = deltaOutputSum / synapse.getFrom().getSigmoidValue();
                synapse.setCalculatedWeight(synapse.getWeight() + deltaWeight);
            }
        }

        //Other levels
        for (int i = (net.getNeuronsLayers().size() - 2); i > 0; i--) {
            for (Neuron neuron : net.getNeuronsLayers().get(i).getNeurons()) {
                double deltaHiddenSum = deltaHiddenSum(net.getNeuronsLayers().get(i + 1), neuron);
                neuron.setDeltaHiddenSum(deltaHiddenSum);
                for (Synapse synapse : neuron.getInputSynapses()) {
                    double gradient = deltaHiddenSum * synapse.getFrom().getSigmoidValue();
                    synapse.setCalculatedWeight(synapse.getWeight() + deltaHiddenSum / synapse.getFrom().getValue());
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
        double error = trainingOutput[0] - resultNeurons.getFirst().getSigmoidValue();
        return -sigmoidPrime(resultNeurons.getFirst().getValue()) * error;
    }

    private double deltaHiddenSum(NeuronsLayer neuronsLayer, Neuron neuron) {
        double deltaHiddenSum = 0;
        for (Synapse synapse : neuron.getOutputSynapses()) {
            deltaHiddenSum += sigmoidPrime(neuron.getValue() * synapse.getWeight() * neuronsLayer.getDeltaSum());
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
        System.out.println(sigmoidPrime(1));
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
