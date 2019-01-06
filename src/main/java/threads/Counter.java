package threads;

/**
 * Created by john on 13.12.2018.
 */
public class Counter {

    int value;

    private Counter(){

    }

    synchronized public void inc(){ value++; }

    public int getValue(){ return value; }

    public void setValue(int value){ this.value = value; }

}
