/**
 * $Id: MapEditorRun.java,v 1.2 2004/10/19 11:48:27 krupenn Exp $
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
 * @version $Revision: 1.2 $, $Date: 2004/10/19 11:48:27 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapEditorRun
{
	private MapEditorRun()
	{
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

