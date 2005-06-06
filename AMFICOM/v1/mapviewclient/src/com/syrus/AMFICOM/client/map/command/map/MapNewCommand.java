/**
 * $Id: MapNewCommand.java,v 1.18 2005/06/06 12:20:31 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.map;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.Map;

/**
 * создание новой карты (Map). включает в себя создание нового вида
 * @author $Author: krupenn $
 * @version $Revision: 1.18 $, $Date: 2005/06/06 12:20:31 $
 * @module mapviewclient_v1
 */
public class MapNewCommand extends AbstractCommand {
	ApplicationContext aContext;

	Map map;

	public MapNewCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public void execute() {
		Environment.log(
				Environment.LOG_LEVEL_CONFIG,
				"Creating new map",
				getClass().getName(),
				"execute()");

		System.out.println("Creating new map context");
		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelMap.getString("MapNew")));
		try {
			Identifier userId = LoginManager.getUserId();
			Identifier domainId = LoginManager.getDomainId();

			this.map = Map.createInstance(userId, domainId, LangModelMap
					.getString("New"), "");
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
//			mapFrame.setTitle( LangModelMap.getString("Map") + " - " + mv.getName());
//		}
		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelGeneral.getString("Finished")));
		setResult(Command.RESULT_OK);
	}

	public Map getMap() {
		return this.map;
	}

}
