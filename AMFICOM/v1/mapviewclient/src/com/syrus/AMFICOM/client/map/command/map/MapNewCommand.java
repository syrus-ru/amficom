/**
 * $Id: MapNewCommand.java,v 1.13 2005/01/13 15:16:24 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.map.MapStorableObjectPool;

/**
 * создание новой карты (Map). включает в себя создание нового вида
 * 
 * 
 * 
 * @version $Revision: 1.13 $, $Date: 2005/01/13 15:16:24 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapNewCommand extends VoidCommand
{
	ApplicationContext aContext;

	Map map;

	public MapNewCommand()
	{
	}

	public MapNewCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public void execute()
	{
		Environment.log(Environment.LOG_LEVEL_CONFIG, "Creating new map", getClass().getName(), "execute()");
		
//		new MapViewNewCommand(mapFrame, aContext).execute();

		System.out.println("Creating new map context");
		aContext.getDispatcher().notify(
				new StatusMessageEvent(
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelMap.getString("MapNew")));
		try
		{
			Identifier userId = new Identifier(
				aContext.getSessionInterface().getAccessIdentifier().user_id);
	
			Identifier domainId = new Identifier(
				aContext.getSessionInterface().getAccessIdentifier().domain_id);
	
			map = Map.createInstance(
				userId,
				domainId,
				LangModelMap.getString("New"),
				"");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			map = null;
			setResult(Command.RESULT_NO);
			return;
		}

		map.setDomainId(
				new Identifier(aContext.getSessionInterface().getAccessIdentifier().domain_id));

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
		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModel.getString("Finished")));
		setResult(Command.RESULT_OK);
	}


	public Map getMap()
	{
		return map;
	}

}
