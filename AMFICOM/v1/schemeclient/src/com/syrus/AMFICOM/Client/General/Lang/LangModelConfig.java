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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.syrus.util.Log;

public class LangModelConfig {

	private static final String			BUNDLE_NAME			= "com.syrus.AMFICOM.Client.General.Lang.config";

	private static final ResourceBundle	RESOURCE_BUNDLE		= ResourceBundle
																	.getBundle(BUNDLE_NAME);


	public LangModelConfig() {
		//symbols = new DateFormatSymbols(locale);
	}

	public static String getString(String keyName)
	{
		//System.out.println("keyName:" + keyName);
		keyName = keyName.replaceAll(" ", "_");
		String string = null;
		try
		{
			string = RESOURCE_BUNDLE.getString(keyName);
		} catch (MissingResourceException e)
		{
			try
			{
				string = RESOURCE_BUNDLE.getString(keyName + "Text");
			} catch (MissingResourceException mre)
			{
				try
				{
					throw new Exception("key '"
												+ keyName + "Text"
												+ "' "
												+ "not found");
				} catch (Exception exc)
				{
					Log.errorMessage(exc);
				}
			} catch (Exception exc)
			{
				Log.errorMessage(exc);
			}
		}
		return string;
	}
}
