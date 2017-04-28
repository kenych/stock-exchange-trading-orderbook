package uk.ken.katas.orderbook.service;

import uk.ken.katas.orderbook.domain.dto.Command;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static uk.ken.katas.orderbook.AppRunner.*;

/**
 * Orders by same id should be executed by same job executor thread to guarantee sequential processing
 * by same order id but concurrent processing by different id.
 */
public class JobExecutor extends Thread {

    private final BlockingQueue<Command> commandsPerThread = new LinkedBlockingQueue<Command>();

    public void addJob(Command command) {
        commandsPerThread.add(command);
    }

    @Override
    public void run() {
        System.out.println("started job executor with thread id:" + Thread.currentThread().getId());

        waitForParser();

        while (isProcessingInProgress()) {
            try {
                for (Command command; (command = commandsPerThread.poll(1, TimeUnit.SECONDS)) != null; ) {
                    instruments.handle(command);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("no jobs in the queue, checking job status");
        }

        System.out.println("finished thread " + Thread.currentThread().getId());
        jobFinished.countDown();
    }

    private void waitForParser() {
        try {
            parsingPhaseStarted.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isProcessingInProgress() {
        return processingPhaseEnded.getCount() != 0;
    }


}
