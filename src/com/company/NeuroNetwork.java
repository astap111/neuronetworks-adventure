package com.company;

import java.util.LinkedList;
import java.util.Random;

public class NeuroNetwork {

    private NeuroNetwork() {
    }

    private LinkedList<NeuronsLayer> neuronsLayers;

    public LinkedList<NeuronsLayer> getNeuronsLayers() {
        return neuronsLayers;
    }

    public void setNeuronsLayers(LinkedList<NeuronsLayer> neuronsLayers) {
        this.neuronsLayers = neuronsLayers;
    }

    public static class Builder {
        private Builder() {
        }

        public static NeuroNetwork build(int... layerQuantities) {
            NeuroNetwork net = new NeuroNetwork();
            net.setNeuronsLayers(new LinkedList<>());

            for (int layerNum = 0; layerNum < layerQuantities.length; layerNum++) {
                net.getNeuronsLayers().add(new NeuronsLayer(layerQuantities[layerNum]));
                if (layerNum > 0) {
                    NeuronsLayer prevLayer = net.getNeuronsLayers().get(net.getNeuronsLayers().size() - 2);
                    for (Neuron currNeuron : net.getNeuronsLayers().get(net.getNeuronsLayers().size() - 1).getNeurons()) {
                        for (Neuron prevNeuron : prevLayer.getNeurons()) {
                            Synapse synapse = new Synapse();
                            prevNeuron.getOutputSynapses().add(synapse);
                            currNeuron.getInputSynapses().add(synapse);
                            synapse.setFrom(prevNeuron);
                            synapse.setTo(currNeuron);
                            synapse.setWeight(new Random().nextDouble() - 0.5);
                        }
                    }
                }
            }
            return net;
        }
    }
}