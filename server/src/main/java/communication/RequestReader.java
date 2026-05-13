package communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.requests.Request;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class RequestReader implements Runnable{
    private final Logger logger = LogManager.getLogger("com.github.dustyace.lab6");
    private final DatagramSocket ds;
    private  DatagramPacket dp;
    RequestReader(DatagramPacket dp, DatagramSocket ds) {
        this.dp = dp;
        this.ds = ds;
    }

    @Override
    public void run() {
        logger.info("Opened a new thread at {}", ds.getLocalPort());
        readRequest(dp.getData());
        try {
            ds.setSoTimeout(60_000);
            while (true) {
                ds.receive(dp);
                readRequest(dp.getData());
            }
        } catch (SocketTimeoutException e) {
            logger.info("No message from client for the past minute. Closing port {}.", ds.getLocalPort());
            return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readRequest(byte[] request) {
        try {
            String req = new String(request, StandardCharsets.UTF_8);
            ObjectMapper om = JsonMapper.builder()
                    .findAndAddModules()
                    .build();
            om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            Request oc = om.readValue(req, Request.class); //this throws jpe

            new Thread( new RequestProcessor(oc, dp, ds)).start();
        } catch (JsonProcessingException e) {
            logger.error("Something went wrong deserializing a request!");
            logger.error(e.getMessage());
        }
    }
}
