package com.freelycar.saas.wxutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class XMLParser {

    private static final Logger logger = LogManager.getLogger(XMLParser.class);

    /**
     * 将map包含的参数封装成xml格式的字符串
     *
     * @param map
     * @return
     */
    public static String getXmlFromMap(Map<String, Object> map) {
        Object[] key_arr = map.keySet().toArray();
        StringBuilder xml = new StringBuilder("<xml>");
        for (Object key : key_arr) {
            xml.append("<").append(key).append(">").append(map.get(key)).append("</").append(key).append(">");
        }
        xml.append("</xml>");
        return xml.toString();
    }

    /**
     * 将xml格式的字符串封装成对应键值对形式的map
     *
     * @param xmlString
     * @return
     */
    public static Map<String, Object> getMapFromXML(String xmlString) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //解决安全问题：XML外部实体注入漏洞（XXE）  参考：https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=23_5
        //add by tangwei    2018-08-14
        String feature = null;
        try {
            //------- XXE漏洞修复 start----------

            //（1）完全禁用DTDs
            // This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
            // Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
//			feature = "http://apache.org/xml/features/disallow-doctype-decl";
//			factory.setFeature(feature, true);

            //（2）部分禁用（禁用外部DTDs）  ---start----
            // If you can't completely disable DTDs, then at least do the following:
            // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
            // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
            // JDK7+ - http://xml.org/sax/features/external-general-entities
            feature = "http://xml.org/sax/features/external-general-entities";
            factory.setFeature(feature, false);

            // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
            // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
            // JDK7+ - http://xml.org/sax/features/external-parameter-entities
            feature = "http://xml.org/sax/features/external-parameter-entities";
            factory.setFeature(feature, false);

            // Disable external DTDs as well
            feature = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
            factory.setFeature(feature, false);

            // and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);
            //部分禁用（禁用外部DTDs）  ---end----

            //------- XXE漏洞修复 end----------


            //解析逻辑
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream is = getStringStream(xmlString);
            if (is == null) {
                return map;
            }
            Document document = builder.parse(is);

            // 获取到document里面的全部结点
            NodeList allNodes = document.getFirstChild().getChildNodes();
            Node node;
            int i = 0;
            while (i < allNodes.getLength()) {
                node = allNodes.item(i);
                if (node instanceof Element) {
                    map.put(node.getNodeName(), node.getTextContent());
                }
                i++;
            }
        } catch (DOMException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            logger.info("ParserConfigurationException was thrown. The feature '" + feature + "' is probably not supported by your XML processor.");
        } catch (SAXException e) {
            e.printStackTrace();
            // On Apache, this should be thrown when disallowing DOCTYPE
            logger.warn("A DOCTYPE was passed into the XML document");
        } catch (IOException e) {
            e.printStackTrace();
            // XXE that points to a file that doesn't exist
            logger.error("IOException occurred, XXE may still possible: " + e.getMessage());
        }
        return map;

    }

    public static InputStream getStringStream(String sInputString) {
        ByteArrayInputStream tInputStringStream = null;
        if (sInputString != null && !sInputString.trim().equals("")) {
            tInputStringStream = new ByteArrayInputStream(
                    sInputString.getBytes(StandardCharsets.UTF_8));
        }
        return tInputStringStream;
    }

    /* 将请求转成xml格式的字符串 */
    public static Map<String, Object> requestToXml(HttpServletRequest request) {
        try {
            StringBuffer sb = new StringBuffer(1000);
            InputStream is = request.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            String xml = sb.toString(); // 次即为接收到微信端发送过来的xml数据
            return getMapFromXML(xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
