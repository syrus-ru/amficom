package com.syrus.AMFICOM.Client.General.Lang;
 
import java.util.*;

public class LangModelAnalyse
{
	private static final String BUNDLE_NAME =
		"com.syrus.AMFICOM.Client.General.Lang.analysis";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
		.getBundle(BUNDLE_NAME);

//	static public Locale locale;
//	static public String language;
//	static public String country;
//	static public ResourceBundle lang;
//	static public DateFormatSymbols symbols;
//	static public String resourceBundle;

	public LangModelAnalyse()
	{
		//symbols = new DateFormatSymbols(locale);
	}

	public static String getString(String keyName)
	{
		//System.out.println("keyName:" + keyName);
		String keyName1 = keyName.replaceAll(" ", "_");
		String string = null;
		try
		{
			string = RESOURCE_BUNDLE.getString(keyName1);
		}
		catch (MissingResourceException e)
		{
			try
			{
				string = RESOURCE_BUNDLE.getString(keyName1 + "Text");
			}
			catch (MissingResourceException mre)
			{
				string = keyName;
				try
				{
					throw new Exception("key '"
						+ keyName1 + "Text"
						+ "' "
						+ "not found");
				}
				catch (Exception exc)
				{
					exc.printStackTrace();
				}
			}
			catch (Exception exc)
			{
				exc.printStackTrace();
			}
		}
		// workaround:
		// �������� ������� ��� Eclipse �� �������� ��
		// ����� ��������� ������ ������, �� ��� ���
		// �������� ������� �� ��������.
		// �������� �������� �� ���
		// �������� ������ __EMPTY__, � ��� �����
		// ��� ����������� ������� � ������.
		// TODO: ��������� ��� GUI ������� ���, �����
		// �� �� ����������� ������� ��������,
		// ���� ��������� �������� �������,
		// ����� ���� ��������� ����� � ������� ��������.
		// //saa, 2004-12
		if (string.equals("__EMPTY__"))
		    string = "";
		return string;
	}
}
