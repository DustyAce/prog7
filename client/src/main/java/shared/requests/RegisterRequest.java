package shared.requests;

import communication.UserStatus;
import shared.responses.BooleanResponse;
import shared.responses.Response;

public class RegisterRequest extends LoginRequest{
    @Override
    public void processResponse(Response r) {
        if ( ( (BooleanResponse) r ).getStatus() ) {
            UserStatus.setCredentials(username,password);
            System.out.printf("Successfully registered in! Welcome, %s.\n", username);
        } else {
            System.out.println("Could not register, a user with such name already exists!");
        }
    }
}
