/**
 * $Id: MoveMarkCommand.java,v 1.2 2004/10/18 15:33:00 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;

import java.awt.geom.Point2D;

/**
 * Команда перемещения метки. вызывает только функцию "обновить состояние 
 * местоположения"
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/10/18 15:33:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MoveMarkCommand extends MapActionCommand
{
	LogicalNetLayer logicalNetLayer;
	ApplicationContext aContext;

	Point2D.Double initialAnchor;
	double initialDistance;

	Point2D.Double anchor;
	double distance;

	MapMarkElement mark;

	public MoveMarkCommand(MapMarkElement mark)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.mark = mark;
		initialAnchor = mark.getAnchor();
		initialDistance = mark.getDistance();
//		setState(mark.getState());
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals(com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle.DELTA_X))
		{
			execute();
		}
		else
		if(field.equals(com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle.DELTA_Y))
		{
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

		distance = mark.getDistance();
		mark.moveToFromStartLt(distance);
	}
	
	public void undo()
	{
		mark.moveToFromStartLt(distance);
	}

	public void redo()
	{
		mark.moveToFromStartLt(initialDistance);
	}
}
