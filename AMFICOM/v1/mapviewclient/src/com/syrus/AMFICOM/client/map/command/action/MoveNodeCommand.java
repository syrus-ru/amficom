/**
 * $Id: MoveNodeCommand.java,v 1.5 2004/12/22 16:38:40 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.AbstractNode;

import java.awt.geom.Point2D;

/**
 * Перемещение узла
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/12/22 16:38:40 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MoveNodeCommand extends MapActionCommand
{
	/**
	 * начальная позиция перемещаемого элемента
	 */
	DoublePoint location;

	/**
	 * абсолютное смещение по оси абсцисс
	 */
	double deltaX = 0.0D;

	/**
	 * абсолютное смещение по оси ординат
	 */
	double deltaY = 0.0D;

	/**
	 * перемещаемый элемент
	 */	
	AbstractNode node;

	public MoveNodeCommand(AbstractNode node)
	{
		super(MapActionCommand.ACTION_MOVE_SELECTION);
		this.node = node;

		// запомнить начальное положение
		location = node.getLocation();
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals(MoveSelectionCommandBundle.DELTA_X))
		{
			deltaX = Double.parseDouble((String )value);
			execute();
		}
		else
		if(field.equals(MoveSelectionCommandBundle.DELTA_Y))
		{
			deltaY = Double.parseDouble((String )value);
			execute();
		}
	}

	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
		this.aContext = logicalNetLayer.getContext();
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");
		
		node.setLocation(new DoublePoint(location.x + deltaX, location.y + deltaY));
	}
	
	public void undo()
	{
		node.setLocation(location);
	}
}
