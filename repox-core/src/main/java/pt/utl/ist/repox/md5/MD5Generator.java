package pt.utl.ist.repox.md5;

import java.security.MessageDigest;

public class MD5Generator {
    String message;

    public String MD5(String message) {
        this.message = message;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            StringBuffer sb = new StringBuffer();
            byte[] md5 = md.digest(message.getBytes());

            for (byte aMd5 : md5) {
                String tmpStr = "0" + Integer.toHexString((0xff & aMd5));
                sb.append(tmpStr.substring(tmpStr.length() - 2));
            }

            return (sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }
}