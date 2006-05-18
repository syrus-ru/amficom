/*-
 * $$Id: MapEditorNewMapCommand.java,v 1.27 2006/02/15 11:12:43 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.command.map.MapNewCommand;
import com.syrus.AMFICOM.client.map.command.map.MapViewCloseCommand;
import com.syrus.AMFICOM.client.map.command.map.MapViewNewCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.util.Log;

/**
 * Класс $RCSfile: MapEditorNewMapCommand.java,v $ используется для создания новой топологической схемы в
 * модуле "Редактор топологических схем". При этом в модуле открываются все
 * окна (команда ViewMapAllCommand) и вызывается команда MapNewCommand
 * 
 * @version $Revision: 1.27 $, $Date: 2006/02/15 11:12:43 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 * @see MapNewCommand
 * @see ViewMapAllCommand
 */
public class MapEditorNewMapCommand extends AbstractCommand {
	ApplicationContext aContext;

	JDesktopPane desktop;

	public MapEditorNewMapCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(mapFrame == null) {
			new ViewMapAllCommand(
					this.desktop,
					this.aContext,
					new MapMapEditorApplicationModelFactory()).execute();
			mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		}
		if(mapFrame == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this, 
							StatusMessageEvent.STATUS_MESSAGE, 
							I18N.getString(MapEditorResourceKeys.MESSAGE_OPEN_MAP_FRAME_FIRST)));
			return;
		}

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
			setResult(Command.RESULT_OK);
		} catch(MapException e) {
			mapFrame.getContext().getDispatcher().firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, I18N.getString(MapEditorResourceKeys.ERROR_MAP_EXCEPTION_SERVER_CONNECTION)));
			Log.errorMessage(e);
		}
//		new MapCloseCommand(mapFrame.getMap()).execute();
//
//		MapNewCommand cmd = new MapNewCommand(mapFrame.getContext());
//		cmd.execute();
//
//		Map map = cmd.getMap();
//
//		MapView mapView = mapFrame.getMapView();
//
//		mapView.setMap(map);
//
//		this.aContext.getDispatcher().firePropertyChange(
//				new MapEvent(this, MapEvent.MAP_VIEW_CHANGED, mapView));
//
//		setResult(Command.RESULT_OK);
	}

}
