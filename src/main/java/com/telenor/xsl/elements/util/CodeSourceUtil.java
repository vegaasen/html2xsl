package com.telenor.xsl.elements.util;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author vegaasen
 * @version 1.1
 * @since 1.0-SNAPSHOT
 */
public class CodeSourceUtil {

    public static File getFile(final String loc) {
        if(StringUtils.isNotEmpty(loc)) {
            return new File(loc);
        }
        return null;
    }
    
    public static InputStream getInputStream(final String string) {
        if(StringUtils.isNotEmpty(string)) {
            InputStream is = null;
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(string).openConnection();
                httpURLConnection.connect();
                is = httpURLConnection.getInputStream();
                httpURLConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return is;
        }
        return null;
    }

}
