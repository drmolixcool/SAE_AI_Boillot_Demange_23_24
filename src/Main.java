import java.util.Arrays;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws Exception {
        int[] layers = {2,2,1};
        double learningRate = Double.parseDouble(args[0]);
        String transferFunctionName = args[1];
        TransferFunction tf = null;
        if (Objects.equals(transferFunctionName, "sigmoid")) tf = new SigmoidFunction();
        else if (Objects.equals(transferFunctionName, "tanh")) tf = new TanHFunction();
        else throw new Exception("Mauvais argument 2 : doit être sigmoide ou tanh");

        MLP mlp = new MLP(layers, learningRate, tf);

        double[][] and = {{0,0,0}, {0,1,0}, {1,0,0}, {1,1,1}};
        double[][] or = {{0,0,0}, {0,1,1}, {1,0,1}, {1,1,1}};
        double[][] xor = {{0,0,0}, {0,1,1}, {1,0,1}, {1,1,0}};

        int nbIterations = 1000;
        for (int i=1; i<=nbIterations;i++){
            System.out.println("Itération : " + i);
            for (double[] sousTable : xor) {
                double error = mlp.backPropagate(new double[]{sousTable[0], sousTable[1]}, new double[]{sousTable[2]});
                System.out.println(error);
            }
        }

        System.out.println("***************************");

        for (double[] sousTable : xor){
            double[] res = mlp.execute(new double[]{sousTable[0], sousTable[1]});
            System.out.println(Arrays.toString(res));
        }

        // XOR marche avec tanh et pas avec sigmoide
    }
}
