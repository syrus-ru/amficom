/*-
 * $$Id: MapRemoveMapCommand.java,v 1.16 2005/10/17 14:10:55 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.map;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.dialogs.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.MapRemoveWrapper;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.MapTableController;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * убрать из вида выбранную схему
 *  
 * @version $Revision: 1.16 $, $Date: 2005/10/17 14:10:55 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapRemoveMapCommand extends AbstractCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;

	public MapRemoveMapCommand(JDesktopPane desktop, ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(mapFrame == null) {
			return;
		}

		MapView mapView = mapFrame.getMapView();

		if(mapView == null) {
			return;
		}

		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						I18N.getString(MapEditorResourceKeys.STATUS_REMOVING_INTERNAL_MAP)));

		MapTableController mapTableController = MapTableController.getInstance();

		Map map = (Map )WrapperedTableChooserDialog.showChooserDialog(
				I18N.getString(MapEditorResourceKeys.TITLE_MAP),
				mapView.getMap().getMaps(),
				mapTableController,
				mapTableController.getKeysArray(),
				MapRemoveWrapper.getInstance(),
				true);

		if(map == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString("Aborted"))); //$NON-NLS-1$
			setResult(Command.RESULT_CANCEL);
			return;
		}

		mapView.getMap().removeMap(map);
		mapFrame.getContext().getDispatcher().firePropertyChange(
				new MapEvent(this, MapEvent.MAP_VIEW_CHANGED, mapFrame.getMapView()));
		mapFrame.getContext().getDispatcher().firePropertyChange(
				new MapEvent(this, MapEvent.UPDATE_SELECTION));
		mapFrame.getContext().getDispatcher().firePropertyChange(
				new MapEvent(this, MapEvent.SELECTION_CHANGED, mapFrame.getMapView().getMap().getSelectedElements()));

		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						I18N.getString("Finished"))); //$NON-NLS-1$
		setResult(Command.RESULT_OK);
	}

}
