package com.aiden.pk.util;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author ay05
 * 获取内部插件
 */
@Component
public class InitInnerPlugin {
    private static Logger log = LoggerFactory.getLogger(InitInnerPlugin.class);
    public static Map<String, String> innerServiceMap = new HashMap();

    public InitInnerPlugin()  {
        //todo 传入需要的接口类
        //initInnerService(IService.class);
    }
    /**
     * 获取某个接口的所有实现类
     */
    public Map initInnerService(Class c){
        //判断是不是接口,不是接口不作处理
        if(c.isInterface()){
            //获得当前包名
            String packageName = c.getPackage().getName();
            try {
                //获得当前包以及子包下的所有类
                List<Class> allClass = getClasses(packageName);
                for (int i = 0; i < allClass.size(); i++) {
                    //判断当前类是否为c的子类 ,放入map
                    if (c.isAssignableFrom(allClass.get(i)) && !c.equals(allClass.get(i))) {
                        String classPath = allClass.get(i).getName();
                        String className = classPath.substring(classPath.lastIndexOf('.') + 1);

                        innerServiceMap.put(className, classPath);
                    }
                }
            } catch (Exception e) {
                log.error(e.toString());
            }
        }
        return innerServiceMap;
    }

    private static List<Class> getClasses(String packageName) throws ClassNotFoundException,IOException{
        ArrayList<Class> classes = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace(".", "/");
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while(resources.hasMoreElements()){
            URL resource = resources.nextElement();
            String protocol = resource.getProtocol();
            if ("jar".equalsIgnoreCase(protocol)){
                JarURLConnection connection = (JarURLConnection) resource.openConnection();
                if (connection != null) {
                    JarFile jarFile = connection.getJarFile();
                    if (jarFile != null) {
                        //得到该jar文件下面的类实体
                        Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                        while (jarEntryEnumeration.hasMoreElements()) {
                            JarEntry entry = jarEntryEnumeration.nextElement();
                            String jarEntryName = entry.getName();
                            //这里我们需要过滤不是class文件
                            if (jarEntryName.contains(".class") && jarEntryName.replaceAll("/", ".").startsWith(packageName)) {
                                String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace("/", ".");
                                Class cls = Class.forName(className);
                                classes.add(cls);
                            }
                        }
                    }
                }
            }else {
                String newPath = resource.getFile().replace("%20", " ");
                dirs.add(new File(newPath));
            }
        }

        for(File directory:dirs){
            classes.addAll(findClass(directory, packageName));
        }
        return classes;
    }

    private static  List<Class> findClass(File directory, String packageName)
            throws ClassNotFoundException{
        List<Class> classes = new ArrayList<>();
        if(!directory.exists()){
            return classes;
        }
        File[] files = directory.listFiles();
        for(File file:files){
            if(file.isDirectory()){
                assert !file.getName().contains(".");
                classes.addAll(findClass(file, packageName+"."+file.getName()));
            }else if(file.getName().endsWith(".class")){
                classes.add(Class.forName(packageName+"."+file.getName().substring(0,file.getName().length()-6)));
            }
        }
        return classes;
    }


}