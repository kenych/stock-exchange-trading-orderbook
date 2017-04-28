package uk.ken.katas.orderbook.domain;

import org.junit.Test;

public class ParsedCommandTest {
    @Test(expected = IllegalArgumentException.class)
    public void testSetAction() throws Exception {
        ParsedCommand parsedCommand = new ParsedCommand();
        parsedCommand.setAction("");

        parsedCommand.toCommand();
    }
}
