package mnist;

import mnist.Etiquette;
import mnist.Imagette;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class Donnees implements Serializable {
    private List<Imagette> liste;

    Donnees(String fichier, Etiquette etiquettes, Integer limit) throws IOException {
        DataInputStream images = new DataInputStream(new FileInputStream(fichier));

        int imageType = images.readInt();
        int nbImage = images.readInt();
        int width = images.readInt();
        int height = images.readInt();

        if (limit == null) {
            limit = nbImage;
        }

        this.liste = new ArrayList<>();
        Iterator<Integer> iterator = etiquettes.liste().iterator();
        for (int i = 0; i < limit; i++) {
            Imagette imagette = new Imagette(width, height, iterator.next());
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int raw_color = images.readUnsignedByte();
                    imagette.setPixels(x, y, raw_color);
                }
            }
            this.liste.add(imagette);
        }
    }

    Donnees(String fichier, Etiquette etiquettes) throws IOException {
        this(fichier, etiquettes, null);
    }

    private Donnees(List<Imagette> liste) {
        this.liste = new ArrayList<>(liste);
    }

    public List<Imagette> liste() throws IOException {
        return Collections.unmodifiableList(this.liste);
    }

    @Deprecated
    public Donnees limit(int nb_limit) {
        return new Donnees(liste.stream().limit(nb_limit).toList());
    }

    public Donnees equalsEtiquette() {
        ArrayList<Imagette> imagettes = new ArrayList<>(this.liste);
        Collections.shuffle(imagettes);

        HashMap<Integer, List<Imagette>> etiquetteListHashMap = new HashMap<>();
        for (Imagette imagette : imagettes) {
            etiquetteListHashMap.putIfAbsent(imagette.getEtiquette(), new ArrayList<>());
            etiquetteListHashMap.get(imagette.getEtiquette()).add(imagette);
        }
        int min_length = etiquetteListHashMap.values().stream().mapToInt(List::size).min().orElse(0);
        List<Imagette> result = etiquetteListHashMap.values().stream()
                .map((List<Imagette> liste) -> liste.subList(0, min_length))
                .flatMap(Collection::stream)
                .toList();

        Collections.shuffle(imagettes);

        return new Donnees(result);
    }
}
