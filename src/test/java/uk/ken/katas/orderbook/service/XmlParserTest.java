package uk.ken.katas.orderbook.service;

import org.junit.Before;
import org.junit.Test;
import uk.ken.katas.orderbook.AppRunner;
import uk.ken.katas.orderbook.domain.dto.Command;
import uk.ken.katas.orderbook.utils.FileUtils;

import java.util.concurrent.LinkedBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.ken.katas.orderbook.AppRunner.commandsQueue;

public class XmlParserTest {

    public static final String TEST_ORDERS_XML = "testOrders.xml";
    XmlParser parser;

    @Before
    public void setUp() throws Exception {
        commandsQueue = new LinkedBlockingQueue<Command>();
        AppRunner.fileName = FileUtils.pathFor(TEST_ORDERS_XML);
        parser = new XmlParser();
    }

    @Test
    public void testRun() {
        parser.run();
        assertThat(commandsQueue).hasSize(13);
    }
}
