/*
 * $Id: MapMapCloseCommand.java,v 1.3 2004/06/28 11:47:51 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.General.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.Map.Editor.MapMDIMain;

import java.util.Vector;

/**
 * Класс $RCSfile: MapMapCloseCommand.java,v $ используется для закрытия карты в окне карты в модуле 
 * "Редактор топологических схем"
 * класс использует команду MapCloseCommand для закрытия карты, после чего
 * генерирует событие закрытия
 * 
 * @version $Revision: 1.3 $, $Date: 2004/06/28 11:47:51 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapCloseCommand
 */
public class MapMapCloseCommand extends VoidCommand
{
	MapMDIMain mapFrame;
	Dispatcher dispatcher;

	public MapMapCloseCommand(MapMDIMain myMapFrame, Dispatcher dispatcher)
	{
		this.mapFrame = myMapFrame;
		this.dispatcher = dispatcher;
	}

	public Object clone()
	{
		return new MapMapCloseCommand(mapFrame, dispatcher);
	}

	public void execute()
	{
		if(mapFrame.mapFrame == null)
			return;

		new MapCloseCommand(mapFrame.mapFrame).execute();
		dispatcher.notify(new OperationEvent(this, 0, "mapcloseevent"));
	}

}