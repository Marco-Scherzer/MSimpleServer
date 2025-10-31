package com.marcoscherzer.msimpleserver.util.fileloader;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MAndroidFileLoader implements MMultiPlatformFileLoader {

    private Context androidContext;
    private String androidPathWithinAssetsPath;
    private String androidStoragePath;
    private String path;

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MAndroidFileLoader initForAndroidAssetsPath(Context contextIfAndroid, String relativePathWithinAssetsDir) {
        androidContext = contextIfAndroid.getApplicationContext();
        androidStoragePath = null;

        androidPathWithinAssetsPath = relativePathWithinAssetsDir.trim();
        if (relativePathWithinAssetsDir.charAt(0) == '/')
            androidPathWithinAssetsPath.replaceFirst("/", "");
        if (!relativePathWithinAssetsDir.endsWith("/")) androidPathWithinAssetsPath += "/";

        path = androidPathWithinAssetsPath;
        System.out.println("androidAssetsPath " + path);
        return this;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MAndroidFileLoader initForAndroidStoragePath(String relativePathWithinUser0Storage) {
        androidContext = null;
        androidPathWithinAssetsPath = null;

        relativePathWithinUser0Storage = relativePathWithinUser0Storage.trim();
        if (relativePathWithinUser0Storage.charAt(0) == '/')
            relativePathWithinUser0Storage.replaceFirst("/", "");
        androidStoragePath = "/storage/emulated/0/" + relativePathWithinUser0Storage;
        if (!androidStoragePath.endsWith("/")) androidStoragePath += "/";

        path = androidStoragePath;
        System.out.println("androidStoragePath " + path);
        return this;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public String getBaseBath() {
        return path;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public byte[] loadByteArray(Path relativePath) throws IOException {
        return loadByteArray(relativePath.toString());
    }

    @Override
    public byte[] loadByteArray(String relativePath) throws IOException {
        if (androidContext != null && androidPathWithinAssetsPath != null) {
            InputStream is = androidContext.getAssets().open(androidPathWithinAssetsPath + relativePath);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[4096];
            int n;
            while ((n = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, n);
            }
            return buffer.toByteArray();
        } else {
            return java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(path, relativePath));
        }
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public InputStream loadByteInputStream(String relativePath) throws IOException {
        if (androidContext != null && androidPathWithinAssetsPath != null) {
            System.out.println("loadByteInputStream " + androidPathWithinAssetsPath + relativePath);
            return androidContext.getAssets().open(androidPathWithinAssetsPath + relativePath);
        } else {
            return java.nio.file.Files.newInputStream(java.nio.file.Paths.get(path, relativePath));
        }
    }

}
