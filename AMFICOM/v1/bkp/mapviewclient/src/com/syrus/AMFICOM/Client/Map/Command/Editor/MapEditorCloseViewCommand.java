/*
 * $Id: MapEditorCloseViewCommand.java,v 1.3 2004/10/19 10:41:03 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewCloseCommand;
import com.syrus.AMFICOM.Client.Map.Editor.MapEditorMainFrame;

/**
 * Класс MapMapCloseCommand используется для закрытия карты в окне карты в модуле 
 * "Редактор топологических схем"
 * класс использует команду MapCloseCommand для закрытия карты, после чего
 * генерирует событие закрытия
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/19 10:41:03 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapCloseCommand
 */
public class MapEditorCloseViewCommand extends VoidCommand
{
	MapEditorMainFrame mainFrame;
	Dispatcher dispatcher;

	public MapEditorCloseViewCommand(MapEditorMainFrame mainFrame, Dispatcher dispatcher)
	{
		this.mainFrame = mainFrame;
		this.dispatcher = dispatcher;
	}

	public Object clone()
	{
		return new MapEditorCloseViewCommand(mainFrame, dispatcher);
	}

	public void execute()
	{
		if(mainFrame.getMapFrame() == null)
			return;

		new MapViewCloseCommand(mainFrame.getMapFrame()).execute();
		dispatcher.notify(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));
		setResult(Command.RESULT_OK);
	}

}
