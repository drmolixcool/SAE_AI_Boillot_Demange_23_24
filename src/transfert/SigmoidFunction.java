package transfert;

public class SigmoidFunction implements TransfertFunction {
    @Override
    public double calculate(double x) {
        return 1 / (1+Math.exp(-x));
    }

    @Override
    public double derivate(double resFunc) {
        return resFunc - Math.pow(resFunc,2);
    }
}
