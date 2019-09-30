package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Author: username
 * Date: 2019-04-11 14:30
 * Describe: 属性文件工具类
 */
public class PropertiesUtil02 {

    private static final String DEFAULT_PROPERTIES="config/kafka.properties";

    /**
     * 获取properties属性值
     * @param propKey
     * @return
     */
    public static String getPropValue(String propKey){
        try {
            Properties props = new Properties();
            InputStream inputStream = PropertiesUtil02.class.getResourceAsStream(DEFAULT_PROPERTIES);
            //*.properties配置文件，要使用UTF-8编码，否则会现中文乱码问题
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            props.load(bf);
            return props.getProperty(propKey);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}