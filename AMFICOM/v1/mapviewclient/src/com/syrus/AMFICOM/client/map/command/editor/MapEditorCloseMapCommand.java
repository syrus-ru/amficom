/*
 * $Id: MapEditorCloseMapCommand.java,v 1.6 2004/12/28 17:35:12 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Command.Map.MapNewCommand;
import com.syrus.AMFICOM.Client.Map.Editor.MapEditorMainFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import javax.swing.JOptionPane;

/**
 * Класс MapMapCloseCommand используется для закрытия карты в окне карты в модуле 
 * "Редактор топологических схем"
 * класс использует команду MapCloseCommand для закрытия карты, после чего
 * генерирует событие закрытия
 * 
 * @version $Revision: 1.6 $, $Date: 2004/12/28 17:35:12 $
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
		MapFrame mapFrame = mainFrame.getMapFrame();

		if(mapFrame == null)
			return;

		if(!mapFrame.checkCanCloseMap())
			return;
		if(!mapFrame.checkCanCloseMapView())
			return;

		new MapCloseCommand(mapFrame.getMap()).execute();

		MapNewCommand cmd = new MapNewCommand(mapFrame.getContext());
		cmd.execute();
		
		Map map = cmd.getMap();

		MapView mapView = mapFrame.getMapView();

		mapView.setMap(map);

		dispatcher.notify(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));
		setResult(Command.RESULT_OK);
	}

}
