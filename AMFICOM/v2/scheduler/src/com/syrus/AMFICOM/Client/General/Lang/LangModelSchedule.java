package com.syrus.AMFICOM.Client.General.Lang;

import java.text.DateFormatSymbols;
import java.util.*;

public class LangModelSchedule 
//extends LangModel 
{

//	public static Locale				locale;
//	public static String				language;
//	public static String				country;

	//public static ResourceBundle lang;

//	public static DateFormatSymbols		symbols;

	//public static String resourceBundle;

	private static final String			BUNDLE_NAME		= "language";

	private static final ResourceBundle	RESOURCE_BUNDLE	= ResourceBundle
																.getBundle(BUNDLE_NAME);

	public LangModelSchedule() {

	}

	public static void initialize() {
		//initialize("com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule");
//		locale = Locale.getDefault();
//		System.out.println("initialize language:" + locale.getLanguage());
//		symbols = new DateFormatSymbols(locale);
//		symbols.setEras(new String[] { String("before_chismas"),
//				String("after_chismas ")});
//		symbols.setMonths(new String[] { String("january"), String("february"),
//				String("march"), String("april"), String("may"),
//				String("june"), String("jule"), String("augest"),
//				String("september"), String("october"), String("november"),
//				String("december")});
//		symbols.setShortMonths(new String[] { String("jan"), String("feb"),
//				String("mar"), String("apr"), String("may"), String("jun"),
//				String("jul"), String("aug"), String("sep"), String("oct"),
//				String("nov"), String("dec")});
	}

	//	public static void initialize(String rb) {
	//		System.out.println("initialize lang - " + rb);
	//		//resourceBundle = new String(rb);
	//		setLangModel("ru", "");
	//	}

	//	public static Vector getLangModels() {
	//		Vector vec = new Vector();
	//		return vec;
	//	}

	//	public static boolean setLangModel(String l, String c) {
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
	//		return true;
	//	}

	//	/**
	//	 * @deprecated use getComponentText
	//	 */
	//	public static String Text(String componentName) {
	//		return getComponentText(lang, componentName);
	//	}
	//	
	//	/**
	//	 * @deprecated use getComponentToolTip
	//	 */
	//	public static String ToolTip(String componentName) {
	//		return getComponentToolTip(lang, componentName);
	//	}

	public static String getComponentText(String componentName) {
		return getString(componentName + "Text");
	}

	public static String getComponentToolTip(String componentName) {
		return getString(componentName + "ToolTip");
	}

	public static String getString(String keyName) {
		//return String(lang, keyName);
		keyName = keyName.replaceAll(" ", "_");
		try {
			return RESOURCE_BUNDLE.getString(keyName);
		} catch (MissingResourceException e) {
			return '!' + keyName + '!';
		}
	}

	//	public static String String(ResourceBundle lang, String keyName) {
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
	//				System.out.println(e);
	//				return "ERROR key!" + keyName;
	//			}
	//		} catch (Exception e) {
	//			System.out.println(e);
	//			return "ERROR key!" + keyName;
	//		}
	//	}
}