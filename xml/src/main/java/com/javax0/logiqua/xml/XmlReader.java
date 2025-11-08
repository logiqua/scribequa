package com.javax0.logiqua.xml;

import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XmlReader {

    private final String input;

    public static XmlReader of(final String input) {
        return new XmlReader(input);
    }

    private XmlReader(final String input) {
        this.input = input;
    }


    private static final ErrorHandler SILENT_HANDLER = new ErrorHandler() {
        @Override
        public void warning(SAXParseException exception) {
            // Ignore warnings
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            throw exception;
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            throw exception;
        }
    };


    public Document read() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(SILENT_HANDLER);
            return db.parse(new InputSource(new StringReader(input)));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new IllegalArgumentException("There was an XML exception formatting XML", e);
        }
    }


}
