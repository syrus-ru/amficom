/**
 * $Id: MapViewNewCommand.java,v 1.8 2004/12/22 16:38:40 krupenn Exp $
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
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Pool;

/**
 * создать новый вид 
 * 
 * 
 * 
 * @version $Revision: 1.8 $, $Date: 2004/12/22 16:38:40 $
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

		mv.setName(LangModelMap.getString("New"));

		Map mc;
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

		mv.setMap(mc);

		setResult(Command.RESULT_OK);
		if (mapFrame != null)
		{
			mv.setLogicalNetLayer(mapFrame.getMapViewer().getLogicalNetLayer());

			MapView mapView = mapFrame.getMapView();
	
			Map map = mapView.getMap();

			mapFrame.setMapView(mv);
			mapFrame.setTitle( LangModelMap.getString("Map") + " - " + mv.getName());
		}
		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModel.getString("Finished")));
			setResult(Command.RESULT_OK);
	}

}
