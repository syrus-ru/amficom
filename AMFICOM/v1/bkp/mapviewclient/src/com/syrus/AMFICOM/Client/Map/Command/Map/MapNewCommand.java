/**
 * $Id: MapNewCommand.java,v 1.9 2004/10/26 13:32:01 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Pool;

/**
 * создание новой карты (Map). включает в себя создание нового вида
 * 
 * 
 * 
 * @version $Revision: 1.9 $, $Date: 2004/10/26 13:32:01 $
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
		mc = new Map();

		mc.setId(aContext.getDataSource().GetUId(Map.typ));
		mc.setDomainId(aContext.getSessionInterface().getDomainId());
		mc.setUserId(aContext.getSessionInterface().getUserId());

//		Pool.put( mc.getTyp(), mc.getId(), mc);

		mv = new MapView();

		mv.setId(aContext.getDataSource().GetUId(MapView.typ));
		mv.setDomainId(aContext.getSessionInterface().getDomainId());
		mv.setName(LangModelMap.getString("New"));

		mv.setMap(mc);

//		Pool.put( MapView.typ, mv.getId(), mv);

		if (mapFrame != null)
		{

			MapView mapView = mapFrame.getMapView();
			Pool.remove(MapView.typ, mapView.getId());
	
			Map map = mapView.getMap();
			Pool.remove(Map.typ, map.getId());
	
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
