package com.syrus.AMFICOM.Client.General.Lang;

import java.util.*;

import java.text.DateFormatSymbols;

public class LangModel {

	/**
	 * @deprecated
	 */
	static public String				country;
	/**
	 * @deprecated
	 */
	static public ResourceBundle		lang;
	/**
	 * @deprecated
	 */
	static public String				language;
	/**
	 * @deprecated
	 */
	static public Locale				locale;
	/**
	 * @deprecated
	 */
	static public String				resourceBundle;
	/**
	 * @deprecated
	 */
	static public DateFormatSymbols		symbols;

	private static final String			BUNDLE_NAME			= "com.syrus.AMFICOM.Client.General.Lang.generalclient";

	private static final ResourceBundle	RESOURCE_BUNDLE		= ResourceBundle
																	.getBundle(BUNDLE_NAME);


	public LangModel() {
		//symbols = new DateFormatSymbols(locale);
	}

	/**
	 * @deprecated use com.syrus.AMFICOM.Client.General.Survey.I18N.getString()
	 */
	public static String getComponentText(ResourceBundle lang,
			String componentName) {
		//return String(componentName + "Text");
		return getString(componentName);
	}

	/**
	 * @deprecated use com.syrus.AMFICOM.Client.General.Survey.I18N.getString()
	 */
	static public String getComponentToolTip(ResourceBundle lang,
			String componentName) {
		//return String(componentName + "ToolTip");
		return getString(componentName);
	}

	/**
	 * @deprecated use com.syrus.AMFICOM.Client.General.Survey.I18N
	 */
	static public Vector getLangModels() {

		Vector vec = new Vector();
		return vec;
	}

	public static String getString(String keyName) {
		//System.out.println("keyName:" + keyName);
		keyName = keyName.replaceAll(" ", "_");
		String string = null;
		try {
			string = RESOURCE_BUNDLE.getString(keyName);
		} catch (MissingResourceException e) {

			try {
				throw new Exception("key '"
									+ keyName
									+ "' "
									+  "not found");
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
		return string;
	}

	/**
	 * @deprecated
	 */
	public static void initialize() {
		//initialize("com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey");
	}

	/**
	 * @deprecated
	 */
	public static void initialize(String rb) {
		//System.out.println("initialize lang - " + rb);
		//resourceBundle = new String(rb);
		//setLangModel("ru", "");
	}

	/**
	 * @deprecated
	 */
	public static boolean setLangModel(String l, String c) {
		return true;
	}

	/**
	 * @deprecated use getString()
	 */
	public static String String(ResourceBundle lang, String keyName) {
		return getString(keyName);
	}

	/**
	 * @deprecated use getString()
	 */
	public static String String(String keyName) {
		return getString(keyName);
	}

	/**
	 * @deprecated use getString()
	 */
	public static String Text(String componentName) {
		return getComponentText(lang, componentName);
	}

	/**
	 * @deprecated use getString()
	 */
	public static String ToolTip(String componentName) {
		return getComponentToolTip(lang, componentName);
	}
}