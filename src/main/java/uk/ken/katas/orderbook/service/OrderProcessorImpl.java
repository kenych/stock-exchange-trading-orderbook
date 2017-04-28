package uk.ken.katas.orderbook.service;

import uk.ken.katas.orderbook.domain.Instrument;
import uk.ken.katas.orderbook.domain.dto.Command;
import uk.ken.katas.orderbook.domain.dto.Order;
import uk.ken.katas.orderbook.utils.PrintUtils;

import java.util.concurrent.ConcurrentMap;

import static uk.ken.katas.orderbook.AppRunner.instruments;
import static uk.ken.katas.orderbook.AppRunner.jobFinished;

public class OrderProcessorImpl implements OrderProcessor {

    private final XmlParser parser = new XmlParser();
    private final JobPool pool = new JobPool();

    @Override
    public void parse() {
        parser.start();
    }

    @Override
    public void printResult() {
        try {
            System.out.println("Printer is waiting for jobs to finish...");
            jobFinished.await();

            printAllOrders();

            for (Instrument instrument : instruments.getInstrumentsMap().values()) {
                System.out.println("\n==== instrument: " + instrument.getName() + " =====\n");
                printByOrders(instrument);
                printByLevels(instrument);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void printByLevels(Instrument instrument) {
        System.out.println("\nbyLevels asks");
        PrintUtils.printView(instrument.getByLevels().getAsksView());
        System.out.println("\nbyLevels bids");
        PrintUtils.printView(instrument.getByLevels().getBidsView());
    }

    private void printByOrders(Instrument instrument) {
        System.out.println("byOrder asks");
        PrintUtils.printView(instrument.getByOrders().getAsksView());
        System.out.println("\nbyOrder bids");
        PrintUtils.printView(instrument.getByOrders().getBidsView());
    }

    private void printAllOrders() {
        System.out.println("\norders\n");
        ConcurrentMap<Long, Order> orders = instruments.getOrders();
        for (Long orderId : orders.keySet()) {
            System.out.println(orders.get(orderId));
        }
    }

    @Override
    public void handle(Command command) {
        long orderId = command.getOrder().getOrderId();
        JobExecutor jobExecutor = pool.getJobExecutor(orderId);
        jobExecutor.addJob(command);
    }

}
