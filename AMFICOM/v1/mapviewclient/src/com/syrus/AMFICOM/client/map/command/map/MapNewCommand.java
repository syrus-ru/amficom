/*-
 * $$Id: MapNewCommand.java,v 1.33 2006/02/15 11:12:43 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.map;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.controllers.MapLibraryController;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.Map;
import com.syrus.util.Log;

/**
 * ???????? ????? ????? (Map). ???????? ? ???? ???????? ?????? ????
 * 
 * @version $Revision: 1.33 $, $Date: 2006/02/15 11:12:43 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapNewCommand extends AbstractCommand {
	ApplicationContext aContext;

	Map map;

	public MapNewCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		Log.debugMessage("Creating new map", Level.INFO); //$NON-NLS-1$
		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						I18N.getString(MapEditorResourceKeys.STATUS_CREATING_NEW_MAP)));
		try {
			Identifier userId = LoginManager.getUserId();
			Identifier domainId = LoginManager.getDomainId();

			this.map = Map.createInstance(
					userId, 
					domainId, 
					I18N.getString(MapEditorResourceKeys.VALUE_NEW),
					MapEditorResourceKeys.EMPTY_STRING);
			this.map.addMapLibrary(MapLibraryController.getDefaultMapLibrary());
		} catch(Exception e) {
			Log.errorMessage(e);
			this.map = null;
			setResult(Command.RESULT_NO);
			return;
		}

		this.map.setDomainId(LoginManager.getDomainId());

// if (mapFrame != null)
//		{
//
//			MapView mapView = mapFrame.getMapView();
//	
//			Map map = mapView.getMap();
//			try
//			{
//				MapStorableObjectPool.delete(map.getId());
//			}
//			catch (CommunicationException e)
//			{
//				Log.errorMessage(e);
//			}
//			catch (DatabaseException e)
//			{
//				Log.errorMessage(e);
//			}
//	
//			mapView.setLogicalNetLayer(mapFrame.getMapViewer().getLogicalNetLayer());
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

	public Map getMap() {
		return this.map;
	}

}
