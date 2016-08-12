package ru.sbt;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

class PluginLoader extends URLClassLoader {

    PluginLoader(URL[] urls) {
        super(urls);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> c = findLoadedClass(name);
        if (c == null) {
            try {
                c = findClass(name);
            } catch (ClassNotFoundException e) {
                if (getParent() != null)
                    c = super.loadClass(name);
            }
        }
        return c;
    }

}
