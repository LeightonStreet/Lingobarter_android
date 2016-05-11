package com.st.leighton.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * for configuration. load properties from file -- project.properties
 * static one
 * @author Li He
 *
 */
@SuppressWarnings("rawtypes")
public class MyProperty {

    private static Properties props;

    static {
        props = new Properties();
    }

    //private static Log log = LogFactory.getLog(WebloggerConfig.class);

    // no, you may not instantiate this class :p
    private MyProperty() {}

    public static void load(InputStream stream) throws IOException{
        props.load(stream);
        stream.close();
    }

    /**
     * Retrieve a property value
     * @param     key Name of the property
     * @return    String Value of property requested, null if not found
     */
    public static String getProperty(String key) {
        //log.debug("Fetching property ["+key+"="+props.getProperty(key)+"]");
        String value = props.getProperty(key);
        return value == null ? value : value.trim();
    }

    /**
     * Retrieve a property value
     * @param     key Name of the property
     * @param     defaultValue Default value of property if not found
     * @return    String Value of property requested or defaultValue
     */
    public static String getProperty(String key, String defaultValue) {
        //log.debug("Fetching property ["+key+"="+props.getProperty(key)+",defaultValue="+defaultValue+"]");
        String value = props.getProperty(key);
        if (value == null) {
            return defaultValue;
        }

        return value.trim();
    }

    /**
     * Retrieve a property as a boolean ... defaults to false if not present.
     */
    public static boolean getBooleanProperty(String name) {
        return getBooleanProperty(name,false);
    }

    /**
     * Retrieve a property as a boolean ... with specified default if not present.
     */
    public static boolean getBooleanProperty(String name, boolean defaultValue) {
        // get the value first, then convert
        String value = MyProperty.getProperty(name);

        if(value == null) {
            return defaultValue;
        }

        return Boolean.valueOf(value);
    }

    /**
     * Retrieve a property as an int ... defaults to 0 if not present.
     */
    public static int getIntProperty(String name) {
        return getIntProperty(name, 0);
    }

    /**
     * Retrieve a property as a int ... with specified default if not present.
     */
    public static int getIntProperty(String name, int defaultValue) {
        // get the value first, then convert
        String value = MyProperty.getProperty(name);

        if (value == null) {
            return defaultValue;
        }

        return Integer.valueOf(value);
    }

    /**
     * Retrieve all property keys
     * @return Enumeration A list of all keys
     **/
    public static Enumeration keys() {
        return props.keys();
    }


    /**
     * Get properties starting with a specified string.
     */
    public static Properties getPropertiesStartingWith(String startingWith) {
        Properties props = new Properties();
        for (Enumeration it = MyProperty.props.keys(); it.hasMoreElements();) {
            String key = (String)it.nextElement();
            props.put(key, MyProperty.props.get(key));
        }
        return props;
    }


    public static long getLongProperty(String name){
        return MyProperty.getLongProperty(name,0L);
    }

    public static long getLongProperty(String name, long defaultValue){
        String value = MyProperty.getProperty(name);

        if (value == null) {
            return defaultValue;
        }

        return Long.valueOf(value);
    }

    public static double getDoubleProperty(String name) {
        return MyProperty.getDoubleProperty(name,0.0);
    }

    public static double getDoubleProperty(String name, double defaultValue){
        String value = MyProperty.getProperty(name);

        if (value == null) {
            return defaultValue;
        }

        return Double.valueOf(value);
    }

}