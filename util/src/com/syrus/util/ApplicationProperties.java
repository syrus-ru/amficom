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
	}

	public static void init(String applicationName) {
		fileName = applicationName + ".properties";
		try {
			FileInputStream fis = new FileInputStream("." + File.separator + fileName);
			resourceBundle = new PropertyResourceBundle(fis);
			fis.close();
		}
		catch (FileNotFoundException fnfe) {
			System.out.println("Cannot find file: " + fileName);
			resourceBundle = null;
		}
		catch (IOException ioe) {
			System.out.println("Exception while reading file " + fileName + ": " + ioe.getMessage());
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
				Log.debugMessage("Cannot get resource '" + key + "' from " + fileName + "; using default -- '" + defaultValue + "'", Log.DEBUGLEVEL02);
				value = defaultValue;
			}
		else {
			Log.errorMessage("File " + fileName + " not loaded; for key '" + key + "' returning default -- '" + defaultValue + "'");
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
				Log.debugMessage("Cannot get resource '" + key + "' from " + fileName + "; using default -- " + defaultValue, Log.DEBUGLEVEL02);
				value = defaultValue;
			}
		else {
			Log.errorMessage("File " + fileName + " not loaded; for key '" + key + "' returning default -- " + defaultValue);
			value = defaultValue;
		}
		return value;
	}

	public static String getFileName() {
		return fileName;
	}
}
