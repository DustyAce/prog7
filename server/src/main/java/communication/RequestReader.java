package communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.requests.Request;

import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;

public class RequestReader implements Runnable{
    private final Logger logger = LogManager.getLogger("com.github.dustyace.lab6");
    private final byte[] request;
    private final DatagramPacket dp;
    RequestReader(byte[] r, DatagramPacket dp) {
        this.request=r;
        this.dp = dp;
    }

    @Override
    public void run() {
        try {
            String req = new String(request, StandardCharsets.UTF_8);
            ObjectMapper om = JsonMapper.builder()
                    .findAndAddModules()
                    .build();
            om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            Request oc = om.readValue(req, Request.class); //this throws jpe

            new Thread( new RequestProcessor(oc, dp)).start();
        } catch (JsonProcessingException e) {
            logger.error("Something went wrong deserializing a request!");
            logger.error(e.getMessage());
        }
    }
}
