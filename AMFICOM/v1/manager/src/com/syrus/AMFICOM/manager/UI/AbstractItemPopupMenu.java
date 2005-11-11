/*-
* $Id: AbstractItemPopupMenu.java,v 1.1 2005/11/11 10:58:02 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import javax.swing.JPopupMenu;

import org.jgraph.graph.DefaultGraphCell;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/11 10:58:02 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class AbstractItemPopupMenu {

	public abstract JPopupMenu getPopupMenu(final DefaultGraphCell cell,
		final ManagerMainFrame managerMainFrame);
	
}

