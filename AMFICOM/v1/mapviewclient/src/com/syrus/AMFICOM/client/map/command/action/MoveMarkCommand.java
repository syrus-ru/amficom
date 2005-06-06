/**
 * $Id: MoveMarkCommand.java,v 1.11 2005/06/06 12:20:30 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.controllers.MarkController;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Mark;

/**
 * Команда перемещения метки. вызывает только функцию "обновить состояние 
 * местоположения"
 * @author $Author: krupenn $
 * @version $Revision: 1.11 $, $Date: 2005/06/06 12:20:30 $
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

		try {
			this.markController = (MarkController )super.logicalNetLayer.getMapViewController().getController(this.mark);
			this.distance = this.mark.getDistance();
			this.markController.moveToFromStartLt(this.mark, this.distance);
			setResult(Command.RESULT_OK);
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
	
	public void undo()
	{
		try {
			this.markController.moveToFromStartLt(this.mark, this.distance);
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void redo()
	{
		try {
			this.markController.moveToFromStartLt(this.mark, this.initialDistance);
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
