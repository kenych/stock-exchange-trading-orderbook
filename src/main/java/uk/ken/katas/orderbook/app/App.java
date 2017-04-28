package uk.ken.katas.orderbook.app;

import uk.ken.katas.orderbook.domain.dto.Command;
import uk.ken.katas.orderbook.service.OrderProcessor;
import uk.ken.katas.orderbook.service.OrderProcessorImpl;

import java.util.concurrent.TimeUnit;

import static uk.ken.katas.orderbook.AppRunner.*;


public class App {

    private OrderProcessor orderProcessor;

    public App(OrderProcessorImpl orderProcessor) {
        this.orderProcessor = orderProcessor;
    }

    public void run() throws Exception {
        parseFile();
        handleCommands();
        printResult();
    }

    private void handleCommands() throws Exception {
        parsingPhaseStarted.await();

        while (isParsingInProgress()) {
            for (Command command; (command = commandsQueue.poll(1, TimeUnit.SECONDS)) != null; ) {
                handle(command);
            }
        }

        processingPhaseEnded.countDown();
    }

    private boolean isParsingInProgress() {
        return parsingPhaseEnded.getCount() != 0;
    }

    private void handle(Command command) {
        orderProcessor.handle(command);
    }

    private void parseFile() {
        orderProcessor.parse();
    }

    private final void printResult() {
        orderProcessor.printResult();
    }
}
