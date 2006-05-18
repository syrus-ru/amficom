/*-
 * $$Id: MoveNodeCommand.java,v 1.24 2005/10/31 12:30:08 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * ѕеремещение узла.
 * 
 * @version $Revision: 1.24 $, $Date: 2005/10/31 12:30:08 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MoveNodeCommand extends MapActionCommand {
	/**
	 * начальна€ позици€ перемещаемого элемента
	 */
	DoublePoint initialLocation;

	/**
	 * конечна€ позици€ перемещаемого элемента
	 */
	DoublePoint newLocation;

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

	public MoveNodeCommand(AbstractNode node) {
		super(MapActionCommand.ACTION_MOVE_SELECTION);
		this.node = node;

		// запомнить начальное положение
		this.initialLocation = node.getLocation();
	}

	@Override
	public void setParameter(String field, Object value) {
		if(field.equals(MoveSelectionCommandBundle.DELTA_X)) {
			this.deltaX = Double.parseDouble((String) value);
			execute();
		}
		else if(field.equals(MoveSelectionCommandBundle.DELTA_Y)) {
			this.deltaY = Double.parseDouble((String) value);
			execute();
		}
	}

	@Override
	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer) {
		this.logicalNetLayer = logicalNetLayer;
		this.aContext = logicalNetLayer.getContext();
	}

	@Override
	public void execute() {
		Log.debugMessage("move node " //$NON-NLS-1$
				+ this.node.getName() 
				+ " (" + this.node.getId() + ")" //$NON-NLS-1$ //$NON-NLS-2$
				+ " to new location ", //$NON-NLS-1$
			Level.FINEST);

		this.newLocation = this.node.getLocation();

		this.newLocation.setLocation(
				this.initialLocation.getX() + this.deltaX,
				this.initialLocation.getY() + this.deltaY);

		this.node.setLocation(this.newLocation);
		setResult(Command.RESULT_OK);
	}

	@Override
	public void redo() {
		this.node.setLocation(this.newLocation);
	}

	@Override
	public void undo() {
		this.node.setLocation(this.initialLocation);
	}
}
