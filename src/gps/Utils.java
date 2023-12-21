package gps;

import gps.liaison.Autoroute;
import gps.liaison.VoieRapide;

import java.util.*;

public class Utils {

    private Utils() {

    }

    public static Map<Commune, List<LiaisonCommune>> grandes_liaisons(List<Commune> communes) {
        Map<Commune, List<LiaisonCommune>> liaisons_communes = new HashMap<>();

        communes = new ArrayList<>(communes);
        communes.sort(Comparator.reverseOrder());
        ArrayList<Commune> communes50first = new ArrayList<>(communes.subList(0, 50));
        ArrayList<Commune> communes50to100 = new ArrayList<>(communes.subList(50, 100));

        for (Commune current : communes50first) {
            ArrayList<LiaisonCommune> liaisons = new ArrayList<>();
            for (Commune commune : communes50first) {
                if (current.equals(commune)) {
                    continue;
                }

                liaisons.add(new LiaisonCommune(current, commune, new Autoroute()));
            }

            for (Commune commune : communes50to100) {
                if (current.equals(commune)) {
                    continue;
                }

                liaisons.add(new LiaisonCommune(current, commune, new VoieRapide()));
            }

            liaisons_communes.put(current, liaisons);
        }

        for (Commune current : communes50to100) {
            List<LiaisonCommune> liaisons = new ArrayList<>();

            ArrayList<Commune> communes100first = new ArrayList<>(communes50first);
            communes100first.addAll(communes50to100);

            for (Commune commune : communes100first) {
                if (current.equals(commune)) {
                    continue;
                }

                liaisons.add(new LiaisonCommune(current, commune, new VoieRapide()));
            }

            liaisons_communes.put(current, liaisons);
        }
        return liaisons_communes;
    }
}
