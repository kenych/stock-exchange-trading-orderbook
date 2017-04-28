package uk.ken.katas.orderbook.service;

import uk.ken.katas.orderbook.domain.ParsedCommand;
import uk.ken.katas.orderbook.domain.dto.Command;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static uk.ken.katas.orderbook.AppRunner.*;


public class XmlParser extends Thread {

    private static final String ADD = "add";
    private static final String EDIT = "edit";
    private static final String REMOVE = "remove";

    @Override
    public void run() {
        parsingPhaseStarted.countDown();

        int orderSize = 0;

        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(new File((fileName)).getAbsolutePath());
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement() && isCommand(event.asStartElement().getName().getLocalPart())) {
                    commandsQueue.add(parseCommand(event));
                    orderSize++;
                }
            }
            eventReader.close();
        } catch (Exception e) {
            Synchroniser.allDone();
            throw new RuntimeException(e);
        }

        System.out.println("Parsing ended, order size is " + orderSize);
        parsingPhaseEnded.countDown();
    }

    private boolean isCommand(String localPart) {
        return localPart.equals(ADD) || localPart.equals(EDIT) || localPart.equals(REMOVE);
    }

    private Command parseCommand(XMLEvent event) {
        ParsedCommand parsedCommand = new ParsedCommand();
        parsedCommand.setAction(event.asStartElement().getName().getLocalPart());
        parsedCommand.setOrderId(getAttOrNull(event, "order-id"));
        parsedCommand.setSymbol(getAttOrNull(event, "symbol"));
        parsedCommand.setType(getAttOrNull(event, "type"));
        parsedCommand.setPrice(getAttOrNull(event, "price"));
        parsedCommand.setQuantity(getAttOrNull(event, "quantity"));

        return parsedCommand.toCommand();
    }

    private String getAttOrNull(XMLEvent event, String attName) {
        Attribute attribute = event.asStartElement().getAttributeByName(new QName(attName));
        return attribute != null ? attribute.getValue() : null;
    }

}
