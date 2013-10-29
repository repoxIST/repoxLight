/*
 * Created on 17/Mar/2006
 *
 */
package pt.utl.ist.repox.recordPackage;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import pt.utl.ist.repox.util.XmlUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class RecordRepoxXpathId implements RecordRepox {
    private static final Logger log = Logger.getLogger(RecordRepoxXpathId.class);

    protected Element dom;
    protected XPath idXpath;
    protected boolean isDeleted = false;

    public Object getId() {
        return idXpath.valueOf(dom);
    }

    public Element getDom() {
        return dom;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public RecordRepoxXpathId() {
    }

    public RecordRepoxXpathId(Element dom, XPath idXpath) {
        this.dom = dom;
        this.idXpath = idXpath;
    }

    public RecordRepoxXpathId(Element dom, XPath idXpath, boolean isDeleted) {
        this(dom, idXpath);
        this.isDeleted = isDeleted;
    }

    public byte[] serialize() {
        try {
            if (dom == null) {
                return null;
            } else {
                byte[] domToBytes = dom.asXML().getBytes("UTF-8");
                return domToBytes;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deserialize(byte[] bytes) throws DocumentException, UnsupportedEncodingException {
        dom = XmlUtil.getRootElement(bytes);
    }
}
