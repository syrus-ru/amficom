package com.syrus.AMFICOM.Client.General.Lang;

import java.text.DateFormatSymbols;
import java.util.*;

public class LangModelSchedule {

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

	private static final String			BUNDLE_NAME			= "com.syrus.AMFICOM.Client.General.Lang.scheduler";

	private static final String			OLDBUNDLE_NAME		= "com.syrus.AMFICOM.Client.General.Lang.oldschedulerkey";
	private static final ResourceBundle	OLDRESOURCE_BUNDLE	= ResourceBundle
																	.getBundle(OLDBUNDLE_NAME);

	private static final ResourceBundle	RESOURCE_BUNDLE		= ResourceBundle
																	.getBundle(BUNDLE_NAME);

	/**
	 * @deprecated use com.syrus.AMFICOM.Client.General.Survey.I18N
	 */
	public LangModelSchedule() {
		//symbols = new DateFormatSymbols(locale);
	}

	/**
	 * @deprecated use com.syrus.AMFICOM.Client.General.Survey.I18N.getString()
	 */
	static public String getComponentText(ResourceBundle lang,
			String componentName) {
		//return String(componentName + "Text");
		return getString(componentName + "Text");
	}

	/**
	 * @deprecated use com.syrus.AMFICOM.Client.General.Survey.I18N.getString()
	 */
	static public String getComponentToolTip(ResourceBundle lang,
			String componentName) {
		//return String(componentName + "ToolTip");
		return getString(componentName + "ToolTip");
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
		String string;
		try {
			string = RESOURCE_BUNDLE.getString(keyName);
		} catch (MissingResourceException e) {
			String key = null;
			string = "!" + keyName + "!";
			try {
				key = OLDRESOURCE_BUNDLE.getString(keyName);
				key = key.replaceAll("\\s+", "");
			} catch (MissingResourceException ex) {
				// nothing
			}
			if (key == null) {
				try {
					key = OLDRESOURCE_BUNDLE.getString(keyName + "Text");
					key = key.replaceAll("\\s+", "");
				} catch (MissingResourceException ex) {
					// nothing
				}
			}
			if (key == null) {
				try {
					key = OLDRESOURCE_BUNDLE.getString(keyName + "ToolTip");
					key = key.replaceAll("\\s+", "");
				} catch (MissingResourceException ex) {
					// nothing
				}
			}

			try {
				String s = "key '"
						+ keyName
						+ "' "
						+ (key == null ? "not found" : " is deprecated , use '"
								+ key + "' key.");
				throw new Exception(s);
			} catch (Exception exc) {
				exc.printStackTrace();
			}
			if (key != null) string = LangModelSurvey.getString(key);

		}
		return string;
	}

	/**
	 * @deprecated
	 */
	static public void initialize() {
		//initialize("com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey");
	}

	/**
	 * @deprecated
	 */
	static public void initialize(String rb) {
		//System.out.println("initialize lang - " + rb);
		//resourceBundle = new String(rb);
		//setLangModel("ru", "");
	}

	/**
	 * @deprecated
	 */
	static public boolean setLangModel(String l, String c) {
		//		language = l;
		//		country = c;
		//		try {
		//			locale = new Locale(language, country);
		//			lang = ResourceBundle.getBundle(resourceBundle, locale);
		//			System.out.println("initialize locale - " + locale.toString());
		//			symbols = new DateFormatSymbols(locale);
		//		} catch (Exception e) {
		//			System.out.println(e);
		//			e.printStackTrace();
		//			return false;
		//		}
		return true;
	}

	/**
	 * @deprecated use getString()
	 */
	static public String String(ResourceBundle lang, String keyName) {
		//		try {
		//			return lang.getString(keyName);
		//		} catch (java.util.MissingResourceException mre) {
		//			try {
		//				Locale loc2 = lang.getLocale();
		//				Locale loc;
		//				if (loc2.getCountry() != null
		//						&& !(loc2.getCountry().equals("")))
		//					loc = new Locale(loc2.getLanguage(), "");
		//				else if (loc2.getLanguage() != null
		//						&& !(loc2.getLanguage().equals("")))
		//					loc = new Locale("", "");
		//				else
		//					throw mre;
		//				ResourceBundle lang2 = ResourceBundle.getBundle(resourceBundle,
		//						loc);
		//				return String(lang2, keyName);
		//			} catch (Exception e) {
		//				System.out.println("LangModelSurvey found: " + e
		//						+ " for key Name " + keyName);
		//				return "ERROR key!" + keyName;
		//			}
		//		} catch (Exception e) {
		//			System.out.println("LangModelSurvey found: " + e + " for key Name "
		//					+ keyName);
		//			return "ERROR key!" + keyName;
		//		}
		return getString(keyName);
	}

	/**
	 * @deprecated use getString()
	 */
	static public String String(String keyName) {
		return getString(keyName);
	}

	/**
	 * @deprecated use getString()
	 */
	static public String Text(String componentName) {
		return getComponentText(lang, componentName);
	}

	/**
	 * @deprecated use getString()
	 */
	static public String ToolTip(String componentName) {
		return getComponentToolTip(lang, componentName);
	}

}