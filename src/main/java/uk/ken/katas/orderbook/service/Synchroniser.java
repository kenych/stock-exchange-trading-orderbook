package uk.ken.katas.orderbook.service;

import uk.ken.katas.orderbook.AppRunner;

public class Synchroniser {

    //call this to stop all awaiting or progress checking threads. alternative would be to interrupt them
    public static void allDone() {
        AppRunner.parsingPhaseEnded.countDown();
        AppRunner.processingPhaseEnded.countDown();
    }
}
