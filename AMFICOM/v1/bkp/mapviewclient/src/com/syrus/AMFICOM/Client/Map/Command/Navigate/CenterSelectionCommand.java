/**
 * $Id: CenterSelectionCommand.java,v 1.5 2004/12/01 10:55:32 krupenn Exp $
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

/**
 * Центрировать геометрическое место точек, являющих собой центры 
 * выделенных элементов карты
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/12/01 10:55:32 $
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

		int count = 0;
		Point2D.Double point = new Point2D.Double(0.0, 0.0);

		for(Iterator it = logicalNetLayer.getMapView().getMap().getNodes().iterator(); it.hasNext();)
		{
			me = (MapElement )it.next();
			if(me.isSelected())
			{
				Point2D.Double an = me.getAnchor();
				point.x += an.x;
				point.y += an.y;
				count ++;
			}
		}

		for(Iterator it = logicalNetLayer.getMapView().getMarkers().iterator(); it.hasNext();)
		{
			me = (MapElement )it.next();
			if(me.isSelected())
			{
				Point2D.Double an = me.getAnchor();
				point.x += an.x;
				point.y += an.y;
				count ++;
			}
		}

		for(Iterator it = logicalNetLayer.getMapView().getMap().getNodeLinks().iterator(); it.hasNext();)
		{
			me = (MapElement )it.next();
			if(me.isSelected())
			{
				Point2D.Double an = me.getAnchor();
				point.x += an.x;
				point.y += an.y;
				count ++;
			}
		}

		for(Iterator it = logicalNetLayer.getMapView().getMap().getPhysicalLinks().iterator(); it.hasNext();)
		{
			me = (MapElement )it.next();
			if(me.isSelected())
			{
				Point2D.Double an = me.getAnchor();
				point.x += an.x;
				point.y += an.y;
				count ++;
			}
		}

		for(Iterator it = logicalNetLayer.getMapView().getCablePaths().iterator(); it.hasNext();)
		{
			me = (MapElement )it.next();
			if(me.isSelected())
			{
				Point2D.Double an = me.getAnchor();
				point.x += an.x;
				point.y += an.y;
				count ++;
			}
		}

		for(Iterator it = logicalNetLayer.getMapView().getMeasurementPaths().iterator(); it.hasNext();)
		{
			me = (MapElement )it.next();
			if(me.isSelected())
			{
				Point2D.Double an = me.getAnchor();
				point.x += an.x;
				point.y += an.y;
				count ++;
			}
		}

		point.x /= count;
		point.y /= count;
		
		logicalNetLayer.setCenter(point);

		logicalNetLayer.repaint(true);
	}
}
