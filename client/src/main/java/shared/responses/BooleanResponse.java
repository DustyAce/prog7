package shared.responses;

public class BooleanResponse implements Response{
    private boolean status;
    public BooleanResponse(){}

    public boolean getStatus() {
        return status;
    }
}
