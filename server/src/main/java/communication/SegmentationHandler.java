package communication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.ArrayUtils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SegmentationHandler {
    private static final Logger logger = LogManager.getLogger("com.github.dustyace.lab6");
    private static final int segmentSize = 2048;
    LinkedList<Void> abc = new LinkedList<>();
    public static LinkedList<byte[]> segment(byte[] msg) {

        LinkedList<byte[]> ret = new LinkedList<>();
//        if (msg.length<segmentSize) {
//            ret.add(msg);
//            return ret;
//        }

        int start = 0;
        while (start < msg.length) {
            int end = Math.min(start+segmentSize, msg.length);
            ret.add(Arrays.copyOfRange(msg, start, end));
            start += segmentSize;
        }
        logger.info("Divided message into {} segments", ret.size());
        return ret;
    }
}
