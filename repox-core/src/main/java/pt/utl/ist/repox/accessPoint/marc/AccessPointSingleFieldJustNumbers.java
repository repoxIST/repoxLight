package pt.utl.ist.repox.accessPoint.marc;


import org.apache.log4j.Logger;

public class AccessPointSingleFieldJustNumbers extends AccessPointSingleField {
    private static final Logger log = Logger.getLogger(AccessPointSingleFieldJustNumbers.class);

    public AccessPointSingleFieldJustNumbers(String id, int tag, Character subfield) {
        super(id, tag, subfield);
    }

    public AccessPointSingleFieldJustNumbers(String id, int tag, Character subfield, boolean tokenizable) {
        super(id, tag, subfield, tokenizable);
    }

    public String indexValue(String value) {
        StringBuffer sb = new StringBuffer();
        char[] chars = value.toCharArray();
        boolean lastWasDigit = false;
        for (char aChar : chars) {
            if (Character.isDigit(aChar)) {
                sb.append(aChar);
                lastWasDigit = true;
            } else {
                if (lastWasDigit)
                    sb.append(' ');
                lastWasDigit = false;
            }
        }
        return sb.toString().trim();
    }
}
