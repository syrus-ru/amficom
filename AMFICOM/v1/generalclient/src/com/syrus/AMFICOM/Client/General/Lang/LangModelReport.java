package com.syrus.AMFICOM.Client.General.Lang;

import java.text.*;
import java.util.*;

public class LangModelReport extends LangModel
{
  static public Locale locale;
  static public String language;
  static public String country;
  static public ResourceBundle lang;
  static public DateFormatSymbols symbols;
  static public String resourceBundle;

  public LangModelReport()
  {
	 symbols = new DateFormatSymbols(locale);
  }

  static public void initialize()
  {
	 initialize("com.syrus.AMFICOM.Client.General.Lang.LangModelReport");
  }

  static public void initialize(String rb)
  {
	 System.out.println("initialize lang - " + rb);
	 resourceBundle = new String(rb);
	 setLangModel("ru", "");
  }

  static public Vector getLangModels()
  {

	 Vector vec = new Vector();
	 return vec;
  }

  static public boolean setLangModel(String l, String c)
  {
	 language = l;
	 country = c;
	 try
	 {
		locale = new Locale(language, country);
		lang = ResourceBundle.getBundle(
			 resourceBundle,
			 locale);
		System.out.println("initialize locale - " + locale.toString());
		symbols = new DateFormatSymbols(locale);
	 }
	 catch(Exception e)
	 {
		System.out.println(e);
		e.printStackTrace();
		return false;
	 }
	 return true;
  }

  static public String Text(String componentName)
  {
	 return getComponentText(lang, componentName);
  }

  static public String ToolTip(String componentName)
  {
	 return getComponentToolTip(lang, componentName);
  }

  static public String getComponentText(
		ResourceBundle lang,
		String componentName)
  {
	 return String(componentName + "Text");
  }

  static public String getComponentToolTip(
		ResourceBundle lang,
		String componentName)
  {
	 return String(componentName + "ToolTip");
  }

  static public String String(String keyName)
  {
	 return String(lang, keyName);
  }

  static public String String(ResourceBundle lang, String keyName)
  {
	 try
	 {
		return lang.getString(keyName);
	 }
	 catch(java.util.MissingResourceException mre)
	 {
		try
		{
		  Locale loc2 = lang.getLocale();
		  Locale loc;
		  if(loc2.getCountry() != null && !(loc2.getCountry().equals("")))
			 loc = new Locale(loc2.getLanguage(), "");
		  else
			 if(loc2.getLanguage() != null && !(loc2.getLanguage().equals("")))
				loc = new Locale("", "");
		  else
			 throw mre;
		  ResourceBundle lang2 =
				ResourceBundle.getBundle(resourceBundle, loc);
		  return String(lang2, keyName);
		}
		catch(Exception e)
		{
		  System.out.println(e);
		  return "ERROR key!" + keyName;
		}
	 }
	 catch(Exception e)
	 {
		System.out.println(e);
		return "ERROR key!" + keyName;
	 }
  }
}