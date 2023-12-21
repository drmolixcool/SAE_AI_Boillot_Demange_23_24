package mnist;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Etiquette {

    private List<Integer> liste;

    Etiquette(String fichier) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(fichier));

        int typeFile = dataInputStream.readInt();
        int nbEtiquette = dataInputStream.readInt();
        this.liste = new ArrayList<>();
        for (int i = 0; i < nbEtiquette; i++) {
            this.liste.add(dataInputStream.readUnsignedByte());
        }
    }

    public List<Integer> liste() throws IOException {
        return Collections.unmodifiableList(this.liste);
    }
}
