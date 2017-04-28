package uk.ken.katas.orderbook.domain.dto;

/**
 * A Data transfer object used by {@link uk.ken.katas.orderbook.app.AppEnvironment} to send data used.
 */
public class Order {
    private final long orderId;
    private String symbol;
    private boolean isBuy;
    private final int price;
    private int quantity;

    public Order(long orderId, String symbol, boolean isBuy, int price, int quantity) {
        this.orderId = orderId;
        this.symbol = symbol;
        this.isBuy = isBuy;
        this.price = price;
        this.quantity = quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getOrderId() {
        return orderId;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", symbol='" + symbol + '\'' +
                ", isBuy=" + isBuy +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }


}
