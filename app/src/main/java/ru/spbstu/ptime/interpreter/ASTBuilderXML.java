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
    private ASTNode parseNodeList(final NodeList lst) throws ASTBuilderXMLException {
        int len = lst.getLength();
        int i = 0;
        while (i < len && lst.item(i).getNodeType() != Node.ELEMENT_NODE)
            ++i;
        if (i == len)
            throw new ASTBuilderXMLException(); /* Среди нод списка нет тегов */
        ASTNode root = parseNode(lst.item(i)), cur = root, next;
        for (++i; i < lst.getLength(); ++i) {
            Node item = lst.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                next = parseNode(item);
                cur.setNext(next);
                cur = next;
            }
        }
        return root;
    }
    private ASTNode parseNode(final Node xmlNode) throws ASTBuilderXMLException {
        ASTNode astNode;
        Attr attr;
        try {
            switch (xmlNode.getNodeName()) {
                case "timer":
                    if (!xmlNode.hasAttributes()) /* Нет атрибутов у <timer> */
                        throw new ASTBuilderXMLException();
                    attr = (Attr)xmlNode.getAttributes().item(0);
                    switch (attr.getName()) {
                        case "time": /* Таймер задан чч:мм:сс */
                            /* TO-DO:
                             * Учесть текущий день. Сейчас дата по дефолту устанавливается на Jan 01 1970 MSK,
                              * хоть и с верным временем дня. */
                            astNode = new ASTTimerByTimeNode(dateFormat.parse(attr.getValue()));
                            break;
                        case "interval": /* Таймер задан интервалом в секундах */
                            astNode = new ASTTimerByIntervalNode(Long.parseLong(attr.getValue()));
                            break;
                        default: /* Какой-то другой аттрибут O_o */
                            throw new ASTBuilderXMLException();
                    }
                    break;
                case "stopwatch":
                    astNode = new ASTStopwatchNode();
                    break;
                case "loop":
                    int iterations = 0;
                    if (!xmlNode.hasAttributes())
                        iterations = -1; /* Нет аттрибутов, не указано число итераций = бесконечный цикл */
                    attr = (Attr) xmlNode.getAttributes().item(0);
                    if (!"iterations".equals(attr.getNodeName())) /* Имя первого атрибута не "iterations" */
                        throw new ASTBuilderXMLException();
                    iterations = Integer.parseInt(attr.getValue());
                    if (iterations < -1 || !xmlNode.hasChildNodes())
                        /* Отрицательное количество итераций или между открывающим и закрывающим тегами пусто. */
                        throw new ASTBuilderXMLException();
                    NodeList lst = xmlNode.getChildNodes();
                    ASTNode inner = parseNodeList(lst);
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
                throw new ASTBuilderXMLException(); /* Первый тег -- не <program> */
            Attr attribute = (Attr) xmlNode.getAttributes().item(0);
            if (!"name".equals(attribute.getName())) /* Первый аттрибут тега -- не name */
                throw new ASTBuilderXMLException();
            String programName = attribute.getValue();
            if (!xmlNode.hasChildNodes()) /* Программа пуста */
                throw new ASTBuilderXMLException();
            NodeList programNodes = xmlNode.getChildNodes(); /* Список нод между тегами <program> ... </program> */
            ASTNode body = parseNodeList(programNodes); /* Корень синтаксического дерева */
            program = new Program(programName, body);
        }
        catch (ASTBuilderXMLException|IOException|ParserConfigurationException|SAXException e) {
            program = null;
        }
    }
    public Program getProgram() {
        return program;
    }
}
