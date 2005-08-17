/*
 * $Id: MapEditorNewViewCommand.java,v 1.20 2005/08/17 14:14:17 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
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
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Класс MapEditorNewViewCommand используется для создания новой топологической схемы в
 * модуле "Редактор топологических схем". При этом в модуле открываются все
 * окна (команда ViewMapAllCommand) и вызывается команда MapNewCommand
 * 
 * @version $Revision: 1.20 $, $Date: 2005/08/17 14:14:17 $
 * @module
 * @author $Author: arseniy $
 * @see MapNewCommand
 * @see ViewMapAllCommand
 */
public class MapEditorNewViewCommand extends AbstractCommand {
	ApplicationContext aContext;

	JDesktopPane desktop;

	public MapEditorNewViewCommand(
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
			mapFrame.getContext().getDispatcher().firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, "Ошибка соединения с картографическими данными"));
			e.printStackTrace();
		}
	}

}
