package shared;

public class CheckRouteExistsRequest extends Request{
    private Long id;
    public CheckRouteExistsRequest() {}
    public CheckRouteExistsRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
