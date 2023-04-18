package org.example.settings;

import org.example.exception.PropertyNotFound;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class GarageSettings {
    private boolean changeable;
    private final String path;

    public GarageSettings(String path) {
        this.path = path;
    }

    public boolean isChangeable() {
        return changeable;
    }

    public void setChangeable(boolean changeable) {
        this.changeable = changeable;
    }

    public void initializeProperty(){
        Properties properties = new Properties();
        try {
            FileReader fileReader = new FileReader(path);
            properties.load(fileReader);
        } catch (IOException e) {
            throw new PropertyNotFound("Property file does not exist");
        }
        this.changeable = properties.getProperty("changeable_number_of_garages").equals("true");
    }
}
