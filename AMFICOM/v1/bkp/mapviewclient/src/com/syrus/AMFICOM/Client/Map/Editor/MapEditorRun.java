/**
 * $Id: MapEditorRun.java,v 1.3 2005/02/07 16:09:26 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */
package com.syrus.AMFICOM.Client.Map.Editor;

import com.syrus.AMFICOM.Client.General.Model.DefaultMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;

import javax.swing.UIManager;

/**
 * Запуск приложения "Редактор топологических схем" 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2005/02/07 16:09:26 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public class MapEditorRun
{
	private MapEditorRun()
	{//empty
	}
	
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		new MapEditor(new DefaultMapEditorApplicationModelFactory());
	}
}

