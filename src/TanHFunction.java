public class TanHFunction implements TransferFunction {
    @Override
    public double evaluate(double x) {
        return Math.tanh(x);
    }

    @Override
    public double evaluateDer(double resFunc) {
        return 1-Math.pow(resFunc, 2);
    }
}
