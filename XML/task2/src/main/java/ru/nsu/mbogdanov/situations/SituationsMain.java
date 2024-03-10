package ru.nsu.mbogdanov.situations;

import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SituationsMain {
    public static void main(String[] args) {
        try (InputStream stream = new FileInputStream("./people.xml")) {
            new SituationsParser().check(stream);
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
