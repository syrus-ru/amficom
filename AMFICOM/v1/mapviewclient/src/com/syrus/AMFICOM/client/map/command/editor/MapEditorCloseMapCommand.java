/*
 * $Id: MapEditorCloseMapCommand.java,v 1.4 2004/10/20 10:14:39 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapCloseCommand;
import com.syrus.AMFICOM.Client.Map.Editor.MapEditorMainFrame;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import javax.swing.JOptionPane;

/**
 * Класс MapMapCloseCommand используется для закрытия карты в окне карты в модуле 
 * "Редактор топологических схем"
 * класс использует команду MapCloseCommand для закрытия карты, после чего
 * генерирует событие закрытия
 * 
 * @version $Revision: 1.4 $, $Date: 2004/10/20 10:14:39 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapCloseCommand
 */
public class MapEditorCloseMapCommand extends VoidCommand
{
	MapEditorMainFrame mainFrame;
	Dispatcher dispatcher;

	public MapEditorCloseMapCommand(MapEditorMainFrame mainFrame, Dispatcher dispatcher)
	{
		this.mainFrame = mainFrame;
		this.dispatcher = dispatcher;
	}

	public void execute()
	{
		if(mainFrame.getMapFrame() == null)
			return;

		if(!mainFrame.getMapFrame().checkCanCloseMap())
			return;
		if(!mainFrame.getMapFrame().checkCanCloseMapView())
			return;

		new MapCloseCommand(mainFrame.getMapFrame()).execute();
		dispatcher.notify(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));
		setResult(Command.RESULT_OK);
	}

}
