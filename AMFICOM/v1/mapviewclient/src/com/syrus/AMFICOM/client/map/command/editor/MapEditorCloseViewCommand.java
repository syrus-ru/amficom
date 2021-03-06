/*-
 * $$Id: MapEditorCloseViewCommand.java,v 1.30 2006/02/14 10:20:06 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.command.map.MapNewCommand;
import com.syrus.AMFICOM.client.map.command.map.MapViewCloseCommand;
import com.syrus.AMFICOM.client.map.command.map.MapViewNewCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.util.Log;

/**
 * ????? MapMapCloseCommand ???????????? ??? ???????? ????? ? ???? ????? ? ?????? 
 * "???????? ?????????????? ????"
 * ????? ?????????? ??????? MapCloseCommand ??? ???????? ?????, ????? ????
 * ?????????? ??????? ????????
 * 
 * @version $Revision: 1.30 $, $Date: 2006/02/14 10:20:06 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 * @see com.syrus.AMFICOM.client.map.command.map.MapCloseCommand
 */
public class MapEditorCloseViewCommand extends AbstractCommand {
	JDesktopPane desktop;

	Dispatcher dispatcher;

	public MapEditorCloseViewCommand(JDesktopPane desktop, Dispatcher dispatcher) {
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

		new MapViewCloseCommand(mapFrame.getMapView()).execute();

		MapNewCommand cmd = new MapNewCommand(mapFrame.getContext());
		cmd.execute();

		Map map = cmd.getMap();

		MapViewNewCommand cmd2 = new MapViewNewCommand(map, mapFrame.getContext());
		cmd2.execute();

		MapView mapView = cmd2.getMapView();

		try {
			mapView.setCenter(mapFrame.getMapViewer().getLogicalNetLayer().getMapContext().getCenter());
			mapView.setScale(mapFrame.getMapViewer().getLogicalNetLayer().getMapContext().getScale());
			mapFrame.setMapView(mapView);
			this.dispatcher.firePropertyChange(new MapEvent(
					this,
					MapEvent.MAP_VIEW_CLOSED));
			setResult(Command.RESULT_OK);
		} catch(MapException e) {
			mapFrame.getContext().getDispatcher().firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, I18N.getString(MapEditorResourceKeys.ERROR_MAP_EXCEPTION_SERVER_CONNECTION)));
			Log.errorMessage(e);
		}
	}

}
