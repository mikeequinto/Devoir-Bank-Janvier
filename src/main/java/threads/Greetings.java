package threads;

/**
 * Created by john on 13.12.2018.
 */
public class Greetings implements Runnable {

    Counter counter;

    public Greetings(Counter counter){
        this.counter = counter;
    }

    public Greetings(){}

    public void run(){

        while(true){
            //counter.inc();
            //System.out.println(counter.getValue() + " hello");
            System.out.println(Thread.currentThread().getName() + " Hello ");
            try{
                Thread.sleep(2000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}
