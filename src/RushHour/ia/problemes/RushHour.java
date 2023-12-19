package RushHour.ia.problemes;

import RushHour.ia.framework.common.Action;
import RushHour.ia.framework.common.State;
import RushHour.ia.framework.recherche.Problem;

import java.util.ArrayList;

public class RushHour extends Problem {

    /**
     * Donne le coût d'une action pour un état donné
     * @param s L'état en question
     * @param a L'action en question
     * @return Le coût
     */
    public double getActionCost(State s, Action a){
        String[] splitAction = a.getName().split(" ");

        return Math.abs(Integer.parseInt(splitAction[2]));
    }


    /**
     * Donne toutes les actions possibles dans un état donné
     * @param s Un état
     * @return La liste des actions possibles
     */
    public ArrayList<Action> getActions(State s){

        RushHourState rhs = (RushHourState) s;
        int[][] currBoard = rhs.getBoard();
        ArrayList<Action> actions = new ArrayList<>();

        // On parcours tout les éléments du plateau
        for (int i = 0; i < currBoard.length; i++) {
            for (int j = 0; j < currBoard[i].length; j++) {

                // Si on trouve un 3 c'est qu'on a une tête et que l'on peut donc bouger horizontalement
                if (currBoard[i][j] == 3) {
                    // Placement de la tête sur le plateau
                    String headPlacement = i + " " + j;
                    int intermediateJ = 0;

                    // Si la case actuelle n'est pas sur un bord, cette partie est pour la partie à droite de la tête du véhicule
                    if (j<5) {

                        // On passe tout les 1 à gauche du j actuel, car c'est des parties du véhicule
                        int nbof1 = countNbOf1or2(currBoard, i , j, 3, 1);
                        intermediateJ = j + nbof1 + 1;
                        int nbDeplacement = 0;

                        if (intermediateJ <= 5) {
                            // On parcours tout les 0 à gauche de j intermédiaire, le véhicule pourra se déplacer sur les cases avec un 0
                            while (currBoard[i][intermediateJ] == 0 && intermediateJ < 5){
                                actions.add( new Action(headPlacement + " " + (++nbDeplacement)));
                                intermediateJ++;
                            }
                        }

                        if (intermediateJ == 5 && currBoard[i][5] == 0){
                            actions.add( new Action(headPlacement + " " + (++nbDeplacement)));
                        }
                    }

                    // Si la case actuelle n'est pas sur un bord, cette partie est pour la partie à gauche de la tête du véhicule
                    if (j>0) {

                        // On compte tout les 1 à droite du j actuel, car c'est des parties du véhicule
                        int nbof1 = countNbOf1or2(currBoard, i , j, 3, -1);
                        intermediateJ = j - nbof1 - 1;
                        int nbDeplacement = 0;

                        if (intermediateJ >= 0) {
                            // Idem que pour au dessus mais par la droite
                            while (currBoard[i][intermediateJ] == 0 && intermediateJ > 0){
                                actions.add( new Action(headPlacement + " " + (--nbDeplacement)));
                                intermediateJ--;
                            }
                        }

                        if (intermediateJ == 0 && currBoard[i][0] == 0){
                            actions.add( new Action(headPlacement + " " + (--nbDeplacement)));
                        }
                    }
                }

                // Si on trouve un 4 c'est qu'on a une tête et que l'on peut donc bouger verticalement
                else if (currBoard[i][j] == 4){
                    String headPlacement = i + " " + j;
                    int intermediateI = 0;

                    // Si la case actuelle n'est pas sur un bord, cette partie est pour la partie en bas de la tête du véhicule
                    if (i<5) {
                        int nbof2 = countNbOf1or2(currBoard, i , j, 4, 1);
                        intermediateI = i + nbof2 + 1;
                        int nbDeplacement = 0;

                        if (intermediateI <= 5) {
                            while (currBoard[intermediateI][j] == 0 && intermediateI < 5){
                                actions.add( new Action(headPlacement + " " + (++nbDeplacement)));
                                intermediateI++;
                            }
                        }

                        if (intermediateI == 5 && currBoard[5][j] == 0){
                            actions.add( new Action(headPlacement + " " + (++nbDeplacement)));
                        }
                    }

                    // Si la case actuelle n'est pas sur un bord, cette partie est pour la partie en haut de la tête du véhicule
                    if (i>0) {
                        int nbof2 = countNbOf1or2(currBoard, i , j, 4, -1);
                        intermediateI = i - nbof2 - 1;
                        int nbDeplacement = 0;

                        if (intermediateI >= 0) {
                            while (currBoard[intermediateI][j] == 0 && intermediateI > 0){
                                actions.add( new Action(headPlacement + " " + (--nbDeplacement)));
                                intermediateI--;
                            }
                        }

                        if (intermediateI == 0 && currBoard[0][j] == 0){
                            actions.add( new Action(headPlacement + " " + (--nbDeplacement)));
                        }
                    }
                }
            }
        }

        return actions;
    }

    /**
     * Fait une action sur un état donné
     * @param s Un état
     * @param a Une action
     * @return Nouvel état après l'action
     */
    public State doAction(State s, Action a){
        RushHourState rhs = (RushHourState) s.clone();
        int[][] currBoard = rhs.getBoard();
        int[][] newBoard = new int[currBoard.length][];
        for (int i = 0; i < currBoard.length; i++) {
            newBoard[i] = currBoard[i].clone();
        }

        String[] splitAction = a.getName().split(" ");

        // index x de la tête du véhicule à déplacer
        int x = Integer.parseInt(splitAction[0]);
        // index y de la tête du véhicule à déplacer
        int y = Integer.parseInt(splitAction[1]);
        // Nombre de case à déplacer
        int deplacement = Integer.parseInt(splitAction[2]);
        int nbOf1or2 = 0;

        // Si la tête est un 3, on déplace horizontalement
        if (currBoard[x][y] == 3){

                // Déplacement négatif = se déplacer à gauche
                if (deplacement < 0) {

                    // On vérifie ou sont positionné les 1, si il y a un 1 à gauche,
                    // on commence à déplacer les 1 jusqu'à la tête
                    if (currBoard[x][y-1] == 1) {

                        // On sait que les 1 sont à gauche, donc on compte le nombre de 1
                        nbOf1or2 = countNbOf1or2(currBoard, x, y, 3, -1);

                        // On déplace le véhicule, comme dit au dessus à partir des 1 à gauche de la tête
                        for (int i = nbOf1or2; i >= 0; i--) {

                            newBoard[x][y + deplacement - i] = currBoard[x][y - i];
                            newBoard[x][y - i] = 0;
                        }

                    // Si les 1 sont à droite du véhicule, on déplace d'abord la tête puis les 1
                    } else {

                        nbOf1or2 = countNbOf1or2(currBoard, x, y, 3, 1);

                        for (int i = 0; i <= nbOf1or2; i++) {

                            newBoard[x][y + deplacement + i] = currBoard[x][y + i];
                            newBoard[x][y + i] = 0;
                        }
                    }

                // Déplacement positif = se déplacer à droite
                } else {

                    // Cette fois ci, on regarde si les 1 sont à droite, puis c'est sensiblement
                    // la même chose que au dessus, le seul changement est qu'on déplace le véhicule
                    // à droite
                    if (currBoard[x][y+1] == 1) {

                        nbOf1or2 = countNbOf1or2(currBoard, x, y, 3, 1);

                        for (int i = nbOf1or2; i >= 0; i--) {

                            newBoard[x][y + deplacement + i] = currBoard[x][y + i];
                            newBoard[x][y + i] = 0;
                        }

                    } else {

                        nbOf1or2 = countNbOf1or2(currBoard, x, y, 3, -1);

                        for (int i = 0; i <= nbOf1or2; i++) {

                            newBoard[x][y + deplacement - i] = currBoard[x][y - i];
                            newBoard[x][y - i] = 0;
                        }

                    }

                }

        // Si on a un 4, c'est qu'on se déplace verticalement
        } else {

                // Si déplacement négatif, on se déplace vers le haut
                if (deplacement < 0) {

                    // On regarde si il y a des 2 au dessus de de la tête et comme pour le déplacement
                    // horizontal, soit on déplace d'abord les 2, ou alors on déplace d'abord la tête
                    if (currBoard[x-1][y] == 2) {

                        nbOf1or2 = countNbOf1or2(currBoard, x, y, 4, -1);

                        for (int i = nbOf1or2; i >= 0; i--) {

                            newBoard[x + deplacement - i][y] = currBoard[x - i][y];
                            newBoard[x - i][y] = 0;
                        }

                    } else {

                        nbOf1or2 = countNbOf1or2(currBoard, x, y, 4, 1);

                        for (int i = 0; i <= nbOf1or2; i++) {

                            newBoard[x + deplacement + i][y] = currBoard[x + i][y];
                            newBoard[x + i][y] = 0;
                        }

                    }

                // Si déplacement positif, on déplace vers le bas
                } else {

                    if (currBoard[x+1][y] == 2) {

                        nbOf1or2 = countNbOf1or2(currBoard, x, y, 4, 1);

                        for (int i = nbOf1or2; i >= 0; i--) {

                            newBoard[x + deplacement + i][y] = currBoard[x + i][y];
                            newBoard[x + i][y] = 0;
                        }

                    } else {

                        nbOf1or2 = countNbOf1or2(currBoard, x, y, 4, -1);

                        for (int i = 0; i <= nbOf1or2; i++) {

                            newBoard[x + deplacement - i][y] = currBoard[x - i][y];
                            newBoard[x - i][y] = 0;
                        }
                    }
                }
        }

        rhs.setBoard(newBoard);

        return rhs;
    }

    /**
     * Permet de compter le nombre de 1 ou de 2 dèrière la tête d'un véhicule
     * @param currBoard plateau courant
     * @param i index x de la tête
     * @param j index y de la tête
     * @param dir 2 = horizontal, 3 = vertical
     * @param sens négatif = en haut ou à gauche, positif = en bas ou à droite
     * @return Le nombre de 1 ou de 2 dèrière la tête
     */
    private int countNbOf1or2(int[][] currBoard, int i, int j, int dir, int sens){
        int res;
        int nbof1or2 = 0;

        if (dir == 3){
            if (sens == -1){
                res = j - 1;
                if (res >= 0) {
                    while (currBoard[i][res] == 1 && res-1 >= 0) {
                        res--;
                        nbof1or2++;
                    }

                    if (res == 0 && currBoard[i][res] == 1) nbof1or2++;
                }
            }
            else {
                res = j + 1;
                if (res < currBoard[i].length) {
                    while (currBoard[i][res] == 1 && res+1 < currBoard[i].length) {
                        res++;
                        nbof1or2++;
                    }

                    if (res  == 5  && currBoard[i][res] == 1) nbof1or2++;
                }
            }
        }
        else{
            if (sens == -1){
                res = i - 1;
                if (res >= 0) {
                    while (currBoard[res][j] == 2 && res-1 >= 0) {
                        res--;
                        nbof1or2++;
                    }

                    if (res == 0 && currBoard[res][j] == 2) nbof1or2++;
                }
            }
            else {
                res = i + 1;
                if (res < currBoard.length) {
                    while (currBoard[res][j] == 2 && res+1 < currBoard.length) {
                        res++;
                        nbof1or2++;
                    }

                    if (res  == 5  && currBoard[res][j] == 2) nbof1or2++;
                }
            }
        }

        return nbof1or2;
    }

    /**
     * Vérifie si un état est l'état but
     * @param s Un état à tester
     */
    @Override
    public boolean isGoalState(State s) {
        RushHourState rhs = (RushHourState) s;
        return rhs.getBoard()[2][5] == 3;
    }
}
