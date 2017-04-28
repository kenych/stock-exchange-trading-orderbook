package uk.ken.katas.orderbook;

import uk.ken.katas.orderbook.app.App;
import uk.ken.katas.orderbook.domain.Instruments;
import uk.ken.katas.orderbook.domain.dto.Command;
import uk.ken.katas.orderbook.service.OrderProcessorImpl;
import uk.ken.katas.orderbook.utils.Preconditions;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

//this normally would be some kind of dependency injection, for the sake of the test leave them as public static beans
public class AppRunner {
    //set default thread size
    public static int threadSize = 3;

    //simulate timeout, this might happen during communication with 3rd party calls etc
    public static int timeOutMs = 0;

    //parsing states
    public volatile static CountDownLatch parsingPhaseStarted = new CountDownLatch(1);
    public volatile static CountDownLatch parsingPhaseEnded = new CountDownLatch(1);

    //middle queue state
    public volatile static CountDownLatch processingPhaseEnded = new CountDownLatch(1);

    //middle queue(from parsed file to thread pool)
    public static LinkedBlockingQueue<Command> commandsQueue = new LinkedBlockingQueue<Command>();

    //actual final job is done by thread
    public volatile static CountDownLatch jobFinished;
    public static Instruments instruments = new Instruments();
    public static String fileName;

    public static void main(String[] args) throws Exception {
        Preconditions.checkArgs(args != null, "File name not provided!");
        fileName = args[0];

        if (args.length > 1) {
            threadSize = Integer.valueOf(args[1]);
            System.out.println("Job pool has " + threadSize + " JobExecutor threads");
        }
        jobFinished = new CountDownLatch(threadSize);

        if (args.length > 2) {
            timeOutMs = Integer.valueOf(args[2]);
            System.out.println("simulated timeout per order is " + timeOutMs + " timeOutMs");
        }


        new App(new OrderProcessorImpl()).run();
    }
}
