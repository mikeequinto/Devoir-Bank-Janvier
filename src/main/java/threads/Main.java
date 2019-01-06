package threads;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by john on 13.12.2018.
 */
public class Main {

    public static void main(String[] args) {

        /*Counter counter = new Counter();

        Greetings greetings = new Greetings(counter);
        greetings.start();

        Salutation salutation = new Salutation(counter);
        salutation.start();*/

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Salutation salutation = new Salutation();
        Greetings greetings = new Greetings();
        executorService.execute(greetings);
        executorService.execute(salutation);


    }

}
