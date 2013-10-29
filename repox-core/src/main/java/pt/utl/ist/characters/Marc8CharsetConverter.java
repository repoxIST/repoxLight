package pt.utl.ist.characters;

import pt.utl.ist.marc.Field;
import pt.utl.ist.marc.Record;
import pt.utl.ist.marc.Subfield;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Marc8CharsetConverter {

    private byte[][] doubleCharsOld;
    private byte[][] doubleCharsNew;
    private byte[] charsOld;
    private byte[] charsNew;
    private String[] stringsOld;
    private String[] stringsNew;
    private byte sepI;
    private byte sepF;

    public Marc8CharsetConverter() {
        init();
    }

    public byte[] convertBytes(byte[] array) {
        byte[] ret = new byte[array.length];
        int r = 0;
        for (int k = 0; k < array.length; k++) {
            if (array[k] == sepI) {
                boolean ends = false;
                int sc = k + 1;
                for (; sc < array.length; sc++) {
                    if (array[sc] == sepF) {
                        ends = true;
                        break;
                    }
                }
                if (ends) {
                    k = sc;
                } else {
                    ret[r] = array[k];
                    r++;
                }
            } else {
                Byte newChar = null;
                if (k < array.length - 1)
                    newChar = findCharAtDouble(array[k], array[k + 1]);
                if (newChar == null)
                    newChar = findChar(array[k]);
                else
                    k++;
                if (newChar != null) {
                    ret[r] = newChar.byteValue();
                } else {
                    ret[r] = array[k];
                }
                r++;
            }
        }

        //remove possible extra characters 0x00 in the array
        int realsz = ret.length;
        byte empty = getByte("0x00");
        for (int idx = ret.length - 1; idx >= 0; idx--) {
            if (ret[idx] == empty) {
                realsz = idx;
            } else
                break;
        }
        if (realsz != ret.length) {
            byte[] newret = new byte[realsz];
            for (int idx = realsz - 1; idx >= 0; idx--) {
                newret[idx] = ret[idx];
            }
            ret = newret;
        }
        return ret;
    }

    public String convertString(String str) {
        String ret = null;
        ret = new String(convertBytes(str.getBytes()));
        for (int idx = stringsOld.length - 1; idx >= 0; idx--) {
            Pattern p = Pattern.compile(toSafeRegExp(stringsOld[idx]));
            Matcher m = p.matcher(ret);
            ret = m.replaceAll(stringsNew[idx]);
        }

        Pattern p = Pattern.compile(toSafeRegExp("0x95[^0x96]+0x96"));
        Matcher m = p.matcher(ret);
        ret = m.replaceAll("");

        return ret;
    }


    public void convertRecord(Record rec) {
        List fields = rec.getFields();

        for (Object field : fields) {
            Field f = (Field) field;
            if (f.isControlField()) {
                String newData = convertString(f.getValue());
                f.setValue(newData);
            } else {
                for (Object o : f.getSubfields()) {
                    Subfield sf = (Subfield) o;
                    String newData = convertString(sf.getValue());
                    sf.setValue(newData);
                }
            }
        }
    }

    /**
     * ***********************************************************************
     * ***********                  Private Methods           ******************
     * ***********************************************************************
     */
    private static byte getByte(String b) {
        return Short.decode(b).byteValue();
    }

    private Byte findCharAtDouble(byte c1, byte c2) {
        for (int c = 0; c < doubleCharsOld.length; c++) {
            if (doubleCharsOld[c][0] == c1) {
                for (int i = 0; i < doubleCharsOld[c].length; i++) {
                    if (doubleCharsOld[c][i] == c2)
                        return new Byte(doubleCharsNew[c][i]);
                }
            }
        }
        return null;
    }

    private String toSafeRegExp(String str) {
        Pattern p = Pattern.compile("\\+");
        Matcher m = p.matcher(str);
        str = m.replaceAll("\\\\+");

        p = Pattern.compile("\\*");
        m = p.matcher(str);
        str = m.replaceAll("\\\\*");

        p = Pattern.compile("\\)");
        m = p.matcher(str);
        str = m.replaceAll("\\\\)");

        p = Pattern.compile("\\(");
        m = p.matcher(str);
        str = m.replaceAll("\\\\(");
        return str;
    }

    private Byte findChar(byte ch) {
        for (int i = 0; i < charsOld.length; i++) {
            if (charsOld[i] == ch) {
                return new Byte(charsNew[i]);
            }
        }
        return null;
    }


    private void init() {
        sepI = getByte("0x95");
        sepF = getByte("0x96");

        doubleCharsOld = new byte[4][];
        doubleCharsNew = new byte[4][];

        doubleCharsOld[0] = new byte[3];
        doubleCharsNew[0] = new byte[3];
        doubleCharsOld[0][0] = getByte("0xF0");
        doubleCharsOld[0][1] = getByte("0x63");
        doubleCharsOld[0][2] = getByte("0x43");
        doubleCharsNew[0][0] = getByte("0x00");
        doubleCharsNew[0][1] = getByte("0xe7");//ç
        doubleCharsNew[0][2] = getByte("0xc7");//Ç

        doubleCharsOld[1] = new byte[11];
        doubleCharsNew[1] = new byte[11];
        doubleCharsOld[1][0] = getByte("0xE1");
        doubleCharsOld[1][1] = getByte("0x61");
        doubleCharsOld[1][2] = getByte("0x41");
        doubleCharsOld[1][3] = getByte("0x65");
        doubleCharsOld[1][4] = getByte("0x45");
        doubleCharsOld[1][5] = getByte("0x69");
        doubleCharsOld[1][6] = getByte("0x49");
        doubleCharsOld[1][7] = getByte("0x6F");
        doubleCharsOld[1][8] = getByte("0x4F");
        doubleCharsOld[1][9] = getByte("0x75");
        doubleCharsOld[1][10] = getByte("0x55");
        doubleCharsNew[1][0] = getByte("0x00");
        doubleCharsNew[1][1] = getByte("0xe0");//
        doubleCharsNew[1][2] = getByte("0xc0");//
        doubleCharsNew[1][3] = getByte("0xe8");//
        doubleCharsNew[1][4] = getByte("0xc8");//
        doubleCharsNew[1][5] = getByte("0xec");//
        doubleCharsNew[1][6] = getByte("0xCC");//
        doubleCharsNew[1][7] = getByte("0xF2");//
        doubleCharsNew[1][8] = getByte("0xD2");//
        doubleCharsNew[1][9] = getByte("0x75");//
        doubleCharsNew[1][10] = getByte("0xD9");//

        doubleCharsOld[2] = new byte[13];
        doubleCharsNew[2] = new byte[13];
        doubleCharsOld[2][0] = getByte("0xE2");
        doubleCharsOld[2][1] = getByte("0x61");
        doubleCharsOld[2][2] = getByte("0x41");
        doubleCharsOld[2][3] = getByte("0x65");
        doubleCharsOld[2][4] = getByte("0x45");
        doubleCharsOld[2][5] = getByte("0x69");
        doubleCharsOld[2][6] = getByte("0x49");
        doubleCharsOld[2][7] = getByte("0x6F");
        doubleCharsOld[2][8] = getByte("0x4F");
        doubleCharsOld[2][9] = getByte("0x75");
        doubleCharsOld[2][10] = getByte("0x55");
        doubleCharsOld[2][11] = getByte("0x79");
        doubleCharsOld[2][12] = getByte("0x59");
        doubleCharsNew[2][0] = getByte("0x00");
        doubleCharsNew[2][1] = getByte("0xE1");//
        doubleCharsNew[2][2] = getByte("0xC1");//
        doubleCharsNew[2][3] = getByte("0xE9");//
        doubleCharsNew[2][4] = getByte("0xC9");//
        doubleCharsNew[2][5] = getByte("0xED");//
        doubleCharsNew[2][6] = getByte("0xCD");//
        doubleCharsNew[2][7] = getByte("0xF3");//
        doubleCharsNew[2][8] = getByte("0xD3");//
        doubleCharsNew[2][9] = getByte("0xFA");//
        doubleCharsNew[2][10] = getByte("0xDA");//
        doubleCharsNew[2][11] = getByte("0xFD");//
        doubleCharsNew[2][12] = getByte("0xDD");//

        doubleCharsOld[3] = new byte[7];
        doubleCharsNew[3] = new byte[7];
        doubleCharsOld[3][0] = getByte("0xE4");
        doubleCharsOld[3][1] = getByte("0x61");
        doubleCharsOld[3][2] = getByte("0x41");
        doubleCharsOld[3][3] = getByte("0x6F");
        doubleCharsOld[3][4] = getByte("0x4F");
        doubleCharsOld[3][5] = getByte("0x6E");
        doubleCharsOld[3][6] = getByte("0x4E");
        doubleCharsNew[3][0] = getByte("0x00");
        doubleCharsNew[3][1] = getByte("0xE3");//
        doubleCharsNew[3][2] = getByte("0xC3");//
        doubleCharsNew[3][3] = getByte("0xF5");//
        doubleCharsNew[3][4] = getByte("0xD5");//
        doubleCharsNew[3][5] = getByte("0xF1");//
        doubleCharsNew[3][6] = getByte("0xD1");//

        charsOld = new byte[4];
        charsNew = new byte[4];
        charsOld[0] = getByte("0xE7");//
        charsNew[0] = getByte("0xAA");//
        charsOld[1] = getByte("0xE6");//
        charsNew[1] = getByte("0xBA");//
        charsOld[2] = getByte("0x88");//
        charsNew[2] = getByte("0x3C");//
        charsOld[3] = getByte("0x89");//
        charsNew[3] = getByte("0x3E");//


        stringsOld = new String[2];
        stringsNew = new String[2];
        byte[] tmp = new byte[1];
        stringsOld[0] = "<U+00AA>";
        stringsNew[0] = "ª";
        stringsOld[1] = "<U+00BA>";
        stringsNew[1] = "º";
    }
}
