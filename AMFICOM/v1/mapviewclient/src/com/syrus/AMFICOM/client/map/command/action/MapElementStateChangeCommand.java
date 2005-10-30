/*-
 * $$Id: MapElementStateChangeCommand.java,v 1.17 2005/10/30 16:31:18 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.util.Log;

/**
 * ��������� ������� ��������� ��������� �������� �����
 * 
 * @version $Revision: 1.17 $, $Date: 2005/10/30 16:31:18 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapElementStateChangeCommand extends MapActionCommand {
	MapElement me;
	MapElementState initialState;
	MapElementState finalState;

	public MapElementStateChangeCommand(
			MapElement me,
			MapElementState initialState,
			MapElementState finalState) {
		super(MapActionCommand.ACTION_DROP_LINE);
		this.me = me;
		this.initialState = initialState;
		this.finalState = finalState;
	}

	public MapElement getElement() {
		return this.me;
	}

	@Override
	public void execute() {
		assert Log.debugMessage("state change for element "  //$NON-NLS-1$
				+ this.me.getName() + " (" + this.me.getId() + ") from\n" //$NON-NLS-1$ //$NON-NLS-2$
				+ this.initialState.toString() + " \nto\n" //$NON-NLS-1$
				+ this.finalState.toString(), 
			Level.FINEST);
		this.me.revert(this.finalState);
		setResult(Command.RESULT_OK);
	}

	@Override
	public void redo() {
		this.me.revert(this.finalState);
	}

	@Override
	public void undo() {
		this.me.revert(this.initialState);
	}
}
