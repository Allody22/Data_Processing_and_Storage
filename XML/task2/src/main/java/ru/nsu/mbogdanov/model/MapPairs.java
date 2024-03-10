package ru.nsu.mbogdanov.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;

@Data
@AllArgsConstructor
public class MapPairs {
    private HashMap<String, PersonInfo> idsPairs;

    private ArrayList<PersonInfo> noIds;
}
