package ru.spbstu.ptime.interpreter;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

class ASTBuilderXMLException extends Exception {}

public class ASTBuilderXML implements ASTBuilder {
    private Program program;
    private DateFormat dateFormat;
    public ASTBuilderXML() {
        program = null;
        dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    }
    private Node nextElement(final Node xmlNode) throws ASTBuilderXMLException {
        NodeList lst = xmlNode.getChildNodes();
        int i;
        for (i = 0; i < lst.getLength() && lst.item(i).getNodeType() != Node.ELEMENT_NODE; ++i);
        if (i == lst.getLength())
            throw new ASTBuilderXMLException();
        return lst.item(i);
//        Node cur = xmlNode.getFirstChild();
//        while (cur != null && cur.getNodeType() != Node.ELEMENT_NODE)
//            cur = cur.getFirstChild();
//        if (cur == null)
//            throw new ASTBuilderXMLException();
//        return cur;
    }
    private ASTNode parseNode(final Node xmlNode) throws ASTBuilderXMLException {
        ASTNode astNode;
        Attr attr;
        try {
            switch (xmlNode.getNodeName()) {
                case "timer":
                    if (!xmlNode.hasAttributes())
                        throw new ASTBuilderXMLException();
                    attr = (Attr) xmlNode.getAttributes().item(0);
                    switch (attr.getName()) {
                        case "time":
                            astNode = new ASTTimerByTimeNode(dateFormat.parse(attr.getValue()));
                            break;
                        case "interval":
                            astNode = new ASTTimerByIntervalNode(Long.parseLong(attr.getValue()));
                            break;
                        default:
                            throw new ASTBuilderXMLException();
                    }
                    break;
                case "stopwatch":
                    astNode = new ASTStopwatchNode();
                    break;
                case "loop":
                    int iterations = 0;
                    if (!xmlNode.hasAttributes())
                        iterations = -1; /* Бесконечный цикл */
                    attr = (Attr) xmlNode.getAttributes().item(0);
                    if (!"iterations".equals(attr.getNodeName())) /* Имя атрибута не "iterations" */
                        throw new ASTBuilderXMLException();
                    iterations = Integer.parseInt(attr.getValue());
                    if (iterations < -1 || !xmlNode.hasChildNodes())
                        throw new ASTBuilderXMLException();
//                    Node innerXML = nextElement(xmlNode);
//                    ASTNode inner = parseNode(innerXML);
//                    ASTNode cur = inner;
                    NodeList lst = xmlNode.getChildNodes();
                    ASTNode inner = parseNode(lst.item(0)), cur = inner;
                    ASTNode nextNode;
                    for (int i = 1; i < lst.getLength(); ++i) {
                        Node item = lst.item(i);
                        if (item.getNodeType() == Node.ELEMENT_NODE) {
                            nextNode = parseNode(item);
                            cur.setNext(nextNode);
                            cur = nextNode;
                        }
                    }
                    astNode = new ASTLoopNode(inner, iterations);
                    break;
                default:
                    throw new ASTBuilderXMLException();
            }
        }
        catch (ParseException|NumberFormatException e) {
            throw new ASTBuilderXMLException();
        }
        return astNode;
    }
    public void build(File file) {
        try {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            Node xmlNode = doc.getDocumentElement();
            if (!"program".equals(xmlNode.getNodeName()) || !xmlNode.hasAttributes())
                throw new ASTBuilderXMLException();
            Attr attribute = (Attr) xmlNode.getAttributes().item(0);
            if (!"name".equals(attribute.getName()))
                throw new ASTBuilderXMLException();
            String programName = attribute.getValue();
            if (xmlNode.hasChildNodes())
                xmlNode = nextElement(xmlNode);
            else
                throw new ASTBuilderXMLException();
            ASTNode root = parseNode(xmlNode);
            ASTNode current = root, next;
            NodeList lst = xmlNode.getChildNodes();
            for (int i = 0; i < lst.getLength(); ++i) {
                if (xmlNode.getNodeType() == Node.ELEMENT_NODE) {
                    next = parseNode(xmlNode);
                    current.setNext(next);
                    current = next;
                }
            }
            program = new Program(programName, root);
        }
        catch (ASTBuilderXMLException|IOException|ParserConfigurationException|SAXException e) {
            program = null;
        }
    }
    public Program getProgram() {
        return program;
    }
}
