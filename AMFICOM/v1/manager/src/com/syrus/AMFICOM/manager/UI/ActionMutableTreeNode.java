/*-
* $Id: ActionMutableTreeNode.java,v 1.1 2005/11/28 14:47:04 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.tree.DefaultMutableTreeNode;

import com.syrus.AMFICOM.manager.perspective.Perspective;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/28 14:47:04 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class ActionMutableTreeNode extends DefaultMutableTreeNode {
	
	private final AbstractAction	abstractAction;
	private final Perspective	perspective;

	public ActionMutableTreeNode(final AbstractAction abstractAction,
			final Perspective perspective) {
		super(abstractAction.getValue(Action.SHORT_DESCRIPTION), true);
		this.abstractAction = abstractAction;
		this.perspective = perspective;
	}
	
	public final AbstractAction getAbstractAction() {
		return this.abstractAction;
	}
	
	public final Perspective getPerspective() {
		return this.perspective;
	}
	
}
