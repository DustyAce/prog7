package shared.requests;

import communication.UserStatus;
import shared.responses.BooleanResponse;
import shared.responses.Response;
import userIO.InputHandler;

public class LoginRequest extends Request{
    protected String username;
    protected String password;
    public LoginRequest() {
//        if (UserStatus.isLoggedIn()) return;
//        optional - if forbidding user from re-logging in, session may be locked if user is removed from db
        username = InputHandler.stringInput("username");
        password = InputHandler.stringInput("password");
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    @Override
    public void processResponse(Response r) {
        if ( ( (BooleanResponse) r ).getStatus() ) {
            UserStatus.setCredentials(username,password);
            System.out.printf("Successfully logged in! Welcome, %s.\n", username);
        } else {
            System.out.println("Bad username/password :(");
        }
    }
}
