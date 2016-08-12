package ru.sbt.browser;

import ru.sbt.Plugin;

public class Window implements Plugin{
    @Override
    public void run() {
        System.out.println("-------------------");
        System.out.println("|                 |");
        System.out.println("|       test      |");
        System.out.println("|                 |");
        System.out.println("-------------------");
    }
}
