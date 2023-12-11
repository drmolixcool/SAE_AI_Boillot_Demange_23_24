public class TanHFunction implements TransferFunction, FalseValue {
    @Override
    public double evaluate(double x) {
        return Math.tanh(x);
    }

    @Override
    public double evaluateDer(double resFunc) {
        return 1-Math.pow(resFunc, 2);
    }

    @Override
    public double getFalseValue() {
        return -1;
    }
}
