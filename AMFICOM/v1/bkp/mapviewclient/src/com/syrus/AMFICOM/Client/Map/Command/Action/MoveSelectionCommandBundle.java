/**
 * $Id: MoveSelectionCommandBundle.java,v 1.4 2004/10/20 10:14:39 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;

import java.awt.Point;
import java.awt.geom.Point2D;

import java.util.Iterator;

/**
 * Перемещение объектов по карте. Команда является пучком команд 
 * (CommandBundle), передвгающих отдельные элементы.
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/10/20 10:14:39 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MoveSelectionCommandBundle extends MapActionCommandBundle
{
	/** конечная точка сдвига */
	public static final String END_POINT = "endpoint";
	/** начальгая точка сдвига */
	public static final String START_POINT = "startpoint";
	/** абсолютное смещение по оси абсцисс */
	public static final String DELTA_X = "deltax";
	/** абсолютное смещение по оси ординат */
	public static final String DELTA_Y = "deltay";

	/**
	 * начальная точка смещения
	 */
	Point startPoint;
	
	/**
	 * конечная точка смещения
	 */
	Point endPoint;
	
	/**
	 * абсолютное смещение по оси абсцисс
	 */
	double deltaX = 0.0D;
	
	/**
	 * абсолютное смещение по оси ординат
	 */
	double deltaY = 0.0D;

	public MoveSelectionCommandBundle(Point point)
	{
		startPoint = point;
	}

	public MoveSelectionCommandBundle(LogicalNetLayer logicalNetLayer)
	{
		super();
		super.setLogicalNetLayer(logicalNetLayer);
	}
	
	/**
	 * при установке параметров перемещения параметры передаются всем командам
	 * в пучке
	 */
	public void setParameter(String field, Object value)
	{
		if(field.equals(DELTA_X))
		{
			deltaX = Double.parseDouble((String )value);
			super.setParameter(field, value);
		}
		else
		if(field.equals(DELTA_Y))
		{
			deltaY = Double.parseDouble((String )value);
			super.setParameter(field, value);
		}
		else
		if(field.equals(START_POINT))
		{
			startPoint = (Point )value;
			endPoint = (Point )value;
			//пересчитать смещение
			this.setShift();
			super.setParameter(DELTA_X, String.valueOf(deltaX));
			super.setParameter(DELTA_Y, String.valueOf(deltaY));
		}
		else
		if(field.equals(END_POINT))
		{
			endPoint = (Point )value;
			//пересчитать смещение
			this.setShift();
			super.setParameter(DELTA_X, String.valueOf(deltaX));
			super.setParameter(DELTA_Y, String.valueOf(deltaY));
		}
			
	}

	/**
	 * при установке логического сетевого слоя оздаются команды на перемещение
	 * выбранных объектов. выполнение удаления осуществляется только
	 * при вызове execute()
	 */
	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		super.setLogicalNetLayer(logicalNetLayer);

		this.setElements();
	}
	
	/**
	 * обновить абсолютное смещение по начальной и конечной точкам сдвига
	 */
	protected void setShift()
	{
		Point2D.Double sp = logicalNetLayer.convertScreenToMap(startPoint);
		Point2D.Double ep = logicalNetLayer.convertScreenToMap(endPoint);
		deltaX = ep.getX() - sp.getX();
		deltaY = ep.getY() - sp.getY();
	}
	
	/**
	 * создать отдельные команды на перемещение для всех выделенных
	 * точечных объектов
	 */
	protected void setElements()
	{
		Iterator e = logicalNetLayer.getMapView().getMap().getNodes().iterator();

		while (e.hasNext() )
		{
			MapNodeElement node = (MapNodeElement )e.next();
			if (node.isSelected())
			{
				if(node instanceof MapMarkElement)
				{
					MapMarkElement mme = (MapMarkElement )node;
					super.add(new MoveMarkCommand(mme));
				}
				super.add(new MoveNodeCommand(node));
			}
		}
	}
	
	public void execute()
	{
		super.execute();
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
}
