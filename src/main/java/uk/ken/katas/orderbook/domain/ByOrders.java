package uk.ken.katas.orderbook.domain;

import uk.ken.katas.orderbook.domain.dto.ByOrderView;
import uk.ken.katas.orderbook.domain.dto.Order;
import uk.ken.katas.orderbook.domain.dto.OrderUpdate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

import static uk.ken.katas.orderbook.utils.Preconditions.checkState;


public class ByOrders {
    private final SortedSet<Order> asks;
    private final SortedSet<Order> bids;


    public ByOrders(OrderUpdate orderUpdate) {
        asks = new ConcurrentSkipListSet(asc);
        bids = new ConcurrentSkipListSet(des);

        handle(orderUpdate);
    }

    public List<? extends View> getAsksView() {
        return getByOrdersFor(asks);
    }

    public List<? extends View> getBidsView() {
        return getByOrdersFor(bids);
    }

    private List<ByOrderView> getByOrdersFor(SortedSet<Order> byOrdersMap) {

        List<ByOrderView> byOrderView;

        byOrderView = new ArrayList<ByOrderView>(byOrdersMap.size());
        for (Order order : byOrdersMap) {
            byOrderView.add(new ByOrderView(order.getOrderId(), order.getQuantity(), order.getPrice()));
        }

        return byOrderView;
    }

    public void handle(OrderUpdate orderUpdate) {

        Order order = orderUpdate.getNewOrder();

        if (order.isBuy()) {
            update(orderUpdate, bids);
        } else {
            update(orderUpdate, asks);
        }

    }

    private void update(OrderUpdate orderUpdate, SortedSet<Order> set) {

        if (orderUpdate.getAction() == Action.ADD) {
            boolean added = set.add(orderUpdate.getNewOrder());
            checkState(added, "Order already exists:" + orderUpdate.getNewOrder());

        } else if (orderUpdate.getAction() == Action.REMOVE) {
            boolean removed = set.remove(orderUpdate.getOldOrder());
            checkState(removed, "nothing removed for: " + orderUpdate.getOldOrder());

        } else if (orderUpdate.getAction() == Action.EDIT) {
            //orders are based on sorted set, so updating fields mean restructuring the set, so we need to remove and add
            boolean removed = set.remove(orderUpdate.getOldOrder());
            checkState(removed, "nothing removed for: " + orderUpdate.getOldOrder());

            boolean added = set.add(orderUpdate.getNewOrder());
            checkState(added, "Order already exists:" + orderUpdate.getNewOrder());
        }
    }

    private static int defaultCompare(Order o1, Order o2) {
        if (o1.getQuantity() != (o2.getQuantity())) {
            return Integer.compare(o2.getQuantity(), o1.getQuantity());
        } else {
            return Long.compare(o1.getOrderId(), o2.getOrderId());
        }
    }

    Comparator<Order> asc = new Comparator<Order>() {
        @Override
        public int compare(Order o1, Order o2) {
            if (o1.getPrice() != o2.getPrice()) {
                //lower ask price has higher priority
                return Integer.compare(o1.getPrice(), o2.getPrice());
            } else {
                return defaultCompare(o1, o2);
            }
        }
    };
    Comparator<Order> des = new Comparator<Order>() {
        @Override
        public int compare(Order o1, Order o2) {
            if (o1.getPrice() != o2.getPrice()) {
                //higher bid price has higher priority
                return Integer.compare(o2.getPrice(), o1.getPrice());
            } else {
                return defaultCompare(o1, o2);
            }

        }
    };

}
