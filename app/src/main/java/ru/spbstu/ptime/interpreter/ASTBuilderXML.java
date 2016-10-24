package ru.spbstu.ptime.interpreter;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ASTBuilderXML implements ASTBuilder {
    private Program program;
    public ASTBuilderXML() {
        program = null;
    }
    public void build(File file) {
        try {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            Node xmlNode = doc.getDocumentElement();
            String programName = xmlNode.getNodeName();
            ASTNode root, current;
            //...
        }
        catch (Exception e) {
            //...
        }
    }
    public Program getProgram() {
        return program;
    }
}