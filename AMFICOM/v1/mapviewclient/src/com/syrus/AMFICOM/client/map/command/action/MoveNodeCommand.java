/**
 * $Id: MoveNodeCommand.java,v 1.8 2005/02/08 15:11:09 krupenn Exp $
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
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;

/**
 * Перемещение узла.
 * @author $Author: krupenn $
 * @version $Revision: 1.8 $, $Date: 2005/02/08 15:11:09 $
 * @module mapviewclient_v1
 */
public class MoveNodeCommand extends MapActionCommand
{
	/**
	 * начальная позиция перемещаемого элемента
	 */
	DoublePoint initialLocation;

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
		this.initialLocation = node.getLocation();
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals(MoveSelectionCommandBundle.DELTA_X))
		{
			this.deltaX = Double.parseDouble((String )value);
			execute();
		}
		else
		if(field.equals(MoveSelectionCommandBundle.DELTA_Y))
		{
			this.deltaY = Double.parseDouble((String )value);
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
		
		DoublePoint nodeLocation = this.node.getLocation();

		nodeLocation.setLocation(this.initialLocation.getX() + this.deltaX, this.initialLocation.getY() + this.deltaY);

		this.node.setLocation(nodeLocation);
	}
	
	public void undo()
	{
		this.node.setLocation(this.initialLocation);
	}
}
