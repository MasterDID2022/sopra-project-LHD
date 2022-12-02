package fr.univtln.lhd.entitys.slots;

public class Classroom {

    private final String name;
    private final String buildingName; //? might not be a string in database ?

    public Classroom(String name, String buildingName) {
        this.name = name;
        this.buildingName = buildingName;
    }

    public String getName() { return name; }

    public String getBuildingName() { return buildingName; }
}
