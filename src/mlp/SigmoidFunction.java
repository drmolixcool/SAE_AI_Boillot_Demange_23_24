package mlp;

public class SigmoidFunction implements TransferFunction, FalseValue {
    @Override
    public double evaluate(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    @Override
    public double evaluateDer(double resFunc) {
        return resFunc - Math.pow(resFunc, 2);
    }

    @Override
    public double getFalseValue() {
        return 0;
    }
}
