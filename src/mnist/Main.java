package mnist;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        Mnist mnist = new Mnist("fashion");

        int[] array = Arrays.stream(args).mapToInt(Integer::parseInt).toArray();

        long start_time = System.nanoTime();
        System.out.println(mnist.start(array));
        System.out.println(Duration.ofNanos(System.nanoTime() - start_time));
    }
}
