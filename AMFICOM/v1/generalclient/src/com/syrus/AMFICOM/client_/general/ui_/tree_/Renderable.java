/*
 * $Id: Renderable.java,v 1.1 2005/03/28 11:40:28 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree_;

import javax.swing.tree.TreeCellRenderer;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/28 11:40:28 $
 * @module generalclient_v1
 */

public interface Renderable {
	TreeCellRenderer getRenderer();
}
