package com.syrus.util.mail;

import java.io.*;
import java.net.*;

/**
 * @todo Change the code to utilize java.net.InetSocketAddress
 *
 * @version $Revision: 1.1 $, $Date: 2004/05/01 17:45:22 $
 * @author $Author: cvsadmin $
 * @module util
 */
public class SimpleMailer {
	private static final String DEFAULT_SMTP_HOST = "localhost";

	private static final int DEFAULT_SMTP_PORT = 25;

	private InetAddress host;

	private int port;

	public SimpleMailer() {
		this(DEFAULT_SMTP_HOST, DEFAULT_SMTP_PORT);
	}

	public SimpleMailer(String host) {
		this(host, DEFAULT_SMTP_PORT);
	}

	public SimpleMailer(int port) {
		this(DEFAULT_SMTP_HOST, port);
	}

	public SimpleMailer(String host, int port) {
		if ((port < 0) || (port > 0xFFFF))
			throw new IllegalArgumentException("Port out of range: " + port + '.');
		if (host == null)
			throw new IllegalArgumentException("Host can't be null.");
		try {
			this.host = InetAddress.getByName(host);
		} catch (UnknownHostException uhe) {
			throw new IllegalArgumentException("Host unknown: " + host + '.');
		}
		this.port = port;
	}

	/**
	 * @todo Implement error checking while sending a message, instead of plain
	 *       java.io.IOException
	 */
	public void sendMail(MailIdentity from, MailIdentity to, String subject, String body) throws IOException {
		if (to == null)
			throw new IllegalArgumentException("The \"To:\" field cannot be empty.");
		if ((body == null) || (body.length() == 0))
			throw new IllegalArgumentException("Message body cannot be empty.");
		Socket socket = new Socket(host, port);
		PrintWriter out = new PrintWriter(socket.getOutputStream());
		final LineNumberReader in = new LineNumberReader(new InputStreamReader(socket.getInputStream()));
		Thread thread = new Thread() {
			public void run() {
				String line;
				try {
					do {
						line = in.readLine();
						if (line != null)
							System.out.println(line);
					} while (line != null);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		};
		
		thread.start();

		out.println("EHLO " + getLocalHostName());
		out.println("MAIL FROM: " + from.getAddress());
		out.println("RCPT TO: " + to.getAddress());
		out.println("DATA");
		out.println("From: " + from);
		out.println("To: " + to);
		out.println("Subject: " + subject);
		out.println();
		out.println(body);
		out.println('.');
		out.println("QUIT");
		
		out.flush();
		try {
			thread.join();
		} catch (InterruptedException ie) {
		}

		in.close();
		out.close();
		socket.close();
	}

	public void sendMail(MailIdentity to, String subject, String body) throws IOException {
		sendMail(getDefaultMailIdentity(), to, subject, body);
	}

	public void sendMail(MailIdentity from, MailIdentity to, String body) throws IOException {
		sendMail(from, to, getDefaultSubject(), body);
	}

	public void sendMail(MailIdentity to, String body) throws IOException {
		sendMail(getDefaultMailIdentity(), to, getDefaultSubject(), body);
	}

	/**
	 * @todo On UNIX, obtain user's real name from /etc/passwd file. The
	 *       following sequence works fine:
	 *       grep ^${LOGNAME}\: /etc/passwd | cut -d \: -f 5 | cut -d , -f 1
	 */
	private static MailIdentity getDefaultMailIdentity() {
		return new MailIdentity(System.getProperty("user.name") + '@' + getLocalHostName());
	}

	private static String getDefaultSubject() {
		return "(no subject)";
	}

	/**
	 * @todo Use InetAddress.getCanonicalHostName()
	 */
	private static String getLocalHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException uhe) {
			return "localhost";
		}
	}

//	public static void main(String args[]) throws IOException {
//		MailIdentity from = new MailIdentity("\"Andrew ``Bass'' Shcheglov\"", "bass@localhost");
//		MailIdentity to = new MailIdentity("Andrew ``Bass'' Shcheglov", "bass");
//		String subject = "Sample Subject.";
//		String body = "Sample Body.\n\n--\nYours sincerely,\n\tAndrew ``Bass'' Shcheglov.";
//		SimpleMailer simpleMailer = new SimpleMailer();
//		simpleMailer.sendMail(to, body);
//		simpleMailer.sendMail(from, to, body);
//		simpleMailer.sendMail(to, subject, body);
//		simpleMailer.sendMail(from, to, subject, body);
//	}
}
