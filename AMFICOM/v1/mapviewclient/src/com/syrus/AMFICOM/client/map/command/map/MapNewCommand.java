/**
 * $Id: MapNewCommand.java,v 1.11 2004/12/24 15:42:12 krupenn Exp $
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
 * @version $Revision: 1.11 $, $Date: 2004/12/24 15:42:12 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapNewCommand extends VoidCommand
{
	ApplicationContext aContext;
	MapFrame mapFrame;

	public MapView mv;
	public Map mc;

	public MapNewCommand()
	{
	}

	public MapNewCommand(MapFrame mapFrame, ApplicationContext aContext)
	{
		this.mapFrame = mapFrame;
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
			mc = Map.createInstance(
				new Identifier(aContext.getSessionInterface().getAccessIdentifier().user_id), 
				"", 
				"");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}

		mc.setDomainId(
				new Identifier(aContext.getSessionInterface().getAccessIdentifier().domain_id));

		mv = new MapView(null);

		mv.setName(LangModelMap.getString("New"));

		mv.setMap(mc);

//		Pool.put( MapView.typ, mv.getId(), mv);

		if (mapFrame != null)
		{

			MapView mapView = mapFrame.getMapView();
	
			Map map = mapView.getMap();
			try
			{
				MapStorableObjectPool.delete(map.getId());
			}
			catch (CommunicationException e)
			{
				e.printStackTrace();
			}
			catch (DatabaseException e)
			{
				e.printStackTrace();
			}
	
			mv.setLogicalNetLayer(mapFrame.getMapViewer().getLogicalNetLayer());
			mapFrame.setMapView(mv);
			mapFrame.setTitle( LangModelMap.getString("Map") + " - " + mv.getName());
		}
		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModel.getString("Finished")));
		setResult(Command.RESULT_OK);
	}

}
