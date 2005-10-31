/*
 * $Id: ApplicationProperties.java,v 1.16 2005/10/31 12:29:58 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @version $Revision: 1.16 $, $Date: 2005/10/31 12:29:58 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public class ApplicationProperties {
	public static final String COMMON_FILE_NAME = "common";
	public static final String FILE_EXTENSION = "properties";
	public static final String DOT = ".";

	private static String commonFileName;
	private static ResourceBundle commonResourceBundle;
	private static String applicationFileName;
	private static ResourceBundle applicationResourceBundle;

	private ApplicationProperties() {
		assert false;
	}

	public static void init(String applicationName) {
		commonFileName = COMMON_FILE_NAME + DOT + FILE_EXTENSION;
		commonResourceBundle = createResourceBundle(commonFileName);

		applicationFileName = applicationName + DOT + FILE_EXTENSION;
		applicationResourceBundle = createResourceBundle(applicationFileName);
	}

	private static ResourceBundle createResourceBundle(final String fileName) {
		ResourceBundle resourceBundle = null;
		String filepath = System.getProperty("user.dir", ".") + File.separator + fileName;
		File file = new File(filepath);
		String absolutePath;
		try {
			absolutePath = file.getCanonicalPath();
		} catch (final IOException ioe) {
			absolutePath = file.getAbsolutePath();
		}
		System.out.println("Reading file:" + absolutePath);
		if (file.exists()) {
			try {
				FileInputStream fis = new FileInputStream(filepath);
				resourceBundle = new PropertyResourceBundle(fis);
				fis.close();
			} catch (FileNotFoundException fnfe) {
				System.err.println("Cannot find file: " + absolutePath);
			} catch (IOException ioe) {
				System.err.println("Exception while reading file " + absolutePath + ": " + ioe.getMessage());
				ioe.printStackTrace();
			}
		} else {
			System.err.println("Cannot find file: " + absolutePath);
		}
		
		return resourceBundle;
	}

	public static String getString(String key, String defaultValue) {
		if (applicationResourceBundle != null) {
			try {
				return applicationResourceBundle.getString(key);
			} catch (Exception e) {
				Log.debugMessage("Cannot get resource '" + key + "' from " + applicationFileName + "; using common file", Log.DEBUGLEVEL02);
			}
		}

		if (commonResourceBundle != null) {
			try {
				return commonResourceBundle.getString(key);
			} catch (Exception e) {
				Log.debugMessage("Cannot get resource '" + key + "' from " + commonFileName, Log.DEBUGLEVEL02);
			}
		}

		Log.debugMessage("Returning default for key '" + key + "' -- '" + defaultValue + "'", Log.DEBUGLEVEL02);
		return defaultValue;
	}

	public static int getInt(String key, int defaultValue) {
		if (applicationResourceBundle != null) {
			try {
				return Integer.parseInt(applicationResourceBundle.getString(key));
			} catch (Exception e) {
				Log.debugMessage("Cannot get resource '" + key + "' from " + applicationFileName + "; using common file", Log.DEBUGLEVEL02);
			}
		}

		if (commonResourceBundle != null) {
			try {
				return Integer.parseInt(commonResourceBundle.getString(key));
			} catch (Exception e) {
				Log.debugMessage("Cannot get resource '" + key + "' from " + commonFileName, Log.DEBUGLEVEL02);
			}
		}

		Log.debugMessage("Returning default for key '" + key + "' -- " + defaultValue, Log.DEBUGLEVEL02);
		return defaultValue;
	}

	public static boolean getBoolean(final String key,
			final boolean defaultValue) {
		return Boolean.valueOf(getString(key, Boolean.valueOf(defaultValue).toString())).booleanValue();
	}

	public static String getCommonFileName() {
		return commonFileName;
	}

	public static String getApplicationFileName() {
		return applicationFileName;
	}
}
