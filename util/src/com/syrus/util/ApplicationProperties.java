/*
 * $Id: ApplicationProperties.java,v 1.4 2005/03/04 08:05:49 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.util.ResourceBundle;
import java.util.PropertyResourceBundle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ApplicationProperties {
	private static String fileName;
	private static ResourceBundle resourceBundle;

	private ApplicationProperties() {
		assert false;
	}

	public static void init(String applicationName) {
		fileName = applicationName + ".properties"; //$NON-NLS-1$
		try {
			FileInputStream fis = new FileInputStream("." + File.separator + fileName); //$NON-NLS-1$
			resourceBundle = new PropertyResourceBundle(fis);
			fis.close();
		}
		catch (FileNotFoundException fnfe) {
			System.out.println("Cannot find file: " + fileName); //$NON-NLS-1$
			resourceBundle = null;
		}
		catch (IOException ioe) {
			System.out.println("Exception while reading file " + fileName + ": " + ioe.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
			ioe.printStackTrace();
			resourceBundle = null;
		}
	}

	public static String getString(String key, String defaultValue) {
		String value = defaultValue;
		if (resourceBundle != null)
			try {
				value = resourceBundle.getString(key);
			}
			catch (Exception e) {
				Log.debugMessage("Cannot get resource '" + key + "' from " + fileName + "; using default -- '" + defaultValue + "'", Log.DEBUGLEVEL02); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				value = defaultValue;
			}
		else {
			Log.errorMessage("File " + fileName + " not loaded; for key '" + key + "' returning default -- '" + defaultValue + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			value = defaultValue;
		}
		return value;
	}

	public static int getInt(String key, int defaultValue) {
		int value = defaultValue;
		if (resourceBundle != null)
			try {
				value = Integer.parseInt(resourceBundle.getString(key));
			}
			catch (Exception e) {
				Log.debugMessage("Cannot get resource '" + key + "' from " + fileName + "; using default -- " + defaultValue, Log.DEBUGLEVEL02); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				value = defaultValue;
			}
		else {
			Log.errorMessage("File " + fileName + " not loaded; for key '" + key + "' returning default -- " + defaultValue); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			value = defaultValue;
		}
		return value;
	}

	public static String getFileName() {
		return fileName;
	}
}
