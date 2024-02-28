package ru.nsu.mbogdanov.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;


@Data
public class PersonInfo {

    private String id;
    private String firstName;
    private String lastName;
    private String gender;

    // parents
    private Set<String> parentsId = new HashSet<>();
    private String motherId;
    private String fatherId;
    private Set<String> parentsName = new HashSet<>();
    private String motherName;
    private String fatherName;

    // children
    private Set<String> childrenId = new HashSet<>();
    private Set<String> sonsId = new HashSet<>();
    private Set<String> daughtersId = new HashSet<>();
    private Set<String> childrenName = new HashSet<>();
    private Set<String> sonsName = new HashSet<>();
    private Set<String> daughtersName = new HashSet<>();

    // siblings
    private Set<String> siblingsId = new HashSet<>();
    private Set<String> brothersId = new HashSet<>();
    private Set<String> sistersId = new HashSet<>();
    private Set<String> siblingsName = new HashSet<>();
    private Set<String> brothersName = new HashSet<>();
    private Set<String> sistersName = new HashSet<>();

    // spouse
    private String spouseId;
    private String husbandId;
    private String wifeId;
    private String spouseName;
    private String husbandName;
    private String wifeName;

    private Integer childrenCount = null;
    private Integer siblingsCount = null;


    public void addSonsId(String id) {
        sonsId.add(id);
    }

    public void addDaughterId(String id) {
        daughtersId.add(id);
    }

    public void addChildrenId(String id) {
        childrenId.add(id);
    }

    public void addDaughtersName(String name) {
        daughtersName.add(name);
    }

    public void addSistersId(String id) {
        sistersId.add(id);
    }

    public void addBrothersId(String id) {
        brothersId.add(id);
    }

    public void addBrothersName(String name) {
        brothersName.add(name);
    }

    public void addSistersName(String name) {
        sistersName.add(name);
    }

    public void addAllSiblings(Set<String> ids) {
        siblingsId.addAll(ids);
    }


    public void addAllBrothersName(Set<String> names) {
        brothersName.addAll(names);
    }

    public void addAllBrothersId(Set<String> id) {
        brothersId.addAll(id);
    }

    public void addAllSistersName(Set<String> names) {
        sistersName.addAll(names);
    }


    public void addAllSisterId(Set<String> ids) {
        sistersId.addAll(ids);
    }

    public void addSiblingsId(String id) {
        siblingsId.add(id);
    }

    public void addSonsName(String name) {
        sonsName.add(name);
    }

    public void addAllSonsName(Set<String> names) {
        sonsName.addAll(names);
    }

    public void addAllDaughtersName(Set<String> names) {
        daughtersName.addAll(names);
    }

    public void addAllSonsId(Set<String> ids) {
        sonsId.addAll(ids);
    }

    public void addAllDaughtersIds(Set<String> ids) {
        daughtersId.addAll(ids);
    }

    public void addChildrenName(String name) {
        childrenName.add(name);
    }

    public void addAllChildrenId(Set<String> ids) {
        childrenId.addAll(ids);
    }

    public void addAllChildrenNames(Set<String> names) {
        childrenName.addAll(names);
    }


    public void addParentId(String id) {
        parentsId.add(id);
    }

    public void addAllParentId(Set<String> ids) {
        parentsId.addAll(ids);
    }

    public void addAllParentsName(Set<String> names) {
        parentsName.addAll(names);
    }

    public void addAllSiblingsName(Set<String> names) {
        siblingsName.addAll(names);
    }

    public PersonInfo extractingOfAllAvailableInformation(PersonInfo theSamePerson, PersonInfo anotherPerson) {
        if (theSamePerson == null && anotherPerson == null) {
            return this;
        }
        if (theSamePerson == null) {
            return anotherPerson;
        } else if (anotherPerson == null) {
            return theSamePerson;
        } else {
            PersonInfo newPerson = new PersonInfo();
            if (theSamePerson.getId() == null && anotherPerson.getId() == null) {
                newPerson.setId(null);
            } else if (theSamePerson.getId() == null) {
                newPerson.setId(anotherPerson.getId());
            } else {
                newPerson.setId(theSamePerson.getId());
            }
            if (theSamePerson.getFirstName() == null && anotherPerson.getFirstName() == null) {
                newPerson.setFirstName(null);
            } else if (theSamePerson.getFirstName() == null) {
                newPerson.setFirstName(anotherPerson.getFirstName());
            } else {
                newPerson.setFirstName(theSamePerson.getFirstName());
            }
            if (theSamePerson.getLastName() == null && anotherPerson.getLastName() == null) {
                newPerson.setLastName(null);
            } else if (theSamePerson.getLastName() == null) {
                newPerson.setLastName(anotherPerson.getLastName());
            } else {
                newPerson.setLastName(theSamePerson.getLastName());
            }

            if (theSamePerson.getGender() == null && anotherPerson.getGender() == null) {
                newPerson.setGender(null);
            } else if (theSamePerson.getGender() == null) {
                newPerson.setGender(anotherPerson.getGender());
            } else {
                newPerson.setGender(theSamePerson.getGender());
            }

            newPerson.addAllParentId(anotherPerson.getParentsId());
            newPerson.addAllParentId(theSamePerson.getParentsId());

            if (theSamePerson.getLastName() == null && anotherPerson.getLastName() == null) {
                newPerson.setLastName(null);
            } else if (theSamePerson.getLastName() == null) {
                newPerson.setLastName(anotherPerson.getLastName());
            } else {
                newPerson.setLastName(theSamePerson.getLastName());
            }

            //parents
            newPerson.addAllParentId(anotherPerson.getParentsId());
            newPerson.addAllParentId(theSamePerson.getParentsId());
            if (theSamePerson.getMotherId() == null && anotherPerson.getMotherId() == null) {
                newPerson.setMotherId(null);
            } else if (theSamePerson.getMotherId() == null) {
                newPerson.setMotherId(anotherPerson.getMotherId());
            } else {
                newPerson.setMotherId(theSamePerson.getMotherId());
            }

            if (theSamePerson.getFatherId() == null && anotherPerson.getFatherId() == null) {
                newPerson.setFatherId(null);
            } else if (theSamePerson.getFatherId() == null) {
                newPerson.setFatherId(anotherPerson.getFatherId());
            } else {
                newPerson.setFatherId(theSamePerson.getFatherId());
            }

            if (theSamePerson.getMotherName() == null && anotherPerson.getMotherName() == null) {
                newPerson.setMotherName(null);
            } else if (theSamePerson.getMotherName() == null) {
                newPerson.setMotherName(anotherPerson.getMotherName());
            } else {
                newPerson.setMotherName(theSamePerson.getMotherName());
            }

            if (theSamePerson.getFatherName() == null && anotherPerson.getFatherName() == null) {
                newPerson.setFatherName(null);
            } else if (theSamePerson.getFatherName() == null) {
                newPerson.setFatherName(anotherPerson.getFatherName());
            } else {
                newPerson.setFatherName(theSamePerson.getFatherName());
            }

            newPerson.addAllParentsName(theSamePerson.getParentsName());
            newPerson.addAllParentsName(anotherPerson.getParentsName());

            //children
            newPerson.addAllChildrenId(theSamePerson.getChildrenId());
            newPerson.addAllChildrenId(anotherPerson.getChildrenId());

            newPerson.addAllSonsName(theSamePerson.getSonsName());
            newPerson.addAllSonsName(anotherPerson.getSonsName());

            newPerson.addAllDaughtersName(theSamePerson.getDaughtersName());
            newPerson.addAllDaughtersName(anotherPerson.getDaughtersName());

            newPerson.addAllSonsId(theSamePerson.getSonsId());
            newPerson.addAllSonsId(anotherPerson.getSonsId());

            newPerson.addAllDaughtersIds(theSamePerson.getDaughtersId());
            newPerson.addAllDaughtersIds(anotherPerson.getDaughtersId());

            newPerson.addAllChildrenNames(theSamePerson.getChildrenName());
            newPerson.addAllChildrenNames(anotherPerson.getChildrenName());

            //siblings
            newPerson.addAllSiblings(theSamePerson.getSiblingsId());
            newPerson.addAllSiblings(anotherPerson.getSiblingsId());

            newPerson.addAllSiblingsName(theSamePerson.getSiblingsName());
            newPerson.addAllSiblingsName(anotherPerson.getSiblingsName());

            newPerson.addAllBrothersId(theSamePerson.getBrothersId());
            newPerson.addAllBrothersId(anotherPerson.getBrothersId());

            newPerson.addAllBrothersName(theSamePerson.getBrothersName());
            newPerson.addAllBrothersName(anotherPerson.getBrothersName());

            newPerson.addAllSisterId(theSamePerson.getSistersId());
            newPerson.addAllSisterId(anotherPerson.getSistersId());

            newPerson.addAllSistersName(theSamePerson.getSistersName());
            newPerson.addAllSistersName(anotherPerson.getSistersName());

            //spouse
            if (theSamePerson.getSpouseId() == null && anotherPerson.getSpouseId() == null) {
                newPerson.setSpouseId(null);
            } else if (theSamePerson.getSpouseId() == null) {
                newPerson.setSpouseId(anotherPerson.getSpouseId());
            } else {
                newPerson.setSpouseId(theSamePerson.getSpouseId());
            }

            if (theSamePerson.getHusbandId() == null && anotherPerson.getHusbandId() == null) {
                newPerson.setHusbandId(null);
            } else if (theSamePerson.getHusbandId() == null) {
                newPerson.setHusbandId(anotherPerson.getHusbandId());
            } else {
                newPerson.setHusbandId(theSamePerson.getHusbandId());
            }

            if (theSamePerson.getWifeId() == null && anotherPerson.getWifeId() == null) {
                newPerson.setWifeId(null);
            } else if (theSamePerson.getWifeId() == null) {
                newPerson.setWifeId(anotherPerson.getWifeId());
            } else {
                newPerson.setWifeId(theSamePerson.getWifeId());
            }

            if (theSamePerson.getSpouseName() == null && anotherPerson.getSpouseName() == null) {
                newPerson.setSpouseName(null);
            } else if (theSamePerson.getSpouseName() == null) {
                newPerson.setSpouseName(anotherPerson.getSpouseName());
            } else {
                newPerson.setSpouseName(theSamePerson.getSpouseName());
            }

            if (theSamePerson.getHusbandName() == null && anotherPerson.getHusbandName() == null) {
                newPerson.setHusbandName(null);
            } else if (theSamePerson.getHusbandName() == null) {
                newPerson.setHusbandName(anotherPerson.getHusbandName());
            } else {
                newPerson.setHusbandName(theSamePerson.getHusbandName());
            }

            if (theSamePerson.getWifeName() == null && anotherPerson.getWifeName() == null) {
                newPerson.setWifeName(null);
            } else if (theSamePerson.getWifeName() == null) {
                newPerson.setWifeName(anotherPerson.getWifeName());
            } else {
                newPerson.setWifeName(theSamePerson.getWifeName());
            }

            //assertions numbers
            if (theSamePerson.getChildrenCount() == null && anotherPerson.getChildrenCount() == null) {
                newPerson.setChildrenCount(null);
            } else if (theSamePerson.getChildrenCount() == null) {
                newPerson.setChildrenCount(anotherPerson.getChildrenCount());
            } else {
                newPerson.setChildrenCount(theSamePerson.getChildrenCount());
            }
            if (theSamePerson.getSiblingsCount() == null && anotherPerson.getSiblingsCount() == null) {
                newPerson.setSiblingsCount(null);
            } else if (theSamePerson.getSiblingsCount() == null) {
                newPerson.setSiblingsCount(anotherPerson.getSiblingsCount());
            } else {
                newPerson.setSiblingsCount(theSamePerson.getSiblingsCount());
            }
            return newPerson;
        }
    }
}