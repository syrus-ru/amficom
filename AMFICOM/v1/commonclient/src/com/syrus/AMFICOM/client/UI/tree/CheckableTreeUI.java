/*-
 * $Id: CheckableTreeUI.java,v 1.1 2005/05/25 07:55:08 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI.tree;

import com.syrus.AMFICOM.logic.Item;

/**
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2005/05/25 07:55:08 $
 * @module generalclient_v1
 */

public class CheckableTreeUI extends IconedTreeUI {

	public CheckableTreeUI(Item rootItem) {
		super(rootItem);
		super.treeUI.setRenderer(CheckableNode.class, CheckableRenderer.getInstance());
		super.treeUI.setEditor(CheckableNode.class, CheckableEditor.getInstance());
		super.treeUI.getTree().setEditable(true);
	}
}
