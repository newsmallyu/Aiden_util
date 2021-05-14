package com.aiden.pk.test;

/**
 * 懒加载单例模式
 * @author ay05
 */
public class Singleton {
    static class SingletonHolder{
        static Singleton instance = new Singleton();
    }
    public static Singleton getSingleton(){
        return SingletonHolder.instance;
    }
}
