package uk.ken.katas.orderbook.service;

import uk.ken.katas.orderbook.domain.dto.Command;

public interface OrderProcessor {
    void parse();

    void handle(Command command);

    void printResult();
}
