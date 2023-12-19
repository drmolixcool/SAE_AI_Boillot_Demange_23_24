package mnist;

import mlp.FalseValue;
import mlp.SigmoidFunction;
import mlp.TanHFunction;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Stream;

import mlp.MLP;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Mnist mnist = new Mnist("fashion");

        int[] array = Arrays.stream(args).mapToInt(Integer::parseInt).toArray();

        long start_time = System.nanoTime();
        System.out.println(mnist.start(array));
        System.out.println(Duration.ofNanos(System.nanoTime()-start_time));
    }
}
