/**
 * $Id: MapViewNewCommand.java,v 1.11 2004/12/29 19:05:20 krupenn Exp $
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
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.mapview.MapViewStorableObjectPool;

/**
 * создать новый вид 
 * 
 * 
 * 
 * @version $Revision: 1.11 $, $Date: 2004/12/29 19:05:20 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapViewNewCommand extends VoidCommand
{
	ApplicationContext aContext;

	MapView mapView;
	Map map;

	public MapViewNewCommand()
	{
	}

	public MapViewNewCommand(Map map, ApplicationContext aContext)
	{
		this.map = map;
		this.aContext = aContext;
	}

	public void execute()
	{
		Environment.log(Environment.LOG_LEVEL_CONFIG, "Creating new map view", getClass().getName(), "execute()");
		
		System.out.println("Creating new map view");
		aContext.getDispatcher().notify(
				new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModelMap.getString("MapNew")));

		AccessIdentifier_Transferable ait = 
			aContext.getSessionInterface().getAccessIdentifier();
		Identifier creatorId = new Identifier(ait.user_id);
		Identifier domainId = new Identifier(ait.domain_id);

		try
		{
			mapView = new MapView(creatorId, domainId, map);

			MapViewStorableObjectPool.putStorableObject(mapView.getMapViewStorable());
		}
		catch (CreateObjectException e)
		{
			e.printStackTrace();
			setResult(RESULT_NO);
			return;
		}
		catch (IllegalObjectEntityException e)
		{
			e.printStackTrace();
			setResult(RESULT_NO);
			return;
		}

		mapView.setName(LangModelMap.getString("New"));

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
//			mapFrame.setTitle( LangModelMap.getString("Map") + " - " + mv.getName());
//		}
		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModel.getString("Finished")));
			setResult(Command.RESULT_OK);
	}


	public MapView getMapView()
	{
		return mapView;
	}

}
