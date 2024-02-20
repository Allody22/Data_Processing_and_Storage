package ru.nsu.mbogdanov.situations;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.mbogdanov.model.PersonInfo;
import ru.nsu.mbogdanov.model.SituationsCount;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;

@Slf4j
public class SituationsParser {
    private final SituationsCount situationsCount = new SituationsCount();

    public void check(InputStream stream) throws XMLStreamException {
        ArrayList<PersonInfo> data = new ArrayList<>();
        int peopleCount = 0;

        XMLInputFactory streamFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = streamFactory.createXMLStreamReader(stream);

        PersonInfo info = null;

        while (reader.hasNext()) {
            reader.next();
            int event_type = reader.getEventType();
            switch (event_type) {
                case XMLStreamConstants.START_ELEMENT -> {
                    switch (reader.getLocalName()) {
                        case "people":
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("count")) {
                                    peopleCount = Integer.parseInt(reader.getAttributeValue(i));
                                } else {
                                    situationsCount.setPeopleError(situationsCount.getPeopleError() + 1);
                                    log.warn("Что-то новое и странное в <people>: " + attributeName + "=" + reader.getAttributeValue(i));
                                }
                            }
                            break;
                        case "person":
                            info = new PersonInfo();
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                switch (attributeName) {
                                    case "name" -> {
                                        String[] full = reader.getAttributeValue(i).trim().split("\\s+");
                                        info.setFirstName(full[0]);
                                        info.setLastName(full[1]);
                                        situationsCount.setPersonName(situationsCount.getPersonName() + 1);
                                    }
                                    case "id" -> {
                                        situationsCount.setPersonId(situationsCount.getPersonId() + 1);
                                        info.setId(reader.getAttributeValue(i).trim());
                                    }
                                    default -> {
                                        log.warn("Что-то новое и странное в <person>: " + attributeName + "=" + reader.getAttributeValue(i));
                                        situationsCount.setPersonError(situationsCount.getPersonError() + 1);
                                    }
                                }
                            }
                            break;
                        case "id":
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("value")) {
                                    info.setId(reader.getAttributeValue(i).trim());
                                    situationsCount.setIdValue(situationsCount.getIdValue() + 1);
                                } else if (attributeName.equals("val")) {
                                    info.setId(reader.getAttributeValue(i).trim());
                                    situationsCount.setIdVal(situationsCount.getIdVal() + 1);
                                } else {
                                    log.warn("Что-то новое и странное в <id>: " + attributeName + "=" + reader.getAttributeValue(i));
                                    situationsCount.setIdError(situationsCount.getIdError() + 1);
                                }
                            }
                            break;
                        case "fullname":
                            while (reader.hasNext()) {
                                int eventType = reader.next();
                                if (eventType == XMLStreamReader.START_ELEMENT) {
                                    String elementName = reader.getLocalName();
                                    if (elementName.equals("first")) {
                                        reader.next();
                                        info.setFirstName(reader.getText().trim());
                                        reader.next();
                                        situationsCount.setFullNameFirst(situationsCount.getFullNameFirst() + 1);
                                    } else if (elementName.equals("firstname")) {
                                        reader.next();
                                        info.setFirstName(reader.getText().trim());
                                        reader.next();
                                        situationsCount.setFullNameFirstnameLow(situationsCount.getFullNameFirstnameLow() + 1);
                                    } else if (elementName.equals("firstName")) {
                                        reader.next();
                                        info.setFirstName(reader.getText().trim());
                                        reader.next();
                                        situationsCount.setFullNameFirstName(situationsCount.getFullNameFirstName() + 1);
                                    } else if (elementName.equals("family")) {
                                        reader.next();
                                        info.setLastName(reader.getText().trim());
                                        reader.next();
                                        situationsCount.setFullNameFamily(situationsCount.getFullNameFamily() + 1);
                                    } else if (elementName.equals("secondname")) {
                                        reader.next();
                                        info.setLastName(reader.getText().trim());
                                        reader.next();
                                        situationsCount.setFullNameSecondnameLow(situationsCount.getFullNameSecondnameLow() + 1);
                                    } else if (elementName.equals("secondName")) {
                                        reader.next();
                                        info.setLastName(reader.getText().trim());
                                        reader.next();
                                        situationsCount.setFullNameSecondName(situationsCount.getFullNameSecondName() + 1);
                                    } else if (elementName.equals("surname")) {
                                        reader.next();
                                        info.setLastName(reader.getText().trim());
                                        reader.next();
                                        situationsCount.setFullNameSurname(situationsCount.getFullNameSurname() + 1);
                                    } else {
                                        log.warn("Что-то новое и странное в fullname: " + reader.getText().trim());
                                        situationsCount.setFullNameError(situationsCount.getFullNameError() + 1);
                                    }
                                } else if (eventType == XMLStreamReader.END_ELEMENT && "fullname".equals(reader.getLocalName())) {
                                    break;
                                }
                            }
                            break;
                        case "first":
                            String firstValue = null;
                            String textValue = null;
                            if (reader.getAttributeCount() > 0) {
                                for (int i = 0; i < reader.getAttributeCount(); i++) {
                                    String attributeName = reader.getAttributeLocalName(i);
                                    if (attributeName.equals("val")) {
                                        firstValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setFirstVal(situationsCount.getFirstVal() + 1);
                                    } else if (attributeName.equals("value")) {
                                        firstValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setFirstValue(situationsCount.getFirstValue() + 1);
                                    }
                                }
                            } else {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    textValue = reader.getText().trim();
                                }
                                firstValue = String.join(" ", textValue);
                                situationsCount.setFirst(situationsCount.getFirst() + 1);
                            }
                            if (firstValue != null) {
                                info.setFirstName(firstValue);
                            } else {
                                situationsCount.setFirstEmpty(situationsCount.getFirstEmpty() + 1);
                                log.warn("Атрибут для имени есть, но внутри пусто");
                            }
                            break;
                        case "firstname":
                            String firstnameValue = null;
                            String textnameValue = null;
                            if (reader.getAttributeCount() > 0) {
                                for (int i = 0; i < reader.getAttributeCount(); i++) {
                                    String attributeName = reader.getAttributeLocalName(i);
                                    if (attributeName.equals("val")) {
                                        firstnameValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setFirstnameLowVal(situationsCount.getFirstnameLowVal() + 1);
                                    } else if (attributeName.equals("value")) {
                                        firstnameValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setFirstnameLowValue(situationsCount.getFirstnameLowValue() + 1);
                                    }
                                }
                            } else {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    textnameValue = reader.getText().trim();
                                }
                                firstnameValue = String.join(" ", textnameValue);
                                situationsCount.setFirstnameLow(situationsCount.getFirstnameLow() + 1);
                            }
                            if (firstnameValue != null) {
                                info.setFirstName(firstnameValue);
                            } else {
                                situationsCount.setFirstnameLowEmpty(situationsCount.getFirstnameLowEmpty() + 1);
                                log.warn("Атрибут для имени есть, но внутри пусто");
                            }
                            break;
                        case "firstName":
                            String firstNameValue = null;
                            String textNameValue = null;
                            if (reader.getAttributeCount() > 0) {
                                for (int i = 0; i < reader.getAttributeCount(); i++) {
                                    String attributeName = reader.getAttributeLocalName(i);
                                    if (attributeName.equals("val")) {
                                        firstNameValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setFirstNameVal(situationsCount.getFirstNameVal() + 1);
                                    } else if (attributeName.equals("value")) {
                                        firstNameValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setFirstNameVal(situationsCount.getFirstNameValue() + 1);
                                    }
                                }
                            } else {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    textNameValue = reader.getText().trim();
                                }
                                firstNameValue = String.join(" ", textNameValue);
                                situationsCount.setFirstName(situationsCount.getFirstName() + 1);
                            }
                            if (firstNameValue != null) {
                                info.setFirstName(firstNameValue);
                            } else {
                                situationsCount.setFirstNameEmpty(situationsCount.getFirstNameEmpty() + 1);
                                log.warn("Атрибут для имени есть, но внутри пусто");
                            }
                            break;
                        case "first-name":
                            String first_NameValue = null;
                            String text_NameValue = null;
                            if (reader.getAttributeCount() > 0) {
                                for (int i = 0; i < reader.getAttributeCount(); i++) {
                                    String attributeName = reader.getAttributeLocalName(i);
                                    if (attributeName.equals("val")) {
                                        first_NameValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setFirst_nameVal(situationsCount.getFirst_nameVal() + 1);
                                    } else if (attributeName.equals("value")) {
                                        first_NameValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setFirst_nameValue(situationsCount.getFirst_nameValue() + 1);
                                    }
                                }
                            } else {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    text_NameValue = reader.getText().trim();
                                }
                                first_NameValue = String.join(" ", text_NameValue);
                                situationsCount.setFirst_name(situationsCount.getFirst_name() + 1);
                            }
                            if (first_NameValue != null) {
                                info.setFirstName(first_NameValue);
                            } else {
                                situationsCount.setFirst_nameEmpty(situationsCount.getFirst_nameEmpty() + 1);
                                log.warn("Атрибут для имени есть, но внутри пусто");
                            }
                            break;
                        case "surname":
                            String surnameValue = null;
                            String textSurnameValue = null;
                            if (reader.getAttributeCount() > 0) {
                                for (int i = 0; i < reader.getAttributeCount(); i++) {
                                    String attributeName = reader.getAttributeLocalName(i);
                                    if (attributeName.equals("val")) {
                                        surnameValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setSurnameVal(situationsCount.getSurnameVal() + 1);
                                    } else if (attributeName.equals("value")) {
                                        surnameValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setSurnameValue(situationsCount.getSurnameValue() + 1);
                                    }
                                }
                            } else {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    textSurnameValue = reader.getText().trim();
                                }
                                surnameValue = String.join(" ", textSurnameValue);
                                situationsCount.setSurname(situationsCount.getSurname() + 1);
                            }
                            if (surnameValue != null) {
                                info.setLastName(surnameValue);
                            } else {
                                situationsCount.setSurnameEmpty(situationsCount.getSurnameEmpty() + 1);
                                log.warn("Атрибут для имени есть, но внутри пусто");
                            }
                            break;
                        case "lastname":
                            String lastnameLowValue = null;
                            String textLastNameLowValue = null;
                            if (reader.getAttributeCount() > 0) {
                                for (int i = 0; i < reader.getAttributeCount(); i++) {
                                    String attributeName = reader.getAttributeLocalName(i);
                                    if (attributeName.equals("val")) {
                                        lastnameLowValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setLastnameLowVal(situationsCount.getLastnameLowVal() + 1);
                                    } else if (attributeName.equals("value")) {
                                        lastnameLowValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setLastnameLowValue(situationsCount.getLastnameLowValue() + 1);
                                    }
                                }
                            } else {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    textLastNameLowValue = reader.getText().trim();
                                }
                                lastnameLowValue = String.join(" ", textLastNameLowValue);
                                situationsCount.setLastnameLow(situationsCount.getLastnameLow() + 1);
                            }
                            if (lastnameLowValue != null) {
                                info.setLastName(lastnameLowValue);
                            } else {
                                situationsCount.setLastnameLowEmpty(situationsCount.getLastnameLowEmpty() + 1);
                                log.warn("Атрибут для имени есть, но внутри пусто");
                            }
                            break;
                        case "family-name":
                            String family_nameValue = null;
                            String textfamily_nameValue = null;
                            if (reader.getAttributeCount() > 0) {
                                for (int i = 0; i < reader.getAttributeCount(); i++) {
                                    String attributeName = reader.getAttributeLocalName(i);
                                    if (attributeName.equals("val")) {
                                        family_nameValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setFamily_nameVal(situationsCount.getFamily_nameVal() + 1);
                                    } else if (attributeName.equals("value")) {
                                        family_nameValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setFamily_nameValue(situationsCount.getFirst_nameValue() + 1);
                                    }
                                }
                            } else {
                                reader.next(); // Переход к текстовому содержимому, если атрибутов нет
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    textfamily_nameValue = reader.getText().trim();
                                }
                                family_nameValue = String.join(" ", textfamily_nameValue);
                                situationsCount.setFamily_name(situationsCount.getFamily_name() + 1);
                            }
                            if (family_nameValue != null) {
                                info.setLastName(family_nameValue);
                            } else {
                                situationsCount.setFamily_nameEmpty(situationsCount.getFirst_nameEmpty() + 1);
                                log.warn("Атрибут для имени есть, но внутри пусто");
                            }
                            break;
                        case "patronymic":
                            String patronymicValue = null;
                            String textPatronymicValue = null;
                            if (reader.getAttributeCount() > 0) {
                                for (int i = 0; i < reader.getAttributeCount(); i++) {
                                    String attributeName = reader.getAttributeLocalName(i);
                                    if (attributeName.equals("val")) {
                                        patronymicValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setPatronymicVal(situationsCount.getPatronymicVal() + 1);
                                    } else if (attributeName.equals("value")) {
                                        patronymicValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setPatronymicValue(situationsCount.getPatronymicValue() + 1);
                                    }
                                }
                            } else {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    textPatronymicValue = reader.getText().trim();
                                }
                                patronymicValue = String.join(" ", textPatronymicValue);
                                situationsCount.setPatronymic(situationsCount.getPatronymic() + 1);
                            }
                            if (patronymicValue != null) {
                                info.setLastName(patronymicValue);
                            } else {
                                situationsCount.setPatronymicEmpty(situationsCount.getPatronymicEmpty() + 1);
                                log.warn("Атрибут для имени есть, но внутри пусто");
                            }
                            break;
                        case "family":
                            String familyValue = null;
                            String textFamilyValue = null;
                            if (reader.getAttributeCount() > 0) {
                                for (int i = 0; i < reader.getAttributeCount(); i++) {
                                    String attributeName = reader.getAttributeLocalName(i);
                                    if (attributeName.equals("val")) {
                                        familyValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setFamilyVal(situationsCount.getFamilyVal() + 1);
                                    } else if (attributeName.equals("value")) {
                                        familyValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setFamilyValue(situationsCount.getFamilyValue() + 1);
                                    }
                                }
                            } else {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    textFamilyValue = reader.getText().trim();
                                }
                                familyValue = String.join(" ", textFamilyValue);
                                situationsCount.setFamilyValue(situationsCount.getFamilyValue() + 1);
                            }
                            if (familyValue != null) {
                                info.setLastName(familyValue);
                            } else {
                                situationsCount.setFamilyEmpty(situationsCount.getFamilyEmpty() + 1);
                                log.warn("Атрибут для имени есть, но внутри пусто");
                            }
                            break;
                        case "last name":
                            String lastNameValue = null;
                            String textLastNameValue = null;
                            if (reader.getAttributeCount() > 0) {
                                for (int i = 0; i < reader.getAttributeCount(); i++) {
                                    String attributeName = reader.getAttributeLocalName(i);
                                    if (attributeName.equals("val")) {
                                        lastNameValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setLastNameVal(situationsCount.getLastNameVal() + 1);
                                    } else if (attributeName.equals("value")) {
                                        lastNameValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setLastNameValue(situationsCount.getLastNameValue() + 1);
                                    }
                                }
                            } else {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    textLastNameValue = reader.getText().trim();
                                }
                                lastNameValue = String.join(" ", textLastNameValue);
                                situationsCount.setLastName(situationsCount.getLastName() + 1);
                            }
                            if (lastNameValue != null) {
                                info.setLastName(lastNameValue);
                            } else {
                                situationsCount.setLastNameEmpty(situationsCount.getLastNameEmpty() + 1);
                                log.warn("Атрибут для имени есть, но внутри пусто");
                            }
                            break;
                        case "last_name":
                            String last_nameValue = null;
                            String text_lastNameValue = null;
                            if (reader.getAttributeCount() > 0) {
                                for (int i = 0; i < reader.getAttributeCount(); i++) {
                                    String attributeName = reader.getAttributeLocalName(i);
                                    if (attributeName.equals("val")) {
                                        last_nameValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setLast_nameVal(situationsCount.getLast_nameVal() + 1);
                                    } else if (attributeName.equals("value")) {
                                        last_nameValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                        situationsCount.setLast_nameValue(situationsCount.getLast_nameValue() + 1);
                                    }
                                }
                            } else {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    text_lastNameValue = reader.getText().trim();
                                }
                                last_nameValue = String.join(" ", text_lastNameValue);
                                situationsCount.setLast_name(situationsCount.getLast_name() + 1);
                            }
                            if (last_nameValue != null) {
                                info.setLastName(last_nameValue);
                            } else {
                                situationsCount.setLast_nameEmpty(situationsCount.getLast_nameEmpty() + 1);
                                log.warn("Атрибут для имени есть, но внутри пусто");
                            }
                            break;

                        case "gender":
                            String gender = null;
                            if (reader.getAttributeCount() > 0) {
                                for (int i = 0; i < reader.getAttributeCount(); i++) {
                                    String attributeName = reader.getAttributeLocalName(i);
                                    if (attributeName.equals("value")) {
                                        //По первой букве male = m и female = f и так всё понятно
                                        gender = attributeName.trim().toUpperCase().substring(0, 1);
                                        situationsCount.setGenderValue(situationsCount.getGenderValue() + 1);
                                        break;
                                    } else if (attributeName.equals("val")) {
                                        gender = attributeName.trim().toUpperCase().substring(0, 1);
                                        situationsCount.setGenderVal(situationsCount.getGenderVal() + 1);
                                        break;
                                    } else {
                                        log.warn("Какой-то странный атрибут внутри гендера: " + attributeName);
                                        situationsCount.setGenderError(situationsCount.getGenderError() + 1);
                                    }
                                }
                            }
                            if (gender == null) {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    gender = reader.getText().trim().toUpperCase().substring(0, 1);
                                    situationsCount.setGender(situationsCount.getGender() + 1);
                                }
                            }
                            if (gender != null) {
                                info.setGender(gender);
                            } else {
                                log.warn("Атрибут для гендера есть, но внутри пусто");
                                situationsCount.setGenderEmpty(situationsCount.getGenderEmpty() + 1);
                            }
                            break;
                        case "spouse":
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("value")) {
                                    String attributeValue = reader.getAttributeValue(i);
                                    if (!attributeValue.trim().equals("NONE") && !attributeValue.trim().equals("none") && !attributeValue.trim().equals("null")) {
                                        info.setSpouseName(attributeValue);
                                        situationsCount.setSpouseValue(situationsCount.getSpouseValue() + 1);
                                    }
                                } else if (attributeName.equals("val")) {
                                    String attributeValue = reader.getAttributeValue(i);
                                    if (!attributeValue.trim().equals("NONE") && !attributeValue.trim().equals("none") && !attributeValue.trim().equals("null")) {
                                        info.setSpouseName(attributeValue);
                                        situationsCount.setSpouseVal(situationsCount.getSpouseVal() + 1);
                                    }
                                } else {
                                    log.warn("Что-то новое и странное в <spouce>: " + attributeName + "=" + reader.getAttributeValue(i));
                                    situationsCount.setSpouseError(situationsCount.getSpouseError() + 1);
                                }
                            }
                            if (reader.hasText()) {
                                String attributeValue = reader.getText();
                                if (!attributeValue.trim().equals("NONE") && !attributeValue.trim().equals("none") && !attributeValue.trim().equals("null")) {
                                    info.setSpouseName(attributeValue);
                                    situationsCount.setSpouse(situationsCount.getSpouse() + 1);
                                }
                            }
                            break;
                        case "spouce":
                            String spouceValue = null;
                            String[] spouceValueText = null;

                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("value")) {
                                    String attributeValue = reader.getAttributeValue(i);
                                    if (!attributeValue.trim().equals("NONE") && !attributeValue.trim().equals("none") && !attributeValue.trim().equals("null")) {
                                        spouceValueText = reader.getAttributeValue(i).trim().split("\\s+");
                                        spouceValue = String.join(" ", spouceValueText);
                                        info.setSpouseName(spouceValue);
                                        situationsCount.setSpouceValue(situationsCount.getSpouceValue() + 1);
                                        break;
                                    }
                                } else if (attributeName.equals("val")) {
                                    String attributeValue = reader.getAttributeValue(i);
                                    if (!attributeValue.trim().equals("NONE") && !attributeValue.trim().equals("none") && !attributeValue.trim().equals("null")) {
                                        spouceValueText = reader.getAttributeValue(i).trim().split("\\s+");
                                        spouceValue = String.join(" ", spouceValueText);
                                        info.setSpouseName(spouceValue);
                                        situationsCount.setSpouseVal(situationsCount.getSpouseVal() + 1);
                                        break;
                                    }
                                } else {
                                    log.warn("Что-то новое и странное в <spouce>: " + attributeName + "=" + reader.getAttributeValue(i));
                                    situationsCount.setSpouceError(situationsCount.getSpouceError() + 1);
                                }
                            }
                            if (reader.hasText()) {
                                String attributeValue = reader.getText();
                                if (!attributeValue.trim().equals("NONE") && !attributeValue.trim().equals("none") && !attributeValue.trim().equals("null")) {
                                    spouceValueText = reader.getText().trim().split("\\s+");
                                    spouceValue = String.join(" ", spouceValueText);
                                    info.setSpouseName(spouceValue);
                                    situationsCount.setSpouce(situationsCount.getSpouce() + 1);
                                }
                            }
                            break;
                        case "husband":
                            String husbandId = null;
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("value")) {
                                    husbandId = reader.getAttributeValue(i).trim();
                                    situationsCount.setHusbandValue(situationsCount.getHusbandValue() + 1);
                                    break;
                                } else if (attributeName.equals("val")) {
                                    husbandId = reader.getAttributeValue(i).trim();
                                    situationsCount.setHusbandVal(situationsCount.getHusbandVal() + 1);
                                    break;
                                } else {
                                    log.warn("Что-то новое и странное у мужа: " + attributeName + "=" + reader.getAttributeValue(i));
                                    situationsCount.setHusbandError(situationsCount.getHusbandError() + 1);
                                }
                            }
                            if (husbandId == null) {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    husbandId = reader.getText().trim();
                                    situationsCount.setHusband(situationsCount.getHusband() + 1);
                                }
                            }
                            if (husbandId != null) {
                                info.setHusbandId(husbandId);
                            } else {
                                situationsCount.setHusbandEmpty(situationsCount.getHusbandEmpty() + 1);
                                log.warn("Атрибут для мужа есть, но внутри пусто");
                            }
                            break;
                        case "wife":
                            String wifeId = null;
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("value")) {
                                    wifeId = reader.getAttributeValue(i).trim();
                                    situationsCount.setWifeValue(situationsCount.getWifeValue() + 1);
                                    break;
                                } else if (attributeName.equals("val")) {
                                    wifeId = reader.getAttributeValue(i).trim();
                                    situationsCount.setWifeVal(situationsCount.getWifeVal() + 1);
                                    break;
                                } else {
                                    situationsCount.setWifeError(situationsCount.getWifeError() + 1);
                                    log.warn("Что-то новое и странное у жены: " + attributeName + "=" + reader.getAttributeValue(i));
                                }
                            }
                            if (wifeId == null) {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    wifeId = reader.getText().trim();
                                    situationsCount.setWife(situationsCount.getWife() + 1);
                                }
                            }
                            if (wifeId != null) {
                                info.setWifeId(wifeId);
                            } else {
                                situationsCount.setWifeEmpty(situationsCount.getWifeEmpty() + 1);
                                log.warn("Атрибут для жены есть, но внутри пусто");
                            }
                            break;
                        case "siblings":
                            break;

                        case "brother":
                            String[] valueBrotherText = null;
                            String valueBrother = null;
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("val")) {
                                    valueBrotherText = reader.getAttributeValue(i).trim().split("\\s+");
                                    valueBrother = String.join(" ", valueBrotherText);
                                    situationsCount.setBrotherVal(situationsCount.getBrotherVal() + 1);
                                } else if (attributeName.equals("value")) {
                                    valueBrotherText = reader.getAttributeValue(i).trim().split("\\s+");
                                    valueBrother = String.join(" ", valueBrotherText);
                                    situationsCount.setBrotherValue(situationsCount.getBrotherValue() + 1);
                                } else if (attributeName.equals("id")) {
                                    valueBrotherText = reader.getAttributeValue(i).trim().split("\\s+");
                                    valueBrother = String.join(" ", valueBrotherText);
                                    situationsCount.setBrotherId(situationsCount.getBrotherId() + 1);
                                }
                            }
                            if (valueBrother == null) {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    valueBrother = reader.getText().trim();
                                    situationsCount.setBrother(situationsCount.getBrother() + 1);
                                }
                            }
                            if (valueBrother != null) {
                                if (valueBrother.contains(" ")) {
                                    info.addBrothersName(valueBrother);
                                } else {
                                    situationsCount.setBrotherId(situationsCount.getBrotherId() + 1);
                                    info.addBrothersId(valueBrother);
                                }
                            } else {
                                log.warn("Не удалось извлечь данные отдельно для брата");
                                situationsCount.setBrotherEmpty(situationsCount.getBrotherEmpty() + 1);
                            }
                            break;

                        case "sister":
                            String[] valueSisterText = null;
                            String valueSister = null;
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("val")) {
                                    valueSisterText = reader.getAttributeValue(i).trim().split("\\s+");
                                    valueSister = String.join(" ", valueSisterText);
                                    situationsCount.setSisterVal(situationsCount.getSisterVal() + 1);
                                } else if (attributeName.equals("value")) {
                                    valueSisterText = reader.getAttributeValue(i).trim().split("\\s+");
                                    valueSister = String.join(" ", valueSisterText);
                                    situationsCount.setSisterValue(situationsCount.getSisterValue() + 1);
                                } else if (attributeName.equals("id")) {
                                    valueSisterText = reader.getAttributeValue(i).trim().split("\\s+");
                                    valueSister = String.join(" ", valueSisterText);
                                    situationsCount.setSisterId(situationsCount.getBrotherId() + 1);
                                }
                            }
                            if (valueSister == null) {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    valueSister = reader.getText().trim();
                                    situationsCount.setSister(situationsCount.getSister() + 1);
                                }
                            }
                            if (valueSister != null) {
                                if (valueSister.contains(" ")) {
                                    info.addSistersName(valueSister);
                                } else {
                                    situationsCount.setSisterId(situationsCount.getBrotherId() + 1);
                                    info.addSistersId(valueSister);
                                }
                            } else {
                                log.warn("Не удалось извлечь данные отдельно для брата");
                                situationsCount.setSisterEmpty(situationsCount.getSisterEmpty() + 1);
                            }
                            break;

                        case "siblingsNumber":
                        case "siblingsnumber":
                        case "siblingsCount":
                        case "siblingscount":
                        case "siblings-count":
                        case "siblings-number": //Доказано только это пока
                            String siblings_number = null;
                            String[] siblings_number_text = null;
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("val")) {
                                    siblings_number_text = reader.getAttributeValue(i).trim().split("\\s+");
                                    siblings_number = String.join(" ", siblings_number_text);
                                    situationsCount.setSiblings_number_val(situationsCount.getSiblings_number() + 1);
                                } else if (attributeName.equals("value")) {
                                    siblings_number_text = reader.getAttributeValue(i).trim().split("\\s+");
                                    siblings_number = String.join(" ", siblings_number_text);
                                    situationsCount.setSiblings_number_value(situationsCount.getSiblings_number_value() + 1);
                                } else {
                                    situationsCount.setSiblings_number_error(situationsCount.getSiblings_number_error() + 1);
                                    log.warn("Что-то неизвестное в количестве родственников");
                                }
                            }
                            if (siblings_number == null) {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    siblings_number = reader.getText().trim();
                                    situationsCount.setSiblings_number(situationsCount.getSiblings_number() + 1);
                                }
                            }
                            if (siblings_number != null) {
                                info.setSiblingsCount(Integer.parseInt(siblings_number));
                            } else {
                                log.warn("Не удалось извлечь данные о количестве родственников");
                                situationsCount.setSiblings_number_error(situationsCount.getSiblings_number_error() + 1);
                            }
                        case "child":
                            String childId = null;
                            String[] childNameValue = null;
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("value")) {
                                    childNameValue = reader.getAttributeValue(i).trim().split("\\s+");
                                    situationsCount.setChildValue(situationsCount.getChildValue() + 1);
                                    break;
                                } else if (attributeName.equals("val")) {
                                    childNameValue = reader.getAttributeValue(i).trim().split("\\s+");
                                    situationsCount.setChildVal(situationsCount.getChildVal() + 1);
                                    break;
                                } else if (attributeName.equals("id")) {
                                    childId = reader.getAttributeValue(i).trim();
                                    situationsCount.setChildId(situationsCount.getChildId() + 1);
                                    break;
                                } else {
                                    log.warn("Что-то новое и странное у ребёнка: " + attributeName + "=" + reader.getAttributeValue(i));
                                    situationsCount.setChildError(situationsCount.getChildError() + 1);
                                }
                            }
                            if (childNameValue == null) {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    childNameValue = reader.getText().trim().split("\\s+");
                                    situationsCount.setChild(situationsCount.getChild() + 1);
                                }
                            }
                            if (childId != null) {
                                info.addChildrenId(childId);
                                break;
                            }
                            if (childNameValue != null) {
                                String fullChildrenName = String.join(" ", childNameValue);
                                info.addChildrenName(fullChildrenName);
                            } else {
                                log.warn("Не удалось извлечь данные для <child>");
                                situationsCount.setChildEmpty(situationsCount.getChildEmpty() + 1);
                            }
                            break;

                        case "son":
                            String sonValue = null;
                            boolean isSonIdPresent = false;
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("value")) {
                                    sonValue = reader.getAttributeValue(i).trim();
                                    situationsCount.setSonValue(situationsCount.getSonValue() + 1);
                                    break;
                                } else if (attributeName.equals("val")) {
                                    sonValue = reader.getAttributeValue(i).trim();
                                    situationsCount.setSonVal(situationsCount.getSonVal() + 1);
                                    break;
                                } else if (attributeName.equals("id")) {
                                    sonValue = reader.getAttributeValue(i).trim();
                                    isSonIdPresent = true;
                                    situationsCount.setSonId(situationsCount.getSonId() + 1);
                                    break;
                                } else {
                                    situationsCount.setSonError(situationsCount.getSonError() + 1);
                                    log.warn("Что-то новое и странное у сына: " + attributeName + "=" + reader.getAttributeValue(i));
                                }
                            }
                            if (!isSonIdPresent) {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    sonValue = reader.getText().trim();
                                    situationsCount.setSon(situationsCount.getSon() + 1);
                                }
                            }
                            if (sonValue != null) {
                                if (sonValue.contains(" ") || !isSonIdPresent) {
                                    info.addSonsName(sonValue);
                                } else {
                                    situationsCount.setSonId(situationsCount.getSonId() + 1);
                                    info.addSonsId(sonValue);
                                }
                            } else {
                                situationsCount.setSonEmpty(situationsCount.getSonEmpty() + 1);
                                log.warn("Не удалось извлечь данные отдельно для дочери");
                            }
                            break;


                        case "daughter":
                            String daughterValue = null;
                            boolean isIdPresent = false;
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("value")) {
                                    daughterValue = reader.getAttributeValue(i).trim();
                                    situationsCount.setDaughterValue(situationsCount.getDaughterValue() + 1);
                                    break;
                                } else if (attributeName.equals("val")) {
                                    daughterValue = reader.getAttributeValue(i).trim();
                                    situationsCount.setDaughterVal(situationsCount.getDaughterVal() + 1);
                                    break;
                                }
                                if (attributeName.equals("id")) {
                                    daughterValue = reader.getAttributeValue(i).trim();
                                    isIdPresent = true;
                                    situationsCount.setDaughterId(situationsCount.getDaughterId() + 1);
                                    break;
                                } else {
                                    log.warn("Что-то новое и странное у дочери: " + attributeName + "=" + reader.getAttributeValue(i));
                                    situationsCount.setDaughterError(situationsCount.getDaughterError() + 1);
                                }
                            }
                            if (!isIdPresent) {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    daughterValue = reader.getText().trim();
                                    situationsCount.setDaughter(situationsCount.getDaughter() + 1);
                                }
                            }
                            if (daughterValue != null) {
                                if (daughterValue.contains(" ") || !isIdPresent) {
                                    info.addDaughtersName(daughterValue);
                                } else {
                                    situationsCount.setDaughterId(situationsCount.getDaughterId() + 1);
                                    info.addDaughterId(daughterValue);
                                }
                            } else {
                                situationsCount.setDaughterEmpty(situationsCount.getDaughterEmpty() + 1);
                                log.warn("Не удалось извлечь данные отдельно для дочери");
                            }
                            break;

                        case "parent":
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                if (reader.getAttributeLocalName(i).equals("value")) {
                                    if (!reader.getAttributeValue(i).trim().equals("UNKNOWN")) {
                                        situationsCount.setParentValue(situationsCount.getParentValue() + 1);
                                        info.addParentId(reader.getAttributeValue(i).trim());
                                    }
                                } else if (reader.getAttributeLocalName(i).equals("val")) {
                                    if (!reader.getAttributeValue(i).trim().equals("UNKNOWN")) {
                                        ;
                                        situationsCount.setParentVal(situationsCount.getParentVal() + 1);
                                        info.addParentId(reader.getAttributeValue(i).trim());
                                    }
                                } else {
                                    situationsCount.setParentError(situationsCount.getParentError() + 1);
                                    log.warn("Что-то странное в <parent>");
                                }
                            }
                            break;
                        case "father":
                            String[] textFatherValue = null;
                            String father = null;
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("val")) {
                                    textFatherValue = reader.getAttributeValue(i).trim().split("\\s+");
                                    father = String.join(" ", textFatherValue);
                                    situationsCount.setFatherVal(situationsCount.getFatherVal() + 1);
                                } else if (attributeName.equals("value")) {
                                    textFatherValue = reader.getAttributeValue(i).trim().split("\\s+");
                                    father = String.join(" ", textFatherValue);
                                    situationsCount.setFatherValue(situationsCount.getFatherValue() + 1);
                                } else {
                                    situationsCount.setFatherError(situationsCount.getFatherError() + 1);
                                    log.warn("Что-то странное в <father>");
                                }
                            }
                            if (father == null) {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    father = reader.getText().trim();
                                    situationsCount.setFather(situationsCount.getFather() + 1);
                                }
                                father = String.join(" ", father);
                            }
                            if (father != null) {
                                info.setFatherName(father);
                            } else {
                                situationsCount.setFatherEmpty(situationsCount.getFatherEmpty() + 1);
                                log.warn("Не удалось извлечь данные про отца");
                            }
                            break;
                        case "mother":
                            String[] textMotherValue = null;
                            String mother = null;
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("val")) {
                                    textMotherValue = reader.getAttributeValue(i).trim().split("\\s+");
                                    mother = String.join(" ", textMotherValue);
                                    situationsCount.setMotherVal(situationsCount.getMotherVal() + 1);
                                } else if (attributeName.equals("value")) {
                                    textMotherValue = reader.getAttributeValue(i).trim().split("\\s+");
                                    mother = String.join(" ", textMotherValue);
                                    situationsCount.setMotherValue(situationsCount.getMotherValue() + 1);
                                } else {
                                    situationsCount.setMotherError(situationsCount.getMotherError() + 1);
                                    log.warn("Что-то странное в <mother>");
                                }
                            }
                            if (mother == null) {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    mother = reader.getText().trim();
                                    situationsCount.setMother(situationsCount.getMother() + 1);
                                }
                                mother = String.join(" ", mother);
                            }
                            if (mother != null) {
                                info.setFatherName(mother);
                            } else {
                                situationsCount.setMotherEmpty(situationsCount.getMotherEmpty() + 1);
                                log.warn("Не удалось извлечь данные про мать");
                            }
                            break;
                        case "childrenNumber":
                        case "childrennumber":
                        case "childrenCount":
                        case "childrencount":
                        case "children-count":
                        case "children-number":
                            String children_number = null;
                            String[] children_number_text = null;
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("val")) {
                                    children_number_text = reader.getAttributeValue(i).trim().split("\\s+");
                                    children_number = String.join(" ", children_number_text);
                                    situationsCount.setChildren_numberVal(situationsCount.getChildren_numberVal() + 1);
                                } else if (attributeName.equals("value")) {
                                    children_number_text = reader.getAttributeValue(i).trim().split("\\s+");
                                    children_number = String.join(" ", children_number_text);
                                    situationsCount.setChildren_numberValue(situationsCount.getChildren_numberValue() + 1);
                                } else {
                                    situationsCount.setChildren_numberError(situationsCount.getChildren_numberError() + 1);
                                    log.warn("Что-то неизвестное в количестве детей");
                                }
                            }
                            if (children_number == null) {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    children_number = reader.getText().trim();
                                    situationsCount.setChildren_number(situationsCount.getChildren_number() + 1);
                                }
                            }
                            if (children_number != null) {
                                info.setSiblingsCount(Integer.parseInt(children_number));
                            } else {
                                log.warn("Не удалось извлечь данные о количестве детей");
                                situationsCount.setChildren_numberEmpty(situationsCount.getChildren_numberEmpty() + 1);
                            }
                    }
                }
                case XMLStreamConstants.END_ELEMENT -> {
                    if (reader.getLocalName().equals("person")) {
                        data.add(info);
                        info = null;
                    }
                }
                default -> {
                }
            }
        }
        reader.close();
        System.out.println(situationsCount.beautifulOutputSorted());
        return;
    }
}
