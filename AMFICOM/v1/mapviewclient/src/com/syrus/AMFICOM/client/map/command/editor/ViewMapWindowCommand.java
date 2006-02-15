/*-
 * $$Id: ViewMapWindowCommand.java,v 1.39 2006/02/15 11:12:43 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.editor;

import java.awt.Dimension;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.command.map.MapNewCommand;
import com.syrus.AMFICOM.client.map.command.map.MapViewNewCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.model.MapApplicationModelFactory;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.util.Log;

/**
 * Команда отображает окно карты
 *  
 * @version $Revision: 1.39 $, $Date: 2006/02/15 11:12:43 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class ViewMapWindowCommand extends AbstractCommand {
	ApplicationContext aContext;

	JDesktopPane desktop;

	MapApplicationModelFactory factory;

	protected MapFrame mapFrame;

	public ViewMapWindowCommand(
			JDesktopPane desktop,
			ApplicationContext aContext,
			MapApplicationModelFactory factory) {
		this.desktop = desktop;
		this.aContext = aContext;
		this.factory = factory;
	}

	@Override
	public void execute() {
		try {
			this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

			if(this.mapFrame == null) {
				ApplicationContext aC = new ApplicationContext();
				aC.setApplicationModel(this.factory.create());
				aC.setDispatcher(this.aContext.getDispatcher());

				this.mapFrame = new MapFrame(aC);

				this.desktop.add(this.mapFrame);
				Dimension dim = this.desktop.getSize();
				this.mapFrame.setLocation(0, 0);
				this.mapFrame.setSize(dim.width * 3 / 5, dim.height);

				setMapFrame();
			}

			this.mapFrame.setVisible(true);
			this.mapFrame.toFront();
			this.aContext.getDispatcher().firePropertyChange(
					new MapEvent(this, MapEvent.MAP_FRAME_SHOWN, this.mapFrame));
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString("Finished"))); //$NON-NLS-1$
			setResult(Command.RESULT_OK);
		} catch(MapConnectionException e) {
			this.aContext.getDispatcher().firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, I18N.getString(MapEditorResourceKeys.ERROR_MAP_EXCEPTION_SERVER_CONNECTION)));
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					I18N.getString(MapEditorResourceKeys.ERROR_MAP_EXCEPTION_SERVER_CONNECTION),
					MapEditorResourceKeys.EMPTY_STRING,
					JOptionPane.ERROR_MESSAGE);
		} catch(MapDataException e) {
			this.aContext.getDispatcher().firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, I18N.getString("MapException.ServerConnection2"))); //$NON-NLS-1$
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					I18N.getString(MapEditorResourceKeys.ERROR_MAP_EXCEPTION_DATA),
					MapEditorResourceKeys.EMPTY_STRING,
					JOptionPane.ERROR_MESSAGE);
		} catch(ApplicationException e) {
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					I18N.getString(MapEditorResourceKeys.ERROR_APPLICATION_EXCEPTION),
					MapEditorResourceKeys.EMPTY_STRING,
					JOptionPane.ERROR_MESSAGE);
			Log.errorMessage(e);
		}
	}

	protected void setMapFrame()
			throws MapConnectionException, MapDataException {
		MapView mapView = null;
		Map map = null;

		MapNewCommand mnc = new MapNewCommand(this.aContext);
		mnc.execute();
		if(mnc.getResult() == Command.RESULT_OK) {
			map = mnc.getMap();
		}
		else {
			return;
		}

		MapViewNewCommand mvnc = new MapViewNewCommand(map, this.aContext);
		mvnc.execute();
		if(mvnc.getResult() == Command.RESULT_OK) {
			mapView = mvnc.getMapView();
			mapView.setCenter(this.mapFrame.getMapViewer().getLogicalNetLayer().getMapContext().getCenter());
			mapView.setScale(this.mapFrame.getMapViewer().getLogicalNetLayer().getMapContext().getScale());
		}
		else {
			return;
		}

		mapView.setMap(map);

		this.mapFrame.setMapView(mapView);
	}

	public MapFrame getMapFrame() {
		return this.mapFrame;
	}

}
