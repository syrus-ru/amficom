/**
 * $Id: LangModelMap.java,v 1.5 2004/09/13 12:02:00 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� - ������� ������������������� ��������������������
 * 			����������������� �������� � ���������� �����������
 *
 * ���������: java 1.4.1
*/
package com.syrus.AMFICOM.Client.General.Lang;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * ������ ����������� ������������� ��������� ��� ������ �������� ��������������
 * ���� ���������� ����� �� ������� 
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/09/13 12:02:00 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class LangModelMap
{
	private static final String BUNDLE_NAME = 
			"com.syrus.AMFICOM.Client.General.Lang.map";
	private static final ResourceBundle RESOURCE_BUNDLE = 
			ResourceBundle.getBundle(BUNDLE_NAME);

	private LangModelMap()
	{
	}

	public static String getString(String keyName) 
	{
		String searchKey = keyName.replaceAll(" ", "_");
		String string = "";
		try 
		{
			string = RESOURCE_BUNDLE.getString(searchKey);
		} 
		catch (MissingResourceException e) 
		{
			string = "!" + searchKey + "!";

			try 
			{
				String s = "key '" + searchKey + "' not found";
				throw new Exception(s);
			} catch (Exception exc) 
			{
//				System.out.println(exc.getMessage());
				exc.printStackTrace();
			}
		}
		return string;
	}
}