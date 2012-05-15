package com.telenor.xsl.elements.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.telenor.xsl.elements.util.DocumentUtilities.getInputStreamFromString;
import static com.telenor.xsl.elements.util.DocumentUtilities.repairXMLErrors;

/**
 * simple parser for html->xsl
 *
 * todo: a lot.
 *
 * @author vegaasen
 * @version 0.2
 * @since 1.0-SNAPSHOT
 */
public class ParseDocument {

    private static final Logger LOGGER = Logger.getLogger(ParseDocument.class);

    private static final String
            XSL_ELEMENT = "xsl:element",
            XSL_ATTRIBUTE = "xsl:attribute",
            XSL_VALUE_OF = "xsl:value-of",
            XSL_TEXT = "xsl:text",
            XSL_OUTPUT = "xsl:output",
            XSL_STYLESHEET = "xsl:stylesheet";
    private static final String
            CDATA_START = "<![CDATA[",
            CDATA_END = "]]>";

    private static Document targetDocument;

    public static String parseDocument(final InputStream stream) {
        LOGGER.debug(String.format("Document-parsing starting.."));
        try {
            LOGGER.debug("Building DOM Document");

            LOGGER.debug("Trying to refine document.");

            InputStream c_stream = getInputStreamFromString(repairXMLErrors(stream));

            LOGGER.debug("Document has been refined. Continue with normal procedure.");
            
            Document document = getDocumentBuilder().parse(c_stream);

            document.getDocumentElement().normalize();

            setTargetDocument(getDocumentBuilder().newDocument());

            addXslTopElement(document);

            Element rootElement = targetDocument.getDocumentElement();

            domTree(rootElement);

            return AssembleDocument.assembleDocument(targetDocument);

        } catch (IOException e) {
            LOGGER.error("IOException occured. Unable to read the inputsource." + e);
        } catch (SAXException e) {
            LOGGER.error("Unable to parse the document. Error that occured: \n" + e);
        }
        return "/yourmom/";
    }

    private static void addXslTopElement(final Document originalDocument) {
        if(DocumentUtilities.verifyNotNull(originalDocument)) {
            final Element e_xslStylesheet = targetDocument.createElement(XSL_STYLESHEET);
            e_xslStylesheet.setAttribute("version", "1.0");
            e_xslStylesheet.setAttribute("xmlns:xsl", "http://www.w3.org/1999/XSL/Transform");
            final Element e_xslOuput = targetDocument.createElement(XSL_OUTPUT);
            e_xslOuput.setAttribute("method","xml");
            e_xslOuput.setAttribute("version","1.0");
            e_xslOuput.setAttribute("encoding","utf-8");
            e_xslOuput.setAttribute("omit-xml-declaration","yes");
            e_xslOuput.setAttribute("indent","yes");
            e_xslStylesheet.appendChild(e_xslOuput);
            targetDocument.appendChild(e_xslStylesheet);
            Element copyOfExistingTree = (Element) targetDocument.importNode(originalDocument.getDocumentElement(), true);
            e_xslStylesheet.appendChild(copyOfExistingTree);
        }else{
            LOGGER.error("Unable to create element. Document is null.");
        }
    }

    private static void domTree(Element element) {
        if(!element.getNodeName().contains("xsl")) {
            List<Attr> attrList = getElementAttributes(element);
            Element e_enhancedElement = enhanceDomElement(attrList, element);
            enhanceDomWithAttribute(attrList, e_enhancedElement);
            element = e_enhancedElement;
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
            Element e_xslAttribute = targetDocument.createElement(XSL_ATTRIBUTE);
            Attr attr = targetDocument.createAttribute("name");
            attr.setValue(name);
            e_xslAttribute.setTextContent(value);
            e_xslAttribute.setAttributeNode(attr);
            newElement.appendChild(e_xslAttribute);
        }
    }

    private static Element enhanceDomElement(List<Attr> attributes, Element oldElement) {
        Element e = (Element) targetDocument.renameNode(oldElement, "", XSL_ELEMENT);
        e.setAttribute("name", oldElement.getNodeName());
        for(Attr attr : attributes) {
            if(StringUtils.isNotEmpty(e.getAttribute(attr.getNodeName()))) {
                e.removeAttribute(attr.getNodeName());
            }
        }
        return e;
    }

    private static void enhanceTextNode(Text element) {
        String currentText = element.getWholeText();
        if(StringUtils.isNotEmpty(currentText) && !currentText.trim().equals("")){
            LOGGER.debug("Evaluating current string: " + currentText);
            //todo: escape cdata
            //element.setTextContent(String.format("%s%s%s",CDATA_START,currentText,CDATA_END));
        }
    }

    private static void getChildNodes(Element element) {
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (isElement(currentNode)) {
                domTree((Element) currentNode);
            }else if(isTextNode(currentNode)) {
                enhanceTextNode((Text) currentNode);
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

    private static boolean isTextNode(final Node node) {
        return (node instanceof Text);
    }

    public static void setTargetDocument(Document targetDocument) {
        ParseDocument.targetDocument = targetDocument;
    }

    public static DocumentBuilder getDocumentBuilder() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            return documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
