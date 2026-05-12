package shared.elements;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import userIO.InputHandler;
import userIO.InputValidator;

import java.io.Serializable;

/**
 * Coordinates class
 */
public class Coordinates implements Cloneable, Serializable {
    @JsonProperty
    @JacksonXmlProperty
    private Integer x; //Максимальное значение поля: 926
    @JsonProperty
    @JacksonXmlProperty
    private Float y;//Значение поля должно быть больше -974, Поле не может быть null

    public Coordinates() {
        if (Route.isLoading) {return;}

        x = InputHandler.intInput("x",
                new InputValidator<Integer>()
                        .nullable()
                        .upper(927)
        );

        y = InputHandler.floatInput("y",
                new InputValidator<Float>()
                        .lower(-974f)
        );
    }

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