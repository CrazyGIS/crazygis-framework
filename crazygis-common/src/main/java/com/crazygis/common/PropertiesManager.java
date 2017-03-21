package com.crazygis.common;

import com.crazygis.common.exceptions.ApplicationException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by xuguolin on 2017/3/18.
 */
public class PropertiesManager {

    private Properties properties = null;
    private String path = null;
    private String key = null;
    private String propertiesName = null;

    private static Map<String, PropertiesManager> managerDictionary;
    private static Object locker;
    private final static String WEB_PATH_DIRECTORY = "WEB-INF";
    private final static String DEFAULT_PROPERTIES_NAME = "appconfig.properties";
    private final  static String FILE_PREFIX = "file:/";

    static {
        locker = new Object();
        managerDictionary = new HashMap<String, PropertiesManager>();
    }

    public static PropertiesManager getInstance() {
        return getInstance(DEFAULT_PROPERTIES_NAME);
    }

    public static PropertiesManager getInstance(String propertiesName) {
        return getOrCreatePropertiesManager(propertiesName, false);
    }

    public static PropertiesManager getWebInstance() {
        return getWebInstance(DEFAULT_PROPERTIES_NAME);
    }

    public static PropertiesManager getWebInstance(String propertiesName) {
        return getOrCreatePropertiesManager(propertiesName, true);
    }

    private static PropertiesManager getOrCreatePropertiesManager(String propertiesName, boolean isWeb) {
        String key = propertiesName;
        String propertiesPath = null;
        if(isWeb) {
            key = "web." + propertiesName;
            propertiesPath = createWebPath();
        } else {
            propertiesPath = createPath();
        }

        if(!managerDictionary.containsKey(key)) {
            synchronized (locker) {
                if(!managerDictionary.containsKey(key)) {
                    Properties p = null;
                    InputStream in = null;
                    try {
                        in = new FileInputStream(propertiesPath + propertiesName);
                        p = new Properties();
                        p.load(in);
                    } catch (IOException e) {
                        throw new ApplicationException("properties load exception.", e);
                    } finally {
                        if(in != null) {
                            try {
                                in.close();
                            } catch (Exception e) {}
                        }
                    }

                    PropertiesManager manager = new PropertiesManager();
                    manager.setProperties(p);
                    manager.setPath(propertiesPath);
                    manager.setKey(key);
                    manager.setPropertiesName(propertiesName);
                    managerDictionary.put(key, manager);
                }
            }
        }
        return managerDictionary.get(key);
    }

    private static String createPath() {
        String url = PropertiesManager.class.getResource("/").toString();
        url = url.substring(FILE_PREFIX.length());
        try {
            url = URLDecoder.decode(url, "utf-8");
        } catch (Exception e) {
            throw new ApplicationException("URL decoder error", e);
        }
        return url;
    }

    private static String createWebPath() {
        String url = createPath();
        int index = -1;
        char c;
        for(int i = url.length() - 1; i >= 0; i--) {
            c = url.charAt(i);
            if(c == '/') {
                if(i == url.length() - 1) {
                    continue;
                }
                index = i + 1;
                break;
            }
        }
        url = url.substring(0, index) + "config/";
        return url;
    }

    public String get(String key) {
        if(containsKey(key)) {
            return properties.getProperty(key);
        } else {
            return null;
        }
    }

    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }

    public Properties getProperties() {
        return properties;
    }

    private void setProperties(Properties p) {
        properties = p;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String p) {
        path = p;
    }

    public String getKey() {
        return key;
    }

    private void setKey(String key) {
        this.key = key;
    }

    public String getPropertiesName() {
        return propertiesName;
    }

    private void setPropertiesName(String propertiesName) {
        this.propertiesName = propertiesName;
    }
}
