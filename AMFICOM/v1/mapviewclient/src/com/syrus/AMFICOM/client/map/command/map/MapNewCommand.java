/**
 * $Id: MapNewCommand.java,v 1.28 2005/09/25 16:08:02 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.map;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.controllers.MapLibraryController;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.Map;
import com.syrus.util.Log;

/**
 * создание новой карты (Map). включает в себя создание нового вида
 * @author $Author: krupenn $
 * @version $Revision: 1.28 $, $Date: 2005/09/25 16:08:02 $
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
						LangModelMap.getString(MapEditorResourceKeys.STATUS_CREATING_NEW_MAP)));
		try {
			Identifier userId = LoginManager.getUserId();
			Identifier domainId = LoginManager.getDomainId();

			this.map = Map.createInstance(
					userId, 
					domainId, 
					LangModelMap.getString(MapEditorResourceKeys.VALUE_NEW),
					MapEditorResourceKeys.EMPTY_STRING);
			this.map.addMapLibrary(MapLibraryController.getDefaultMapLibrary());
		} catch(Exception e) {
			e.printStackTrace();
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
//				e.printStackTrace();
//			}
//			catch (DatabaseException e)
//			{
//				e.printStackTrace();
//			}
//	
//			mapView.setLogicalNetLayer(mapFrame.getMapViewer().getLogicalNetLayer());
//			mapFrame.setMapView(mv);
//			mapFrame.setTitle( LangModelMap.getString(MapEditorResourceKeys.TITLE_MAP) + " - " + mv.getName());
//		}
		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelGeneral.getString("Finished"))); //$NON-NLS-1$
		setResult(Command.RESULT_OK);
	}

	public Map getMap() {
		return this.map;
	}

}
