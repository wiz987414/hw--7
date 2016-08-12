package ru.sbt;

import java.io.*;
import java.util.Objects;

public class EncryptedClassloader extends ClassLoader {
    private final String key;
    private final File dir;

    public EncryptedClassloader(String key, File dir, ClassLoader parent) {
        super(parent);
        this.key = key;
        this.dir = dir;

    }

    protected File encryptFile(String name, File dir) throws IOException {
        File encyptedFile = null;
        if (dir.isDirectory() && name != null) {
            for (File search : dir.listFiles()) {
                if (Objects.equals(search.getName(), name)) {
                    InputStream is = null;
                    try {
                        is = new FileInputStream(search);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    long size = search.length();
                    byte[] bytes = new byte[(int) size];
                    int offset = 0;
                    int numRead = 0;
                    try {
                        while (offset < bytes.length
                                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                            offset += numRead;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (offset < bytes.length) {
                            throw new IOException("Could not completely read file " + search.getName());
                    }
                        is.close();
                }
            }
        } else {

        }
        return encyptedFile;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
