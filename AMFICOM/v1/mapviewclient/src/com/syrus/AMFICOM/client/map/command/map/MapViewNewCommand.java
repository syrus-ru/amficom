/**
 * $Id: MapViewNewCommand.java,v 1.26 2005/08/02 16:57:04 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.map.command.map;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
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
import com.syrus.util.Log;

/**
 * ������� ����� ��� 
 * @author $Author: krupenn $
 * @version $Revision: 1.26 $, $Date: 2005/08/02 16:57:04 $
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
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "Creating new map view", Level.CONFIG);

		System.out.println("Creating new map view");
		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelMap.getString("MapViewNew")));

		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();

		DoublePoint center = MapPropertiesManager.getCenter();
		double zoom = MapPropertiesManager.getZoom();

		try {
			this.mapView = MapView.createInstance(
					userId,
					domainId,
					LangModelMap.getString("MapViewNew"),
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
