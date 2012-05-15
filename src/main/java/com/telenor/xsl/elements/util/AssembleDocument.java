package com.telenor.xsl.elements.util;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.Properties;

/**
 * @author vegaasen
 * @version 1.0-SNAPSHOT
 */
public class AssembleDocument {

    private static final Logger LOGGER = Logger.getLogger(AssembleDocument.class);

    public static String assembleDocument(final Document targetDocument) {
        LOGGER.debug("Initiate assembly");
        if (targetDocument != null) {
            try {
                DOMSource domSource = new DOMSource(targetDocument);
                StreamResult streamResult = new StreamResult(System.out);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                for(Properties p : DocumentUtilities.getTransformProperties()) {
                    transformer.setOutputProperties(p);
                }
                
                transformer.transform(domSource, streamResult);

                return "no idea.";
            } catch (Exception e) {
                LOGGER.error("No document printed. See error: " + e);
            }
        }
        return "Something went wrong. Most likely the document is pretty <null>.";
    }

}
