package shared.elements;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import handlers.OutputHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    @JsonProperty
    private Location from; //[+] Поле может быть null
    @JsonProperty
    private Location to;//[+] Поле может быть null
    private Long distance; //[+] Поле не может быть null, Значение поля должно быть больше 1

    public Route() {}

    private Route(Route r) {
        this.id = r.id;
        this.name = r.name;
        this.coordinates = r.coordinates == null ? null : r.coordinates.clone();
        this.creationDate = r.creationDate;
        this.from = r.from == null? null : r.from.clone();
        this.to = r.to == null? null : r.to.clone();
        this.distance = r.distance;
    }

    public Route(ResultSet rs) {
        try  {
            this.id = rs.getLong("id");
            this.name = rs.getString("name");
            this.coordinates = new Coordinates(rs);
            this.creationDate = rs.getTimestamp("creationdate").toLocalDateTime();
            if (rs.getInt("from") != 0) {
                this.from = new Location(
                        rs.getLong("from.x"),
                        rs.getFloat("from.y"),
                        rs.getFloat("from.z"),
                        rs.getString("from.name")
                );
            }

            if (rs.getInt("to") != 0) {
                this.to = new Location(
                        rs.getLong("to.x"),
                        rs.getFloat("to.y"),
                        rs.getFloat("to.z"),
                        rs.getString("to.name")
                );
            }
            this.distance = rs.getLong("distance");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
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

    public void setId(Long id) {
        this.id = id;
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