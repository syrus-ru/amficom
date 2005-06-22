/*
 * $Id: MapEditorCloseMapCommand.java,v 1.13 2005/06/22 08:43:47 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.command.map.MapCloseCommand;
import com.syrus.AMFICOM.client.map.command.map.MapNewCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Класс MapMapCloseCommand используется для закрытия карты в окне карты в модуле 
 * "Редактор топологических схем"
 * класс использует команду MapCloseCommand для закрытия карты, после чего
 * генерирует событие закрытия
 * 
 * @version $Revision: 1.13 $, $Date: 2005/06/22 08:43:47 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapCloseCommand
 */
public class MapEditorCloseMapCommand extends AbstractCommand
{
	JDesktopPane desktop;
	Dispatcher dispatcher;

	public MapEditorCloseMapCommand(JDesktopPane desktop, Dispatcher dispatcher)
	{
		this.desktop = desktop;
		this.dispatcher = dispatcher;
	}

	public void execute()
	{
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

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

		this.dispatcher.firePropertyChange(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));
		setResult(Command.RESULT_OK);
	}

}
