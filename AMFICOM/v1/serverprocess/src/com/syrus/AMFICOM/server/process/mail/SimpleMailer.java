/*
 * $Id: SimpleMailer.java,v 1.1 2004/06/22 09:57:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.process.mail;

import com.syrus.AMFICOM.server.process.prefs.SMTPConnectionManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.*;

/**
 * @todo Change the code to utilize java.net.InetSocketAddress
 *
 * @version $Revision: 1.1 $, $Date: 2004/06/22 09:57:10 $
 * @author $Author: bass $
 * @module serverprocess
 */
public class SimpleMailer {
	private static final boolean DEBUG = true;

	private InetAddress host;

	private int port;

	public SimpleMailer() {
		this(SMTPConnectionManager.getHost(), SMTPConnectionManager.getPort());
	}

	public SimpleMailer(String host) {
		this(host, SMTPConnectionManager.getPort());
	}

	public SimpleMailer(int port) {
		this(SMTPConnectionManager.getHost(), port);
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

	public void sendMail(MailIdentity from, MailIdentity to, String subject, String body) throws IOException {
		if (to == null)
			throw new IllegalArgumentException("The \"To:\" field cannot be empty.");
		if ((body == null) || (body.length() == 0))
			throw new IllegalArgumentException("Message body cannot be empty.");

		boolean useAuth = SMTPConnectionManager.getUseAuth();
		Properties props = new Properties();
		props.put("mail.smtp.host", host.getCanonicalHostName());
		props.put("mail.smtp.port", String.valueOf(port));
		props.put("mail.smtp.auth", String.valueOf(useAuth));
		props.put("mail.smtp.allow8bitmime", "true");
		Authenticator authenticator;
		if (useAuth)
			authenticator = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(
						SMTPConnectionManager.getUsername(),
						SMTPConnectionManager.getPassword());
				}
			};
		else
			authenticator = null;
		Session session = Session.getDefaultInstance(props, authenticator);
		session.setDebug(DEBUG);
		try {
			MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(from.toInternetAddress());
			mimeMessage.setRecipients(RecipientType.TO, new InternetAddress[]{to.toInternetAddress()});
			mimeMessage.setSubject(subject, MailIdentity.CHARSET);
			mimeMessage.setSentDate(new Date());
			mimeMessage.setContent(body, "text/plain; charset=" + MailIdentity.CHARSET);
			Transport.send(mimeMessage);
		} catch (MessagingException me) {
			me.printStackTrace();
			try {
				me.getNextException().printStackTrace();
			} catch (NullPointerException npe) {
				;
			}
		}
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
		return new MailIdentity(SMTPConnectionManager.getReturnAddress());
	}

	private static String getDefaultSubject() {
		return "(no subject)";
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
