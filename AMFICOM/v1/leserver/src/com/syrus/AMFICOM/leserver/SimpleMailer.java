/*-
 * $Id: SimpleMailer.java,v 1.1 2005/11/02 14:22:18 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static java.util.logging.Level.WARNING;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;


/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/11/02 14:22:18 $
 * @module leserver
 */
public final class SimpleMailer {
	/*-********************************************************************
	 * Keys                                                               * 
	 **********************************************************************/

	private static final String KEY_SMTP_HOST = "SmtpHost";

	private static final String KEY_SMTP_PORT = "SmtpPort";

	private static final String KEY_USE_SMTP_AUTH = "UseSmtpAuth";

	private static final String KEY_SMTP_USERNAME = "SmtpUsername";

	private static final String KEY_SMTP_PASSWORD = "SmtpPassword";

	private static final String KEY_MAIL_DEBUG = "MailDebug";

	/*-********************************************************************
	 * Custom values                                                      *
	 **********************************************************************/

	private static final String MAIL_DEBUG_NONE = "none";

	private static final String MAIL_DEBUG_STDOUT = "stdout";

	private static final String MAIL_DEBUG_STDERR = "stderr";

	/*-********************************************************************
	 * Default values                                                     *
	 **********************************************************************/

	private static final String DEFAULT_SMTP_HOST = "localhost";

	private static final int DEFAULT_SMTP_PORT = 25;

	private static final boolean DEFAULT_USE_SMTP_AUTH = false;

	private static final String DEFAULT_SMTP_USERNAME = System.getProperty("user.name");

	private static final String DEFAULT_SMTP_PASSWORD = "";

	private static final String DEFAULT_MAIL_DEBUG = MAIL_DEBUG_NONE;


	static final String CHARSET = System.getProperty("file.encoding", "KOI8-R");

	private static Session session;

	private static volatile boolean initialized;

	private static String returnAddress;

	/**
	 * DCL's are bad, yes I know that.
	 */
	private static void initialize() {
		if (!initialized) {
			synchronized (SimpleMailer.class) {
				if (!initialized) {
					initialize0();
					initialized = true;
				}
			}
		}
	}

	private static void initialize0() {
		String smtpHost = ApplicationProperties.getString(KEY_SMTP_HOST, DEFAULT_SMTP_HOST);
		if (smtpHost == null || smtpHost.length() == 0) {
			Log.debugMessage("SMTP host is either null or empty; changing to " + DEFAULT_SMTP_HOST, WARNING); 
			smtpHost = DEFAULT_SMTP_HOST;
		}

		int smtpPort = ApplicationProperties.getInt(KEY_SMTP_PORT, DEFAULT_SMTP_PORT);
		if (0 > smtpPort || smtpPort > 0xFFFF) {
			Log.debugMessage("SMTP port out of range; changing to " + DEFAULT_SMTP_PORT, WARNING); 
			smtpPort = DEFAULT_SMTP_PORT;
		}

		boolean useAuth = ApplicationProperties.getBoolean(KEY_USE_SMTP_AUTH, DEFAULT_USE_SMTP_AUTH);

		String smtpUsername = null;
		if (useAuth) {
			smtpUsername = ApplicationProperties.getString(KEY_SMTP_USERNAME, DEFAULT_SMTP_USERNAME);
			if (smtpUsername == null || smtpUsername.length() == 0) {
				Log.debugMessage("SMTP username is either null or empty; disabling SMTP authentication", WARNING);
				useAuth = false;
			}
		}

		String smtpPassword = null;
		if (useAuth) {
			smtpPassword = ApplicationProperties.getString(KEY_SMTP_PASSWORD, DEFAULT_SMTP_PASSWORD);
			if (smtpPassword == null) {
				Log.debugMessage("SMTP password is null; disabling SMTP authentication", WARNING);
				useAuth = false;
			}
		}

		final Authenticator authenticator;
		if (useAuth) {
			final PasswordAuthentication passwordAuthentication = new PasswordAuthentication(smtpUsername, smtpPassword);
			authenticator = new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return passwordAuthentication;
				}
			};
		} else {
			authenticator = null;
		}

		String mailDebug = ApplicationProperties.getString(KEY_MAIL_DEBUG, DEFAULT_MAIL_DEBUG).intern();
		final boolean debug = (mailDebug != MAIL_DEBUG_NONE);
		final boolean debugOutToStderr = (mailDebug != MAIL_DEBUG_STDOUT);
		if (debug && debugOutToStderr && mailDebug != MAIL_DEBUG_STDERR) {
			Log.debugMessage("Unrecognized MailDebug property value: " + mailDebug + "; changing to " + DEFAULT_MAIL_DEBUG, WARNING);
			mailDebug = DEFAULT_MAIL_DEBUG;
		}

		final Properties properties = new Properties();
		properties.put("mail.smtp.host", smtpHost);
		properties.put("mail.smtp.port", Integer.toString(smtpPort));
		properties.put("mail.smtp.auth", Boolean.toString(useAuth));
		properties.put("mail.smtp.allow8bitmime", "true");
		session = Session.getDefaultInstance(properties, authenticator);

		if (debug) {
			session.setDebugOut(debugOutToStderr ? System.err : System.out);
		}
		session.setDebug(debug);
	}

	private SimpleMailer() {
		assert false;
	}

	public static void sendMail(MailIdentity from, MailIdentity to, String subject, String body) {
		if (to == null)
			throw new IllegalArgumentException("The \"To:\" field cannot be empty.");
		if ((body == null) || (body.length() == 0))
			throw new IllegalArgumentException("Message body cannot be empty.");

		initialize();

		try {
			MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(from.toInternetAddress());
			mimeMessage.setRecipients(RecipientType.TO, new InternetAddress[]{to.toInternetAddress()});
			mimeMessage.setSubject(subject, CHARSET);
			mimeMessage.setSentDate(new Date());
			mimeMessage.setContent(body, "text/plain; charset=" + CHARSET);
			Transport.send(mimeMessage);
		} catch (final MessagingException me) {
			me.printStackTrace();
			try {
				me.getNextException().printStackTrace();
			} catch (final NullPointerException npe) {
				// empty
			}
		}
	}

	public static void sendMail(final MailIdentity to, final String subject, final String body) {
		sendMail(getDefaultMailIdentity(), to, subject, body);
	}

	public static void sendMail(final MailIdentity from, final MailIdentity to, final String body) {
		sendMail(from, to, getDefaultSubject(), body);
	}

	public static void sendMail(final MailIdentity to, final String body) {
		sendMail(getDefaultMailIdentity(), to, getDefaultSubject(), body);
	}

	/**
	 * @todo On UNIX, obtain user's real name from /etc/passwd file. The
	 *       following sequence works fine:
	 *       grep ^${LOGNAME}\: /etc/passwd | cut -d \: -f 5 | cut -d , -f 1
	 */
	private static MailIdentity getDefaultMailIdentity() {
		return new MailIdentity(returnAddress);
	}

	private static String getDefaultSubject() {
		return "(no subject)";
	}

	public static void main(final String args[]) {
		Application.init("leserver");
		initialize();
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

	private static final class MailIdentity {
		private String name;

		private String address;

		public MailIdentity(final String address) {
			this(null, address);
		}

		/**
		 * @todo Check the e-mail address for validity. Addresses may be local
		 *       (jsmith) or remote (jsmith@mydomain.com).
		 * @todo Implement filtering of quotation marks in name.
		 */
		public MailIdentity(final String name, final String address) {
			if (address == null || address.length() == 0) {
				throw new IllegalArgumentException("E-mail address is invalid.");
			}
			this.name = name;
			this.address = address;
		}

		String getAddress() {
			return this.address;
		}

		public InternetAddress toInternetAddress() throws AddressException {
			try {
				return new InternetAddress(this.address, this.name, CHARSET);
			} catch (final UnsupportedEncodingException uee) {
				return new InternetAddress(this.address, true);
			}
		}

		@Override
		public String toString() {
			return (this.name == null || this.name.length() == 0)
					? this.address
					: this.name + " <" + this.address + '>';
		}
	}
}
