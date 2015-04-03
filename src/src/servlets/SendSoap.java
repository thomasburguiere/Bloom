/**
 * src.servlets
 * SendSoap
 * TODO
 */
package src.servlets;

import java.net.*;
import java.io.*;

public class SendSoap {


    public SendSoap(){

    }

    public void init() throws Exception{
	String soapMessage = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:oca=\"http://oca\">"
		+ " <soap:Header/>"
		+ "  <soap:Body>"
		+ " <oca:sayHello>"
		+ "  <name>Olivier</name>"
		+ " </oca:sayHello>"
		+ "</soap:Body>" + "</soap:Envelope>";

	String res = sendSOAP("http://localhost:8080/manage/", soapMessage);
	System.out.println(res);
    }

    public static String sendSOAP(String SOAPUrl, String soapMessage)
	    throws Exception {
	URL url = new URL(SOAPUrl);
	URLConnection connection = url.openConnection();
	HttpURLConnection httpConn = (HttpURLConnection) connection;


	byte[] byteArray = soapMessage.getBytes("UTF-8");

	httpConn.setRequestProperty("Content-Length", String
		.valueOf(byteArray.length));
	httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
	httpConn.setRequestProperty("SOAPAction", "");
	httpConn.setRequestMethod("POST");

	httpConn.setDoOutput(true);
	httpConn.setDoInput(true);

	OutputStream out = httpConn.getOutputStream();
	out.write(byteArray);
	out.close();
	BufferedReader in = null;
	StringBuffer resultMessage= new StringBuffer();
	try {
	    InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
	    in = new BufferedReader(isr);
	    String inputLine;
	    while ((inputLine = in.readLine()) != null) {
		resultMessage.append(inputLine);
	    }

	} finally {
	    if (in != null) {
		in.close();
	    }
	}
	return resultMessage.toString();
    }
}