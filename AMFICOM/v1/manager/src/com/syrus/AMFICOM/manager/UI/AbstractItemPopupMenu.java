/*-
* $Id: AbstractItemPopupMenu.java,v 1.2 2005/11/17 09:00:35 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import javax.swing.JPopupMenu;

import org.jgraph.graph.DefaultGraphCell;

import com.syrus.AMFICOM.manager.perspective.Perspective;


/**
 * @version $Revision: 1.2 $, $Date: 2005/11/17 09:00:35 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class AbstractItemPopupMenu<T extends Perspective> {

	public abstract JPopupMenu getPopupMenu(final DefaultGraphCell cell,
		final T perspective);
}

