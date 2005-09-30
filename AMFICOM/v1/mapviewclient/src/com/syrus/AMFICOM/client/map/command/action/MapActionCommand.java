/*-
 * $$Id: MapActionCommand.java,v 1.12 2005/09/30 16:08:37 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;

/**
 *  
 * @version $Revision: 1.12 $, $Date: 2005/09/30 16:08:37 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapActionCommand extends AbstractCommand
{
	protected static final int ACTION_NONE = 0;
	protected static final int ACTION_DRAW_NODE = 1;
	protected static final int ACTION_DRAW_LINE = 2;
	protected static final int ACTION_MOVE_SELECTION = 3;
	protected static final int ACTION_DELETE_SELECTION = 4;
	protected static final int ACTION_DROP_NODE = 5;
	protected static final int ACTION_DROP_LINE = 6;
	
	protected int action = ACTION_NONE;
	protected MapElement element = null;
	protected MapElementState elementState = null;

	LogicalNetLayer logicalNetLayer = null;
	ApplicationContext aContext = null;

	protected Throwable exception = null;
	
	protected boolean undoable = true;

	public MapActionCommand(int action)
	{
		this.action = action;
	}

	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
		this.aContext = logicalNetLayer.getContext();
	}
	
	protected ApplicationContext getContext()
	{
		return this.aContext;
	}
	
	protected LogicalNetLayer getLogicalNetLayer()
	{
		return this.logicalNetLayer;
	}

	public Throwable getException()
	{
		return this.exception;
	}
	
	public void setException(Throwable exception)
	{
		this.exception = exception;
	}

	public boolean isUndoable() {
		return this.undoable;
	}

	protected void setUndoable(boolean undoable) {
		this.undoable = undoable;
	}

}
