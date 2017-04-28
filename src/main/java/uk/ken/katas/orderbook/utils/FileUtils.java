package uk.ken.katas.orderbook.utils;

import java.io.File;
import java.net.URL;

public class FileUtils {
    public static String pathFor(String fileName) {
        URL url = FileUtils.class.getClassLoader().getResource(fileName);
        if (url == null) {
            throw new AssertionError("Resource not found! :" + fileName);
        }
        return new File(url.getFile()).getAbsolutePath();
    }
}
