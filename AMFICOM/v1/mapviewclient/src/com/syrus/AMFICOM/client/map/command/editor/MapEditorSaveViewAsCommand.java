/*-
 * $$Id: MapEditorSaveViewAsCommand.java,v 1.25 2005/10/31 12:30:09 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.editor;

import java.util.Iterator;
import java.util.logging.Level;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.command.map.MapViewSaveAsCommand;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.util.Log;

/**
 * Класс MapEditorSaveViewAsCommand используется для сохранения топологической
 * схемы в модуле "Редактор топологических схем" с новым именем. Использует
 * команду MapSaveAsCommand
 * 
 * @version $Revision: 1.25 $, $Date: 2005/10/31 12:30:09 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 * @see MapViewSaveAsCommand
 */
public class MapEditorSaveViewAsCommand extends AbstractCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;

	/**
	 * @param aContext контекст модуля "Редактор топологических схем"
	 */
	public MapEditorSaveViewAsCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(mapFrame == null) {
			Log.debugMessage("MapView SaveAs: map frame is null! Cannot complete operation.", Level.SEVERE); //$NON-NLS-1$
			setResult(Command.RESULT_NO);
			return;
		}
		MapViewSaveAsCommand saveAsCommand = new MapViewSaveAsCommand(mapFrame
				.getMapView(), this.aContext);

		saveAsCommand.execute();

		if(saveAsCommand.getResult() == RESULT_OK) {
			MapView newMapView = saveAsCommand.getNewMapView();
			MapView oldMapView = mapFrame.getMapView();

			MapViewController controller = mapFrame.getMapViewer()
					.getLogicalNetLayer().getMapViewController();

			if(mapFrame != null) {
				try {
					controller.setMapView(newMapView);
				} catch(Exception e) {
					JOptionPane
							.showMessageDialog(
									Environment.getActiveWindow(),
									I18N.getString(MapEditorResourceKeys.ERROR_MAP_VIEW_SAVED_BUT_FAILED_TO_OPEN),
									I18N.getString(MapEditorResourceKeys.MESASGE_ERROR_OPENING),
									JOptionPane.WARNING_MESSAGE);
					setResult(Command.RESULT_OK);
					e.printStackTrace();
					return;
				}

				controller.setMap(oldMapView.getMap());

				for(Iterator it = oldMapView.getSchemes().iterator(); it
						.hasNext();) {
					controller.addScheme((Scheme )it.next());
				}

				mapFrame.setTitle(I18N.getString(MapEditorResourceKeys.TITLE_MAP_VIEW) + " - " //$NON-NLS-1$
						+ newMapView.getName());
				mapFrame.getMapViewer().getLogicalNetLayer().sendMapEvent(MapEvent.MAP_VIEW_CHANGED);
			}
			setResult(Command.RESULT_OK);
		}

	}

}
