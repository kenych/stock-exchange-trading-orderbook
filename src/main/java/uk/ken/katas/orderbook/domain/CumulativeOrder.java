package uk.ken.katas.orderbook.domain;

import uk.ken.katas.orderbook.domain.dto.Order;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static uk.ken.katas.orderbook.utils.Preconditions.checkState;


public class CumulativeOrder {

    private final ConcurrentMap<Long, Order> orders;

    public CumulativeOrder(Order newOrder) {
        orders = new ConcurrentHashMap<Long, Order>();
        orders.put(newOrder.getOrderId(), newOrder);
    }

    public void add(Order order) {
        Order existingOrder = orders.put(order.getOrderId(), order);
        checkState(existingOrder == null, "Order already exists:" + existingOrder);
    }

    public int size() {
        return orders.size();
    }

    public void remove(long orderId) {
        Order removedOrder = orders.remove(orderId);
        checkState(removedOrder != null, "Order doesn't exist: " + removedOrder);
    }

    public int getQuantitySum() {
        int quantitySum = 0;
        for (Long orderId : orders.keySet()) {
            quantitySum += orders.get(orderId).getQuantity();
        }

        return quantitySum;
    }

    public void updateQuantityFor(Order newOrder) {
        Order order = orders.get(newOrder.getOrderId());
        order.setQuantity(newOrder.getQuantity());
    }
}
