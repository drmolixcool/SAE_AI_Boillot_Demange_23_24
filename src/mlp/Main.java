package mlp;

import java.util.*;

public class Main {

    public static int getRandomIndexWhichFalse(List<Boolean> booleans) {
        Random random = new Random();
        ArrayList<Integer> ints = new ArrayList<>();
        for (int i = 0; i < booleans.size(); i++) {
            if (!booleans.get(i)) {
                ints.add(i);
            }
        }
        int i = random.nextInt(ints.size());
        return ints.get(i);
    }

    public static double[][] transformInput(double[][] array, FalseValue transferFunction) {
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[0].length; y++) {
                if (array[x][y] == 0 || array[x][y] == -1) {
                    array[x][y] = transferFunction.getFalseValue();
                }
            }
        }
        return array;
    }

    public static void main(String[] args) throws Exception {
        int[] layers = {2, 10, 1};
        double learningRate = 0.3;
        if (args.length > 1)
            learningRate = Double.parseDouble(args[0]);
        String transferFunctionName = "";
        if (args.length > 2)
            transferFunctionName = args[1];
        TransferFunction tf = null;
        if (Objects.equals(transferFunctionName, "sigmoid"))
            tf = new SigmoidFunction();
//        else if (Objects.equals(transferFunctionName, "tanh")){
//            tf = new TanHFunction();
//        }
        else
            tf = new TanHFunction();
//        else throw new Exception("Mauvais argument 2 : doit être sigmoid ou tanh");

        MLP mlp = new MLP(layers, learningRate, tf);

        double[][] and = {{0, 0, 0}, {0, 1, 0}, {1, 0, 0}, {1, 1, 1}};
        double[][] or = {{0, 0, 0}, {0, 1, 1}, {1, 0, 1}, {1, 1, 1}};
        double[][] xor = {{0, 0, 0}, {0, 1, 1}, {1, 0, 1}, {1, 1, 0}};
        List<Boolean> appris = new ArrayList<>(List.of(false, false, false, false));
        double[][] tablePrise = transformInput(xor, (FalseValue) tf);

        int nbIterations = 5000;
        for (int i = 1; i <= nbIterations; i++) {
            System.out.println("Itération : " + i);

            int randomIndex = getRandomIndexWhichFalse(appris);

            double[] randomEntry = tablePrise[randomIndex];

            double error = mlp.backPropagate(new double[]{randomEntry[0], randomEntry[1]}, new double[]{randomEntry[2]});
            if (error < 0.05) appris.set(randomIndex, true);
            System.out.println(error);

            if (!appris.contains(false)) break;

        }

        System.out.println("***************************");

        for (double[] sousTable : tablePrise) {
            double[] res = mlp.execute(new double[]{sousTable[0], sousTable[1]});
            System.out.println(Arrays.toString(res));
        }

        // XOR marche avec tanh et pas avec sigmoide
    }
}
