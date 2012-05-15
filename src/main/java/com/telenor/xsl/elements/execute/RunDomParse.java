package com.telenor.xsl.elements.execute;

import com.telenor.xsl.elements.util.ParseDocument;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static com.telenor.xsl.elements.util.DocumentUtilities.getFileInputStream;
import static com.telenor.xsl.elements.util.DocumentUtilities.getInputStreamFromString;
import static com.telenor.xsl.elements.util.DocumentUtilities.getInputStreamFromURL;

/**
 * Simple HTMLDOM -> XSL Stylesheet-converter.
 * Please note:
 * This is just for simplifying our days - its not ment to be a complete parser for documents. The "parser"
 * only knows of the <strong>simplest</strong> xsl-types
 *
 * @author vegaasen
 * @version 1.0-SNAPSHOT
 */
public class RunDomParse {

    private static final Logger LOGGER = Logger.getLogger(RunDomParse.class);

    private static final String
            PARAM_TYPE = "-t=",
            PARAM_LOCATION = "-l=";
    private static final String
            PARAM_TYPE_INPUT_STRING = "i",
            PARAM_TYPE_URL = "u",
            PARAM_TYPE_FILE_LOC = "f";

    public static void main(String... args) {
        if (args.length > 0) {
            String location = "",
                    type = "";
            for (String arg : args) {
                if (arg.contains(PARAM_LOCATION)) {
                    LOGGER.debug(String.format("Argument found: %s", arg));
                    location = arg;
                    location = location.replace(PARAM_LOCATION, "");
                } else if (arg.contains(PARAM_TYPE)) {
                    type = arg;
                    type = type.replace(PARAM_TYPE, "");
                }
            }
            if (StringUtils.isNotEmpty(location)) {
                InputStream inputStream;
                try {
                    if (type.equals(PARAM_TYPE_INPUT_STRING)) {
                        inputStream = getInputStreamFromString(location);
                    } else if (type.equals(PARAM_TYPE_URL)) {
                        inputStream = getInputStreamFromURL(new URL(location));
                    } else if(type.equals(PARAM_TYPE_FILE_LOC)) {
                        inputStream = getFileInputStream(new File(location));
                    } else {
                        inputStream = getFileInputStream(new File(location));
                    }
                    String resultLocation = ParseDocument.parseDocument(inputStream);
                    LOGGER.info(String.format("Document parsed and saved @ location %s", resultLocation));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            LOGGER.error("Missing arguments. Legal arguments is: \n -t=<type> \n -l=<location{url,file}>");
        }
    }
}
