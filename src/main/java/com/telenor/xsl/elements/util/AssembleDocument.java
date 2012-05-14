package com.telenor.xsl.elements.util;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

                DOMSource domSource = new DOMSource(targetDocument);
                StreamResult streamResult = new StreamResult(System.out);
                transformer.transform(domSource, streamResult);

                return "no idea.";
            } catch (Exception e) {
                LOGGER.error("No document printed. See error: " + e);
            }
        }
        return "Something went wrong. Most likely the document is pretty <null>.";
    }

}
