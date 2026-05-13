package shared.requests;

import shared.responses.CheckRouteExistsResponse;
import shared.responses.Response;

public class CheckRouteExistsRequest extends Request{
    private Long id;
    private static boolean result = false;
    private String username;
    public CheckRouteExistsRequest() {

    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public static boolean getResult() {
        return result;
    }

    @Override
    public void processResponse(Response r) {
        result = ((CheckRouteExistsResponse) r).result();
    }
}
