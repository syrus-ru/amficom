/**
 * $Id: CenterSelectionCommand.java,v 1.18 2005/06/06 12:20:31 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.command.navigate;

import java.util.Iterator;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Центрировать геометрическое место точек, являющих собой центры 
 * выделенных элементов карты
 * @author $Author: krupenn $
 * @version $Revision: 1.18 $, $Date: 2005/06/06 12:20:31 $
 * @module mapviewclient_v1
 */
public class CenterSelectionCommand extends MapNavigateCommand {
	public CenterSelectionCommand(LogicalNetLayer logicalNetLayer) {
		super(logicalNetLayer);
	}

	public void execute() {
		if(this.logicalNetLayer == null) {
			return;
		}

		MapElement me;
		
		MapView mapView = this.logicalNetLayer.getMapView();

		int count = 0;
		DoublePoint point = new DoublePoint(0.0, 0.0);
		
		double x = 0.0D;
		double y = 0.0D;

		for(Iterator it = mapView.getMap().getSelectedElements().iterator(); it.hasNext();) {
			me = (MapElement)it.next();
			DoublePoint an = me.getLocation();
			x += an.getX();
			y += an.getY();
			count++;
		}

/*
		for(Iterator it = mapView.getMap().getNodes().iterator(); it.hasNext();)
		{
			me = (MapElement)it.next();
			if(me.isSelected())
			{
				DoublePoint an = me.getLocation();
				x += an.getX();
				y += an.getY();
				count++;
			}
		}

		for(Iterator it = mapView.getMarkers().iterator(); it.hasNext();)
		{
			me = (MapElement)it.next();
			if(me.isSelected())
			{
				DoublePoint an = me.getLocation();
				x += an.getX();
				y += an.getY();
				count++;
			}
		}

		for(Iterator it = mapView.getMap().getAllNodeLinks().iterator(); it.hasNext();)
		{
			me = (MapElement)it.next();
			if(me.isSelected())
			{
				DoublePoint an = me.getLocation();
				x += an.getX();
				y += an.getY();
				count++;
			}
		}

		for(Iterator it = mapView.getMap().getAllPhysicalLinks().iterator(); it.hasNext();)
		{
			me = (MapElement)it.next();
			if(me.isSelected())
			{
				DoublePoint an = me.getLocation();
				x += an.getX();
				y += an.getY();
				count++;
			}
		}

		for(Iterator it = mapView.getCablePaths().iterator(); it.hasNext();)
		{
			me = (MapElement)it.next();
			if(me.isSelected())
			{
				DoublePoint an = me.getLocation();
				x += an.getX();
				y += an.getY();
				count++;
			}
		}

		for(Iterator it = mapView.getMeasurementPaths().iterator(); it.hasNext();)
		{
			me = (MapElement)it.next();
			if(me.isSelected())
			{
				DoublePoint an = me.getLocation();
				x += an.getX();
				y += an.getY();
				count++;
			}
		}
*/
		x /= count;
		y /= count;
		
		point.setLocation(x, y);
		
		try {
			this.logicalNetLayer.setCenter(point);
			this.logicalNetLayer.repaint(true);
		} catch(MapConnectionException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		} catch(MapDataException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
}
