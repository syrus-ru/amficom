/**
 * $Id: MoveNodeCommand.java,v 1.13 2005/07/11 13:18:04 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.util.Log;

/**
 * Перемещение узла.
 * @author $Author: bass $
 * @version $Revision: 1.13 $, $Date: 2005/07/11 13:18:04 $
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
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);
		
		DoublePoint nodeLocation = this.node.getLocation();

		nodeLocation.setLocation(this.initialLocation.getX() + this.deltaX, this.initialLocation.getY() + this.deltaY);

		this.node.setLocation(nodeLocation);
		setResult(Command.RESULT_OK);
	}
	
	public void undo()
	{
		this.node.setLocation(this.initialLocation);
	}
}
