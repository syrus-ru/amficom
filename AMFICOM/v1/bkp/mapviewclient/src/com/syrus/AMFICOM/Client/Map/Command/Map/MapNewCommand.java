/**
 * $Id: MapNewCommand.java,v 1.15 2005/02/08 15:11:10 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.Map;

/**
 * создание новой карты (Map). включает в себя создание нового вида
 * @author $Author: krupenn $
 * @version $Revision: 1.15 $, $Date: 2005/02/08 15:11:10 $
 * @module mapviewclient_v1
 */
public class MapNewCommand extends VoidCommand
{
	ApplicationContext aContext;

	Map map;

	public MapNewCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public void execute()
	{
		Environment.log(Environment.LOG_LEVEL_CONFIG, "Creating new map", getClass().getName(), "execute()");
		
//		new MapViewNewCommand(mapFrame, aContext).execute();

		System.out.println("Creating new map context");
		this.aContext.getDispatcher().notify(
				new StatusMessageEvent(
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelMap.getString("MapNew")));
		try
		{
			Identifier userId = new Identifier(
				this.aContext.getSessionInterface().getAccessIdentifier().user_id);
	
			Identifier domainId = new Identifier(
				this.aContext.getSessionInterface().getAccessIdentifier().domain_id);
	
			this.map = Map.createInstance(
				userId,
				domainId,
				LangModelMap.getString("New"),
				"");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			this.map = null;
			setResult(Command.RESULT_NO);
			return;
		}

		this.map.setDomainId(
				new Identifier(this.aContext.getSessionInterface().getAccessIdentifier().domain_id));

//		if (mapFrame != null)
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
		this.aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModel.getString("Finished")));
		setResult(Command.RESULT_OK);
	}


	public Map getMap()
	{
		return this.map;
	}

}
