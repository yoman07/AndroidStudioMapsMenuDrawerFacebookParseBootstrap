package com.zdobywacz;

/**
 * Created by yoman on 05.11.2013.
 */
public class test {
    private static test ourInstance = new test();

    public static test getInstance() {
        return ourInstance;
    }

    private test() {
    }
}
