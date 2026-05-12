package shared.responses;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BooleanResponse.class),
        @JsonSubTypes.Type(value = CheckRouteExistsResponse.class),
        @JsonSubTypes.Type(value = CommandResponse.class) }
)
public interface Response {
}
