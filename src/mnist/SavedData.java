package mnist;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SavedData {
    public static void main(String[] args) throws IOException {
        String train_labels = "train-labels.idx1-ubyte";
        Etiquette etiquettes = new Etiquette(train_labels);

        String train_images = "train-images.idx3-ubyte";
        Donnees images = new Donnees(train_images, etiquettes);

        String test_labels = "t10k-labels.idx1-ubyte";
        Etiquette etiquettes_test = new Etiquette(test_labels);

        String test_images = "t10k-images.idx3-ubyte";
        Donnees imagesTest = new Donnees(test_images, etiquettes_test);

        try (ObjectOutputStream dataOutputStream = new ObjectOutputStream(new FileOutputStream("mnist.bin"))) {
            dataOutputStream.writeObject(images);
            dataOutputStream.writeObject(imagesTest);
        }
    }
}
