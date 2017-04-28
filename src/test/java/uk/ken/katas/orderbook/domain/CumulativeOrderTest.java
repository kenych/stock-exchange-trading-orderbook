package uk.ken.katas.orderbook.domain;

import org.junit.Test;
import uk.ken.katas.orderbook.domain.dto.Order;

import static org.assertj.core.api.Assertions.assertThat;

public class CumulativeOrderTest {
    @Test
    public void testUpdateQuantityFor() {
        CumulativeOrder cumulativeOrder = new CumulativeOrder(new Order(1l, "xx", false, 10, 1));
        cumulativeOrder.updateQuantityFor(new Order(1l, "xx", false, 10, 2));

        cumulativeOrder.add(new Order(12, "xx", false, 10, 3));

        assertThat(cumulativeOrder.getQuantitySum()).isEqualTo(5);
    }
}
