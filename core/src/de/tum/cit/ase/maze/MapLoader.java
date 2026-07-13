package de.tum.cit.ase.maze;

import com.badlogic.gdx.files.FileHandle;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
/**
 * The MapLoader class provides methods for loading and processing maps.
 * Catches IOException in case of file not found, permissions issues, etc.
 */
public class MapLoader {
    /**
     * Loads a map from a specified file.
     *
     * @param mapFile The file handle for the map file.
     * @return A map containing key-value pairs loaded from the file.
     */
    public static Map<String, String> loadMap(FileHandle mapFile) {
        Map<String, String> map = new HashMap<>();

        try {
            Properties properties = new Properties();
            properties.load(mapFile.read());
            //Map with key values pairs from the properties file
            for (String key : properties.stringPropertyNames()) {
                String value = properties.getProperty(key);
                map.put(key, value);
            }
        //Catch Exception for streams, files and directories
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * Loads a map from a Propertis object
     *
     * @param properties object containing key-value pairs
     * @return map with key-pairs from Properties Object
     */
    public static Map<String, String> loadMap(Properties properties) {
        Map<String, String> map = new HashMap<>();
            //Map with key values pairs from the Properties object
            for (String key : properties.stringPropertyNames()) {
                String value = properties.getProperty(key);
                map.put(key, value);
            }


        return map;
    }

    //Getters and Setters
    //Gets the number of columns in the loaded map.
    public static int getColumns(Map<String, String> loadedMap) {
        int maxFirstCoordinate = Integer.MIN_VALUE;
        //Find the maximum value of the 1st coordinate in the map
        for (String key : loadedMap.keySet()) {
            String[] coordinates = key.split(",");
            int firstCoordinate = Integer.parseInt(coordinates[0]);

            maxFirstCoordinate = Math.max(maxFirstCoordinate, firstCoordinate);
        }

        return maxFirstCoordinate;
    }
    //Gets the number of rows in the loaded map.
    public static int getRows(Map<String, String> loadedMap) {
        int maxSecondCoordinate = Integer.MIN_VALUE;
        //Find the maximum value of the 2nd coordinate in the map
        for (String key : loadedMap.keySet()) {
            String[] coordinates = key.split(",");
            int secondCoordinate = Integer.parseInt(coordinates[1]);

            maxSecondCoordinate = Math.max(maxSecondCoordinate, secondCoordinate);
        }

        return maxSecondCoordinate;
    }
    /**
     * Creates a default Properties object representing a map with coordinates from 0,0 to 15,15.
     *
     * @return The default Properties object.
     */
    public static Properties createMapProperties() {
        Properties properties = new Properties();

        // Generate properties for coordinates 0,0 to 15,15
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                String key = i + "," + j;
                String value = "0";

                properties.setProperty(key, value);
            }
        }
        return properties;
        // Save properties to file
        //(Note: This line is commented out as saving to file is not implemented here)

    }
}
