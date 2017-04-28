package uk.ken.katas.orderbook.utils;

public class Preconditions {
    public static void checkState(boolean condition, String message) {
        if (!condition) throw new IllegalStateException(message);
    }

    public static void checkArgs(boolean condition, String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }
}
