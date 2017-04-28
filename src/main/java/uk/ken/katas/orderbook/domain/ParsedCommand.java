package uk.ken.katas.orderbook.domain;


import uk.ken.katas.orderbook.domain.dto.Command;
import uk.ken.katas.orderbook.domain.dto.Order;

import static uk.ken.katas.orderbook.utils.Preconditions.checkState;


public class ParsedCommand {
    private String action;
    private String orderId;
    private String symbol;
    private String type;
    private String price;
    private String quantity;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Command toCommand() {

        //ideally would be nice to have all kind of validations but let's assume the file is correct
        checkState(action != null, "Action can not be null");

        if (action.toUpperCase().equals(Action.ADD.name())) {
            return new Command(Action.ADD,
                    new Order(Long.valueOf(orderId),
                            symbol,
                            type.equals("buy"),
                            Integer.valueOf(price),
                            Integer.valueOf(quantity)));
        } else if (action.toUpperCase().equals(Action.EDIT.name())) {
            return new Command(Action.EDIT,
                    new Order(Long.valueOf(orderId),
                            null,
                            false,
                            Integer.valueOf(price),
                            Integer.valueOf(quantity)));
        } else {//remove
            return new Command(Action.REMOVE,
                    new Order(Long.valueOf(orderId),
                            null,
                            false,
                            0,
                            0));
        }
    }

    @Override
    public String toString() {
        return "ParsedCommand{" +
                "action='" + action + '\'' +
                ", orderId='" + orderId + '\'' +
                ", symbol='" + symbol + '\'' +
                ", type='" + type + '\'' +
                ", price='" + price + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }
}
