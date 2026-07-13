package de.tum.cit.ase.maze;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
/**
 * The CreateMapProperties class generates a properties file representing a map with random values.
 * It includes random walls, a key, and an exit, and saves the properties to a file.
 */
public class CreateMapProperties {

    //The main method generates a map with random properties and saves it to a file
    public static void main(String[] args) {
        String filePath = "maps/map.properties";

        createMapProperties(filePath);

        System.out.println("Properties file created successfully at: " + filePath);
    }
    /**
     * Creates a properties file with random values representing a map.
     *
     * @param filePath The path where the properties file will be saved.
     */
    public static void createMapProperties(String filePath) {
        Properties properties = new Properties();
        Random random = new Random();

        // Generate properties for coordinates 0,0 to 9,9
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String key = i + "," + j;
                int randomNumber = random.nextInt(4);

                String value=null;
                switch (randomNumber){
                    case 0: value="0";break;
                }
                if(value != null){
                properties.setProperty(key, value);
                }
            }
        }

        int keyX= random.nextInt(9);
        int keyY= random.nextInt(9);
        properties.setProperty(keyX+","+keyY, "5"); //Key object


        int exitX= random.nextInt(9);
        int exitY= random.nextInt(9);
        properties.setProperty(keyX+","+keyY, "2"); // Exit door

        // Save properties to file
        try (FileOutputStream output = new FileOutputStream(filePath)) {
            properties.store(output, "Map Properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
