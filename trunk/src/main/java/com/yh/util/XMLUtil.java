package com.yh.util;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import org.apache.commons.betwixt.BindingConfiguration;
import org.apache.commons.betwixt.IntrospectionConfiguration;
import org.apache.commons.betwixt.expression.Context;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.strategy.DecapitalizeNameMapper;
import org.apache.commons.betwixt.strategy.DefaultObjectStringConverter;
import org.apache.commons.betwixt.strategy.HyphenatedNameMapper;
import org.xml.sax.SAXException;

public class XMLUtil {

    public static <T> T xmlToObject(String content, Class<T> clazz)
            throws IntrospectionException, IOException, SAXException {

        //String className = clazz.getSimpleName();
        //content = content + "</" + class
        StringReader xmlReader  = new StringReader(content);
        BeanReader reader = new BeanReader();
        reader.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(false);
        BindingConfiguration bc = reader.getBindingConfiguration();
        bc.setObjectStringConverter(new DateConverter());
        bc.setMapIDs(false);
        T obj = null;
        reader.registerBeanClass(clazz.getSimpleName().toLowerCase(), clazz);
        obj = (T) reader.parse(xmlReader);
        xmlReader.close();
        return obj;
    }

    public static String objectToxml(Object obj) throws IOException, SAXException, IntrospectionException {
        if (obj == null)
            throw new IllegalArgumentException("给定的参数不能为null！");
        StringWriter sw = new StringWriter();
        //sw.write(xmlHead);// 写xml文件头
        BeanWriter writer = new BeanWriter(sw);
        IntrospectionConfiguration config = writer.getXMLIntrospector().getConfiguration();
        BindingConfiguration bc = writer.getBindingConfiguration();
        bc.setObjectStringConverter(new DateConverter());
        bc.setMapIDs(false);
        config.setAttributesForPrimitives(false);
        config.setAttributeNameMapper(new HyphenatedNameMapper());
        config.setElementNameMapper(new DecapitalizeNameMapper());
        writer.enablePrettyPrint();
        writer.write(obj.getClass().getSimpleName().toLowerCase(), obj);
        writer.close();
        return sw.toString();
    }

    public static void main(String[] args) throws IOException, SAXException, IntrospectionException {
        Error error = new Error();
//<error><ret>0</ret><message></message><skey>@crypt_d60d32d2_687034280f36b619bef39acffee51dde</skey><wxsid>j0wslPKPN9xpaNvh</wxsid><wxuin>1690000439</wxuin><pass_ticket>9p6ysnFGyhFZ1TPW5DLEvjXQVUAx%2Fh%2B8BmmJB%2FxGnu6EAGJiWSlfviMYxKFUEuUM</pass_ticket><isgrayscale>1</isgrayscale></error>
        System.out.println(JSONUtil.toJSONString(xmlToObject("<error><ret>0</ret><message></message><skey>@crypt_d60d32d2_687034280f36b619bef39acffee51dde</skey><wxsid>j0wslPKPN9xpaNvh</wxsid><wxuin>1690000439</wxuin><pass_ticket>9p6ysnFGyhFZ1TPW5DLEvjXQVUAx%2Fh%2B8BmmJB%2FxGnu6EAGJiWSlfviMYxKFUEuUM</pass_ticket><isgrayscale>1</isgrayscale></error>", Error.class)));
        System.out.println(objectToxml(error));
    }
}

/**
 * 日期转换，主要是解决日期为null或者空字符串解析报错问题
 *
 * @author LiuJunGuang
 * @date 2013年12月31日下午6:56:49
 */
class DateConverter extends DefaultObjectStringConverter {
    private static final long serialVersionUID = -197858851188189916L;

    @Override
    @SuppressWarnings("rawtypes")
    public String objectToString(Object object, Class type, String flavour, Context context) {
        return super.objectToString(object, type, flavour, context);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Object stringToObject(String string, Class type, String flavour, Context context) {
        if (string == null || "".equals(string))
            return null;
        return super.stringToObject(string, type, flavour, context);
    }

}