package shared.elements;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import userIO.InputHandler;
import userIO.InputValidator;

import java.io.Serializable;

public class Location implements Cloneable, Serializable {
    @JsonProperty
    @JacksonXmlProperty
    private Long x;
    @JsonProperty
    @JacksonXmlProperty
    private Float y; //Поле не может быть null
    @JsonProperty
    @JacksonXmlProperty
    private Float z; //Поле не может быть null
    @JsonProperty
    @JacksonXmlProperty
    private String name = "";

    Location() {
        if (Route.isLoading) {return;}
        x = InputHandler.longInput("Location x", new InputValidator<Long>()
                .nullable());

        y = InputHandler.floatInput("Location y");
        z = InputHandler.floatInput("Location z");
        name = InputHandler.stringInput("Location name");
    }

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