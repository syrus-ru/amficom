/*
 * $Id: MapCloseCommand.java,v 1.4 2004/07/14 07:25:04 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.General.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapMainFrame;

import java.util.Vector;

/**
 * Класс $RCSfile: MapCloseCommand.java,v $ используется для закрытия карты при сохранении на экране
 * самого окна карты. При этом в азголовке окна отображается информация о том,
 * что активной карты нет, и карта центрируется по умолчанию
 * 
 * @version $Revision: 1.4 $, $Date: 2004/07/14 07:25:04 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapCloseCommand extends VoidCommand
{
	/**
	 * окно карты
	 */
	MapMainFrame mapFrame;

	public MapCloseCommand()
	{
	}

	public MapCloseCommand(MapMainFrame myMapFrame)
	{
		this.mapFrame = myMapFrame;
	}

	public Object clone()
	{
		return new MapCloseCommand(mapFrame);
	}

	public void execute()
	{
		if(mapFrame == null)
			return;
        System.out.println("Closing new map context");
        mapFrame.setMapContext(null);

        MapMainFrame.iniFile.setValue( "last_long", String.valueOf( mapFrame.myMapViewer.getCenter()[0])  );
        MapMainFrame.iniFile.setValue( "last_lat", String.valueOf( mapFrame.myMapViewer.getCenter()[1]) );

        if ( MapMainFrame.iniFile.saveKeys() )
        {
			System.out.println("Params saved");
        }
        mapFrame.setTitle( LangModelMap.getString("AppTitle"));

         // mapFrame.setTitle( mapFrame.getTitle() + " - " + mapFrame.mapPanel.myMapViewer.lnlgetMapContext().name);
	}

}