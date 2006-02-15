/*-
 * $$Id: MapViewNewCommand.java,v 1.39 2006/02/15 11:12:43 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.map;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * создать новый вид
 *  
 * @version $Revision: 1.39 $, $Date: 2006/02/15 11:12:43 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapViewNewCommand extends AbstractCommand {
	ApplicationContext aContext;

	MapView mapView;

	Map map;

	public MapViewNewCommand(Map map, ApplicationContext aContext) {
		this.map = map;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		Log.debugMessage("Creating new map view", Level.INFO); //$NON-NLS-1$
		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						I18N.getString(MapEditorResourceKeys.STATUS_CREATING_NEW_MAP_VIEW)));

		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();

		DoublePoint center = MapPropertiesManager.getCenter();
		double zoom = MapPropertiesManager.getZoom();

		try {
			this.mapView = MapView.createInstance(
					userId,
					domainId,
					I18N.getString(MapEditorResourceKeys.VALUE_NEW_MAP_VIEW),
					MapEditorResourceKeys.EMPTY_STRING,
					center.getX(),
					center.getY(),
					zoom,
					zoom,
					this.map);

			StorableObjectPool.putStorableObject(this.mapView);
		} catch(CreateObjectException e) {
			Log.errorMessage(e);
			setResult(RESULT_NO);
			return;
		} catch(IllegalObjectEntityException e) {
			Log.errorMessage(e);
			setResult(RESULT_NO);
			return;
		}

		setResult(Command.RESULT_OK);
//		if (mapFrame != null)
//		{
//			mv.setLogicalNetLayer(mapFrame.getMapViewer().getLogicalNetLayer());
//
//			MapView mapView = mapFrame.getMapView();
//	
//			Map map = mapView.getMap();
//
//			mapFrame.setMapView(mv);
//			mapFrame.setTitle( I18N.getString(MapEditorResourceKeys.TITLE_MAP) + " - " + mv.getName());
//		}
		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						I18N.getString("Finished"))); //$NON-NLS-1$
		setResult(Command.RESULT_OK);
	}

	public MapView getMapView() {
		return this.mapView;
	}

}
