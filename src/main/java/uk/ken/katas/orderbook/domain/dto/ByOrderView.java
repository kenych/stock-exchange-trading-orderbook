package uk.ken.katas.orderbook.domain.dto;

import uk.ken.katas.orderbook.domain.View;

public class ByOrderView implements View {
    private final long orderId;
    private final int size;
    private final int price;

    public ByOrderView(long orderId, int size, int price) {
        this.orderId = orderId;
        this.size = size;
        this.price = price;
    }

    public long getOrderId() {
        return orderId;
    }

    public int getSize() {
        return size;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "ByOrderView{" +
                "orderId=" + orderId +
                ", size=" + size +
                ", price=" + price +
                '}';
    }
}
