package com.yh;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;

public class Config {
    private static final Log log = LogFactory.getLog(Config.class);
    private static Config config;
    private Properties props;

    private Config() {
        props = new Properties();
    }

    public static Config get() {
        if(config == null) {
            config = new Config();
        }

        return config;
    }

    public void load(String path) {
        try {
            InputStream is = new FileInputStream(path);
            props.load(is);
        }
        catch(FileNotFoundException e) {
            log.error("Can not found the config file." + e.getMessage());
        }
        catch(IOException e) {
            log.error("Can not read the config file." + e.getMessage());
        }
    }

    public Object getVal(String key) {
        return props.get(key);
    }

    public String getStringVal(String key) {
        Object val =  getVal(key);
        return val == null ? null : val.toString();
    }

    public Object getIntVal(String key) {
        Object val =  getVal(key);
        return val == null ? 0 : Integer.parseInt(val.toString());
    }

    public String[] getArray(String key) {
        Object val =  getVal(key);
        String value = val == null ? null : val.toString();
        return value == null ? null : value.split(",");
    }
}
