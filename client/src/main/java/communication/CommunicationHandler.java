package communication;

import shared.CheckRouteExistsRequest;
import shared.Request;
import shared.Response;
import shared.commands.CommandEnum;
import shared.commands.CommandRequest;
import shared.elements.Route;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
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

    public static boolean checkRouteExistsRequest(Long id) {
        try {
            Request req = new CheckRouteExistsRequest(id);
            establishConnection(req);
            sendRequest();
            return receiveCheckRouteResponse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private static boolean receiveCheckRouteResponse() throws IOException {
         ByteBuffer bb = ByteBuffer.allocate(1);
         dc.receive(bb);
         return bb.array()[0] == 1;
    }

    private static void receiveResponse() throws IOException {
        responseBuffer.clear();
        dc.register(selector, SelectionKey.OP_WRITE);
        do { //fuckass method doesn't work with 10k elements, need to figue out blocking
            int i = selector.select();
            segment_buffer = ByteBuffer.allocate(2048);
            dc.receive(segment_buffer);
            responseBuffer.add(segment_buffer.array());
        } while (segment_buffer.array()[0]!=-1);
        responseBuffer.removeLast();
        desegmentedResponse = SegmentationHandler.desegment(responseBuffer);
    }

    private static void processResponse(CommandRequest req) throws IOException {
        try {

            Route.isLoading = true;
            Response resp = deserializeResponse(desegmentedResponse);

            if (!resp.routes().isEmpty() && req.getCommand() != CommandEnum.PSZH)  {
                resp.routes().forEach(System.out::println);
            }

            if (!resp.output().isBlank())
            { System.out.println(resp.output().strip()); }

            Route.updateInstanceCounter( resp.routes().stream()
                    .mapToLong(Route::getId)
                    .max()
                    .orElse(-1) + 1);

            LocalCollectionHandler.updateExistingRoutes( resp.routes().stream()
                                                                .map(Route::getId)
                                                                .toList() );

        } finally {
            Route.isLoading = false;
            dc.close();
        }
    }
}
