package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * This abstract class is used for all data reading
 */
public abstract class DataReader {

    private static final String PROPERTY_FILE = ".properties";
    private static final String SEPARATOR = ",";

    public File getFile(String fileName){
        URL dirURL = getClass().getClassLoader().getResource(fileName);
        try {
            return new File(dirURL.getFile());
        } catch (NullPointerException e){
            return new File(fileName);
        }
    }

    /**
     * Gets all the Properties objects within a folder
     * @param folderOfFiles A File object representing a folder of .Properties files
     * @return A map where the key is the name of the file, and the value is a Properties object
     * @throws IOException Thrown when the file passed through does not exist
     */
    public Map<String, Properties> getPropertiesForAllFilesInFolder(File folderOfFiles) throws IOException {
        Map<String, Properties> allProperties = new HashMap<>();
        for (File file: folderOfFiles.listFiles()) {
            if (file.getName().endsWith(PROPERTY_FILE)) {
                FileInputStream fileInput = new FileInputStream(file);
                Properties properties = new Properties();
                properties.load(fileInput);
                allProperties.put(file.getName(), properties);
            }
        }
        return allProperties;
    }

    public String getSEPARATOR() {
        return SEPARATOR;
    }

}
