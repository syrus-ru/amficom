//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * ����������� ��������� ������������ � ����������                      * //
// *                                                                      * //
// * ������: ������� - ������� ������������������� ��������������������   * //
// *         ����������������� �������� � ���������� �����������          * //
// *                                                                      * //
// *         ���������� ��������������� ������� �����������               * //
// *                                                                      * //
// * ��������: ������ ����������� ������������� ��������� ��� ������      * //
// *         ���������������� ���������� ����� �� �������                 * //
// *                                                                      * //
// * ���: Java 1.2.2                                                      * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 1.0                                                          * //
// * ��: 1 jul 2002                                                       * //
// * ������������: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\General\Lang\LangModelConfig.java                      * //
// *                                                                      * //
// * ����� ����������: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * ����������: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * ������: ����������                                                   * //
// *                                                                      * //
// * ���������:                                                           * //
// *  ���         ����   �����      �����������                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * ��������:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General.Lang;

import java.util.*;

public class LangModelMap {

	private static final String			BUNDLE_NAME			= "com.syrus.AMFICOM.Client.General.Lang.map";

	private static final ResourceBundle	RESOURCE_BUNDLE		= ResourceBundle
																	.getBundle(BUNDLE_NAME);


	public LangModelMap() {
		//symbols = new DateFormatSymbols(locale);
	}

	/**
	 * @deprecated
	 * @param keyName
	 * @return
	 */
	public static String String(String keyName) {
		return getString (keyName);
	}
	public static String Text(String keyName) {
		return getString (keyName);
	}
	public static String ToolTip(String keyName) {
		return getString (keyName + "ToolTip");
	}

	public static String getString(String keyName)
	{
		//System.out.println("keyName:" + keyName);
		keyName = keyName.replaceAll(" ", "_");
		String string = "";
		try
		{
			string = RESOURCE_BUNDLE.getString(keyName);
		}
		catch (MissingResourceException e)
		{
			try
			{
				string = RESOURCE_BUNDLE.getString(keyName + "Text");
			}
			catch (MissingResourceException mre)
			{
				try
				{
					throw new Exception("key '" + keyName + "' not found");
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
		return string;
	}
}
