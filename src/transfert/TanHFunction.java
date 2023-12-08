package transfert;

public class TanHFunction implements TransfertFunction {
    @Override
    public double calculate(double x) {
        return Math.tanh(x);
    }

    @Override
    public double derivate(double resFunc) {
        return 1-Math.pow(resFunc, 2);
    }
}
