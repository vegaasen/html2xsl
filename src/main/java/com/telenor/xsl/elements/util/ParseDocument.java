package com.telenor.xsl.elements.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

/**
 * Simple documentparser.
 *
 * @author vegaasen
 * @version 1.0-SNAPSHOT
 */
public class ParseDocument {

    private static final Logger LOGGER = Logger.getLogger(ParseDocument.class);

    private static Document targetDocument;

    private static final String
            XSL_ELEMENT = "xsl:element",
            XSL_ATTRIBUTE = "xsl:attribute",
            XSL_VALUE_OF = "xsl:value-of",
            XSL_TEXT = "xsl:text";

    public static String parseDocument(final String fileLocation) {
        LOGGER.debug(String.format("Starting parsing of the Document. Location: %s", fileLocation));
        try {
            final File file = new File(fileLocation);

            LOGGER.debug("Building DOM Document");
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = builderFactory.newDocumentBuilder();

            Document document = builder.parse(file);

            setTargetDocument(document);

            document.getDocumentElement().normalize();

            Element rootElement = document.getDocumentElement();

            domTree(rootElement);

            return AssembleDocument.assembleDocument(targetDocument);

        } catch (Exception e) {
            LOGGER.error("Unable to locate file. Aka. 404!" + e);
        }
        return "/yourmom/";
    }

    private static void domTree(Element element) {
        if(!element.getNodeName().contains("xsl")) {
            List<Attr> attrList = getElementAttributes(element);

            Element newElement = enhanceDomElement(attrList, element);
            enhanceDomWithAttribute(attrList, newElement);
            element = newElement;
        }
        if (hasChildNodes(element)) {
            getChildNodes(element);
        }
    }

    private static void enhanceDomWithAttribute(List<Attr> attributes, Element newElement) {
        LOGGER.debug("Writing for element:" + newElement.getNodeName());
        for (Attr attribute : attributes) {
            String value = attribute.getNodeValue();
            String name = attribute.getNodeName();
            Element node = targetDocument.createElement(XSL_ATTRIBUTE);
            Attr attr = targetDocument.createAttribute("name");
            attr.setValue(name);
            node.setTextContent(value);
            node.setAttributeNode(attr);
            newElement.appendChild(node);
        }
    }

    private static Element enhanceDomElement(List<Attr> attributes, Element oldElement) {
        //String content = oldElement.getTextContent();
        Element e = (Element) targetDocument.renameNode(oldElement, "", XSL_ELEMENT);
        e.setAttribute("name", oldElement.getNodeName());
        for(Attr attr : attributes) {
            if(StringUtils.isNotEmpty(e.getAttribute(attr.getNodeName()))) {
                e.removeAttribute(attr.getNodeName());
            }
        }
        /*
        if(StringUtils.isNotEmpty(content)) {
            Element valueOf = targetDocument.createElement(XSL_TEXT);
            valueOf.setAttribute("disable-output-escaping", "yes");
            valueOf.setTextContent(content);
            e.setTextContent("");
            e.appendChild(valueOf);
        }
        */
        return e;
    }

    private static void getChildNodes(Element element) {
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (isElement(currentNode)) {
                domTree((Element) currentNode);
            }
        }
    }

    private static List<Attr> getElementAttributes(Node element) {
        if (hasAttributes(element)) {
            NamedNodeMap attributes = element.getAttributes();
            if (attributes.getLength() > 0) {
                List<Attr> attrList = new ArrayList<Attr>();
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (attributes.item(i) instanceof Attr) {
                        attrList.add((Attr) attributes.item(i));
                    }
                }
                return attrList;
            }
        }
        return Collections.emptyList();
    }

    private static boolean hasChildNodes(final Element element) {
        return element.hasChildNodes();
    }

    private static boolean hasAttributes(final Node element) {
        return element.getAttributes().getLength() > 0;
    }

    private static boolean isElement(final Node node) {
        return (node instanceof Element);
    }

    public static void setTargetDocument(Document targetDocument) {
        ParseDocument.targetDocument = targetDocument;
    }
}
