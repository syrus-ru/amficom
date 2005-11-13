/*-
 * $Id: SimpleMailer.java,v 1.4 2005/11/13 06:11:55 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.mail.EmailAddressRegexp;


/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/11/13 06:11:55 $
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

	private static final String KEY_OVERRIDE_FROM_ADDRESS = "OverrideFromAddress";

	private static final String KEY_OVERRIDE_FROM_PERSONAL = "OverrideFromPersonal";

	private static final String KEY_NOTIFY_SENDER = "NotifySender";

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

	private static final String DEFAULT_OVERRIDE_FROM_ADDRESS = null;

	private static final String DEFAULT_OVERRIDE_FROM_PERSONAL = null;

	private static final boolean DEFAULT_NOTIFY_SENDER = false;

	private static final String DEFAULT_MAIL_DEBUG = MAIL_DEBUG_NONE;


	private static final String CHARSET = System.getProperty("file.encoding", "KOI8-R");

	private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(EmailAddressRegexp.BOB_S_ADVANCED.getValue());

	private static final Pattern PASSWD_RECORD_PATTERN = Pattern.compile("^([^:]*):([^:]*):([^:]*):([^:]*):([^:]*):([^:]*):([^:]*)$");

	private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("([^,]*)(,[^,]*){0,3}");

	private static final boolean NOTIFY_SENDER = ApplicationProperties.getBoolean(KEY_NOTIFY_SENDER, DEFAULT_NOTIFY_SENDER);

	private static Session session;

	private static InternetAddress from;

	static {
		initialize();
	}

	private SimpleMailer() {
		assert false;
	}

	private static void initialize() {
		String smtpHost = ApplicationProperties.getString(KEY_SMTP_HOST, DEFAULT_SMTP_HOST);
		if (smtpHost == null || smtpHost.length() == 0) {
			Log.debugMessage("SMTP host is either null or empty; changing to ``"
					+ DEFAULT_SMTP_HOST + "''",
					WARNING); 
			smtpHost = DEFAULT_SMTP_HOST;
		}

		int smtpPort = ApplicationProperties.getInt(KEY_SMTP_PORT, DEFAULT_SMTP_PORT);
		if (0 > smtpPort || smtpPort > 0xFFFF) {
			Log.debugMessage("SMTP port: " + smtpPort
					+ " is out of range; changing to "
					+ DEFAULT_SMTP_PORT,
					WARNING); 
			smtpPort = DEFAULT_SMTP_PORT;
		}

		boolean useAuth = ApplicationProperties.getBoolean(KEY_USE_SMTP_AUTH, DEFAULT_USE_SMTP_AUTH);

		String smtpUsername = ApplicationProperties.getString(KEY_SMTP_USERNAME, DEFAULT_SMTP_USERNAME);
		if (smtpUsername == null || smtpUsername.length() == 0) {
			Log.debugMessage("SMTP username is either null or empty; changing to ``"
					+ DEFAULT_SMTP_USERNAME + "''",
					WARNING);
			smtpUsername = DEFAULT_SMTP_USERNAME;
		}

		String smtpPassword = null;
		if (useAuth) {
			smtpPassword = ApplicationProperties.getString(KEY_SMTP_PASSWORD, DEFAULT_SMTP_PASSWORD);
			if (smtpPassword == null) {
				Log.debugMessage("SMTP password is null; disabling SMTP authentication",
						WARNING);
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
			Log.debugMessage("Unrecognized MailDebug property value: ``"
					+ mailDebug + "''; changing to ``"
					+ DEFAULT_MAIL_DEBUG + "''",
					WARNING);
			mailDebug = DEFAULT_MAIL_DEBUG;
		}

		final Properties properties = new Properties();
		properties.put("mail.smtp.host", smtpHost);
		properties.put("mail.smtp.port", Integer.toString(smtpPort));
		properties.put("mail.smtp.auth", Boolean.toString(useAuth));
		properties.put("mail.smtp.allow8bitmime", "true");
		properties.put("mail.smtp.quitwait", "true");

		if (NOTIFY_SENDER) {
			properties.put("mail.smtp.dsn.ret", "FULL"); // "FULL"|"HDRS"
			properties.put("mail.smtp.dsn.notify", "SUCCESS,FAILURE,DELAY"); // "NEVER|SUCCESS|FAILURE|DELAY"
		}

		session = Session.getDefaultInstance(properties, authenticator);

		if (debug) {
			session.setDebugOut(debugOutToStderr ? System.err : System.out);
		}
		session.setDebug(debug);

		from = getInternetAddress(smtpUsername + '@' + smtpHost, true);
	}

	/**
	 * @param address
	 */
	private static String getPersonal(final String address) {
		final String smtpUsername = address.substring(0, address.indexOf('@'));
		final String smtpHostname = address.substring(address.indexOf('@') + 1);

		boolean lookupPersonal = false;
		try {
			final InetAddress localHost = InetAddress.getLocalHost();
			final InetAddress smtpHost = InetAddress.getByName(smtpHostname);
			final String canonicalHostName = smtpHost.getCanonicalHostName();
			final String hostAddress = smtpHost.getHostAddress();
			if (smtpHost.isLoopbackAddress()) {
				Log.debugMessage("SMTP host is a loopback address; sender's personal name lookup will be performed",
						INFO);
				lookupPersonal = true;
			}
			if (localHost.getCanonicalHostName().equals(canonicalHostName)) {
				Log.debugMessage("SMTP host is a local host since canonical host names match: ``"
						+ canonicalHostName
						+ "''; sender's personal name lookup will be performed",
						INFO);
				lookupPersonal = true;
			}
			if (localHost.getHostAddress().equals(hostAddress)) {
				Log.debugMessage("SMTP host is a local host since host addresses match: ``"
						+ hostAddress
						+ "''; sender's personal name lookup will be performed",
						INFO);
				lookupPersonal = true;
			}
		} catch (final UnknownHostException uhe) {
			Log.debugMessage(uhe, SEVERE);
		}

		if (!lookupPersonal) {
			return null;
		}

		final String osName = System.getProperty("os.name");
		if (osName == null || (osName.indexOf("SunOS") == -1
				&& osName.indexOf("Solaris") == -1
				&& osName.indexOf("Linux") == -1)) {
			return null;
		}

		try {
			String personal = null;
			final BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("/etc/passwd"), CHARSET));
			String line;
			while ((line = in.readLine()) != null) {
				final Matcher passwdRecordmatcher = PASSWD_RECORD_PATTERN.matcher(line);
				if (!passwdRecordmatcher.matches()) {
					Log.debugMessage("The line: ``" + line
							+ "'' is not a valid passwd file record; skipping",
							WARNING);
					continue;
				}

				final String login = passwdRecordmatcher.group(1);
				if (!login.equals(smtpUsername)) {
					continue;
				}

				Log.debugMessage("User's logname: ``" + login
						+ "'' found in the passwd file",
						INFO);
				final String description = passwdRecordmatcher.group(5);
				final Matcher descriptionMatcher = DESCRIPTION_PATTERN.matcher(description);
				if (!descriptionMatcher.matches()) {
					Log.debugMessage("The line: ``" + description
							+ "'' is not a valid description entry; aborting",
							WARNING);
					break;
				}
				personal = descriptionMatcher.group(1);
				Log.debugMessage("Personal name: ``" + personal
						+ "'' found for logname: ``"
						+ login + "''",
						INFO);
				if (personal.length() == 0) {
					personal = null;
				}
			}
			in.close();

			if (personal == null) {
				Log.debugMessage("No or empty personal name was found for logname: ``"
						+ smtpUsername
						+ "''",
						WARNING);
			}
			return personal;
		} catch (final IOException ioe) {
			Log.debugMessage(ioe, SEVERE);
			return null;
		}
	}

	/**
	 * @param address
	 * @param subject
	 * @param body
	 */
	public static void sendMail(final String address, final String subject, final String body) {
		if (body == null || body.length() == 0) {
			throw new IllegalArgumentException("Message body cannot be empty.");
		}

		try {
			final MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(from);
			mimeMessage.setRecipients(RecipientType.TO, new InternetAddress[]{getInternetAddress(address, false)});
			mimeMessage.setSubject(subject, CHARSET);
			mimeMessage.setSentDate(new Date());
			mimeMessage.setContent(body, "text/plain; charset=" + CHARSET);
			if (NOTIFY_SENDER) {
				mimeMessage.setHeader("Disposition-Notification-To", from.toString());
			}
			Transport.send(mimeMessage);
		} catch (MessagingException me) {
			while (me != null) {
				Log.debugMessage(me, SEVERE);
				final Exception nextException = me.getNextException();
				if (nextException instanceof MessagingException
						|| nextException == null) {
					me = (MessagingException) nextException;
					continue;
				}
				Log.debugMessage(nextException, SEVERE);
				break;
			}
		}
	}

	/**
	 * @param address
	 * @param overrideAddress
	 */
	private static InternetAddress getInternetAddress(final String address, final boolean overrideAddress) {
		if (address == null) {
			throw new NullPointerException("e-mail address is null");
		} else if (!EMAIL_ADDRESS_PATTERN.matcher(address).matches()) {
			throw new IllegalArgumentException("e-mail address: ``" + address + "'' is invalid");
		}

		final String overriddenAddress = overrideAddress
				? ApplicationProperties.getString(
						KEY_OVERRIDE_FROM_ADDRESS,
						DEFAULT_OVERRIDE_FROM_ADDRESS)
				: null;
		final String effectiveAddress = (overriddenAddress != null && overriddenAddress.length() != 0)
				? overriddenAddress
				: address;
		final boolean overridePersonal = (overriddenAddress != null && overriddenAddress.length() != 0);

		try {
			final String personal = overridePersonal
					? ApplicationProperties.getString(
							KEY_OVERRIDE_FROM_PERSONAL,
							DEFAULT_OVERRIDE_FROM_PERSONAL)
					: getPersonal(effectiveAddress);
			final String effectivePersonal = (personal != null && personal.length() != 0)
					? personal
					: null;
			return new InternetAddress(effectiveAddress, effectivePersonal, CHARSET);
		} catch (final UnsupportedEncodingException uee) {
			/*
			 * Almost never.
			 */
			try {
				return new InternetAddress(effectiveAddress, true);
			} catch (final AddressException ae) {
				Log.debugMessage(ae, SEVERE);
				try {
					return new InternetAddress(effectiveAddress, false);
				} catch (final AddressException ae2) {
					/*
					 * Never.
					 */
					assert false;
					return null;
				}
			}
		}
	}
}
