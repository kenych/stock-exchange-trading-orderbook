package uk.ken.katas.orderbook.domain.dto;

import uk.ken.katas.orderbook.domain.View;

public class ByLevelView implements View {
    private final int price;
    private final int size;
    private final int count;

    public ByLevelView(int price, int size, int count) {
        this.price = price;
        this.size = size;
        this.count = count;
    }

    public int getPrice() {
        return price;
    }

    public int getSize() {
        return size;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "ByLevelView{" +
                "price=" + price +
                ", size=" + size +
                ", count=" + count +
                '}';
    }
}
