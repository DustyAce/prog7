package shared.elements;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import userIO.InputHandler;
import userIO.InputValidator;

import java.time.LocalDateTime;

/**
 * Route class - main class of the collection
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Route implements Comparable<Route>, Cloneable{
    public static boolean isLoading = false;

    private static Long instanceCounter = 0L;
    private Long id; //[+] Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name;
    //[+] Поле не может быть null, Строка не может быть пустой
    @JsonProperty
    @JacksonXmlProperty
    private Coordinates coordinates; //[+]
    @JsonInclude
    @JsonProperty
    @JacksonXmlProperty
    private LocalDateTime creationDate; //[+] Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @JsonInclude
    @JsonProperty
    @JacksonXmlProperty
    private Location from; //[+] Поле может быть null
    @JsonInclude
    @JsonProperty
    @JacksonXmlProperty
    private Location to;//[+] Поле может быть null
    private Long distance;


    public Route() {
        if (isLoading) {return;}

        this.id = Route.instanceCounter++;
        this.creationDate = LocalDateTime.now();

        this.name = InputHandler.stringInput("Route name");

        System.out.println("Coordinates:");
        this.coordinates = new Coordinates();

        if (InputHandler.ynPrompt("Add 'from' Location?")) { this.from = new Location(); }
        if (InputHandler.ynPrompt("Add 'to' Location?")) { this.from = new Location(); }

        this.distance = InputHandler.longInput("distance",
                new InputValidator<Long>()
                        .lower(1L)
        );
    }

    private Route(Route r) {
        this.id = r.id;
        this.name = r.name;
        this.coordinates = r.coordinates == null ? null : r.coordinates.clone();
        this.creationDate = r.creationDate;
        this.from = r.from == null? null : r.from.clone();
        this.to = r.to == null? null : r.to.clone();
        this.distance = r.distance;
    }

    public boolean validate() {
        boolean r = (id != null
                && !name.isBlank()
                && coordinates != null
                && coordinates.validate()
                && creationDate != null
                && (from == null || from.validate())
                && (to == null || to.validate())
                && (distance != null && distance > 1)
                );
        return r;
    }

    /**
     * @return Route's id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return Route's distance
     */
    public Long getDistance() {
        return distance;
    }

    /**
     * @return Route's name
     */
    public String getName() {
        return name;
    }

    /**
     * Prompts user to update one of Route object's fields
     */
    public Route update(Route r) {
        this.id = r.id;
        this.name = r.name;
        this.coordinates = r.coordinates == null ? null : r.coordinates.clone();
        this.creationDate = r.creationDate;
        this.from = r.from == null? null : r.from.clone();
        this.to = r.to == null? null : r.to.clone();
        this.distance = r.distance;
        return this;
    }

    /**
     * Set Route instanceCounter to id
     * @param id new instanceCounter value
     */
    public static void updateInstanceCounter(Long id) {
        instanceCounter = id;
    }


    @Override
    public int compareTo(Route o) {
        return Long.compare(this.distance, o.distance);
        //can't really do this via IDs
        //because add_if_min becomes pointless
        //(well technically I could but it would require making routes with HIGHER ids less)
    }

    @Override
    public String toString() {
        return String.format("%s: %s (%s)", this.id, this.name, this.distance);
    }

    /**
     * @return A more detailed String representation of Route object
     */
    public String more() {
        return String.format("id: %s\n name: '%s'\n Coordinates: %s\n from: %s\n to: %s\n distance:%s\n created:%s",
                this.id, this.name,
                this.coordinates,
                this.from,
                this.to,
                this.distance,
                this.creationDate);
    }

    @Override
    public Route clone()  {
        return new Route(this);
    }
}