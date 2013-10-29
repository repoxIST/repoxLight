/* Client.java - created on 19 de Abr de 2013, Copyright (c) 2011 The European Library, all rights reserved */
package harvesterUI.server.sru;

import org.w3c.dom.Document;

import javax.xml.soap.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;

/**
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 19 de Abr de 2013
 */
public class TestClient {
    static String testUrl = "http://127.0.0.1:8888/srurecordupdate/recUpdate";

    public static void main(String[] args) throws Exception {
        testDeleteRequest();
    }

    public static void testUpdateRequest() throws Exception {
        SOAPConnection connection;

        SOAPConnectionFactory connectionFactory =
                SOAPConnectionFactory.newInstance();
        connection = connectionFactory.createConnection();

        String outString = "";

        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            Document soapMessageDom = XmlUtil.parseDomFromFile(new File("example3.xml"));
            SOAPMessage outgoingMessage = SoapUtil.toSOAPMessage(soapMessageDom);

            SOAPPart soappart = outgoingMessage.getSOAPPart();
            SOAPEnvelope envelope = soappart.getEnvelope();
            SOAPHeader header = envelope.getHeader();
            SOAPBody body = envelope.getBody();

            URL client = new URL(testUrl);

            outString += "SOAP outgoingMessage sent\n";

            SOAPMessage incomingMessage = connection.
                    call(outgoingMessage, client);

            if (incomingMessage != null) {
                ByteArrayOutputStream incomingFile = new ByteArrayOutputStream();
                incomingMessage.writeTo(incomingFile);
                incomingFile.close();
                outString +=
                        "SOAP outgoingMessage received:\n" + new String(incomingFile.toByteArray());
            }

            System.out.println(outString);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }


    public static void testDeleteRequest() throws Exception {
        SOAPConnection connection;

        SOAPConnectionFactory connectionFactory =
                SOAPConnectionFactory.newInstance();
        connection = connectionFactory.createConnection();

        String outString = "";

        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            Document soapMessageDom = XmlUtil.parseDomFromFile(new File("example_delete.xml"));
            SOAPMessage outgoingMessage = SoapUtil.toSOAPMessage(soapMessageDom);

            SOAPPart soappart = outgoingMessage.getSOAPPart();
            SOAPEnvelope envelope = soappart.getEnvelope();
            SOAPHeader header = envelope.getHeader();
            SOAPBody body = envelope.getBody();

            URL client = new URL(testUrl);

            outString += "SOAP outgoingMessage sent\n";

            SOAPMessage incomingMessage = connection.
                    call(outgoingMessage, client);

            if (incomingMessage != null) {
                ByteArrayOutputStream incomingFile = new ByteArrayOutputStream();
                incomingMessage.writeTo(incomingFile);
                incomingFile.close();
                outString +=
                        "SOAP outgoingMessage received:\n" + new String(incomingFile.toByteArray());
            }

            System.out.println(outString);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}
