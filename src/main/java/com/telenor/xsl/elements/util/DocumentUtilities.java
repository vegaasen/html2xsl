package com.telenor.xsl.elements.util;

import javax.xml.transform.OutputKeys;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

/**
 * Some code is snapped from the common-utils-project that is created.
 * <a href="https://github.com/vegaasen/common-utils">Common Utilities</a>
 *
 * @author vegaasen.com
 * @version 0.1
 * @since 1.0-SNAPSHOT
 */
public class DocumentUtilities {

    protected static final String E_FILE_NOT_EXIST = "File does not exist";
    protected static final String E_OBJECT_WAS_NULL = "Object is null";
    protected static final String E_NO_SUCH_ELEMENT = "No Such Element!";

    public static Properties getTransformProperties() {
        Properties tProp = new Properties();

        tProp.put(OutputKeys.INDENT, "yes");
        tProp.put("{http://xml.apache.org/xslt}indent-amount", "2");
        tProp.put(OutputKeys.ENCODING, "utf-8");
        tProp.put(OutputKeys.OMIT_XML_DECLARATION, "no");
        tProp.put(OutputKeys.METHOD, "xml");

        return tProp;
    }

    public static synchronized InputStream getInputStreamFromString(final String input) {
        if(verifyNotNull(input)) {
            return new ByteArrayInputStream(input.getBytes());
        }
        throw new NullPointerException(E_OBJECT_WAS_NULL);
    }

    public static synchronized InputStream getInputStreamFromURL(final URL location)
            throws IOException, NullPointerException {
        if (verifyNotNull(location)) {
            HttpURLConnection httpUrlConnection = (HttpURLConnection) location.openConnection();
            if (httpUrlConnection.getResponseCode() == HttpStatusCodes.OK_FOUND.getCode()) {
                return httpUrlConnection.getInputStream();
            }
            throw new ConnectException(String.format("Could not fetch InputStream. Http returned status %s, not %s",
                    httpUrlConnection.getResponseCode(), HttpStatusCodes.OK_FOUND.getCode()));
        }
        throw new NullPointerException(E_OBJECT_WAS_NULL);
    }

    public static synchronized FileInputStream getFileInputStream(final File file)
            throws IOException,
            NullPointerException {
        if (verifyNotNull(file)) {
            if (file.exists()) {
                if (file.isDirectory()) {
                    throw new IOException(String.format("The file <%s> is not a file, its a directory!", file));
                }
                if (!file.canRead()) {
                    throw new IOException(String.format("The file <%s> could not be altered", file));
                }
            } else {
                throw new IOException(E_FILE_NOT_EXIST);
            }
            return new FileInputStream(file);
        }
        throw new NullPointerException(String.format(E_OBJECT_WAS_NULL));
    }

    public static String repairXMLErrors(final InputStream inputStream) {
        if(verifyNotNull(inputStream)) {
            String result = convertStreamToString(inputStream);
            return result.replaceAll("&([^;|^=|^\"]+(?!(?:\\w|;)))", "&amp;$1");
        }
        throw new NullPointerException(String.format(E_OBJECT_WAS_NULL));
    }

    public static String convertStreamToString(final InputStream inputStream) {
        try {
            return new Scanner(inputStream).useDelimiter("\\A").next();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(String.format(E_OBJECT_WAS_NULL));
        }
    }

    public static boolean verifyNotNull(final Object... object) {
        boolean objectWasNotNull = true;
        for (Object o : object) {
            if (o == null) {
                objectWasNotNull = false;
                break;
            }
        }
        return objectWasNotNull;
    }

    public static String propertiesReader(final String key) {
        Properties p = new Properties();
        try {
            p.load(DocumentUtilities.class.getResource("system.properties").openStream());
            p.get(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public enum HttpStatusCodes {
        OK_FOUND(200);

        private int statusCode;

        HttpStatusCodes(int statusCode) {
            this.statusCode = statusCode;
        }

        public int getCode() {
            return statusCode;
        }
    }

}
