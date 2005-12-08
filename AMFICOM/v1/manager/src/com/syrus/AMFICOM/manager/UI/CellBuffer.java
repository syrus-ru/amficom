/*-
* $Id: CellBuffer.java,v 1.2 2005/12/08 13:21:09 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import org.jgraph.graph.GraphModel;

import com.syrus.AMFICOM.manager.graph.ManagerGraphCell;

/**
 * @version $Revision: 1.2 $, $Date: 2005/12/08 13:21:09 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
final class CellBuffer {

	private final ManagerMainFrame	managerMainFrame;

	private ManagerGraphCell managerGraphCell;

	public CellBuffer(final ManagerMainFrame managerMainFrame) {
		this.managerMainFrame = managerMainFrame;
	}
	
	public final void putCells(final ManagerGraphCell cell) {
		if (this.managerGraphCell != null) {
			final GraphModel model = this.managerMainFrame.graph.getModel();
			model.remove(new Object[] { this.managerGraphCell});				
		}
		this.managerGraphCell = cell;
	}
	
	public final boolean isExists() {
		return this.managerGraphCell != null;
	}
	
	public final boolean isExists(final ManagerGraphCell cell) {
		return this.managerGraphCell == cell;
	}
	
	public final ManagerGraphCell getCell() {
		return this.managerGraphCell;
	}
	
	public final void clear() {
		this.managerGraphCell = null;
	}
}

