/**
 * $Id: MoveMarkCommand.java,v 1.7 2005/01/21 16:19:57 krupenn Exp $
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
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Mark;

import com.syrus.AMFICOM.Client.Map.Controllers.MarkController;
import java.awt.geom.Point2D;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;

/**
 * Команда перемещения метки. вызывает только функцию "обновить состояние 
 * местоположения"
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2005/01/21 16:19:57 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MoveMarkCommand extends MapActionCommand
{
	DoublePoint initialLocation;
	double initialDistance;

	DoublePoint location;
	double distance;

	Mark mark;
	
	MarkController mc;

	public MoveMarkCommand(Mark mark)
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

	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		mc = (MarkController )super.logicalNetLayer.getMapViewController().getController(mark);

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
