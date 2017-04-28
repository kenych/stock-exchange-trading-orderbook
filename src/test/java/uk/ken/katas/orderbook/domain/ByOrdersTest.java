package uk.ken.katas.orderbook.domain;

import org.junit.Before;
import org.junit.Test;
import uk.ken.katas.orderbook.domain.dto.ByOrderView;
import uk.ken.katas.orderbook.domain.dto.Command;
import uk.ken.katas.orderbook.domain.dto.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ByOrdersTest {

    public static final boolean BUY = true;
    public static final boolean SELL = false;
    public static final String MSFT_L = "MSFT.L";

    Instruments instruments;

    @Before
    public void setUp() {
        instruments = new Instruments();
    }

    @Test
    public void testAddInRandomOrderAndCheckForSortingOrder() {

        //commands to be tested
        List<Command> commands = new ArrayList<Command>(Arrays.asList(
                new Command(Action.ADD, new Order(1L, MSFT_L, SELL, 19, 10)),
                new Command(Action.ADD, new Order(2L, MSFT_L, SELL, 19, 2)),
                new Command(Action.ADD, new Order(3L, MSFT_L, SELL, 21, 9)),
                new Command(Action.ADD, new Order(4L, MSFT_L, SELL, 21, 8)),
                new Command(Action.ADD, new Order(5L, MSFT_L, SELL, 22, 7))
        ));

        //added in random order
        Collections.shuffle(commands);
        for (Command command : commands) {
            instruments.handle(command);
        }

        List<? extends View> byOrderView = instruments.getInstrumentsMap().get(MSFT_L).getByOrders().getAsksView();

        assertThat(byOrderView).hasSize(5);
        assertThat(byOrderView.get(0)).isEqualsToByComparingFields(new ByOrderView(1, 10, 19));
        assertThat(byOrderView.get(1)).isEqualsToByComparingFields(new ByOrderView(2, 2, 19));
        assertThat(byOrderView.get(2)).isEqualsToByComparingFields(new ByOrderView(3, 9, 21));
        assertThat(byOrderView.get(3)).isEqualsToByComparingFields(new ByOrderView(4, 8, 21));
        assertThat(byOrderView.get(4)).isEqualsToByComparingFields(new ByOrderView(5, 7, 22));
    }

    @Test
    public void testOrderIs_19_20_AndThenIs_18_19_AfterEdit() {
        instruments.handle(new Command(Action.ADD, new Order(1L, MSFT_L, SELL, 20, 5)));
        instruments.handle(new Command(Action.ADD, new Order(2L, MSFT_L, SELL, 19, 5)));

        List<? extends View> byOrderView = instruments.getInstrumentsMap().get(MSFT_L).getByOrders().getAsksView();

        assertThat(byOrderView).hasSize(2);
        assertThat(byOrderView.get(0)).isEqualsToByComparingFields(new ByOrderView(2, 5, 19));
        assertThat(byOrderView.get(1)).isEqualsToByComparingFields(new ByOrderView(1, 5, 20));

        instruments.handle(new Command(Action.EDIT, new Order(1L, null, false, 18, 5)));

        byOrderView = instruments.getInstrumentsMap().get(MSFT_L).getByOrders().getAsksView();

        assertThat(byOrderView).hasSize(2);
        assertThat(byOrderView.get(0)).isEqualsToByComparingFields(new ByOrderView(1, 5, 18));
        assertThat(byOrderView.get(1)).isEqualsToByComparingFields(new ByOrderView(2, 5, 19));
    }

}
