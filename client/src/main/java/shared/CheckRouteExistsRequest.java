package shared;

public class CheckRouteExistsRequest extends Request{
    private final Long id;
    public CheckRouteExistsRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
