/*
 * $Id: MapEditorSaveViewAsCommand.java,v 1.16 2005/08/11 12:43:30 arseniy Exp $
 * Syrus Systems Научно-технический центр Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.map.command.editor;

import java.util.Iterator;

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
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.Scheme;

/**
 * Класс MapEditorSaveViewAsCommand используется для сохранения топологической
 * схемы в модуле "Редактор топологических схем" с новым именем. Использует
 * команду MapSaveAsCommand
 * 
 * @author $Author: arseniy $
 * @version $Revision: 1.16 $, $Date: 2005/08/11 12:43:30 $
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

	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(mapFrame == null) {
			System.out
					.println("MapView SaveAs: map frame is null! Cannot complete operation.");
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
									"Map View saved, but failed to open it in Map Frame",
									"Map View SaveAs Command",
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

				mapFrame.setTitle(LangModelMap.getString("MapView") + " - "
						+ newMapView.getName());
				mapFrame.getMapViewer().getLogicalNetLayer().sendMapEvent(MapEvent.MAP_VIEW_CHANGED);
			}
			setResult(Command.RESULT_OK);
		}

	}

}
