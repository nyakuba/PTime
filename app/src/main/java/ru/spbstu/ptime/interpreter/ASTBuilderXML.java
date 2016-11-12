package ru.spbstu.ptime.interpreter;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

class ASTBuilderXMLException extends Exception {}

public class ASTBuilderXML implements ASTBuilder {
    public static DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
    private Program program;
    public ASTBuilderXML() {
        program = null;
    }
    private ASTNode parseNodeList(final NodeList lst) throws ASTBuilderXMLException {
        int len = lst.getLength();
        int i = 0;
        while (i < len && lst.item(i).getNodeType() != Node.ELEMENT_NODE)
            ++i;
        if (i == len)
            throw new ASTBuilderXMLException(); /* Среди нод списка нет тегов */
        ASTNode root = parseNode(lst.item(i)), cur = root, next;
        for (++i; i < len; ++i) {
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
                            Date current = new Date();  /* Текущее время */
                            Calendar calendar = new GregorianCalendar();
                            String hms[] = attr.getValue().split(":");
                            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hms[0])); /* Заданные часы */
                            calendar.set(Calendar.MINUTE, Integer.parseInt(hms[1]));      /* минуты */
                            calendar.set(Calendar.SECOND, Integer.parseInt(hms[2]));      /* секунды */
                            if (current.after(calendar.getTime()))  /* Если сегодня заданное время уже не наступит, */
                                calendar.roll(Calendar.DATE, true); /* переносим на завтра. */
                            astNode = new ASTTimerByTimeNode(calendar.getTime());
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
        catch (NumberFormatException e) {
            throw new ASTBuilderXMLException();
        }
        return astNode;
    }
    public void build(InputStream stream) {
        try {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(stream);
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
