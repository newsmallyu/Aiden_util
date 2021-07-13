package com.aiden.pk.test;

import javax.naming.spi.ObjectFactory;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

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

    public static void main(String[] args) throws Exception {
        String s = "abc";
        pika(s);
        System.out.println(s);

    }
    static  void pika(String str) throws Exception {
        Field field = str.getClass().getDeclaredField("value");
        field.setAccessible(true);
        field.set(str, new char[]{'p', 'p'});
    }


    static  void pika2(String str) throws Exception {
        str = "cdd";
    }

}
