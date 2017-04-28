package uk.ken.katas.orderbook.domain;

import uk.ken.katas.orderbook.domain.dto.OrderUpdate;

public class Instrument {
    private final String name;
    private final ByOrders byOrders;
    private final ByLevels byLevels;

    public String getName() {
        return name;
    }

    public ByOrders getByOrders() {
        return byOrders;
    }

    public ByLevels getByLevels() {
        return byLevels;
    }

    public Instrument(OrderUpdate orderUpdate) {
        name = orderUpdate.getNewOrder().getSymbol();
        byOrders = new ByOrders(orderUpdate);
        byLevels = new ByLevels(orderUpdate);

    }

    public void handle(OrderUpdate orderUpdate) {
        byOrders.handle(orderUpdate);
        byLevels.handle(orderUpdate);
    }


}
