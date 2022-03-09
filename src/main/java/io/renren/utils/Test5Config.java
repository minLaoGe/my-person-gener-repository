package io.renren.utils;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public class Test5Config {

    public static String name;
    public static int id;
    private static final String active="active";

    private static String property = "application.yml";

    private static Test5Config myConfig;

    static {
//        myConfig = loadConfig();
    }

    private static Test5Config loadConfig() {
        if (myConfig == null) {
            myConfig = new Test5Config();
            Properties properties;
            try {
                properties = PropertiesLoaderUtils.loadAllProperties(property);

                name = properties.getProperty("test5.name");
                id = Integer.valueOf(properties.getProperty("test5.id"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return myConfig;
    }
    public static String loadConfig(String name) {

            Properties properties;
            try {
                properties = PropertiesLoaderUtils.loadAllProperties(property);
                String value = properties.getProperty(name);
                return value;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

    }
    public static String getCurrentEnv(){
        return loadConfig(active);
    }

    public Test5Config getInstance() {
        return myConfig;
    }

}
