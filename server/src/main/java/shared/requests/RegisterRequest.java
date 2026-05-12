package shared.requests;

import shared.responses.BooleanResponse;
import shared.responses.Response;

public class RegisterRequest extends LoginRequest{
    @Override
    public void processResponse(Response r) {
        if ( ( (BooleanResponse) r ).getStatus() ) {}
    }
}
