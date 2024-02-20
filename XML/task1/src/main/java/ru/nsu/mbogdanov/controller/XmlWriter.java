package ru.nsu.mbogdanov.controller;


import ru.nsu.mbogdanov.model.PersonInfo;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

public class XmlWriter {

    public void writePersonsInfoToXml(List<PersonInfo> persons, String filePath) {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        try {
            XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(filePath), "UTF-8");

            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("Persons");

            for (PersonInfo person : persons) {
                writer.writeStartElement("PersonInfo");

                writeElement(writer, "id", person.getId());
                writeElement(writer, "firstName", person.getFirstName());
                writeElement(writer, "lastName", person.getLastName());
                writeElement(writer, "gender", person.getGender());

                writeSetElements(writer, "parentsId", person.getParentsId());
                writeElement(writer, "motherId", person.getMotherId());
                writeElement(writer, "fatherId", person.getFatherId());
                writeSetElements(writer, "parentsName", person.getParentsName());
                writeElement(writer, "motherName", person.getMotherName());
                writeElement(writer, "fatherName", person.getFatherName());

                writeSetElements(writer, "childrenId", person.getChildrenId());
                writeSetElements(writer, "sonsId", person.getSonsId());
                writeSetElements(writer, "daughtersId", person.getDaughtersId());
                writeSetElements(writer, "childrenName", person.getChildrenName());
                writeSetElements(writer, "sonsName", person.getSonsName());
                writeSetElements(writer, "daughtersName", person.getDaughtersName());

                writeSetElements(writer, "siblingsId", person.getSiblingsId());
                writeSetElements(writer, "brothersId", person.getBrothersId());
                writeSetElements(writer, "sistersId", person.getSistersId());
                writeSetElements(writer, "siblingsName", person.getSiblingsName());
                writeSetElements(writer, "brothersName", person.getBrothersName());
                writeSetElements(writer, "sistersName", person.getSistersName());

                writeElement(writer, "spouseId", person.getSpouseId());
                writeElement(writer, "husbandId", person.getHusbandId());
                writeElement(writer, "wifeId", person.getWifeId());
                writeElement(writer, "spouseName", person.getSpouseName());
                writeElement(writer, "husbandName", person.getHusbandName());
                writeElement(writer, "wifeName", person.getWifeName());

                writeElement(writer, "childrenCount", person.getChildrenCount() != null ? person.getChildrenCount().toString() : null);
                writeElement(writer, "siblingsCount", person.getSiblingsCount() != null ? person.getSiblingsCount().toString() : null);

                writer.writeEndElement();
            }

            writer.writeEndElement();
            writer.writeEndDocument();

            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeElement(XMLStreamWriter writer, String elementName, String value) throws Exception {
        if (value != null) {
            writer.writeStartElement(elementName);
            writer.writeCharacters(value);
            writer.writeEndElement();
        }
    }

    private void writeSetElements(XMLStreamWriter writer, String elementName, Set<String> values) throws Exception {
        if (values != null && !values.isEmpty()) {
            writer.writeStartElement(elementName);
            for (String value : values) {
                writeElement(writer, "value", value);
            }
            writer.writeEndElement();
        }
    }

    public static void main(String[] args) {
        try (InputStream stream = new FileInputStream("./people.xml")) {
            System.out.println("Начинаем парсить файл");
            List<PersonInfo> data = new XmlParser().parse(stream);
            System.out.println("Начинаем писать файл");
            new XmlWriter().writePersonsInfoToXml(data, "output.xml");
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
