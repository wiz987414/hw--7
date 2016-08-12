package ru.sbt;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginManager {
    private final String pluginRootDirectory;

    private PluginManager(String pluginRootDirectory) {
        this.pluginRootDirectory = pluginRootDirectory;
    }

    private Plugin load(String pluginName, String pluginClassName) {
        Plugin pl;
        File file = new File(this.pluginRootDirectory);
        if (!file.isDirectory()) {
            throw new IllegalArgumentException();
        }
        if (pluginName != null) {
            file = new File(this.pluginRootDirectory + pluginName + "/");
        } else
            file = new File(this.pluginRootDirectory + "/");
        try {
            URL classUrl = file.toURI().toURL();
            URL[] urls = new URL[]{classUrl};
            URLClassLoader usersPluginLoader = new PluginLoader(urls);
            pl = (Plugin) usersPluginLoader.loadClass(pluginClassName).newInstance();
        } catch (ClassNotFoundException e) {
            //throw new RuntimeException("Unable to load class");
            pl = null;
        } catch (InstantiationException e) {
            throw new RuntimeException("InstantiationException", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("IllegalAccessException", e);
        } catch (MalformedURLException e) {
            throw new RuntimeException("MalformedURLException", e);
        }
        return pl;
    }

    public static void main(String[] args) {
        PluginManager pluginManager = new PluginManager("C://Users/usersPlugins/");
        Plugin plugin1 = pluginManager.load("advancedGraphics", "ru.sbt.browser.Graphics");
        Plugin plugin2 = pluginManager.load("testGraphics", "ru.sbt.browser.Window");
        Plugin plugin3 = pluginManager.load(null, "ru.sbt.browser.Frame");
        plugin1.run();
        plugin2.run();
        plugin3.run();
        plugin1 = plugin2;
        plugin1.run();
        plugin1 = pluginManager.load(null, "ru.sbt.browser.Window");
        plugin1.run();
    }
}