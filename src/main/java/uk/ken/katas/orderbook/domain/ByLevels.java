package uk.ken.katas.orderbook.domain;

import uk.ken.katas.orderbook.domain.dto.ByLevelView;
import uk.ken.katas.orderbook.domain.dto.OrderUpdate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static uk.ken.katas.orderbook.utils.Preconditions.checkState;

public class ByLevels {
    private ConcurrentMap<Integer, CumulativeOrder> asks = new ConcurrentSkipListMap<Integer, CumulativeOrder>(
            new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }

            });

    private ConcurrentMap<Integer, CumulativeOrder> bids = new ConcurrentSkipListMap<Integer, CumulativeOrder>(
            new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o2.compareTo(o1);
                }

            });

    private ReadWriteLock asksReadWriteLock = new ReentrantReadWriteLock();
    private ReadWriteLock bidsReadWriteLock = new ReentrantReadWriteLock();

    private Lock asksReadLock = asksReadWriteLock.readLock();
    private Lock asksWriteLock = asksReadWriteLock.writeLock();

    private Lock bidsReadLock = bidsReadWriteLock.readLock();
    private Lock bidsWriteLock = bidsReadWriteLock.writeLock();

    public ByLevels(OrderUpdate orderUpdate) {
        handle(orderUpdate);
    }

    public void handle(OrderUpdate orderUpdate) {

        if (orderUpdate.getNewOrder().isBuy()) {
            update(bids, orderUpdate, bidsReadLock, bidsWriteLock);
        } else {
            update(asks, orderUpdate, asksReadLock, asksWriteLock);
        }
    }

    public List<? extends View> getAsksView() {
        return getByLevelViewFor(asks, asksReadLock);
    }

    public List<? extends View> getBidsView() {
        return getByLevelViewFor(bids, bidsReadLock);
    }

    private List<ByLevelView> getByLevelViewFor(ConcurrentMap<Integer, CumulativeOrder> byLevelsMap, Lock readLock) {

        List<ByLevelView> byLevelView;
        try {
            readLock.lock();

            byLevelView = new ArrayList<ByLevelView>(byLevelsMap.size());
            for (Integer price : byLevelsMap.keySet()) {
                CumulativeOrder cumulativeOrder = byLevelsMap.get(price);
                int quantitySum = cumulativeOrder.getQuantitySum();
                byLevelView.add(new ByLevelView(price, quantitySum, cumulativeOrder.size()));
            }
        } finally {
            readLock.unlock();
        }
        return byLevelView;
    }

    private void update(ConcurrentMap<Integer, CumulativeOrder> byLevelsMap, OrderUpdate orderUpdate, Lock readLock, Lock writeLock) {
        if (orderUpdate.getAction() == Action.ADD) {
            try {
                //need to lock it so REMOVE would wait until we work on a node, other thread's ADD getAction() though should be lock free
                readLock.lock();

                add(byLevelsMap, orderUpdate);

            } finally {
                readLock.unlock();
            }
        } else if (orderUpdate.getAction() == Action.REMOVE) {
            try {
                //need to lock in a way that ADD/EDIT would not work on the node that is going to be deleted
                writeLock.lock();

                remove(byLevelsMap, orderUpdate);
            } finally {
                writeLock.unlock();
            }
        } else {//EDIT

            //price change means atomic DELETE/ADD, otherwise just update the size on the order
            if (orderUpdate.isPriceChanged()) {
                try {
                    writeLock.lock();

                    remove(byLevelsMap, orderUpdate);

                    add(byLevelsMap, orderUpdate);

                } finally {
                    writeLock.unlock();
                }
            } else {
                try {
                    readLock.lock();

                    edit(byLevelsMap, orderUpdate);

                } finally {
                    readLock.unlock();
                }
            }
        }

    }

    private void edit(ConcurrentMap<Integer, CumulativeOrder> byLevelsMap, OrderUpdate orderUpdate) {
        int price = orderUpdate.getNewOrder().getPrice();
        CumulativeOrder cumulativeOrder = byLevelsMap.get(price);
        cumulativeOrder.updateQuantityFor(orderUpdate.getNewOrder());
    }

    private void remove(ConcurrentMap<Integer, CumulativeOrder> byLevelsMap, OrderUpdate orderUpdate) {

        //get the price of old price to remove it
        int price = orderUpdate.getOldOrder().getPrice();

        CumulativeOrder cumulativeOrder = byLevelsMap.get(price);
        checkState(cumulativeOrder != null, "cumulativeOrder not found for: " + price);

        //if the only order in the node, so delete the node, otherwise delete order from node
        if (cumulativeOrder.size() > 1) {
            cumulativeOrder.remove(orderUpdate.getNewOrder().getOrderId());
        } else {
            cumulativeOrder = byLevelsMap.remove(price);
            checkState(cumulativeOrder != null, "Node already deleted for " + price);
        }
    }

    private void add(ConcurrentMap<Integer, CumulativeOrder> byLevelsMap, OrderUpdate orderUpdate) {
        int price = orderUpdate.getNewOrder().getPrice();

        CumulativeOrder cumulativeOrder = byLevelsMap.get(price);

        if (cumulativeOrder == null) {
            cumulativeOrder = byLevelsMap.putIfAbsent(price, new CumulativeOrder(orderUpdate.getNewOrder()));
        }

        if (cumulativeOrder != null) {
            cumulativeOrder.add(orderUpdate.getNewOrder());
        }
    }


}
