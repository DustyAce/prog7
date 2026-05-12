package shared.requests;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import shared.responses.Response;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CommandRequest.class),
        @JsonSubTypes.Type(value = CheckRouteExistsRequest.class),
        @JsonSubTypes.Type(value = RegisterRequest.class),
        @JsonSubTypes.Type(value = LoginRequest.class) }

)
public abstract class Request {
    public abstract void processResponse(Response r);
}
