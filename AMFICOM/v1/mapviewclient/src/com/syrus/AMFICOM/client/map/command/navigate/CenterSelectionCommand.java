/**
 * $Id: CenterSelectionCommand.java,v 1.12 2005/02/08 15:11:10 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Navigate;

import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Центрировать геометрическое место точек, являющих собой центры 
 * выделенных элементов карты
 * @author $Author: krupenn $
 * @version $Revision: 1.12 $, $Date: 2005/02/08 15:11:10 $
 * @module mapviewclient_v1
 */
public class CenterSelectionCommand extends VoidCommand
{
	LogicalNetLayer logicalNetLayer;
	ApplicationModel aModel;
	
	public CenterSelectionCommand(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("logicalNetLayer"))
			this.logicalNetLayer = (LogicalNetLayer )value;
		if(field.equals("applicationModel"))
			this.aModel = (ApplicationModel )value;
	}

	public void execute()
	{
		if(this.logicalNetLayer == null)
		{
			return;
		}

		MapElement me;
		
		MapView mapView = this.logicalNetLayer.getMapView();

		int count = 0;
		DoublePoint point = new DoublePoint(0.0, 0.0);
		
		double x = 0.0D;
		double y = 0.0D;

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

		for(Iterator it = mapView.getMap().getNodeLinks().iterator(); it.hasNext();)
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

		for(Iterator it = mapView.getMap().getPhysicalLinks().iterator(); it.hasNext();)
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

		x /= count;
		y /= count;
		
		point.setLocation(x, y);
		
		this.logicalNetLayer.setCenter(point);

		this.logicalNetLayer.repaint(true);
	}
}
