package fr.litopia.tux.game.utils;

// see public class comment below

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.net.URL;

/**
 * A utility class that facilitates the manipulation of DOM document, including
 * the evaluation of XPath.
 * <p>
 * Beware: xmlns (default namespace) can not be specified.
 * <p>
 * See inner classes for more information.
 * <p>
 * To simplify usage all methods are static and are declared to throw an
 * indistinct Exception.
 * <p>
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see {@link <a href="https://www.gnu.org/licenses/">...</a>}.
 *
 * @author (C) 2010-2018 Emmanuel Promayon, Universite Joseph Fourier -
 * TIMC-IMAG
 * @coauthor (C) 2017-2018 Nicolas Glade, Universite Grenoble Alpes - TIMC-IMAG
 *
 */
public class XMLUtil {

    /**
     * Inner class to evaluate XPath expression on a DOM Document
     */
    static public class XPathEvaluateExpression {

        /**
         * To use when the XPath evaluation is expected to return a text
         * content.
         *
         * @param xpathExpression the xpath expression to evaluate
         * @param doc the DOM Document to use for the expression
         * @return the resulting evaluation as a String
         * @throws Exception
         *
         */
        public static String getString(String xpathExpression, Document doc) throws Exception {
            // Factory Instanciation
            XPathFactory factory = XPathFactory.newInstance();
            // Create a new XPath object
            XPath xpath = factory.newXPath();
            // Compilation of the expression (String -> XPath)
            XPathExpression expression = xpath.compile(xpathExpression);
            // Evaluation of the XPath expression on doc (only first text answer is collected and returned)
            return (String) expression.evaluate(doc, XPathConstants.STRING);
        }

        /**
         * To use when the XPath evaluation is expected to return a number.
         *
         * @param xpathExpression the xpath expression to evaluate
         * @param doc the DOM Document to use for the expression
         * @return the resulting evaluation as a number (double)
         * @throws Exception
         */
        public static double getNumber(String xpathExpression, Document doc) throws Exception {
            // Factory Instanciation
            XPathFactory factory = XPathFactory.newInstance();
            // Create a new XPath object
            XPath xpath = factory.newXPath();
            // Compilation of the expression (String -> XPath)
            XPathExpression expression = xpath.compile(xpathExpression);
            // Evaluation of the XPath expression on doc (only first text answer is collected and returned)
            return (Double) expression.evaluate(doc, XPathConstants.NUMBER);
        }

        /**
         * To use when the XPath evaluation is expected to return a list of
         * nodes
         *
         * @param xpathExpression the xpath expression to evaluate
         * @param doc the DOM Document to use for the expression
         * @return the resulting evaluation as a DOM list of nodes
         * @throws Exception
         */
        public static NodeList getNodeList(String xpathExpression, Document doc) throws Exception {
            // Factory Instanciation
            XPathFactory factory = XPathFactory.newInstance();
            // Create a new XPath object
            XPath xpath = factory.newXPath();
            // Compilation of the expression (String -> XPath)
            XPathExpression expression = xpath.compile(xpathExpression);
            // Evaluation of the XPath expression on doc
            return (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
        }
    } // ends XPathEvaluateExpression

    /**
     * Inner class to create DOM document with various methods including URL,
     * XSL transformation and files.
     */
    static public class DocumentFactory {

        /**
         * build a DOM Document from a String: this methods parse a String that
         * should represent an XML serialisation.
         *
         * @param xmlString the XML document as a String
         * @return the DOM Document build from the XML document.
         * @throws Exception
         */
        static public Document fromString(String xmlString) throws Exception {
            // initialize DOM context
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true); // never forget this!

            // build the DOM document from the String
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        }

        /**
         * build a DOM Document using a URL: this methods download the URL in a
         * String.
         * <p>
         * Note: the URL should refer to an XML document.
         *
         * @param url the XML document to download
         * @return the DOM Document build from the XML document.
         * @throws Exception
         */
        static public Document fromURL(URL url) throws Exception {
                // download the document and transform it to a flat String
                String xmlDocument = URLUtil.newString(url);

                // initialize DOM context
                DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
                domFactory.setNamespaceAware(true); // never forget this!

                // build the DOM document from the String
                DocumentBuilder builder = domFactory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(xmlDocument)));
                return doc;
        }

        /**
         * Cette m??thode applique une transformation ?? un Document DOM et
         * renvoie le document transform?? sous la forme d'une chaine de
         * caract??res (String).
         *
         * @param doc the DOM document to process by XSLT
         * @param t a transformer to be used to process the Document doc
         * @return a Document resulting from the XSL transformation of doc
         * @throws TransformerException
         */
        public static Document fromTransformation(Transformer t, Document doc) throws TransformerException {
            // r??gle l'encodage du transformateur
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            // Cr??e un r??sultat de type StreamResult (flux) pour stocker le r??sulat de la transformation
            StreamResult result = new StreamResult(new StringWriter());
            // Convertit le document DOM en source exploitable par le transformateur
            DOMSource source = new DOMSource(doc);
            // Transforme le document et stocke le r??sultat dans result
            t.transform(source, result);
            // convertit le r??sultat (StreamResult) en Document renvoy?? par la m??thode
            return (Document) result;
        }

        /**
         * return the DOM document resulting from of an XSL transformation of a
         * source DOM document
         *
         * @param xslStreamSource a source containing the XSL stylesheet
         * @param doc the DOM document to process by XSLT
         * @return the DOM document resulting from the XSL transformation of doc
         * as written in the stylesheet xslFileName
         * @throws Exception
         */
        public static Document fromXSLTransformation(StreamSource xslStreamSource, Document doc) throws Exception {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            //domFactory.setNamespaceAware(true); // never forget this!
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document output = builder.newDocument();
            Result result = new DOMResult(output);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer t = transformerFactory.newTransformer(xslStreamSource);
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            t.transform(new DOMSource(doc), result);
            return (Document) result;
        }

        /**
         * return the DOM document resulting from of an XSL transformation of a
         * source DOM document
         *
         * @param xslFileName name of the file containing the XSL stylesheet
         * @param doc the DOM document to process by XSLT
         * @return the DOM document resulting from the XSL transformation of doc
         * as written in the stylesheet xslFileName
         * @throws Exception
         */
        public static Document fromXSLTransformation(String xslFileName, Document doc) throws Exception {
            return fromXSLTransformation(new StreamSource(xslFileName), doc);
        }

        /**
         * build a DOM Document from an XML file.
         *
         * @param fileName name of the input file (have to be XML)
         * @return the corresponding DOM Document
         * @throws Exception
         */
        public static Document fromFile(String fileName) throws Exception {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true); // never forget this! // XXXXXX
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document theDocument = builder.parse(new File(fileName));
            return theDocument;
        }

        /**
         * build a DOM Document from a Streamsource.
         *
         * @param source a stream source providing a XML content
         * @return the corresponding DOM Document
         * @throws Exception
         */
        public static Document fromStreamSource(StreamSource source) throws Exception {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true); // never forget this!  // XXXXXX
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setSystemId(source.getSystemId());
            is.setByteStream(source.getInputStream());
            is.setCharacterStream(source.getReader());
            Document theDocument = builder.parse(is);
            return theDocument;
        }
    } // ends DocumentFactory

    /**
     * Inner class to apply XSL transformations to serialize DOM document into
     * String or files
     */
    static public class DocumentTransform {

        /**
         * write a DOM Document in a XML file
         *
         * @param doc the DOM Document to serialize in a file
         * @param outputFileName the file name to write to
         * @throws Exception
         */
        public static void writeDoc(Document doc, String outputFileName) throws Exception {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            DocumentType dt = doc.getDoctype();
            if (dt != null) {
                String pub = dt.getPublicId();
                if (pub != null) {
                    t.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, pub);
                }
                t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dt.getSystemId());
            }
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); // NOI18N

            t.setOutputProperty(OutputKeys.INDENT, "yes"); // NOI18N

            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // NOI18N

            Source source = new DOMSource(doc);
            Result result = new StreamResult(new FileOutputStream(outputFileName));
            t.transform(source, result);

        }

        /**
         * Cette m??thode applique une transformation ?? un Document DOM et
         * renvoie le document transform?? sous la forme d'une chaine de
         * caract??res (String).
         *
         * @param doc the DOM document to process by XSLT
         * @param t a transformer to be used to process the Document doc
         * @return a String resulting from the XSL transformation of doc
         * @throws TransformerException
         */
        public static String fromTransformation(Transformer t, Document doc) throws TransformerException {
            // r??gle l'encodage du transformateur
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            // Cr??e un r??sultat de type StreamResult (flux) pour stocker le r??sulat de la transformation
            StreamResult result = new StreamResult(new StringWriter());
            // Convertit le document DOM en source exploitable par le transformateur
            DOMSource source = new DOMSource(doc);

            // Transforme le document et stocke le r??sultat dans result
            t.transform(source, result);

            // convertit le r??sultat (StreamResult) en String renvoy?? par la m??thode
            String xmlString = result.getWriter().toString();
            return xmlString;
        }

        /**
         * Cette m??thode transforme un document DOM en XML.
         *
         * @param doc the DOM document to process by XSLT
         * @return a String resulting from the default transformation of doc, as
         * to say it is serialized into a XML String
         * @throws TransformerConfigurationException
         */
        public static String fromDefaultTransformation(Document doc) throws TransformerConfigurationException, TransformerException {
            // Cr??e un transformateur de Documents DOM ?? partir d'une fabrique de transformateurs
            // le transformateur est cr???? par d??faut : il transformera en XML
            Transformer t = TransformerFactory.newInstance().newTransformer();
            // renvoie la chaine de caractere apr??s transformation
            return fromTransformation(t, doc);
        }

        /**
         * Cette m??thode transforme un document DOM par l'interm??diaire d'une
         * feuille de transformation xsl dont le flux source (SourceStream) est
         * fourni en param??tre. Elle renvoie le document transform?? sous la
         * forme d'une chaine de caract??res (String).
         *
         * @param xslStreamSource a stream source providing the XSL stylesheet
         * @param doc the DOM document to process by XSLT
         * @return a String resulting from the XSL transformation of doc as
         * written in the stylesheet provided by the stream source
         * @throws TransformerConfigurationException
         */
        public static String fromXSLTransformation(StreamSource xslStreamSource, Document doc) throws TransformerConfigurationException, TransformerException {
            // Cr??e un transformateur de Documents DOM ?? partir d'une fabrique de transformateurs
            // le transformateur est cr???? ?? partir d'une source provenant de la feuille de transformation XSL, 
            // donc il transformera le Document selon cette feuille XSL
            Transformer t = TransformerFactory.newInstance().newTransformer(xslStreamSource);
            // renvoie la chaine de caractere apr??s transformation
            return fromTransformation(t, doc);
        }

        /**
         * return the String resulting from of an XSL transformation of a source
         * DOM document
         *
         * @param xslFileName name of the file containing the XSL stylesheet
         * @param doc the DOM document to process by XSLT
         * @return a String resulting from the XSL transformation of doc as
         * written in the stylesheet xslFileName
         * @throws Exception
         */
        public static String fromXSLTransformation(String xslFileName, Document doc) throws Exception {
            return fromXSLTransformation(new StreamSource(xslFileName), doc);
        }
    }


    /// Takes a date in XML format (i.e. ????-??-??) and returns a date
    /// in profile format: dd/mm/yyyy
    public static String xmlDateToProfileDate(String xmlDate) {
        String date;
        // r??cup??rer le jour
        date = xmlDate.substring(xmlDate.lastIndexOf("-") + 1, xmlDate.length());
        date += "/";
        // r??cup??rer le mois
        date += xmlDate.substring(xmlDate.indexOf("-") + 1, xmlDate.lastIndexOf("-"));
        date += "/";
        // r??cup??rer l'ann??e
        date += xmlDate.substring(0, xmlDate.indexOf("-"));

        return date;
    }

    /// Takes a date in profile format: dd/mm/yyyy and returns a date
    /// in XML format (i.e. ????-??-??)
    public static String profileDateToXmlDate(String profileDate) {
        String date;
        // R??cup??rer l'ann??e
        date = profileDate.substring(profileDate.lastIndexOf("/") + 1, profileDate.length());
        date += "-";
        // R??cup??rer  le mois
        date += profileDate.substring(profileDate.indexOf("/") + 1, profileDate.lastIndexOf("/"));
        date += "-";
        // R??cup??rer le jour
        date += profileDate.substring(0, profileDate.indexOf("/"));

        return date;
    }
}
