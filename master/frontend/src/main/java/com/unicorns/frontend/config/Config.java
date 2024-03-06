package com.unicorns.frontend.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();
    private static final String filename = "config.properties";

    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream(filename)) {
            if (input == null) {
                throw new IOException("Unable to find " + filename);
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
