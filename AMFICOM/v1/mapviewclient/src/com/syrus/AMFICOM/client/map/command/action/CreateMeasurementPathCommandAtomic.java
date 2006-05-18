/*-
 * $$Id: CreateMeasurementPathCommandAtomic.java,v 1.26 2005/10/31 12:30:07 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

/**
 * создание прокладки измерительного пути
 * 
 * @version $Revision: 1.26 $, $Date: 2005/10/31 12:30:07 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CreateMeasurementPathCommandAtomic extends MapActionCommand {
	/** создаваемый измерительный путь */
	MeasurementPath measurementPath;

	/** схемный путь */
	SchemePath schemePath;

	/** начальный узел */
	AbstractNode startNode;

	/** конечный узел */
	AbstractNode endNode;

	public CreateMeasurementPathCommandAtomic(
			SchemePath schemePath,
			AbstractNode startNode,
			AbstractNode endNode) {
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.schemePath = schemePath;
		this.startNode = startNode;
		this.endNode = endNode;
	}

	public MeasurementPath getPath() {
		return this.measurementPath;
	}

	@Override
	public void execute() {
		Log.debugMessage("create MeasurementPath for SchemePath "  //$NON-NLS-1$
				+ this.schemePath.getName() 
				+ " (" + this.schemePath.getId()  //$NON-NLS-1$
				+ ") with start at node " + this.startNode.getName()  //$NON-NLS-1$
				+ " (" + this.startNode.getId()  //$NON-NLS-1$
				+ ") and end at node " + this.endNode.getName()  //$NON-NLS-1$
				+ " (" + this.endNode.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
			Level.FINEST);
		
		this.measurementPath = MeasurementPath.createInstance(
				this.schemePath,
				this.startNode, 
				this.endNode,
				this.logicalNetLayer.getMapView());

		this.logicalNetLayer.getMapView().addMeasurementPath(this.measurementPath);
		setUndoable(false);
		setResult(Command.RESULT_OK);
	}
}

