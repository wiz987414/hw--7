package ru.sbt;

import java.io.*;
import java.nio.file.*;
import java.util.Objects;

public class EncryptedClassloader extends ClassLoader {
    private final String key;
    private final File dir;

    public EncryptedClassloader(String key, File dir, ClassLoader parent) {
        super(parent);
        this.key = key;
        this.dir = dir;
    }

    protected void encryptFile(String name) {
        if (dir.isDirectory()) {
            if (dir.listFiles() != null) {
                for (File searchFile : dir.listFiles()) {
                    if (Objects.equals(searchFile.getName(), name)) {
                        try {
                            Path path = Paths.get(this.dir + "/" + name);
                            byte[] fileBytes = Files.readAllBytes(path);
                            Files.delete(path);
                            byte[] byteKey = this.key.getBytes();
                            for (int i = 0; i < fileBytes.length; i++) {
                                fileBytes[i] = (byte) (fileBytes[i] + byteKey[0]);
                            }
                            Files.createFile(path);
                            Files.write(path, fileBytes, StandardOpenOption.WRITE);
                        } catch (IOException e) {
                            throw new RuntimeException("Exception with file operating", e);
                        }
                    }
                }
            } else throw new RuntimeException("Empty directory", new IllegalArgumentException());
        } else throw new RuntimeException("Directory non exists", new IllegalArgumentException());
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> decryptedClass = null;
        if (dir.isDirectory()) {
            if (dir.listFiles() != null) {
                for (File searchFile : dir.listFiles()) {
                    if (Objects.equals(searchFile.getName(), name + ".class")) {
                        try {
                            Path path = Paths.get(this.dir + "/" + name + ".class");
                            byte[] fileBytes = Files.readAllBytes(path);
                            byte[] byteKey = this.key.getBytes();
                            for (int i = 0; i < fileBytes.length; i++) {
                                fileBytes[i] = (byte) (fileBytes[i] - byteKey[0]);
                            }
                            decryptedClass = defineClass("ru.sbt.browser." + name, fileBytes, 0, fileBytes.length);
                        } catch (IOException e) {
                            throw new RuntimeException("Exception with file operating", e);
                        }
                    }
                }
            } else throw new RuntimeException("Empty directory", new IllegalArgumentException());
        } else throw new RuntimeException("Directory non exists", new IllegalArgumentException());
        return decryptedClass;
    }
}
