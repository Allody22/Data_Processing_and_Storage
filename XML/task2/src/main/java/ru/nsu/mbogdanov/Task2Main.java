package ru.nsu.mbogdanov;

import ru.nsu.mbogdanov.controller.XmlParser;
import ru.nsu.mbogdanov.model.PersonInfo;
import ru.nsu.mbogdanov.schema.*;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Task2Main {

    public static void main(String[] args) {
        try (InputStream stream = new FileInputStream("./people.xml")) {
            List<PersonInfo> personInfoList = new XmlParser().parse(stream);
            System.out.println("\nНачинаются попытки работы с JAXB");
            People people = convertToJaxbPersonList(personInfoList);
            marshalPeople(people);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Преобразует список объектов PersonInfo в структуру People, учитывая связи между объектами.
     * Для каждого объекта PersonInfo создается соответствующий объект PersonType,
     * к которому добавляются связи (супруги, родители, дети, братья и сестры) по айдишкам.
     *
     * @param personInfoList - список объектов PersonInfo для преобразования
     * @return структура People с установленными связями между объектами
     */
    private static People convertToJaxbPersonList(List<PersonInfo> personInfoList) {
        People people = new People();

        for (PersonInfo info : personInfoList) {
            PersonType person = new PersonType();
            person.setId(info.getId());
            person.setName(info.getFirstName() + " " + info.getLastName());
            person.setGender(GenderType.valueOf(info.getGender().toUpperCase()));
            people.getPerson().add(person);
        }

        for (PersonInfo info : personInfoList) {
            PersonType person = findPersonTypeById(people, info.getId());
            setSpouse(info, person, people);
            setParents(info, person, people);
            setChildren(info, person, people);
            setSiblings(info, person, people);
        }
        return people;
    }

    /**
     * Устанавливает связь супруга для указанного объекта PersonType.
     * Если айди супруга найдено, то устанавливается связь.
     *
     * @param currentPerson - текущий обрабатываемый объект PersonInfo
     * @param person        - объект PersonType, для которого устанавливается связь
     * @param allPeople     - общий список всех людей (People), необходим для поиска связанных объектов
     */

    private static void setSpouse(PersonInfo currentPerson, PersonType person, People allPeople) {
        if (currentPerson.getSpouseId() != null && findPersonTypeById(allPeople, currentPerson.getSpouseId()) != null) {
            SpouseType spouse = new SpouseType();
            spouse.setSpouse(findPersonTypeById(allPeople, currentPerson.getSpouseId()));
            person.setSpouse(spouse);
        }
    }

    /**
     * Устанавливает родителей для переданного PersonType.
     * Если айди какого-то из родителей найдено, то устанавливается связь.
     *
     * @param currentPerson - текущий обрабатываемый объект PersonInfo
     * @param person        - объект PersonType, для которого устанавливается связь
     * @param allPeople     - общий список всех людей (People), необходим для поиска связанных объектов
     */
    private static void setParents(PersonInfo currentPerson, PersonType person, People allPeople) {
        ParentsType parents = new ParentsType();
        boolean hasParent = false;

        if (currentPerson.getMotherId() != null) {
            PersonType motherPerson = findPersonTypeById(allPeople, currentPerson.getMotherId());
            if (motherPerson != null) {
                parents.setMother(motherPerson);
                hasParent = true;
            }
        }

        if (currentPerson.getFatherId() != null) {
            PersonType fatherPerson = findPersonTypeById(allPeople, currentPerson.getFatherId());
            if (fatherPerson != null) {
                parents.setFather(fatherPerson);
                hasParent = true;
            }
        }

        if (hasParent) {
            person.setParents(parents);
        }
    }


    /**
     * Устанавливает детей для переданного PersonType.
     * Если айди какого-то из детей найдено, то устанавливается связь.
     *
     * @param currentPerson - текущий обрабатываемый объект PersonInfo
     * @param person        - объект PersonType, для которого устанавливается связь
     * @param allPeople     - общий список всех людей (People), необходим для поиска связанных объектов
     */
    private static void setChildren(PersonInfo currentPerson, PersonType person, People allPeople) {
        ChildrenType children = new ChildrenType();
        List<JAXBElement<Object>> childrenList = new ArrayList<>();
        for (String childId : currentPerson.getChildrenId()) {
            if (findPersonTypeById(allPeople, childId) != null) {
                QName qName = new QName("", "child");
                JAXBElement<String> childElement = new JAXBElement<>(qName, String.class, childId);
                childrenList.add((JAXBElement<Object>) (Object) childElement);
            }
        }

        if (!childrenList.isEmpty()) {
            children.getChild().addAll(childrenList);
            person.setChildren(children);
        }
    }


    /**
     * Устанавливает братьев и сестёр для переданного PersonType.
     * Если айди какого-то из родителей найдено, то устанавливается связь.
     *
     * @param currentPerson - текущий обрабатываемый объект PersonInfo
     * @param person        - объект PersonType, для которого устанавливается связь
     * @param allPeople     - общий список всех людей (People), необходим для поиска связанных объектов
     */
    private static void setSiblings(PersonInfo currentPerson, PersonType person, People allPeople) {
        SiblingsType siblingsType = new SiblingsType();
        List<JAXBElement<Object>> siblingsListBrother = new ArrayList<>();
        List<JAXBElement<Object>> siblingsListSisters = new ArrayList<>();
        boolean containsSiblings = false;
        // Измените способ создания элементов списка детей
        for (String brothersId : currentPerson.getBrothersId()) {
            // Убедитесь, что ребенок с таким ID существует
            if (findPersonTypeById(allPeople, brothersId) != null) {
                // Создайте JAXBElement с ID в качестве содержимого
                QName qName = new QName("", "brothers");
                JAXBElement<String> brotherElement = new JAXBElement<>(qName, String.class, brothersId);
                siblingsListBrother.add((JAXBElement<Object>) (Object) brotherElement);
            }
        }
        for (String sistersId : currentPerson.getSistersId()) {
            // Убедитесь, что ребенок с таким ID существует
            if (findPersonTypeById(allPeople, sistersId) != null) {
                // Создайте JAXBElement с ID в качестве содержимого
                QName qName = new QName("", "sisters");
                JAXBElement<String> sisterElement = new JAXBElement<>(qName, String.class, sistersId);
                siblingsListSisters.add((JAXBElement<Object>) (Object) sisterElement);
            }
        }

        if (!siblingsListBrother.isEmpty()) {
            siblingsType.getBrothers().addAll(siblingsListBrother);
            containsSiblings = true;
        }
        if (!siblingsListSisters.isEmpty()) {
            siblingsType.getSisters().addAll(siblingsListSisters);
            containsSiblings = true;
        }
        if (containsSiblings) {
            person.setSiblings(siblingsType);
        }
    }

    /**
     * Ищет объект PersonType по идентификатору среди списка всех людей.
     * Возвращает найденный объект PersonType или null, если объект с таким ID не найден.
     *
     * @param people - список всех людей (People)
     * @param id     - идентификатор объекта PersonType для поиска
     * @return найденный объект PersonType или null
     */

    private static PersonType findPersonTypeById(People people, String id) {
        for (PersonType person : people.getPerson()) {
            if (person.getId().equals(id)) {
                return person;
            }
        }
        return null;
    }

    /**
     * Выполняет маршалинг структуры People в XML-файл.
     * Использует JAXB для преобразования Java-объектов в XML-документ согласно заданной схеме.
     *
     * @param people - структура People для маршалинга
     * @throws Exception в случае возникновения ошибок маршалинга
     */
    private static void marshalPeople(People people) throws Exception {
        System.out.println("Marshaling");
        JAXBContext context = JAXBContext.newInstance(People.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        File schemaFile = new File("C:/Users/Dom/Desktop/ХОИ/XML/task2/src/main/xsd/personInfo2.xsd");
        marshaller.setSchema(sf.newSchema(schemaFile));

        marshaller.marshal(people, new File("./output.xml"));
    }
}
