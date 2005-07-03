/*
 * $Id: SMSMailProvider.java,v 1.2 2004/10/13 07:30:43 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.process.sms;

import com.syrus.AMFICOM.corba.portable.common.*;
import java.io.*;
import java.net.*;

/**
 * @verson $Revision: 1.2 $, $Date: 2004/10/13 07:30:43 $
 * @author $Author: bass $
 * @module serverprocess
 */
final class SMSMailProvider implements SMSProvider {
	private static SMSMailProvider instance;

	static {
		instance = new SMSMailProvider();
	}

	private SMSMailProvider() {
	}

	public void sendMessage(String from, String destination, String message) throws MessageDeliveryFailedException {
		if ((from == null) || (from.length() == 0))
			throw new MessageDeliveryFailedException("\"from\" parameter can't be empty", new PortableStackTraceElement[0]);
		if ((destination == null) || (destination.length() == 0))
			throw new MessageDeliveryFailedException("\"destination\" parameter can't be empty", new PortableStackTraceElement[0]);
		if ((message == null) || (message.length() == 0))
			throw new MessageDeliveryFailedException("\"message\" parameter can't be empty", new PortableStackTraceElement[0]);
		String osName = System.getProperty("os.name");
		String windowSystem;
		String fileEncoding;
		if (osName.indexOf("Windows") != -1) {
			fileEncoding = "windows-1251";
			windowSystem = "Windows";
		} else {
			fileEncoding = "KOI8-R";
			windowSystem = "X11";
		}
		System.setProperty("http.agent", "Mozilla/4.8 [" + System.getProperty("user.language") + "] (" + windowSystem + "; U; " + osName + ' ' + System.getProperty("os.version") + ' ' + System.getProperty("os.arch") + ')');
		message = from + '\n' + message;
		from = "AMFICOM";
		try {
			message = new String(message.getBytes(), fileEncoding);
		} catch (UnsupportedEncodingException uee) {
			;
		}

		try {
			HttpURLConnection httpURLConnection = null;
			httpURLConnection = (HttpURLConnection) ((new URL("HTTP", "smsmail.ru", 80, "/corp/multi.php")).openConnection());
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.connect();

			PrintWriter out = new PrintWriter(httpURLConnection.getOutputStream());
			out.print("login=syrus");
			out.print("&password=B112233");
			out.print("&originator=" + from);
			out.print("&individual_messages=0");
			out.print("&phones=" + destination);
			out.print("&message=" + message);
			out.print("&want_sms_ids=1");
			out.print("&start_date=");
			out.print("&rus=1");
			out.println();
			out.close();

			int responseCode = httpURLConnection.getResponseCode();
			String responseMessage = httpURLConnection.getResponseMessage();
			String response = "" + responseCode + ' ' + responseMessage;
			System.out.println(response);
			InputStream in = httpURLConnection.getInputStream();
			Reader _in = new InputStreamReader(in);
			while (true) {
				int c = _in.read();
				if (c == - 1)
					break;
				System.out.print((char) c);
			}
			_in.close();
			in.close();
			if (responseCode != HttpURLConnection.HTTP_OK)
				throw new MessageDeliveryFailedException(response, new PortableStackTraceElement[0]);
		} catch (IOException ioe) {
			MessageDeliveryFailedException mdfe = new MessageDeliveryFailedException(ioe.getLocalizedMessage(), new PortableStackTraceElement[0]);
			mdfe.initCause(ioe);
			mdfe.setStackTrace(ioe.getStackTrace());
			throw mdfe;
		}
	}

	static SMSMailProvider getInstance() {
		return instance;
	}
}
