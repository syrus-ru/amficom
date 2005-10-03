/*-
 * $$Id: MapEditorCloseMapCommand.java,v 1.17 2005/10/03 10:35:00 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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
 * Класс MapMapCloseCommand используется для закрытия карты в окне карты в
 * модуле "Редактор топологических схем" класс использует команду
 * MapCloseCommand для закрытия карты, после чего генерирует событие закрытия
 * 
 * @version $Revision: 1.17 $, $Date: 2005/10/03 10:35:00 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 * @see MapCloseCommand
 */
public class MapEditorCloseMapCommand extends AbstractCommand {
	JDesktopPane desktop;
	Dispatcher dispatcher;

	public MapEditorCloseMapCommand(JDesktopPane desktop, Dispatcher dispatcher) {
		this.desktop = desktop;
		this.dispatcher = dispatcher;
	}

	@Override
	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(mapFrame == null)
			return;

		if(mapFrame.checkChangesPresent())
			return;

		new MapCloseCommand(mapFrame.getMap()).execute();

		MapNewCommand cmd = new MapNewCommand(mapFrame.getContext());
		cmd.execute();

		Map map = cmd.getMap();

		MapView mapView = mapFrame.getMapView();

		mapView.setMap(map);

		this.dispatcher.firePropertyChange(new MapEvent(
				this,
				MapEvent.MAP_VIEW_CLOSED));
		setResult(Command.RESULT_OK);
	}

}
