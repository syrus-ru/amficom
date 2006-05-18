/*-
* $Id: ManagerGraphCell.java,v 1.2 2005/12/05 14:41:22 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.graph;

import org.jgraph.graph.DefaultGraphCell;

import com.syrus.AMFICOM.manager.beans.AbstractBean;
import com.syrus.AMFICOM.manager.perspective.Perspective;


/**
 * @version $Revision: 1.2 $, $Date: 2005/12/05 14:41:22 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class ManagerGraphCell extends DefaultGraphCell {

	private Perspective perspective;
	
	public ManagerGraphCell(final Object userObject, 
			final Perspective perspective) {
		super(userObject);
		this.perspective = perspective;
	}

	public final void setPerspective(Perspective perspective) {
		this.perspective = perspective;
	}
	
	public final Perspective getPerspective() {
		return this.perspective;
	}	
	
	public MPort getMPort() {
		return (MPort) this.children.get(0);
	}
	
	public void add(final MPort port) {
		super.add(port);
	}
	
	public final AbstractBean getAbstractBean() {
		return this.getMPort().getBean();
	}
}
