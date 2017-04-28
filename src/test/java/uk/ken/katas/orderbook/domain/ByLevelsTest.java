package uk.ken.katas.orderbook.domain;

import org.junit.Before;
import org.junit.Test;
import uk.ken.katas.orderbook.domain.dto.ByLevelView;
import uk.ken.katas.orderbook.domain.dto.Command;
import uk.ken.katas.orderbook.domain.dto.Order;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ByLevelsTest {

    public static final boolean BUY = true;
    public static final boolean SELL = false;
    public static final String MSFT_L = "MSFT.L";

    Instruments instruments;

    @Before
    public void setUp() {
        instruments = new Instruments();
    }

    @Test
    public void testAddAndSortingOrder() {
        instruments.handle(new Command(Action.ADD, new Order(3L, MSFT_L, SELL, 21, 10)));
        instruments.handle(new Command(Action.ADD, new Order(4L, MSFT_L, SELL, 21, 7)));

        instruments.handle(new Command(Action.ADD, new Order(1L, MSFT_L, SELL, 19, 10)));
        instruments.handle(new Command(Action.ADD, new Order(2L, MSFT_L, SELL, 19, 2)));

        instruments.handle(new Command(Action.ADD, new Order(5L, MSFT_L, SELL, 22, 7)));

        List<? extends View> byLevelView = instruments.getInstrumentsMap().get(MSFT_L).getByLevels().getAsksView();

        assertThat(byLevelView).hasSize(3);
        assertThat(byLevelView.get(0)).isEqualsToByComparingFields(new ByLevelView(19, 12, 2));
        assertThat(byLevelView.get(1)).isEqualsToByComparingFields(new ByLevelView(21, 17, 2));
        assertThat(byLevelView.get(2)).isEqualsToByComparingFields(new ByLevelView(22, 7, 1));
    }

    @Test
    public void testEditQuantity() {
        instruments.handle(new Command(Action.ADD, new Order(1L, MSFT_L, SELL, 19, 10)));
        instruments.handle(new Command(Action.EDIT, new Order(1L, null, false, 19, 2)));

        List<? extends View> byLevelView = instruments.getInstrumentsMap().get(MSFT_L).getByLevels().getAsksView();

        assertThat(byLevelView).hasSize(1);
        assertThat(byLevelView.get(0)).isEqualsToByComparingFields(new ByLevelView(19, 2, 1));
    }

    @Test
    public void testEditPrice() {
        instruments.handle(new Command(Action.ADD, new Order(1L, MSFT_L, BUY, 19, 10)));
        instruments.handle(new Command(Action.EDIT, new Order(1L, null, false, 18, 2)));

        List<? extends View> byLevelView = instruments.getInstrumentsMap().get(MSFT_L).getByLevels().getBidsView();

        assertThat(byLevelView).hasSize(1);
        assertThat(byLevelView.get(0)).isEqualsToByComparingFields(new ByLevelView(18, 2, 1));
    }

    @Test
    public void testRemove() {
        instruments.handle(new Command(Action.ADD, new Order(1L, MSFT_L, SELL, 19, 10)));
        instruments.handle(new Command(Action.REMOVE, new Order(1L, null, false, 0, 0)));

        List<? extends View> byLevelView = instruments.getInstrumentsMap().get(MSFT_L).getByLevels().getAsksView();

        assertThat(byLevelView).hasSize(0);
    }

}
