package ru.nsu.mbogdanov.controller;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.mbogdanov.model.MapPairs;
import ru.nsu.mbogdanov.model.PersonInfo;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class XmlParser {

    public List<PersonInfo> parse(InputStream stream) throws XMLStreamException {
        ArrayList<PersonInfo> fullPersonsInfo = new ArrayList<>();
        int peopleCount = 0;

        XMLInputFactory streamFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = streamFactory.createXMLStreamReader(stream);

        PersonInfo currentPersonInfo = null;
        log.info("Начинаем читать файл");

        //Просто код из лекции
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
                                }
                            }
                            break;
                        case "person":
                            currentPersonInfo = new PersonInfo();
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                switch (attributeName) {
                                    case "name" -> {
                                        String[] full = reader.getAttributeValue(i).trim().split("\\s+");
                                        currentPersonInfo.setFirstName(full[0]);
                                        currentPersonInfo.setLastName(full[1]);
                                    }
                                    case "id" -> {
                                        currentPersonInfo.setId(reader.getAttributeValue(i).trim());
                                    }
                                }
                            }
                            break;
                        case "id":
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("value")) {
                                    currentPersonInfo.setId(reader.getAttributeValue(i).trim());
                                }
                            }
                            break;
                        case "fullname":
                            while (reader.hasNext()) {
                                int eventType = reader.next();
                                if (eventType == XMLStreamReader.START_ELEMENT) {
                                    String elementName = reader.getLocalName();
                                    switch (elementName) {
                                        case "first" -> {
                                            reader.next();
                                            currentPersonInfo.setFirstName(reader.getText().trim());
                                            reader.next();
                                        }
                                        case "firstName" -> {
                                            reader.next();
                                            currentPersonInfo.setFirstName(reader.getText().trim());
                                            reader.next();
                                        }
                                        case "family" -> {
                                            reader.next();
                                            currentPersonInfo.setLastName(reader.getText().trim());
                                            reader.next();
                                        }
                                    }
                                } else if (eventType == XMLStreamReader.END_ELEMENT && "fullname".equals(reader.getLocalName())) {
                                    break;
                                }
                            }
                            break;
                        case "firstname":
                            String firstnameValue = null;
                            String textnameValue = null;
                            if (reader.getAttributeCount() > 0) {
                                for (int i = 0; i < reader.getAttributeCount(); i++) {
                                    String attributeName = reader.getAttributeLocalName(i);
                                    if (attributeName.equals("value")) {
                                        firstnameValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                    }
                                }
                            } else {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    textnameValue = reader.getText().trim();
                                }
                                firstnameValue = String.join(" ", textnameValue);
                            }
                            if (firstnameValue != null) {
                                currentPersonInfo.setFirstName(firstnameValue);
                            }
                            break;
                        case "surname":
                            String surnameValue = null;
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("value")) {
                                    surnameValue = String.join(" ", reader.getAttributeValue(i).trim().split("\\s+"));
                                }
                            }
                            if (surnameValue != null) {
                                currentPersonInfo.setLastName(surnameValue);
                            }
                            break;
                        case "family-name":
                            String family_nameValue = null;
                            String textfamily_nameValue = null;
                            reader.next();
                            if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                textfamily_nameValue = reader.getText().trim();
                            }
                            family_nameValue = String.join(" ", textfamily_nameValue);
                            if (family_nameValue != null) {
                                currentPersonInfo.setLastName(family_nameValue);
                            }
                            break;

                        case "gender":
                            String gender = null;
                            if (reader.getAttributeCount() > 0) {
                                for (int i = 0; i < reader.getAttributeCount(); i++) {
                                    String attributeName = reader.getAttributeLocalName(i);
                                    String attributeValue = reader.getAttributeValue(i);
                                    if (attributeName.equals("value") || attributeName.equals("val")) {
                                        gender = attributeValue.trim().toUpperCase().substring(0, 1);
                                        break;
                                    }
                                }
                            }
                            if (gender == null) {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    gender = reader.getText().trim().toUpperCase().substring(0, 1);
                                }
                            }
                            if (gender != null) {
                                currentPersonInfo.setGender(gender);
                            }
                            break;
                        case "spouce":
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("value")) {
                                    String attributeValue = reader.getAttributeValue(i);
                                    if (!attributeValue.trim().equals("NONE") && !attributeValue.trim().equals("none") && !attributeValue.trim().equals("null")) {
                                        currentPersonInfo.setSpouseName(attributeValue);
                                    }
                                }
                            }
                            break;
                        case "husband":
                            String husbandId = null;
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("value")) {
                                    husbandId = reader.getAttributeValue(i).trim();
                                    break;
                                }
                            }
                            if (husbandId != null) {
                                currentPersonInfo.setHusbandId(husbandId);
                            }
                            break;
                        case "wife":
                            String wifeId = null;
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("value")) {
                                    wifeId = reader.getAttributeValue(i).trim();
                                    break;
                                }
                            }
                            if (wifeId != null) {
                                currentPersonInfo.setWifeId(wifeId);
                            }
                            break;
                        case "siblings":
                            break;

                        case "brother":
                            String valueBrother = null;
                            if (valueBrother == null) {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    valueBrother = reader.getText().trim();
                                }
                            }
                            if (valueBrother != null) {
                                currentPersonInfo.addBrothersName(valueBrother);
                            }
                            break;

                        case "sister":
                            String valueSister = null;
                            if (valueSister == null) {
                                reader.next();
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    valueSister = reader.getText().trim();
                                }
                            }
                            if (valueSister != null) {
                                currentPersonInfo.addSistersName(valueSister);
                            }
                            break;

                        case "siblingsNumber":
                        case "siblingsnumber":
                        case "siblingsCount":
                        case "siblingscount":
                        case "siblings-count":
                        case "siblings-number":
                            String siblings_number = null;
                            String[] siblings_number_text = null;
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("value")) {
                                    siblings_number_text = reader.getAttributeValue(i).trim().split("\\s+");
                                    siblings_number = String.join(" ", siblings_number_text);
                                }
                            }
                            if (siblings_number != null) {
                                currentPersonInfo.setSiblingsCount(Integer.parseInt(siblings_number));
                            }
                            break;
                        case "child":
                            String[] childNameValue = null;
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                String attributeValue = reader.getAttributeValue(i).trim();
                                if (attributeName.equals("value")) {
                                    childNameValue = attributeValue.split("\\s+");
                                    break;
                                }
                            }

                            // Перемещаемся к тексту, если атрибут value отсутствует
                            if (childNameValue == null) {
                                reader.next(); // Перемещаемся к следующему событию
                                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                    String textContent = reader.getText().trim();
                                    childNameValue = textContent.split("\\s+");
                                }
                            }

                            if (childNameValue != null) {
                                String fullChildrenName = String.join(" ", childNameValue);
                                currentPersonInfo.addChildrenName(fullChildrenName);
                            }
                            break;
                        case "son":
                            String sonValue = null;
                            boolean isSonIdPresent = false;
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("id")) {
                                    sonValue = reader.getAttributeValue(i).trim();
                                    isSonIdPresent = true;
                                    break;
                                }
                            }
                            if (sonValue != null) {
                                if (sonValue.contains(" ") || !isSonIdPresent) {
                                    currentPersonInfo.addSonsName(sonValue);
                                } else {
                                    currentPersonInfo.addSonsId(sonValue);
                                }
                            }
                            break;

                        case "daughter":
                            String daughterValue = null;
                            boolean isIdPresent = false;
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attributeName = reader.getAttributeLocalName(i);
                                if (attributeName.equals("id")) {
                                    daughterValue = reader.getAttributeValue(i).trim();
                                    isIdPresent = true;
                                    break;
                                }
                            }
                            if (daughterValue != null) {
                                if (daughterValue.contains(" ") || !isIdPresent) {
                                    currentPersonInfo.addDaughtersName(daughterValue);
                                } else {
                                    currentPersonInfo.addDaughterId(daughterValue);
                                }
                            }
                            break;

                        case "parent":
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                if (reader.getAttributeLocalName(i).equals("value")) {
                                    if (!reader.getAttributeValue(i).trim().equals("UNKNOWN")) {
                                        currentPersonInfo.addParentId(reader.getAttributeValue(i).trim());
                                    }
                                }
                            }
                            break;
                        case "father":
                            String father = null;
                            reader.next();
                            if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                father = reader.getText().trim();
                            }
                            father = String.join(" ", father);
                            if (father != null) {
                                currentPersonInfo.setFatherName(father);
                            }
                            break;
                        case "mother":
                            String mother = null;
                            reader.next();
                            if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                                mother = reader.getText().trim();
                            }
                            mother = String.join(" ", mother);
                            if (mother != null) {
                                currentPersonInfo.setFatherName(mother);
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
                                if (attributeName.equals("value")) {
                                    children_number_text = reader.getAttributeValue(i).trim().split("\\s+");
                                    children_number = String.join(" ", children_number_text);
                                }
                            }
                            if (children_number != null) {
                                currentPersonInfo.setChildrenCount(Integer.parseInt(children_number));
                            }
                            break;
                    }
                }
                case XMLStreamConstants.END_ELEMENT -> {
                    if (reader.getLocalName().equals("person")) {
                        fullPersonsInfo.add(currentPersonInfo);
                        currentPersonInfo = null;
                    }
                }
                default -> {
                }
            }
        }

        reader.close();
        log.info("Файл успешно прочитан и информация собрана");
        return normalize(fullPersonsInfo, peopleCount);
    }

    private ArrayList<PersonInfo> normalize(ArrayList<PersonInfo> originalData, Integer peopleCount) {
        System.out.println("\nНачинаем делать данные приличными");
        var data = extractingOfFileIdsInformation(originalData);
        var oneIdToOnePerson = data.getIdsPairs();
        var noIdsArray = data.getNoIds();

        System.out.println("\nИщем айдишники для имён, которые остались без айди");
        var fullInfoArray = findIdsForAllNames(noIdsArray, oneIdToOnePerson);
        // Проверяем, что никакого человека не потеряли
        if (fullInfoArray.size() != peopleCount) {
            throw new RuntimeException("Ожидаемое количество людей: " + peopleCount +
                    ", но найдено уникальных записей: " + oneIdToOnePerson.size());
        }
        if (!fullInfoArray.values().parallelStream().allMatch(x -> x.getFirstName() != null && x.getLastName() != null)) {
            throw new RuntimeException("Не все записи имеют имена и фамилии");
        }

        if (!noIdsArray.parallelStream().allMatch(x -> x.getFirstName() != null && x.getLastName() != null)) {
            throw new RuntimeException("Не все записи без айди имеют имена и фамилии");
        }


        System.out.println("\nИщем детей");
        var fullInfoWithChildren = extractAllChildrenData(fullInfoArray);

        System.out.println("\nПроверяем детей");
        childrenChecker(fullInfoWithChildren);

        System.out.println("\nДаём всем гендеры");
        var fullInfoWithChildrenAndGender = extractAllGenderData(fullInfoWithChildren);

        System.out.println("\nПроверяем гендеры");
        genderChecker(fullInfoWithChildrenAndGender);

        System.out.println("\nИщем всем сиблингов");
        var fullInfoWithChildrenAndGenderAndSiblings = extractAllSiblingsData(fullInfoWithChildrenAndGender);
        System.out.println("\nПроверяем сиблингов");
        siblingsChecker(fullInfoWithChildrenAndGenderAndSiblings);
        return new ArrayList<>(fullInfoWithChildrenAndGenderAndSiblings.values());
    }

    private MapPairs extractingOfFileIdsInformation(ArrayList<PersonInfo> originalData) {
        HashMap<String, PersonInfo> oneIdToOnePerson = new HashMap<>();
        ArrayList<PersonInfo> noIds = new ArrayList<>();
        for (PersonInfo i : originalData) {
            if (i.getId() != null) {
                if (oneIdToOnePerson.containsKey(i.getId())) {
                    PersonInfo personInfo = new PersonInfo().extractingOfAllAvailableInformation(i, oneIdToOnePerson.get(i.getId()));
                    oneIdToOnePerson.replace(i.getId(), personInfo);
                } else {
                    oneIdToOnePerson.put(i.getId(), i);
                }
            } else {
                noIds.add(i);
            }
        }
        System.out.println("Людей с известным айди: " + oneIdToOnePerson.size());
        System.out.println("Людей с неизвестными айди: " + noIds.size());
        return new MapPairs(oneIdToOnePerson, noIds);
    }


    private HashMap<String, PersonInfo> findIdsForAllNames(ArrayList<PersonInfo> noIdsArray,
                                                           HashMap<String, PersonInfo> oneIdToOnePerson) {
        int peopleWithOneSameName = 0;
        int sizeOfNoIdsArray = noIdsArray.size();
        for (var currentPerson : noIdsArray) {
            List<PersonInfo> found = findTheSameInfo(x -> x.getFirstName().equals(currentPerson.getFirstName())
                    && x.getLastName().equals(currentPerson.getLastName()), oneIdToOnePerson.values());
            if (!found.isEmpty()) {
                //Если найдена всего одна такая запись - то у этого человека нет однофамильцев
                // и инфу о нём можно объединть с известным айдишников
                if (found.size() == 1) {
                    peopleWithOneSameName++;
                    PersonInfo theSamePerson = found.get(0);
                    PersonInfo personInfo = new PersonInfo().extractingOfAllAvailableInformation(theSamePerson, currentPerson);
                    oneIdToOnePerson.replace(theSamePerson.getId(), personInfo);
                }
            }
        }
        System.out.println("Кол-во людей в массиве без айди: " + sizeOfNoIdsArray);
        System.out.println("Найдено соответствий для людей без айди: " + peopleWithOneSameName);
        System.out.println("Людей без известного айди осталось: " + (sizeOfNoIdsArray - peopleWithOneSameName));
        return oneIdToOnePerson;
    }

    private HashMap<String, PersonInfo> extractAllChildrenData(HashMap<String, PersonInfo> oneIdToOnePersonHashMap) {
        int childName = 0;
        for (var person : oneIdToOnePersonHashMap.values()) {
            person.addAllChildrenId(person.getSonsId());
            person.addAllChildrenId(person.getDaughtersId());
            for (String s : person.getChildrenName()) {
                childName++;
                List<PersonInfo> f = findTheSameInfo(x -> s.equals(x.getFirstName() + " " + x.getLastName()),
                        oneIdToOnePersonHashMap.values());
                if (f.size() >= 1) {
                    PersonInfo child = f.get(0);
                    if (child != null) {
                        person.addChildrenId(child.getId());
                    }
                }
            }
        }
        System.out.println("Всего добавилось информации через имена детей: " + childName);
        return oneIdToOnePersonHashMap;
    }

    public void childrenChecker(HashMap<String, PersonInfo> oneIdToOnePersonHashMap) {
        int fails = 0;
        for (var person : oneIdToOnePersonHashMap.values()) {
            if (person.getChildrenCount() != null) {
                if (person.getChildrenCount() != person.getChildrenId().size()) {
                    fails++;
                }
            }
        }
        if (fails > 0) {
            throw new RuntimeException("Количество ошибок при подсчете детей: " + fails);
        }
        System.out.println("Нашлись дети для всех");
    }

    private HashMap<String, PersonInfo> extractAllGenderData(HashMap<String, PersonInfo> oneIdToOnePersonHashMap) {
        int foundByHusband = 0;
        int strangers = 0;
        int alreadyGender = 0;
        for (var currentPerson : oneIdToOnePersonHashMap.values()) {
            if (currentPerson.getGender() == null) {
                if (currentPerson.getHusbandId() != null || currentPerson.getHusbandName() != null) {
                    foundByHusband++;
                    currentPerson.setGender("F");
                } else {
                    currentPerson.setGender("M");
                    strangers++;
                }
            } else {
                alreadyGender++;
            }
        }

        System.out.println("Нашлось через мужа: " + foundByHusband);
        System.out.println("Не нашлись почему-то: " + strangers);
        System.out.println("Уже известен гендер: " + alreadyGender);
        return oneIdToOnePersonHashMap;
    }

    public void genderChecker(HashMap<String, PersonInfo> oneIdToOnePersonHashMap) {
        int fails = 0;
        for (var p : oneIdToOnePersonHashMap.values()) {
            if (!(p.getGender().equals("M") || p.getGender().equals("F"))) {
                fails++;
            }
        }
        if (fails > 0) {
            throw new RuntimeException("Количество ошибок при подсчете гендеров: " + fails);
        } else {
            System.out.println("С гендерами всё файн");
        }
    }

    private HashMap<String, PersonInfo> extractAllSiblingsData(HashMap<String, PersonInfo> oneIdToOnePersonHashMap) {
        int foundBrotherNames = 0;
        int foundFather = 0;
        int foundSisterNames = 0;
        for (var p : oneIdToOnePersonHashMap.values()) {
            p.addAllSiblings(p.getBrothersId());
            p.addAllSiblings(p.getSistersId());
            for (String s : p.getSistersName()) {
                List<PersonInfo> sisters = findTheSameInfo(x -> !x.getId().equals(p.getId()) && s.equals(x.getFirstName() + " " + x.getLastName()), oneIdToOnePersonHashMap.values());
                for (var currentSister : sisters) {
                    if (!(currentSister.getFirstName() + " " + currentSister.getLastName()).equals(p.getFirstName() + " " + p.getLastName())) {
                        p.addSiblingsId(currentSister.getId());
                        foundSisterNames++;
                    }
                }
            }
            for (String s : p.getBrothersName()) {
                List<PersonInfo> brothers = findTheSameInfo(x -> !x.getId().equals(p.getId()) && s.equals(x.getFirstName() + " " + x.getLastName()), oneIdToOnePersonHashMap.values());
                for (var currentBrother : brothers) {
                    if (!(currentBrother.getFirstName() + " " + currentBrother.getLastName()).equals(p.getFirstName() + " " + p.getLastName())) {
                        p.addSiblingsId(currentBrother.getId());
                        foundBrotherNames++;
                    }
                }
            }
            if (p.getFatherName() != null) {
                String fatherId = p.getFatherName();
                List<PersonInfo> fatherChildren = findTheSameInfo(x -> fatherId.equals(x.getFirstName() + " " + x.getLastName()), oneIdToOnePersonHashMap.values());
                for (var childrenOfTheFather : fatherChildren) {
                    if (!childrenOfTheFather.getId().equals(p.getId())) {
                        foundFather++;
                        p.addSiblingsId(childrenOfTheFather.getId());
                    }
                }
            }
        }
        System.out.println("Найдено через брата: " + foundBrotherNames);
        System.out.println("Найдено через сестру: " + foundSisterNames);
        System.out.println("Найдено через имя отца: " + foundFather);
        return oneIdToOnePersonHashMap;
    }

    public void siblingsChecker(HashMap<String, PersonInfo> oneIdToOnePersonHashMap) {
        int fails = 0;
        for (String key : oneIdToOnePersonHashMap.keySet()) {
            PersonInfo currentPerson = oneIdToOnePersonHashMap.get(key);
            if (currentPerson.getSiblingsCount() != null) {
                if (!(currentPerson.getSiblingsCount() == currentPerson.getSiblingsId().size())) {
                    fails++;
                }
            }
        }
        System.out.println("Ошибок с сиблингами: " + fails);
    }

    /**
     * Фильтрует коллекцию объектов {@link PersonInfo} на основе заданного предиката.
     * Этот метод применяет параллельный поток для эффективной фильтрации, что может улучшить производительность
     * при обработке больших коллекций. Результатом является список объектов {@link PersonInfo}, которые удовлетворяют условиям,
     * определённым в предикате.
     *
     * @param pred Предикат, определяющий критерии для фильтрации объектов {@link PersonInfo}.
     * @param coll Коллекция объектов {@link PersonInfo}, из которой будут выбраны элементы, соответствующие предикату.
     * @return Список объектов {@link PersonInfo}, удовлетворяющих заданному предикату. Может быть пустым, если ни один объект не соответствует условиям.
     */
    private List<PersonInfo> findTheSameInfo(Predicate<PersonInfo> pred, Collection<PersonInfo> coll) {
        return coll.
                parallelStream().
                filter(pred).
                collect(Collectors.toList());
    }
}