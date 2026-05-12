package shared.responses;

public class BooleanResponse implements Response{
    private boolean status;
    public BooleanResponse(){}

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }
}
