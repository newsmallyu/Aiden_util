package com.aiden.pk.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * 懒加载单例模式 系统启动时内部类是不会加载的。但当内部类第一次被调用时，则会成功初始化实例
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
