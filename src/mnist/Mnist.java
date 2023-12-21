package mnist;

import mlp.FalseValue;
import mlp.MLP;
import mlp.SigmoidFunction;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Mnist {

    private final Donnees images_test;
    private final Donnees images;
    private boolean verbose;

    public Mnist(boolean verbose) throws IOException {
        this();

        this.verbose = verbose;
    }

    public Mnist(String folder) throws IOException {
        String train_labels = folder + "/train-labels.idx1-ubyte";
        Etiquette etiquettes = new Etiquette(train_labels);

        String train_images = folder + "/train-images.idx3-ubyte";
        images = new Donnees(train_images, etiquettes);

        String test_labels = folder + "/t10k-labels.idx1-ubyte";
        Etiquette etiquettes_test = new Etiquette(test_labels);

        String test_images = folder + "/t10k-images.idx3-ubyte";
        images_test = new Donnees(test_images, etiquettes_test);
    }

    public Mnist(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))) {
            this.images = (Donnees) objectInputStream.readObject();
            this.images_test = (Donnees) objectInputStream.readObject();
        }
    }

    public Mnist() throws IOException {
        this(".");
    }

    private static int extractResult(double[] output) {
        double maxed = Double.MIN_VALUE;
        int index_maxed = 0;
        for (int i = 0; i < output.length; i++) {
            if (output[i] > maxed) {
                maxed = output[i];
                index_maxed = i;
            }
        }
        return index_maxed;
    }

    private static double[] getInput(Imagette imagette, FalseValue transfertFunction) {
        double[][] raw_inputs = new double[imagette.getWidth()][imagette.getHeight()];
        for (int x = 0; x < imagette.getWidth(); x++) {
            for (int y = 0; y < imagette.getHeight(); y++) {
                raw_inputs[x][y] = normalize(imagette.getPixels(x, y), transfertFunction);
            }
        }
        return Arrays.stream(raw_inputs).flatMapToDouble(Arrays::stream).toArray();
    }

    private static double normalize(int pixels, FalseValue transfertFunction) {
        double max_value = 1;
        double min_value = transfertFunction.getFalseValue();

        double max_value_pixel = 255;

        // calcule l'étendue entre les valeurs extrèmes qu'on souhaite comme résultat
        // puis adapte / normalise la valeur d'un pixel entre ces deux valeurs extrème.
        //return (pixels * Math.abs(max_value-min_value) / max_value_pixel)+min_value;
        return pixels / max_value_pixel;
    }

    public double start(int... neuronnes_couche_cachee) throws IOException {
        List<Imagette> liste = new ArrayList<>(images.liste());

        int input_neuron = liste.getFirst().getHeight() * liste.getFirst().getWidth();
        int output_neuron = 10;

        // première valeur : nombre d'entrées, soit nombre de pixels : largeur x hauteur
        // la ou les valeurs du milieu : ???
        // dernière valeur : nombre de sorties, soit 10 classes correspondant au 10 chiffres
        int[] layers = new int[neuronnes_couche_cachee.length + 2];
        layers[0] = input_neuron;
        layers[layers.length - 1] = output_neuron;

        int index = 1;
        for (int nb_neurons : neuronnes_couche_cachee) {
            layers[index++] = nb_neurons;
        }

        double learningRate = 0.3;
        var function = new SigmoidFunction();
        MLP mlp = new MLP(layers, learningRate, function);

        List<Boolean> checked = Collections.synchronizedList(new ArrayList<>());
        List<Double> learn_errors = Collections.synchronizedList(new ArrayList<>());

        for (int iter = 1; iter <= 100; iter++) {
            checked.clear();
            for (int i = 0; i < 10; i++) {
                Collections.shuffle(liste);
                for (Imagette imagette : liste) {
                    double[] inputs = getInput(imagette, function);

                    double[] outputs = IntStream.range(0, 10).mapToDouble(el -> function.getFalseValue()).toArray();
                    outputs[imagette.getEtiquette()] = 1;

                    double error = mlp.backPropagate(inputs, outputs);
                    learn_errors.add(error);

                    if (verbose) {
//            System.out.println(Arrays.toString(inputs));
                        System.out.println(Arrays.toString(outputs));
                        System.out.println("error = " + error);
                    }
                }
            }

//        Imagette test = images_test.liste().getFirst();
//        test.save(new File("image.png"));
//
//        double[] inputs = getInput(test, function);
//        double[] executed = mlp.execute(inputs);
//        if (verbose)
//            System.out.println("mlp.execute(inputs) = " + Arrays.toString(executed));
//
//        int index_maxed = extractResult(executed);
//        if (verbose)
//            System.out.println("Most probably value = " + index_maxed);

            List<Imagette> imagettes_test = images_test.liste();
            for (Imagette imagette : imagettes_test) {
                double[] input = getInput(imagette, function);
                double[] output = mlp.execute(input);
                checked.add(extractResult(output) == imagette.getEtiquette());
            }
            System.out.println("iter " + iter + " = " + checked.stream().filter(aBoolean -> aBoolean).count() * 100.0 / checked.size());
            System.out.println("iter " + iter + " = " + learn_errors.stream().mapToDouble(value -> value).average().orElse(0));
        }

        return checked.stream().filter(Boolean::booleanValue).count() * 100.0 / checked.size();
    }
}
