/**
 * $Id: MapViewNewCommand.java,v 1.6 2004/10/20 10:14:39 krupenn Exp $
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
 * создать новый вид 
 * 
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2004/10/20 10:14:39 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapViewNewCommand extends VoidCommand
{
	ApplicationContext aContext;
	MapFrame mapFrame;

	public MapView mv;
	public Map mc;

	public MapViewNewCommand()
	{
	}

	public MapViewNewCommand(MapFrame mapFrame, ApplicationContext aContext)
	{
		this.mapFrame = mapFrame;
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
		mv = new MapView();

		mv.setId(aContext.getDataSource().GetUId(MapView.typ));
		mv.setDomainId(aContext.getSessionInterface().getDomainId());
		mv.setName(LangModelMap.getString("New"));

		Map mc = new Map();
		mc.setId(aContext.getDataSource().GetUId(Map.typ));
		mc.setDomainId(aContext.getSessionInterface().getDomainId());
		mc.setUserId(aContext.getSessionInterface().getUserId());
		Pool.put( mc.getTyp(), mc.getId(), mc);
		mv.setMap(mc);

		setResult(Command.RESULT_OK);
		Pool.put( MapView.typ, mv.getId(), mv);
		if (mapFrame != null)
		{
			mv.setLogicalNetLayer(mapFrame.getMapViewer().getLogicalNetLayer());

			MapView mapView = mapFrame.getMapView();
			Pool.remove(MapView.typ, mapView.getId());
	
			Map map = mapView.getMap();
			Pool.remove(Map.typ, map.getId());
	
			mapFrame.setMapView(mv);
			mapFrame.setTitle( LangModelMap.getString("Map") + " - " + mv.getName());
		}
		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModel.getString("Finished")));
			setResult(Command.RESULT_OK);
	}

}
