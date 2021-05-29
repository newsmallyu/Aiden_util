package com.aiden.pk.test;

import java.nio.charset.StandardCharsets;
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

    public static void main(String[] args) {
        //java 默认使用Unicode，utf-8英文字符占一个字节，汉字字符大多占3-4个字节
        String s = "s";
        System.out.println(s.getBytes(StandardCharsets.UTF_8).length);
    }


}
