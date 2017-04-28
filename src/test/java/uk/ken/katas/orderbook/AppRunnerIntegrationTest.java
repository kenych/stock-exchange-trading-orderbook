package uk.ken.katas.orderbook;

import org.junit.Test;
import uk.ken.katas.orderbook.domain.View;
import uk.ken.katas.orderbook.domain.dto.ByLevelView;
import uk.ken.katas.orderbook.domain.dto.ByOrderView;
import uk.ken.katas.orderbook.utils.FileUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.ken.katas.orderbook.AppRunner.instruments;

public class AppRunnerIntegrationTest {

    public static final String VOD_L = "VOD.L";
    public static final String TEST_ORDERS_XML = "testOrders.xml";

    @Test
    public void testMain() throws Exception {
        AppRunner.main(new String[]{FileUtils.pathFor(TEST_ORDERS_XML)});
        List<? extends View> byOrderViewAsks = instruments.getInstrumentsMap().get(VOD_L).getByOrders().getAsksView();

        assertThat(byOrderViewAsks).hasSize(3);
        assertThat(byOrderViewAsks.get(0)).isEqualsToByComparingFields(new ByOrderView(7, 100, 16));
        assertThat(byOrderViewAsks.get(1)).isEqualsToByComparingFields(new ByOrderView(8, 100, 16));
        assertThat(byOrderViewAsks.get(2)).isEqualsToByComparingFields(new ByOrderView(9, 112, 21));

        List<? extends View> byOrderViewBids = instruments.getInstrumentsMap().get(VOD_L).getByOrders().getBidsView();

        assertThat(byOrderViewBids).hasSize(2);
        assertThat(byOrderViewBids.get(0)).isEqualsToByComparingFields(new ByOrderView(2, 100, 15));
        assertThat(byOrderViewBids.get(1)).isEqualsToByComparingFields(new ByOrderView(6, 150, 12));

        List<? extends View> byLevelViewAsks = instruments.getInstrumentsMap().get(VOD_L).getByLevels().getAsksView();

        assertThat(byLevelViewAsks).hasSize(2);
        assertThat(byLevelViewAsks.get(0)).isEqualsToByComparingFields(new ByLevelView(16, 200, 2));
        assertThat(byLevelViewAsks.get(1)).isEqualsToByComparingFields(new ByLevelView(21, 112, 1));

        List<? extends View> byLevelViewBids = instruments.getInstrumentsMap().get(VOD_L).getByLevels().getBidsView();

        assertThat(byLevelViewBids).hasSize(2);
        assertThat(byLevelViewBids.get(0)).isEqualsToByComparingFields(new ByLevelView(15, 100, 1));
        assertThat(byLevelViewBids.get(1)).isEqualsToByComparingFields(new ByLevelView(12, 150, 1));
    }


}
