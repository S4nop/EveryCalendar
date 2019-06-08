package edu.skku.everycalendar;

import java.util.concurrent.Callable;

public abstract class CallableArg<T> implements Callable<Void> {
    T arg;

    void setArg(T arg){
        this.arg = arg;
    }

    @Override
    public abstract Void call();
}
