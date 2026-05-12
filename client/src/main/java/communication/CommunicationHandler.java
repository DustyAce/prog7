package communication;

import shared.requests.CheckRouteExistsRequest;
import shared.requests.Request;
import shared.responses.CommandResponse;
import shared.commands.CommandEnum;
import shared.requests.CommandRequest;
import shared.elements.Route;
import shared.responses.Response;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import static communication.SerializationHandler.deserializeResponse;
import static communication.SerializationHandler.serializeRequest;

public class CommunicationHandler {
    static HashSet<CommandEnum> collectionPrinters = new HashSet<>();
    static {
        collectionPrinters.add(CommandEnum.SHOW);
        collectionPrinters.add(CommandEnum.FILTER_NAME);
        collectionPrinters.add(CommandEnum.PRINT_ASCENDING);
    }

    static int port = 1430;
    static InetAddress host;
    static SocketAddress addr;
    static DatagramChannel dc;
    static ByteBuffer buf;
    static ByteBuffer segment_buffer;
    static byte[] message;
    static byte[] response;
    static Selector selector;
    static LinkedList<byte[]> responseBuffer = new LinkedList<>();
    static byte[] desegmentedResponse;

    public static void request(Request req) {
        try {
            establishConnection(req);
            if (!sendRequest()) {return;}
            receiveResponse();
            processResponse(req);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void establishConnection(Request req) throws IOException {
        host = InetAddress.getLocalHost();
        addr = new InetSocketAddress(host, port);
        dc = DatagramChannel.open();
        dc.configureBlocking(false);
        selector = Selector.open();
        dc.register(selector, SelectionKey.OP_READ);
        message = serializeRequest(req);
        buf = ByteBuffer.wrap(message);
        response = new byte[2048];
        segment_buffer = ByteBuffer.wrap(response);
    }
    private static boolean sendRequest() throws IOException {
        dc.send(buf, addr);
        int attempts = 0;
        while (true) {
            int i = selector.select(5_000);
            if (i == 0) {
                if (++attempts > 3) { System.out.println("Server not responding :("); return false; }
                System.out.printf("Couldn't reach server, attempt %s...\n", attempts+1);
                buf = ByteBuffer.wrap(message);
                dc.send(buf, addr);
            }  else {break;}
        }
        return true;
    }

    public static void request(CommandRequest req) {
        try {
            establishConnection(req);
            if (!sendRequest()) {return;}
            receiveResponse();
            processResponse(req);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //recieves and desegments a response, storing the result in desegmentedResponse
    //bufsize should be the same on client and server
    private static final int bufsize = 2048;
    private static void receiveResponse() throws IOException {
        byte[] b = new byte[bufsize];
        responseBuffer.clear();
        dc.register(selector, SelectionKey.OP_WRITE);
        do { //fuckass method doesn't work with 10k elements, need to figue out blocking
            int i = selector.select(1000);
            Arrays.fill(b, (byte) -1);
            segment_buffer = ByteBuffer.wrap(b);
            dc.receive(segment_buffer);
            responseBuffer.add(segment_buffer.array());
        } while (b[bufsize-1] != -1);
        responseBuffer.removeLast();
        desegmentedResponse = SegmentationHandler.desegment(responseBuffer);


    }

    public static void processResponse(Request req) throws IOException{
        try {
            Response r = SerializationHandler.deserializeResponse(desegmentedResponse);
            req.processResponse(r);
        } finally {
            dc.close();
        }

    }
}
