//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * �����⠬��� ���⥬��� ��᫥������� � ���ࠡ�⮪                      * //
// *                                                                      * //
// * �஥��: ������� - ��⥬� ��⮬�⨧�஢������ ������㭪樮���쭮��   * //
// *         ��⥫����㠫쭮�� ����஫� � ��ꥪ⭮�� �����ਭ��          * //
// *                                                                      * //
// *         ॠ������ ��⥣�஢����� ���⥬� �����ਭ��               * //
// *                                                                      * //
// * ��������: ����� �࣠����樨 ������몮��� �����প� ��� �����      * //
// *         ���䨣��஢���� ������᪮� ��� �� �������                 * //
// *                                                                      * //
// * ���: Java 1.2.2                                                      * //
// *                                                                      * //
// * ����: ��㯥������ �.�.                                              * //
// *                                                                      * //
// * �����: 1.0                                                          * //
// * ��: 1 jul 2002                                                       * //
// * ��ᯮ�������: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\General\Lang\LangModelAdmin.java                       * //
// *                                                                      * //
// * �।� ࠧࠡ�⪨: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * ���������: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * �����: ࠧࠡ�⪠                                                   * //
// *                                                                      * //
// * ���������:                                                           * //
// *  ���         ����   �����      �������ਨ                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * ���ᠭ��:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General.Lang;

import java.util.*;

public class LangModelAdmin {

	private static final String BUNDLE_NAME			= "com.syrus.AMFICOM.Client.General.Lang.admin";

	private static final ResourceBundle	RESOURCE_BUNDLE		= ResourceBundle
																	.getBundle(BUNDLE_NAME);


	public LangModelAdmin() {
		//symbols = new DateFormatSymbols(locale);
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
}
