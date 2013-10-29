/*
 * DOMBuilder.java
 *
 * Created on 4 de Janeiro de 2002, 10:44
 */

package pt.utl.ist.marc.xml;

import org.apache.log4j.Logger;
import org.dom4j.*;
import pt.utl.ist.marc.Field;
import pt.utl.ist.marc.Record;
import pt.utl.ist.marc.RecordType;
import pt.utl.ist.marc.Subfield;

import java.util.Iterator;
import java.util.List;

/**
 * Utility class to code Marc records in XML
 *
 * @author Nuno Freire
 */
public class MarcXChangeDom4jBuilder {
    private static Logger log = Logger.getLogger(MarcXChangeDom4jBuilder.class);

    public static Document record2Dom(Record rec, String marcFormat) {
        Document ret = DocumentHelper.createDocument();
        Element tmpRoot = ret.addElement("tmp");
        Element recEl = record2DomElement(rec, tmpRoot, null, marcFormat);
        ret.remove(tmpRoot);
        ret.add(recEl);
        return ret;
    }

    public static Element record2DomElement(Record rec, Element parentElem, String marcFormat) {
        return record2DomElement(rec, parentElem, null, marcFormat);
    }

    public static Element record2DomElement(Record rec, Element parentElem, String namespacePrefix, String marcFormat) {
        Namespace namespace = new Namespace(namespacePrefix == null ? "mx" : namespacePrefix, "info:lc/xmlns/marcxchange-v1");
//    	Namespace namespace=new Namespace(namespacePrefix==null ? "mx" : namespacePrefix , "http://www.bs.dk/standards/MarcXchange");
        Element root = parentElem.addElement(new QName("record", namespace));
        //root.addAttribute("format", "Unimarc");

        if (marcFormat != null && !marcFormat.isEmpty() && !marcFormat.equalsIgnoreCase("None")) {
            if (marcFormat.equals("MARC21")) {
                marcFormat = "MARC 21";
            }
            root.addAttribute(new QName("format", namespace), marcFormat);
        }
        if (rec.getRecordType() != null) {
            if (rec.getRecordType() == RecordType.BIBLIOGRAPHIC)
                root.addAttribute("type", "bibliographic");
            else
                root.addAttribute("type", "authority");
        }
        List fields = rec.getFields();


        Element leadElem = root.addElement(new QName("leader", namespace));
        if (rec.getLeader() == null)
            leadElem.setText(Record.DEFAULT_LEADER);
        else
            leadElem.setText(rec.getLeader());

        // append control fields to directory and data
        boolean inDataFields = false;
        QName controlFieldQName = new QName("controlfield", namespace);
        QName dataFieldQName = new QName("datafield", namespace);
        QName subfieldQName = new QName("subfield", namespace);
        for (Object field : fields) {
            Field f = (Field) field;
            if (f.isControlField()) {
                if (inDataFields) {
                    log.warn("Datafields and controlfields not sorted: " + rec + ". Sorting them.");
                    parentElem.remove(root);
                    Record sortedRec = new Record();
                    sortedRec.setLeader(rec.getLeader());
                    sortedRec.setNc(rec.getNc());
                    sortedRec.setRecordType(rec.getRecordType());
                    for (Field fld : rec.getFields()) {
                        sortedRec.addField(fld);
                    }
                    return record2DomElement(sortedRec, parentElem, namespacePrefix);
                }
                Element el = root.addElement(controlFieldQName);
                el.addAttribute("tag", f.getTagAsString());
//	        	el.addAttribute(tagQName, f.getTagAsString());   
                el.addText(f.getValue());
//                byte[] bytes=.getBytes();
//                String data=new String(bytes,System.getProperty("file.encoding"));
//                el.appendChild(document.createTextNode(data));
            } else {
                inDataFields = true;
                Element el = root.addElement(dataFieldQName);
                el.addAttribute("tag", f.getTagAsString());
                el.addAttribute("ind1", String.valueOf(f.getInd1()));
                el.addAttribute("ind2", String.valueOf(f.getInd2()));
//	        	el.addAttribute(tagQName, f.getTagAsString());   
//	        	el.addAttribute(ind1QName, String.valueOf(f.getInd1()));   
//	        	el.addAttribute(ind2QName, String.valueOf(f.getInd2()));                    
                for (Object o : f.getSubfields()) {
                    Subfield sf = (Subfield) o;
                    Element elsf = el.addElement(subfieldQName);
                    elsf.addAttribute("code", String.valueOf(sf.getCode()));
//                    elsf.addAttribute(codeQName, String.valueOf(sf.getCode()));
                    elsf.addText(sf.getValue());
                }
            }
        }
        return root;
    }


    public static Record parseRecord(Element recEl) {
        Record rec = new Record();
        if (recEl.attribute("type") != null) {
            rec.setRecordType(RecordType.valueOf(recEl.attributeValue("type").toUpperCase()));
        }

        for (Iterator<Element> i = recEl.elementIterator(); i.hasNext(); ) {
            Element fieldEl = i.next();
            if (fieldEl.getName().equals("leader"))
                rec.setLeader(fieldEl.getText());
            else if (fieldEl.getName().equals("controlfield")) {
                Field f = rec.addField(Integer.parseInt(fieldEl.attributeValue("tag")));
                f.setValue(fieldEl.getText());
                if (f.getTag() == 001) {
                    rec.setNc(f.getValue());
                }
            } else if (fieldEl.getName().equals("datafield")) {
                Field f = rec.addField(Integer.parseInt(fieldEl.attributeValue("tag")));

                Attribute ai1 = fieldEl.attribute("ind1");
                if (ai1 != null && ai1.getValue().length() > 0)
                    f.setInd1(ai1.getValue().charAt(0));
                else
                    f.setInd1(' ');
                Attribute ai2 = fieldEl.attribute("ind2");
                if (ai2 != null && ai2.getValue().length() > 0)
                    f.setInd2(ai2.getValue().charAt(0));
                else
                    f.setInd2(' ');

                for (Iterator<Element> isf = fieldEl.elementIterator(); isf.hasNext(); ) {
                    Element sfEl = isf.next();
                    if (sfEl.getName().equals("subfield")) {
                        Subfield sf = f.addSubfield(sfEl.attributeValue("code").charAt(0));
                        sf.setValue(sfEl.getText());
                    }
                }
            }
        }
        return rec;
    }
}
