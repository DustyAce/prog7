package shared.requests;

import shared.responses.BooleanResponse;
import shared.responses.Response;

public class LoginRequest extends Request{
    protected String username;
    protected String password;
    public LoginRequest() {
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    @Override
    public void processResponse(Response r) {
    }
}
