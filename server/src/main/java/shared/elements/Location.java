package shared.elements;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    public Location(Long x, Float y, Float z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    boolean validate() {
        return (y != null
                && z != null
                && name != null
                && !name.isBlank());
    }

    public void setValuesInStatement(PreparedStatement ps) throws SQLException {
        ps.clearParameters();
        ps.setObject(1, x);
        ps.setFloat(2, y);
        ps.setFloat(3, z);
        ps.setString(4, name);
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