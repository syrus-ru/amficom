/**
 * $Id: MapViewNewCommand.java,v 1.13 2005/02/08 15:11:10 krupenn Exp $
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
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MapViewStorableObjectPool;

/**
 * создать новый вид 
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/02/08 15:11:10 $
 * @module mapviewclient_v1
 */
public class MapViewNewCommand extends VoidCommand
{
	ApplicationContext aContext;

	MapView mapView;
	Map map;

	public MapViewNewCommand(Map map, ApplicationContext aContext)
	{
		this.map = map;
		this.aContext = aContext;
	}

	public void execute()
	{
		Environment.log(Environment.LOG_LEVEL_CONFIG, "Creating new map view", getClass().getName(), "execute()");
		
		System.out.println("Creating new map view");
		this.aContext.getDispatcher().notify(
				new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModelMap.getString("MapNew")));

		AccessIdentifier_Transferable ait = 
			this.aContext.getSessionInterface().getAccessIdentifier();
		Identifier creatorId = new Identifier(ait.user_id);
		Identifier domainId = new Identifier(ait.domain_id);

		try
		{
			this.mapView = com.syrus.AMFICOM.mapview.MapView.createInstance(
					creatorId,
					domainId,
					LangModelMap.getString("New"),
					"",
					0.0D,
					0.0D,
					1.0D,
					1.0D,
					this.map);

			MapViewStorableObjectPool.putStorableObject(this.mapView);
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

		this.mapView.setName(LangModelMap.getString("New"));

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
		this.aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModel.getString("Finished")));
			setResult(Command.RESULT_OK);
	}


	public MapView getMapView()
	{
		return this.mapView;
	}

}
