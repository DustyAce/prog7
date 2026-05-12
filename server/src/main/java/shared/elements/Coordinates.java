package shared.elements;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * elements.Coordinates class
 */
public class Coordinates implements Cloneable, Serializable {
    @JsonProperty
    private Integer x; //Максимальное значение поля: 926
    @JsonProperty
    private Float y;//Значение поля должно быть больше -974, Поле не может быть null

    public Coordinates() {}

    private Coordinates(Coordinates c) {
        this.x = c.x;
        this.y = c.y;
    }

    boolean validate() {
        return ( (x != null? x<927 : true)
                && y != null
                && y > -974);
    }

    @Override
    protected Coordinates clone()  {
        return new Coordinates(this);
    }

    @Override
    public String toString() {
        return String.format("x: %d y: %f", x, y);
    }
}