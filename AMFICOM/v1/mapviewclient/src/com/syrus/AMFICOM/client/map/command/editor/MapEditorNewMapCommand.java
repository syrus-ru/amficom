/*
 * $Id: MapEditorNewMapCommand.java,v 1.21 2005/09/16 14:53:33 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.map.command.editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.command.map.MapCloseCommand;
import com.syrus.AMFICOM.client.map.command.map.MapNewCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Класс $RCSfile: MapEditorNewMapCommand.java,v $ используется для создания новой топологической схемы в
 * модуле "Редактор топологических схем". При этом в модуле открываются все
 * окна (команда ViewMapAllCommand) и вызывается команда MapNewCommand
 * 
 * @version $Revision: 1.21 $, $Date: 2005/09/16 14:53:33 $
 * @module
 * @author $Author: krupenn $
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
							LangModelMap.getString("StatusMessage.OpenMapFrameFirst"))); //$NON-NLS-1$
			return;
		}

		if(mapFrame.checkChangesPresent())
			return;

		new MapCloseCommand(mapFrame.getMap()).execute();

		MapNewCommand cmd = new MapNewCommand(mapFrame.getContext());
		cmd.execute();

		Map map = cmd.getMap();

		MapView mapView = mapFrame.getMapView();

		mapView.setMap(map);

		this.aContext.getDispatcher().firePropertyChange(
				new MapEvent(this, MapEvent.MAP_VIEW_CHANGED, mapView));

		setResult(Command.RESULT_OK);
	}

}
