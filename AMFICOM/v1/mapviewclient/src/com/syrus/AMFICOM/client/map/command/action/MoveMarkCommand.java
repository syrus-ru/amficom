/**
 * $Id: MoveMarkCommand.java,v 1.8 2005/02/08 15:11:09 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Controllers.MarkController;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Mark;

/**
 * Команда перемещения метки. вызывает только функцию "обновить состояние 
 * местоположения"
 * @author $Author: krupenn $
 * @version $Revision: 1.8 $, $Date: 2005/02/08 15:11:09 $
 * @module mapviewclient_v1
 */
public class MoveMarkCommand extends MapActionCommand
{
	DoublePoint initialLocation;
	double initialDistance;

	DoublePoint location;
	double distance;

	Mark mark;
	
	MarkController markController;

	public MoveMarkCommand(Mark mark)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.mark = mark;
		this.initialLocation = mark.getLocation();
		this.initialDistance = mark.getDistance();
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

		this.markController = (MarkController )super.logicalNetLayer.getMapViewController().getController(this.mark);

		this.distance = this.mark.getDistance();
		this.markController.moveToFromStartLt(this.mark, this.distance);
	}
	
	public void undo()
	{
		this.markController.moveToFromStartLt(this.mark, this.distance);
	}

	public void redo()
	{
		this.markController.moveToFromStartLt(this.mark, this.initialDistance);
	}
}
