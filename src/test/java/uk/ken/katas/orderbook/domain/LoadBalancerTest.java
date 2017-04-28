package uk.ken.katas.orderbook.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.ken.katas.orderbook.domain.LoadBalancer.getRecyclingResourceId;

public class LoadBalancerTest {

    public static final int SIZE = 2;

    @Test
    public void testGetNodeId() throws Exception {
        assertThat(getRecyclingResourceId(SIZE, 1)).isEqualTo(1);
        assertThat(getRecyclingResourceId(SIZE, 2)).isEqualTo(0);
        assertThat(getRecyclingResourceId(SIZE, 3)).isEqualTo(1);
    }
}
