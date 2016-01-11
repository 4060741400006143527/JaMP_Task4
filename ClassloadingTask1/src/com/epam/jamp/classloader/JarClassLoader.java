package com.epam.jamp.classloader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.log4j.Logger;

public class JarClassLoader extends ClassLoader {

    public final static Logger LOGGER = Logger.getLogger(JarClassLoader.class);

    private final Map<String, Class<?>> cache = new HashMap<>();
    private final String jarFileName;
    private final String packageName;

    public JarClassLoader(String jarFileName, String packageName) {
        this.jarFileName = jarFileName;
        this.packageName = packageName;
        cacheClasses();
    }

    private void cacheClasses() {
        try {
            JarFile jarFile = new JarFile(jarFileName);
            Enumeration entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) entries.nextElement();
                if (validate(normalize(jarEntry.getName()), packageName)) {
                    byte[] classData = loadClassData(jarFile, jarEntry);
                    if (classData != null) {
                        Class<?> clazz = defineClass(stripClassName(normalize(jarEntry.getName())), classData, 0, classData.length);
                        cache.put(clazz.getName(), clazz);
                        LOGGER.info("Class " + clazz.getName() + " loaded in cache");
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Warning : No jar file found. Packet unmarshalling won't be possible. Please verify your classpath", e);
        }
    }

    @Override
    public synchronized Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> result = cache.get(name);

        if (result == null) {
            result = cache.get(packageName + "." + name);
        }

        if (result == null) {
            result = super.findSystemClass(name);
        }
        
        LOGGER.info("LoadClass(" + name + ")");
        return result;
    }

    private String stripClassName(String className) {
        return className.substring(0, className.length() - 6);
    }

    private String normalize(String className) {
        return className.replace('/', '.');
    }

    private boolean validate(String className, String packageName) {
        return className.startsWith(packageName) && className.endsWith(".class");
    }

    private byte[] loadClassData(JarFile jarFile, JarEntry jarEntry) throws IOException {
        long size = jarEntry.getSize();
        if (size == -1 || size == 0) {
            return null;
        }
        byte[] data = new byte[(int) size];
        InputStream inputStream = jarFile.getInputStream(jarEntry);
        inputStream.read(data);
        return data;
    }
}
