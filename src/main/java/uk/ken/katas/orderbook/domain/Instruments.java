package uk.ken.katas.orderbook.domain;

import uk.ken.katas.orderbook.domain.dto.Command;
import uk.ken.katas.orderbook.domain.dto.Order;
import uk.ken.katas.orderbook.domain.dto.OrderUpdate;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static uk.ken.katas.orderbook.AppRunner.timeOutMs;
import static uk.ken.katas.orderbook.utils.Preconditions.checkState;

public class Instruments {
    private ConcurrentMap<String, Instrument> instrumentsMap;
    private ConcurrentMap<Long, Order> orders;

    public Instruments() {
        instrumentsMap = new ConcurrentHashMap<String, Instrument>();
        orders = new ConcurrentHashMap<Long, Order>();
    }

    public ConcurrentMap<Long, Order> getOrders() {
        return orders;
    }

    public ConcurrentMap<String, Instrument> getInstrumentsMap() {
        return instrumentsMap;
    }

    public void handle(Command command) {
        OrderUpdate orderUpdate = getOrderUpdate(command);

        //simulate timeout, this might happen during communication with 3rd party calls etc
        try {
            Thread.sleep(timeOutMs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //end

        Instrument existingInstrument = instrumentsMap.get(orderUpdate.getNewOrder().getSymbol());

        if (existingInstrument == null) {
            existingInstrument = instrumentsMap.putIfAbsent(command.getOrder().getSymbol(), new Instrument(orderUpdate));
        }

        if (existingInstrument != null) {
            existingInstrument.handle(orderUpdate);
        }
    }

    private OrderUpdate getOrderUpdate(Command command) {
        Order newOrder = command.getOrder();

        OrderUpdate orderUpdate;
        boolean priceChanged = false;

        if (command.getAction() == Action.ADD) {
            Order existingOrder = orders.put(newOrder.getOrderId(), newOrder);
            checkState(existingOrder == null, "Order already exists:" + existingOrder);

            orderUpdate = new OrderUpdate(null, newOrder, command.getAction(), priceChanged);

        } else if (command.getAction() == Action.EDIT) {
            Order oldOrder = orders.get(newOrder.getOrderId());
            checkState(oldOrder != null, "non existing order can not be edited");

            enhanceOrder(newOrder, oldOrder);

            if (oldOrder.getPrice() != newOrder.getPrice()) {
                priceChanged = true;
            }

            orderUpdate = new OrderUpdate(oldOrder, newOrder, command.getAction(), priceChanged);
            orders.put(newOrder.getOrderId(), newOrder);
        } else {//command.action == Action.REMOVE
            Order oldOrder = orders.get(newOrder.getOrderId());
            checkState(oldOrder != null, "non existing order can not be deleted");

            enhanceOrder(newOrder, oldOrder);

            orderUpdate = new OrderUpdate(oldOrder, newOrder, command.getAction(), priceChanged);
            orders.remove(newOrder.getOrderId());
        }

        return orderUpdate;
    }

    //edit/remove doesn't have some fields, set it from old order
    private void enhanceOrder(Order newOrder, Order oldOrder) {
        newOrder.setSymbol(oldOrder.getSymbol());
        newOrder.setBuy(oldOrder.isBuy());
    }
}
