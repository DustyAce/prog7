package shared.requests;

import shared.responses.BooleanResponse;
import shared.responses.CheckRouteExistsResponse;
import shared.responses.Response;

public class CheckRouteExistsRequest extends Request{
    private final Long id;
    private static boolean result = false;
    private final String username;
    public CheckRouteExistsRequest(Long id, String username) {
        this.id = id;
        this.username=username;
    }

    public Long getId() {
        return id;
    }

    public static boolean getResult() {
        return result;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void processResponse(Response r) {
        result = ((BooleanResponse) r).getStatus();
    }
}
