/*
 * $Id: MapEditorSaveMapAsCommand.java,v 1.4 2004/12/28 17:35:12 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapSaveAsCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.map.Map;

/**
 * Класс $RCSfile: MapEditorSaveMapAsCommand.java,v $ используется для сохранения топологической схемы в модуле
 * "Редактор топологических схем" с новым именем. Использует команду
 * MapSaveAsCommand
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/28 17:35:12 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapSaveAsCommand
 */
public class MapEditorSaveMapAsCommand extends VoidCommand
{
	MapFrame mapFrame;
	ApplicationContext aContext;

	/**
	 * 
	 * @param mapFrame окно карты, из которого сохранять схему
	 * @param aContext контекст модуля "Редактор топологических схем"
	 */
	public MapEditorSaveMapAsCommand(MapFrame mapFrame, ApplicationContext aContext)
	{
		this.mapFrame = mapFrame;
		this.aContext = aContext;
	}
	
	public void setParameter(String param, Object val)
	{
		if(param.equals("mapFrame"))
			this.mapFrame = (MapFrame)val;
	}

	public void execute()
	{
		if(mapFrame == null)
		{
			System.out.println("map frame is null! Cannot create new map.");
			setResult(Command.RESULT_NO);
			return;
		}
		MapSaveAsCommand msac = new MapSaveAsCommand(mapFrame.getMap(), aContext);
		msac.execute();
		
		if(msac.getResult() == RESULT_OK)
		{
			Map newMap = msac.getNewMap();
		
			if (mapFrame != null)
			{
				mapFrame.getMapView().setMap(newMap);
				mapFrame.setTitle( 
					LangModelMap.getString("Map") + " - " + newMap.getName());
			}
		}

		setResult(Command.RESULT_OK);
	}

}
