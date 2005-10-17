/*-
 * $$Id: MapViewRemoveSchemeCommand.java,v 1.21 2005/10/17 14:08:46 krupenn Exp $$
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
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.SchemeTableController;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.Scheme;

/**
 * убрать из вида выбранную схему
 *  
 * @version $Revision: 1.21 $, $Date: 2005/10/17 14:08:46 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapViewRemoveSchemeCommand extends AbstractCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;

	public MapViewRemoveSchemeCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(mapFrame == null) {
			return;
		}

		MapViewController controller = mapFrame.getMapViewer()
				.getLogicalNetLayer().getMapViewController();

		MapView mapView = mapFrame.getMapView();

		if(mapView == null) {
			return;
		}

		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						I18N.getString(MapEditorResourceKeys.STATUS_REMOVING_SCHEME_FROM_MAP_VIEW)));

		SchemeTableController schemeTableController = 
			SchemeTableController.getInstance();

		Scheme scheme = (Scheme )WrapperedTableChooserDialog.showChooserDialog(
				I18N.getString(MapEditorResourceKeys.TITLE_SCHEME),
				mapView.getSchemes(),
				schemeTableController,
				schemeTableController.getKeysArray(),
				null,
				false);

		if(scheme == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString("Aborted"))); //$NON-NLS-1$
			setResult(Command.RESULT_CANCEL);
			return;
		}

		controller.removeScheme(scheme);
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
