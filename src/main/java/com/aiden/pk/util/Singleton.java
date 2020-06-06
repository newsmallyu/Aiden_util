package com.aiden.pk.util;

/**
 * 懒汉式
 */
public class Singleton {
    private Singleton(){

    }
    private static class StaticSingleton {
        private static final Singleton instance = new Singleton();
    }
    public static Singleton getInstance(){
        return StaticSingleton.instance;
    }
}


