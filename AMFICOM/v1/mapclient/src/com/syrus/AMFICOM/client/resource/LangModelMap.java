/**
 * $Id: LangModelMap.java,v 1.1 2005/06/06 12:52:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ - система Автоматизированного Многофункционального
 * 			Интеллектуального Контроля и Объектного Мониторинга
 *
 * Платформа: java 1.4.1
*/
package com.syrus.AMFICOM.client.resource;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * модуль организации многоязыковой поддержки для модуля Редактор топологических
 * схем клиентской части ПО АМФИКОМ 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2005/06/06 12:52:01 $
 * @module mapclient_v2
 * @author $Author: krupenn $
 */
public final class LangModelMap
{
	private static final String BUNDLE_NAME = 
			"com.syrus.AMFICOM.Client.General.Lang.map";
	private static final ResourceBundle RESOURCE_BUNDLE = 
			ResourceBundle.getBundle(BUNDLE_NAME);

	private LangModelMap()
	{//empty
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