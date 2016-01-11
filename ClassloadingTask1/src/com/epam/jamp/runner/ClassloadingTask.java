package com.epam.jamp.runner;

import com.epam.jamp.demo.api.Example;
import com.epam.jamp.classloader.JarClassLoader;
import java.io.IOException;
import org.apache.log4j.Logger;

public class ClassloadingTask {

    public final static Logger LOGGER = Logger.getLogger(ClassloadingTask.class);

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

        JarClassLoader jarClassLoader = new JarClassLoader("D:/JarExample.jar", "com.epam.jamp.demo.impl");
        Class<?> clazz = Class.forName("com.epam.jamp.demo.impl.JarExample", true, jarClassLoader);
//        Class<?> clazz = jarClassLoader.loadClass("JarExample");
        Example sample = (Example) clazz.newInstance();
        sample.demo("First string");

        jarClassLoader = new JarClassLoader("D:/JarExample1.jar", "com.epam.jamp.demo.impl");
        clazz = Class.forName("com.epam.jamp.demo.impl.JarExample", true, jarClassLoader);
//        clazz = jarClassLoader.loadClass("JarExample");
        sample = (Example) clazz.newInstance();
        sample.demo("Second string");
    }

}
