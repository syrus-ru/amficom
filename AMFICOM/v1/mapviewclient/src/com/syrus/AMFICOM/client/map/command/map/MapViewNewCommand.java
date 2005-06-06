/**
 * $Id: MapViewNewCommand.java,v 1.22 2005/06/06 12:57:02 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.command.map;

import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * создать новый вид 
 * @author $Author: krupenn $
 * @version $Revision: 1.22 $, $Date: 2005/06/06 12:57:02 $
 * @module mapviewclient_v1
 */
public class MapViewNewCommand extends AbstractCommand {
	ApplicationContext aContext;

	MapView mapView;

	Map map;

	public MapViewNewCommand(Map map, ApplicationContext aContext) {
		this.map = map;
		this.aContext = aContext;
	}

	public void execute() {
		Environment.log(
				Environment.LOG_LEVEL_CONFIG,
				"Creating new map view",
				getClass().getName(),
				"execute()");

		System.out.println("Creating new map view");
		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelMap.getString("MapNew")));

		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();

		DoublePoint center = MapPropertiesManager.getCenter();
		double zoom = MapPropertiesManager.getZoom();

		try {
			this.mapView = MapView.createInstance(
					userId,
					domainId,
					LangModelMap.getString("New"),
					"",
					center.getX(),
					center.getY(),
					zoom,
					zoom,
					this.map);

			StorableObjectPool.putStorableObject(this.mapView);
		} catch(CreateObjectException e) {
			e.printStackTrace();
			setResult(RESULT_NO);
			return;
		} catch(IllegalObjectEntityException e) {
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
		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelGeneral.getString("Finished")));
		setResult(Command.RESULT_OK);
	}

	public MapView getMapView() {
		return this.mapView;
	}

}
