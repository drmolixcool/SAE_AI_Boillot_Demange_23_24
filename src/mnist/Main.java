package mnist;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        long start_time = System.nanoTime();

        String train_labels = "train-labels.idx1-ubyte";
        Etiquette etiquettes = new Etiquette(train_labels);

        String train_images = "train-images.idx3-ubyte";
        Donnees images = new Donnees(train_images, etiquettes);

        String test_labels = "t10k-labels.idx1-ubyte";
        Etiquette etiquettes_test = new Etiquette(test_labels);

        String test_images = "t10k-images.idx3-ubyte";
        Donnees images_test = new Donnees(test_images, etiquettes_test);

//        Imagette first_image_test = images_test.liste().get(0);
//        System.out.println(first_image_test.getEtiquette());
//        int analyse = new PlusProche(images).analyse(first_image_test);
//        System.out.println(analyse);

        AlgoClassification algo = null; // TODO change here
        List<Boolean> checked = Collections.synchronizedList(new ArrayList<>());
        List<Imagette> test = images_test.liste();
        for (Imagette imagette : test) {
            Thread thread = new Thread(() -> {
                int analyse1 = algo.analyse(imagette);
                checked.add(analyse1 == imagette.getEtiquette());
            });
            thread.start();
        }
        while (test.size() != checked.size()) {}
        System.out.println(checked.stream().filter(aBoolean -> aBoolean).count()*100/checked.size());

        System.out.println(Duration.ofNanos(System.nanoTime()-start_time));
    }
}
