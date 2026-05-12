package shared.requests;

import shared.responses.CheckRouteExistsResponse;
import shared.responses.Response;

public class CheckRouteExistsRequest extends Request{
    private final Long id;
    private static boolean result = false;
    public CheckRouteExistsRequest(Long id) {
        this.id = id;
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
