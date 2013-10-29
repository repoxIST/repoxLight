package pt.utl.ist.repox.util;

import org.apache.log4j.Logger;
import pt.utl.ist.repox.reports.LogUtil;
import pt.utl.ist.util.DateUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Date;

public class StringUtil {
    private static final Logger log = Logger.getLogger(StringUtil.class);

    private static final boolean LOG_TO_LOG4J = true;
    private static final String COLLECTION_SEPARATOR_STRING = " ";

    public static String getArrayAsString(Object[] array) {
        StringBuffer result = new StringBuffer();

        result.append("[");
        for (Object currentObject : array) {
            result.append("<");
            result.append(currentObject);
            result.append(">").append(COLLECTION_SEPARATOR_STRING);
        }
        result.append("]");

        return result.toString();
    }

    public static String getCollectionAsString(Collection collection, Method stringMethod) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        StringBuffer result = new StringBuffer();

        result.append("[");
        for (Object currentObject : collection) {
            result.append("<");
            result.append(stringMethod.invoke(currentObject));
            result.append(">").append(COLLECTION_SEPARATOR_STRING);
        }
        result.append("]");

        return result.toString();
    }

    public static String getFixedSizeNumber(int numberToParse, int numberDigits) {
        String numberToParseAsString = String.valueOf(numberToParse);
        if (numberToParseAsString.length() > numberDigits) {
            return String.valueOf((int) Math.pow(10, numberDigits) - 1);
        } else {
            String resultNumber = numberToParseAsString;
            while (resultNumber.length() < numberDigits) {
                resultNumber = "0" + resultNumber;
            }

            return resultNumber;
        }
    }

    public static String getFileModificationTime(File file) {
        return DateUtil.date2String(new Date(file.lastModified()), TimeUtil.SHORT_DATE_FORMAT);
    }

    public static String encode(String stringToEncode) throws UnsupportedEncodingException {
        return URLEncoder.encode(stringToEncode, "UTF-8");

    }

    public static String sanitizeJavascript(String originalString) {
        return originalString.replaceAll("[\\\\(\"\')\\]\\[]", "\\\\$0");
    }

    public static void simpleLog(String message, Class clazz, File file) {
        LogUtil.addSimpleInfoLog(message, clazz, file, true);
    }

    public static void simpleLog(String message, Class clazz, File file, boolean writeToXML) {
        LogUtil.addSimpleInfoLog(message, clazz, file, writeToXML);
    }

    public static void simpleLog(String message, Exception cause, Class clazz, File file) {
        LogUtil.addSimpleErrorLog(message, clazz, file, cause);
    }
}
