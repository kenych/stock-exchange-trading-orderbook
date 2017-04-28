package uk.ken.katas.orderbook.domain.dto;

import uk.ken.katas.orderbook.domain.Action;

public class OrderUpdate {
    private final Order oldOrder;
    private final Order newOrder;
    private final Action action;
    private final boolean isPriceChanged;

    public OrderUpdate(Order oldOrder, Order newOrder, Action action, boolean isPriceChanged) {
        this.oldOrder = oldOrder;
        this.newOrder = newOrder;
        this.action = action;
        this.isPriceChanged = isPriceChanged;
    }

    public Order getOldOrder() {
        return oldOrder;
    }

    public Order getNewOrder() {
        return newOrder;
    }

    public Action getAction() {
        return action;
    }

    public boolean isPriceChanged() {
        return isPriceChanged;
    }

    @Override
    public String toString() {
        return "OrderUpdate{" +
                "oldOrder=" + oldOrder +
                ", newOrder=" + newOrder +
                ", action=" + action +
                ", isPriceChanged=" + isPriceChanged +
                '}';
    }
}
