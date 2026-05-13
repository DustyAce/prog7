package communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import shared.requests.Request;
import shared.responses.Response;

import java.nio.charset.StandardCharsets;

public class SerializationHandler  {

    public static Request deserialize_request(byte[] arr) throws JsonProcessingException{
        String req = new String(arr, StandardCharsets.UTF_8);
        ObjectMapper om = JsonMapper.builder()
                .findAndAddModules()
                .build();
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            Request oc = om.readValue(req, Request.class); //this throws jpe
            return oc;
    }

    public static byte[] serialize_response(Response r) {
        ObjectMapper om = JsonMapper.builder()
                .findAndAddModules()
                .build();
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            return om.writeValueAsString(r).getBytes();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
