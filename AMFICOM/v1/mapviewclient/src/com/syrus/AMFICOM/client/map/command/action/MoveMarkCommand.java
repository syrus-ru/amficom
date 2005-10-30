/*-
 * $$Id: MoveMarkCommand.java,v 1.24 2005/10/30 16:31:18 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.map.controllers.MarkController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * Команда перемещения метки. вызывает только функцию "обновить состояние
 * местоположения"
 * 
 * @version $Revision: 1.24 $, $Date: 2005/10/30 16:31:18 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MoveMarkCommand extends MapActionCommand {
	DoublePoint initialLocation;
	double initialDistance;

	DoublePoint location;
	double distance;

	Mark mark;

	MarkController markController;

	public MoveMarkCommand(Mark mark) {
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.mark = mark;
		this.initialLocation = mark.getLocation();
		this.initialDistance = mark.getDistance();
		// setState(mark.getState());
	}

	@Override
	public void setParameter(String field, Object value) {
		if(field.equals(MoveSelectionCommandBundle.DELTA_X)) {
			execute();
		}
		else if(field.equals(MoveSelectionCommandBundle.DELTA_Y)) {
			execute();
		}
	}

	@Override
	public void execute() {
		assert Log.debugMessage("move mark " //$NON-NLS-1$
				+ this.mark.getName() 
				+ " (" + this.mark.getId() + ")" //$NON-NLS-1$ //$NON-NLS-2$
				+ " to distance " + this.distance,  //$NON-NLS-1$
			Level.FINEST);

		try {
			this.markController = (MarkController) 
				super.logicalNetLayer.getMapViewController().getController(this.mark);
			this.distance = this.mark.getDistance();
			this.markController.moveToFromStartLt(this.mark, this.distance);
			setResult(Command.RESULT_OK);
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			assert Log.debugMessage(e, Level.SEVERE);
		}
	}

	@Override
	public void undo() {
		try {
			this.markController.moveToFromStartLt(this.mark, this.distance);
		} catch(MapException e) {
			// TODO Auto-generated catch block
			assert Log.debugMessage(e, Level.SEVERE);
		}
	}

	@Override
	public void redo() {
		try {
			this.markController.moveToFromStartLt(
					this.mark,
					this.initialDistance);
		} catch(MapException e) {
			// TODO Auto-generated catch block
			assert Log.debugMessage(e, Level.SEVERE);
		}
	}
}
