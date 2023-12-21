package gps;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Itineraire {
    private Itineraire() {

    }

    public static List<Commune> lePlusCourt(String origine, String destination) throws IOException, InterruptedException {
        return lePlusCourt(PointDegree.adresse(origine), PointDegree.adresse(destination));
    }

    public static List<Commune> lePlusCourt(PointDegree origine, PointDegree destination) throws IOException, InterruptedException {
        // utiliser A star pour trouver le chemin le plus court entre origine et destination
        // l'origine et la destination sont des points de coordonnées GPS représentés par la classe PointDegree
        // et les points intermédiaires sont des communes représentées par la classe Commune
        // les liaisons peuvent être entre un point et une commune ou entre deux communes
        // utiliser la distance entre origine et destination comme heuristique

        // on récupère les 100 plus grandes villes en population de France
        List<Commune> locCommunes100 = Commune.communes().stream().sorted(Comparator.reverseOrder()).limit(100).toList();

//        // DEBUG
//        ArrayList<Commune> communes = new ArrayList<>(locCommunes100);
//        communes.sort(Comparator.reverseOrder());
//        for (Commune commune : communes) {
//            System.out.println(commune);
//        }

        // on récupère les liaisons qui relient tous les villes qui servent de points de passage
        Map<Commune, List<LiaisonCommune>> tree = Utils.grandes_liaisons(locCommunes100);

        // la ville d'origine est la commune la plus proche de l'origine
        Commune villeDepart = locCommunes100.stream().min(Comparator.comparingDouble(commune -> commune.distance(new PointRadians(origine)))).get();

        // la ville d'arrivée est la commune la plus proche de la destination
        Commune villeArrivee = locCommunes100.stream().min(Comparator.comparingDouble(commune -> commune.distance(new PointRadians(destination)))).get();

        // la distance à vol d'oiseau entre l'origine et la destination
//        double volOiseau = villeArrivee.distance(new PointRadians(origine));

        // utiliser l'algorithme A star pour trouver le chemin le plus court entre origine et destination
        // l'origine et la destination sont des points de coordonnées GPS représentés par la classe PointDegree

        // Utiliser A* pour trouver le chemin
        List<LiaisonCommune> chemin = aStar(villeDepart, villeArrivee, tree);

        // on retourne la liste des communes qui servent de points de passage
        return chemin.stream().map(LiaisonCommune::getDestination).toList();
    }

    /**
     * @param depart the start city
     * @param arrivee the end city
     * @param liaisons the map of all the cities and their links to other cities
     * example : with a start city Lille and an end city Nice, we can have :
     *                 Lille -> Paris -> Lyon -> Marseille -> Nice
     * algorythm A star : there are Commune records and LiaisonCommune class
     * liaisons_communes is a map of all the cities and their links to other all cities
     * LiaisonCommune is a class that represent a link between two cities with origin city, destination city and distance between them
     * liaisons is a map of all the cities and their links to other all cities with distance
     *                 Lille -> [Lille -> Paris, Lille -> Lyon, Lille -> Marseille, Lille -> Nice, Lille -> Bordeaux, Lille -> Colmar, Lille -> Nancy, Lille -> Quimper, Lille -> Brest]
     * the problem is that program give me always the same path : only the start city and the end city
     *                 Lille -> Nice
     * Because the program don't find the shortest path between the start city and the end city
     *                 Lille -> Paris -> Lyon -> Marseille -> Nice
     * @return the shortest path between the start city and the end city
     */
    private static List<LiaisonCommune> aStar(Commune depart, Commune arrivee, Map<Commune, List<LiaisonCommune>> liaisons) {
        Set<Commune> openSet = new HashSet<>();
        Set<Commune> closedSet = new HashSet<>();
        Map<Commune, Commune> cameFrom = new HashMap<>();
        Map<Commune, Double> gScore = new HashMap<>();
        Map<Commune, Double> hScore = new HashMap<>();

        // Initialisation des coûts pour le nœud de départ
        gScore.put(depart, 0.0);
        hScore.put(depart, depart.distance(arrivee));
        openSet.add(depart);

        while (!openSet.isEmpty()) {
            Commune current = openSet.stream()
                    .min(Comparator.comparingDouble(node -> gScore.getOrDefault(node, Double.MAX_VALUE) + hScore.getOrDefault(node, Double.MAX_VALUE)))
                    .orElseThrow();

            if (current.equals(arrivee)) {
                return reconstructPath(cameFrom, liaisons, depart,arrivee);
            }

            openSet.remove(current);
            closedSet.add(current);

            for (LiaisonCommune liaison : liaisons.getOrDefault(current, Collections.emptyList())) {
                Commune neighbor = liaison.getDestination();

                if (closedSet.contains(neighbor)) {
                    continue;
                }

                double tentativeGScore = gScore.getOrDefault(current, Double.MAX_VALUE) + liaison.distance();

                if (!openSet.contains(neighbor) || tentativeGScore < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    hScore.put(neighbor, neighbor.distance(arrivee));

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        // Aucun chemin trouvé
        return Collections.emptyList();
    }

    private static List<LiaisonCommune> reconstructPath(Map<Commune, Commune> cameFrom, Map<Commune, List<LiaisonCommune>> liaisons, Commune depart, Commune arrivee) {
        List<LiaisonCommune> totalPath = new ArrayList<>();
        Commune current = arrivee;

        while (cameFrom.containsKey(current)) {
            Commune previous = cameFrom.get(current);

            // Ajouter les liaisons du chemin, sauf la liaison directe entre la ville de départ et d'arrivée
            List<LiaisonCommune> liaisonsToAdd = liaisons.get(previous).stream()
                    .filter(liaison -> !liaison.getDestination().equals(arrivee))
                    .collect(Collectors.toList());

            totalPath.addAll(liaisonsToAdd);
            current = previous;
        }

        Collections.reverse(totalPath);
        return totalPath;
    }
}
