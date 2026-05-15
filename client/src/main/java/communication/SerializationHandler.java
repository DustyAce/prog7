package communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import shared.requests.Request;
import shared.responses.CommandResponse;
import shared.responses.Response;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SerializationHandler {

    public static byte[] serializeRequest(Request req) {
        ObjectMapper om = JsonMapper.builder()
                .findAndAddModules()
                .build();
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            return om.writeValueAsString(req).getBytes();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public static Response deserializeResponse(byte[] arr) throws JsonProcessingException{
        String req = new String(arr, StandardCharsets.UTF_8);
        req = req.replaceAll("\0", "");
        ObjectMapper om = JsonMapper.builder()
                .findAndAddModules()
                .build();
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        Response r = om.readValue(req, Response.class);
        return r;
    }
}
