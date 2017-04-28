package uk.ken.katas.orderbook.utils;

import uk.ken.katas.orderbook.domain.View;

import java.util.List;

public class PrintUtils {
    public static void printView(List<? extends View> views) {
        for (View view : views) {
            System.out.println(view);
        }
    }
}
