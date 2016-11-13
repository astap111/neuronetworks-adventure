package com.company;

import java.util.Arrays;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        NeuroNetwork net = NeuroNetwork.Builder.build(2, 2, 1);

        NetworkTrainer trainer = new NetworkTrainer();
        trainer.setNet(net);

        LinkedList<TrainingEntry> trainigSet = new LinkedList<>();
        TrainingEntry entry1 = new TrainingEntry();
        entry1.setInput(new double[]{0d, 0d});
        entry1.setOutput(new double[]{0d});
        TrainingEntry entry2 = new TrainingEntry();
        entry2.setInput(new double[]{1d, 0d});
        entry2.setOutput(new double[]{1d});
        TrainingEntry entry3 = new TrainingEntry();
        entry3.setInput(new double[]{0d, 1d});
        entry3.setOutput(new double[]{1d});
        TrainingEntry entry4 = new TrainingEntry();
        entry4.setInput(new double[]{1d, 1d});
        entry4.setOutput(new double[]{0d});
        trainigSet.addAll(Arrays.asList(entry1, entry2, entry3, entry4));
        trainer.setTrainingSet(trainigSet);

        trainer.stochasticTrain(1000000);

        System.out.println(net);
    }
}
