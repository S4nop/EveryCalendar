package edu.skku.everycalendar.functions;

import java.util.concurrent.Callable;

public abstract class CallableArg<T> implements Callable<Void> {
    public T arg;

    void setArg(T arg){
        this.arg = arg;
    }

    @Override
    public abstract Void call();

}
