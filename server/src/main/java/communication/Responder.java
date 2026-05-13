package communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.requests.Request;
import shared.responses.Response;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.LinkedList;

public class Responder implements Runnable{
    private final Logger logger = LogManager.getLogger("com.github.dustyace.lab6");
    private final DatagramPacket dp;

    private final Response req;
    Responder(Response req, DatagramPacket dp) {
        this.req=req;
        this.dp=dp;
    }

    @Override
    public void run() {
        ObjectMapper om = JsonMapper.builder()
                .findAndAddModules()
                .build();
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        byte[] message;
        try {
            message = om.writeValueAsString(req).getBytes();
        } catch (JsonProcessingException e) {
            logger.error("Something went wrong serializing the response!");
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        LinkedList<byte[]> segments = SegmentationHandler.segment(message);
        try {
            for (byte[] segment : segments) {
                sendResponse(segment);
            }
        } catch (IOException e) {
            logger.error("Something went wrong sending the response!");
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private void sendResponse(byte[] message) throws IOException {
        DatagramPacket dpr = new DatagramPacket(message, message.length, dp.getAddress(), dp.getPort());
        CommunicationHandler.getDs().send(dpr);
    }
}
