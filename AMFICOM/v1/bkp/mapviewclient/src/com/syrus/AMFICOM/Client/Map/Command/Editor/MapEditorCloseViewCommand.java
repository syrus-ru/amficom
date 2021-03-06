/*
 * $Id: MapEditorCloseViewCommand.java,v 1.14 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * ??????-??????????? ?????
 * ??????: ???????
 *
 * ?????????: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapNewCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewCloseCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewNewCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * ????? MapMapCloseCommand ???????????? ??? ???????? ????? ? ???? ????? ? ?????? 
 * "???????? ?????????????? ????"
 * ????? ?????????? ??????? MapCloseCommand ??? ???????? ?????, ????? ????
 * ?????????? ??????? ????????
 * 
 * @version $Revision: 1.14 $, $Date: 2005/05/27 15:14:55 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see com.syrus.AMFICOM.Client.Map.Command.Map.MapCloseCommand
 */
public class MapEditorCloseViewCommand extends AbstractCommand {
	JDesktopPane desktop;

	Dispatcher dispatcher;

	public MapEditorCloseViewCommand(JDesktopPane desktop, Dispatcher dispatcher) {
		this.desktop = desktop;
		this.dispatcher = dispatcher;
	}

	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(mapFrame == null)
			return;

		if(!mapFrame.checkCanCloseMap())
			return;
		if(!mapFrame.checkCanCloseMapView())
			return;

		new MapViewCloseCommand(mapFrame.getMapView()).execute();

		MapNewCommand cmd = new MapNewCommand(mapFrame.getContext());
		cmd.execute();

		Map map = cmd.getMap();

		MapViewNewCommand cmd2 = new MapViewNewCommand(map, mapFrame.getContext());
		cmd2.execute();

		MapView mapView = cmd2.getMapView();

		try {
			mapView.setCenter(mapFrame.getMapViewer().getLogicalNetLayer().getCenter());
			mapView.setScale(mapFrame.getMapViewer().getLogicalNetLayer().getScale());
			mapFrame.setMapView(mapView);
			this.dispatcher.firePropertyChange(new MapEvent(
					this,
					MapEvent.MAP_VIEW_CLOSED));
			setResult(Command.RESULT_OK);
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
