package ru.nsu.mbogdanov;

import ru.nsu.mbogdanov.controller.XmlParser;

import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainParser {

    public static void main(String[] args) {
        try (InputStream stream = new FileInputStream("./people.xml")) {
            new XmlParser().parse(stream);
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
    }

}