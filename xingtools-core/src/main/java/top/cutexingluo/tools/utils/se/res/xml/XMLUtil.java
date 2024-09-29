package top.cutexingluo.tools.utils.se.res.xml;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.XmlUtil;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * 解析xml文件
 * 可以用 JDOM，DOM4j等
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/9/15 14:21
 */
public class XMLUtil {

    public static boolean printTrace = true;
    public static Consumer<Exception> exceptionHandler = null;


    /**
     * hutool方式
     */
    public static class Hu {
        /**
         * 读取XML文档
         */
        public static Document getDocument(String path) {
            return XmlUtil.readXML(FileUtil.file(path));
        }

        /**
         * 保存xml文档
         */
        public static void save(Document target, String path) {
            XmlUtil.toFile(target, path, CharsetUtil.UTF_8);
        }
    }

    /**
     * DOM 方式，整体读入
     */
    public static class DOM {
        /**
         * 获取xml文件得到对象
         *
         * @return {@link Document}
         */
        public static Document getDocumentByStream(String path) throws ParserConfigurationException, IOException, SAXException {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
        }

        /**
         * 获取xml文件得到对象
         *
         * @return {@link Document}
         */
        public static Document getDocument(String path) throws ParserConfigurationException, IOException, SAXException {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(path);
        }

        /**
         * 将对象写入文件中
         */
        public static boolean saveDocument(Document target, File tarFile) throws TransformerException {
            if (target == null) {
                return false;
            }
            DOMSource source = new DOMSource(target);
            StreamResult result = new StreamResult(tarFile);
            TransformerFactory.newInstance().newTransformer().transform(source, result);
            return true;
        }
    }

    /**
     * 推读取
     */
    public static class SAX {
        /**
         * 执行读取xml
         */
        public static <T extends DefaultHandler> T read(String path, T handler) throws IOException, SAXException {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            reader.parse(path);
            return handler;
        }
    }

    /**
     * 拉读取
     */
    public static class Stax {
        public interface StaxStreamHandler {
            /**
             * 处理reader
             *
             * @param streamReader 流读取器
             * @param event        事件, 使用 XMLStreamConstants 常量比较
             */
            void handle(XMLStreamReader streamReader, int event);
        }

        public interface StaxEventHandler {
            /**
             * 处理事件
             *
             * @param eventReader 事件读取器
             * @param event       事件, 使用 event 的方法进行操作
             */
            void handle(XMLEventReader eventReader, XMLEvent event);
        }


        /**
         * 按流读取
         *
         * @param path    文件路径
         * @param handler 处理器
         */
        public static void readByStream(String path, StaxStreamHandler handler) {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
            XMLStreamReader xmlStreamReader = null;
            try {
                xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileReader(path));
            } catch (XMLStreamException | FileNotFoundException e) {
                if (exceptionHandler != null) exceptionHandler.accept(e);
                else if (printTrace) e.printStackTrace();
            }
            try {
                assert xmlStreamReader != null;
                while (xmlStreamReader.hasNext()) {
                    int event = xmlStreamReader.next();
                    handler.handle(xmlStreamReader, event);
                }
                xmlStreamReader.close();
            } catch (XMLStreamException e) {
                if (exceptionHandler != null) exceptionHandler.accept(e);
                else if (printTrace) e.printStackTrace();
            }
        }

        /**
         * 按事件读取
         *
         * @param path    文件路径
         * @param handler 处理器
         */
        public static void readByEvent(String path, StaxEventHandler handler) {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
            XMLEventReader xmlEventReader = null;
            try {
                xmlEventReader = xmlInputFactory.createXMLEventReader(new FileReader(path));
            } catch (XMLStreamException | FileNotFoundException e) {
                if (exceptionHandler != null) exceptionHandler.accept(e);
                else if (printTrace) e.printStackTrace();
            }
            try {
                assert xmlEventReader != null;
                while (xmlEventReader.hasNext()) {
                    XMLEvent event = xmlEventReader.nextEvent();
                    handler.handle(xmlEventReader, event);
                }
                xmlEventReader.close();
            } catch (XMLStreamException e) {
                if (exceptionHandler != null) exceptionHandler.accept(e);
                else if (printTrace) e.printStackTrace();
            }
        }
    }


}
