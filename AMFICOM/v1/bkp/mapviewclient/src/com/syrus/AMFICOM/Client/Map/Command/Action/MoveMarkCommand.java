/**
 * $Id: MoveMarkCommand.java,v 1.4 2004/12/07 17:05:54 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.DoublePoint;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;

import com.syrus.AMFICOM.Client.Resource.Map.MarkController;
import java.awt.geom.Point2D;

/**
 * Команда перемещения метки. вызывает только функцию "обновить состояние 
 * местоположения"
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/07 17:05:54 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MoveMarkCommand extends MapActionCommand
{
	LogicalNetLayer logicalNetLayer;
	ApplicationContext aContext;

	DoublePoint initialLocation;
	double initialDistance;

	DoublePoint location;
	double distance;

	MapMarkElement mark;
	
	MarkController mc;

	public MoveMarkCommand(MapMarkElement mark)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.mark = mark;
		initialLocation = mark.getLocation();
		initialDistance = mark.getDistance();
//		setState(mark.getState());
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals(MoveSelectionCommandBundle.DELTA_X))
		{
			execute();
		}
		else
		if(field.equals(MoveSelectionCommandBundle.DELTA_Y))
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

		mc = (MarkController )logicalNetLayer.getMapViewController().getController(mark);

		distance = mark.getDistance();
		mc.moveToFromStartLt(mark, distance);
	}
	
	public void undo()
	{
		mc.moveToFromStartLt(mark, distance);
	}

	public void redo()
	{
		mc.moveToFromStartLt(mark, initialDistance);
	}
}
