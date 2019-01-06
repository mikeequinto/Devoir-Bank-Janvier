package threads;

/**
 * Created by john on 13.12.2018.
 */
public class Salutation extends Thread {

    Counter counter;

    public Salutation(Counter counter){
        this.counter = counter;
    }

    public Salutation(){}

    public void run(){

        while (true) {
            counter.inc();
            System.out.println(counter.getValue() + " salut");
            //System.out.println(Thread.currentThread().getName() + " Salut ");
            try{
                Thread.sleep(2000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}
