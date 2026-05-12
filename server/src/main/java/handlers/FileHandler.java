package handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.elements.Route;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Handler for file IO
 */
public class FileHandler {
    private static final Logger logger = LogManager.getLogger("com.github.dustyace.lab6");

    static private String saveLocation = "save";
    static {
        String tmp = System.getenv("PROG_5_SAVE");
        if (tmp!=null) {saveLocation = tmp;}
        else {logger.warn("Could not get save location from environment. Using 'save' as default");}
    }

    /**
     * Saves current collection to a file
     */
    public static void save(HashSet<Route> routes) {
        XmlMapper xm = XmlMapper.builder()
                .findAndAddModules()
                .build();
        xm.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            File f = new File(saveLocation);
            if (f.exists()) { f.delete(); }
            File parent = f.getParentFile();
            if (parent != null) {parent.mkdirs();}
            f.createNewFile();
            StringBuilder out = new StringBuilder();
            for (Route r: routes) {
                out.append(xm.writeValueAsString(r));
                out.append("\n");
            }
            PrintWriter pw = new PrintWriter(f);
            pw.print(out);
            pw.close();
            logger.info("Successfully saved!");
        } catch (JsonProcessingException e) {
            logger.error("uhh bad file somehow");
        } catch (FileNotFoundException e) {
            logger.warn("save file not found");
        } catch (IOException e) {
            logger.error("IO fail");
        }
    }

    /**
     * Loads a current collection from the save file
     */
    public static void load() {
        Scanner sc;
        HashSet<Route> out = new HashSet<>();
        XmlMapper xm = XmlMapper.builder()
                    .findAndAddModules()
                    .build();
        xm.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        xm.configure(FromXmlParser.Feature.EMPTY_ELEMENT_AS_NULL, true);
        try {
            sc = new Scanner(new File(saveLocation));
        } catch (FileNotFoundException e) {
            logger.warn("No save file found");
            return;
        }

        Route.isLoading = true;

        Long maxId = 0L;
        HashSet<Long> existingIds = new HashSet<>();
        try {
            while (sc.hasNext()) {
                Route r = xm.readValue(sc.nextLine(), Route.class);
                if (!existingIds.contains(r.getId()) && r.validate()) {
                    out.add(r);
                    existingIds.add(r.getId());
                    Long tmpId = r.getId();
                    maxId = (tmpId > maxId) ? tmpId : maxId;
                } else {
                    logger.warn("Invalid route '" + r + "' ignored");
                }

            }
            CollectionHandler.setRoutes(out);
            Route.updateInstanceCounter(maxId+1);
            logger.info("Successfully loaded {} objects", out.size());
            if (out.isEmpty()) {logger.warn("save file was empty.");}
        } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
                logger.error("Save file corrupt!");
        } finally { Route.isLoading = false; }



    }

}
