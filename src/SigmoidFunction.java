public class SigmoidFunction implements TransferFunction {
    @Override
    public double evaluate(double x) {
        return 1 / (1+Math.exp(-x));
    }

    @Override
    public double evaluateDer(double resFunc) {
        return resFunc - Math.pow(resFunc,2);
    }
}
