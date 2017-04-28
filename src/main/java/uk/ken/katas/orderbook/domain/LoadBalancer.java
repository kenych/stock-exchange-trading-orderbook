package uk.ken.katas.orderbook.domain;

public class LoadBalancer {
    public static int getRecyclingResourceId(int size, long id) {
        return (int) (id % size);
    }
}
