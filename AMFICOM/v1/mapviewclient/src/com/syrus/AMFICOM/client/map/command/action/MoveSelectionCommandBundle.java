/**
 * $Id: MoveSelectionCommandBundle.java,v 1.11 2005/06/06 12:20:30 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;
import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;

/**
 * Перемещение объектов по карте. Команда является пучком команд 
 * (CommandBundle), передвгающих отдельные элементы.
 * @author $Author: krupenn $
 * @version $Revision: 1.11 $, $Date: 2005/06/06 12:20:30 $
 * @module mapviewclient_v1
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
		this.startPoint = point;
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
			this.deltaX = Double.parseDouble((String )value);
			super.setParameter(field, value);
		}
		else
		if(field.equals(DELTA_Y))
		{
			this.deltaY = Double.parseDouble((String )value);
			super.setParameter(field, value);
		}
		else
		if(field.equals(START_POINT))
		{
			this.startPoint = (Point )value;
			this.endPoint = (Point )value;
			//пересчитать смещение
			this.setShift();
			super.setParameter(DELTA_X, String.valueOf(this.deltaX));
			super.setParameter(DELTA_Y, String.valueOf(this.deltaY));
		}
		else
		if(field.equals(END_POINT))
		{
			this.endPoint = (Point )value;
			//пересчитать смещение
			this.setShift();
			super.setParameter(DELTA_X, String.valueOf(this.deltaX));
			super.setParameter(DELTA_Y, String.valueOf(this.deltaY));
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
		try {
			DoublePoint sp = this.logicalNetLayer.convertScreenToMap(this.startPoint);
			DoublePoint ep = this.logicalNetLayer.convertScreenToMap(this.endPoint);
			this.deltaX = ep.getX() - sp.getX();
			this.deltaY = ep.getY() - sp.getY();
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * создать отдельные команды на перемещение для всех выделенных
	 * точечных объектов
	 */
	protected void setElements()
	{
		Map map = this.logicalNetLayer.getMapView().getMap();
		Iterator iter = map.getSelectedElements().iterator();

		while (iter.hasNext() )
		{
			MapElement mapElement = (MapElement )iter.next();
			if(mapElement instanceof AbstractNode) {
				AbstractNode node = (AbstractNode)mapElement;
				if(node instanceof Mark) {
					Mark mme = (Mark)node;
					super.add(new MoveMarkCommand(mme));
				}
				// do not move external nodes
				else if(!map.getExternalNodes().contains(node))
					super.add(new MoveNodeCommand(node));
			}
		}
	}
	
	public void execute()
	{
		super.execute();
		this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
}
