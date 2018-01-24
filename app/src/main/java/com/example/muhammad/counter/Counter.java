package com.example.muhammad.counter;

/**
 * Created by muhammad on 24/01/2018.
 */

public class Counter {
    private int counter = 0;


    public int getCounter() {
        return counter;
    }

    public String getCounterStr() {
        return String.valueOf(counter);
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void adjustCounter(int counter){
        this.counter += counter;
    }
}
