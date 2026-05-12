package communication;

import java.nio.ByteBuffer;
import java.util.LinkedList;

public class SegmentationHandler {
    public static byte[] desegment(LinkedList<byte[]> segments) {
        int totalSize = 0;
        for (byte[] seg: segments) { totalSize+= seg.length;}
        ByteBuffer bb = ByteBuffer.allocate(totalSize);
        for (byte[] seg: segments) { bb.put(seg); }
        return bb.array();
    }
}
