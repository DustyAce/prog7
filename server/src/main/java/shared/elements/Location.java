package shared.elements;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * elements.Location class
 */
public class Location implements Cloneable, Serializable {
    @JsonProperty
    private Long x;
    @JsonProperty
    private Float y; //Поле не может быть null
    @JsonProperty
    private Float z; //Поле не может быть null
    @JsonProperty
    private String name = ""; //Строка не может быть пустой, Поле не может быть null

    Location() {}

    private Location(Location l){
        this.x = l.x;
        this.y = l.y;
        this.z = l.z;
        this.name = l.name;
    }

    boolean validate() {
        return (y != null
                && z != null
                && name != null
                && !name.isBlank());
    }

    @Override
    protected Location clone() {
        return new Location(this);
    }

    @Override
    public String toString() {
        return String.format("%s: (x: %s, y: %s, z: %s)", name, x, y, z);
    }
}