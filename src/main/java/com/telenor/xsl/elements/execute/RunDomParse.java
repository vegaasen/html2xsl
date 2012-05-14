package com.telenor.xsl.elements.execute;

import com.telenor.xsl.elements.util.ParseDocument;
import org.apache.log4j.Logger;

/**
 * Simple DOM-XSL converter. Please note:
 * This is just for simplifying our days - its not ment to be a complete parser for documents. The parser
 * only knows of the <strong>simplest</strong> xsl-types..
 *
 * todo: fix the content location. must be coming from either file or url..
 *
 * @author vegaasen
 * @version 1.0-SNAPSHOT
 */
public class RunDomParse {

    private static final Logger LOGGER = Logger.getLogger(RunDomParse.class);

    public static void main(String... args) {
        if(args.length>0) {
            String fileLocation = null;
            for (String arg : args) {
                if (arg.contains("loc=")) {
                    LOGGER.debug(String.format("Argument found: %s", arg));
                    fileLocation = arg;
                    fileLocation = fileLocation.replace("-loc=", "");
                }
            }
            if(fileLocation!=null) {
                String resultLocation = ParseDocument.parseDocument(fileLocation);
                System.out.println(String.format("Saved @ location: %s", resultLocation));
                LOGGER.info(String.format("Document parsed and saved @ location %s", resultLocation));
            }
        }else{
            LOGGER.error("Missing arguments.");
        }
    }

}
