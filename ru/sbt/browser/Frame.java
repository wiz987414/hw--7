package ru.sbt.browser;

import ru.sbt.Plugin;

public class Frame implements Plugin{
    @Override
    public void run() {
        System.out.println("/// New frame ///");
    }
}
