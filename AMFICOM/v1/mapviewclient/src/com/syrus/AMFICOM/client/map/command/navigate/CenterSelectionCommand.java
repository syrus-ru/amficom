/**
 * $Id: CenterSelectionCommand.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Navigate;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;

import java.awt.geom.Point2D;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Центрировать геометрическое место точек, являющих собой центры 
 * выделенных элементов карты
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module
 * @author $Author: krupenn $
 * @see
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
			logicalNetLayer = (LogicalNetLayer )value;
		if(field.equals("applicationModel"))
			aModel = (ApplicationModel )value;
	}

	public Object clone()
	{
		return new CenterSelectionCommand(logicalNetLayer);
	}

	public void execute()
	{
		if(logicalNetLayer == null)
			return;

		MapElement me;

		// список, который будет хранить центры выделенных объектов
		LinkedList lis = new LinkedList();

		for(Iterator it = logicalNetLayer.getMapView().getMap().getNodes().iterator(); it.hasNext();)
		{
			me = (MapElement )it.next();
			if(me.isSelected())
				lis.add(me.getAnchor());
		}

		for(Iterator it = logicalNetLayer.getMapView().getMarkers().iterator(); it.hasNext();)
		{
			me = (MapElement )it.next();
			if(me.isSelected())
				lis.add(me.getAnchor());
		}

		for(Iterator it = logicalNetLayer.getMapView().getMap().getNodeLinks().iterator(); it.hasNext();)
		{
			me = (MapElement )it.next();
			if(me.isSelected())
				lis.add(me.getAnchor());
		}

		for(Iterator it = logicalNetLayer.getMapView().getMap().getPhysicalLinks().iterator(); it.hasNext();)
		{
			me = (MapElement )it.next();
			if(me.isSelected())
				lis.add(me.getAnchor());
		}

		for(Iterator it = logicalNetLayer.getMapView().getCablePaths().iterator(); it.hasNext();)
		{
			me = (MapElement )it.next();
			if(me.isSelected())
				lis.add(me.getAnchor());
		}

		for(Iterator it = logicalNetLayer.getMapView().getPaths().iterator(); it.hasNext();)
		{
			me = (MapElement )it.next();
			if(me.isSelected())
				lis.add(me.getAnchor());
		}

		// ГМТ центров выделенных элементов
		Point2D.Double point = new Point2D.Double(0.0, 0.0);
		
		for(Iterator it = lis.iterator(); it.hasNext();)
		{
			Point2D.Double pt = (Point2D.Double )it.next();

			point.x += pt.x;
			point.y += pt.y;
		}
		point.x /= lis.size();
		point.y /= lis.size();
		
		logicalNetLayer.setCenter(point);

		logicalNetLayer.repaint();
	}
}
