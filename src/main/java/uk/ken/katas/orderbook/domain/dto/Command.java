package uk.ken.katas.orderbook.domain.dto;

import uk.ken.katas.orderbook.domain.Action;

public class Command {
    private final Action action;
    private final Order order;

    public Command(Action action, Order order) {
        this.action = action;
        this.order = order;
    }

    public Action getAction() {
        return action;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "Command{" +
                "action=" + action +
                ", order=" + order +
                '}';
    }
}
