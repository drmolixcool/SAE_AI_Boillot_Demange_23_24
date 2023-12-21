package gps;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
//        PointDegree degIci = PointDegree.ici();
//        PointRadians ici = new PointRadians(degIci);
//        System.out.println("ici = " + degIci);
//
//        PointDegree degThomas = PointDegree.adresse("6 allée du val Heillecourt");
//        PointRadians thomas = new PointRadians(degThomas);
//        System.out.println("thomas = " + degThomas);
//
//        PointDegree degHugo = PointDegree.adresse("8 c rue d'Alsace Thiaville sur Meurthe");
//        PointRadians hugo = new PointRadians(degHugo);
//        System.out.println("hugo = " + hugo);
//
//        double distance = ici.distance(thomas);
//        System.out.println("distance = " + distance);
//
//        System.out.println("distance = " + ici.distance(hugo));
//
//        List<Commune> communes = Commune.communes();
//        Collections.sort(communes);
//        Collections.reverse(communes);
//        List<Commune> communes100 = communes.stream().limit(100).toList();
//
//        System.out.println("Commune.communes() = " + communes100);
//        PointRadians paris = new PointRadians(communes100.getFirst().location());
//        PointRadians marseille = new PointRadians(communes100.get(1).location());
//        double distance1 = paris.distance(marseille);
//        System.out.println("distance1 = " + distance1);
//
//        Commune last = communes100.getLast();
//        System.out.println(last);
//        System.out.println(paris.distance(new PointRadians(last.location())));
//
//        long time = System.nanoTime();
//        List<Commune> locCommunes100 = communes.stream().limit(100).toList();
//        System.out.println(Duration.ofNanos(System.nanoTime() - time));
//        Map<Commune, List<Liaison>> tree = LiaisonCommune.grandes_liaisons(locCommunes100);
//
//        System.out.println(tree.size());

//        List<Commune> locCommunes100 = Commune.communes().stream().limit(100).toList();
//        Map<Commune, List<LiaisonCommune>> tree = Utils.grandes_liaisons(locCommunes100);
//
//        PointDegree origine = PointDegree.adresse("Thiaville sur Meurthe");
//        PointDegree destination = PointDegree.adresse("Lorient");
//
//        assert destination != null;
//
//        Commune villeArrivee = locCommunes100.stream().min(Comparator.comparingDouble(commune -> commune.distance(new PointRadians(destination)))).get();

        System.out.println(Itineraire.lePlusCourt("Thiaville sur Meurthe", "Plouay"));


    }
}
