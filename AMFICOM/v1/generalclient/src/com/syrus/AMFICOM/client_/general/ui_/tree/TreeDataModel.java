/*
 * $Id: TreeDataModel.java,v 1.1 2005/03/05 11:33:03 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree;

import java.awt.Color;
import java.util.List;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/05 11:33:03 $
 * @module generalclient_v1
 */

public interface TreeDataModel {
	StorableObjectTreeNode getRoot();
	List getChildNodes(StorableObjectTreeNode node);
	Class getNodeChildClass(StorableObjectTreeNode node);
	ObjectResourceController getNodeChildController(StorableObjectTreeNode node);
	Color getNodeTextColor(StorableObjectTreeNode node);

	void nodeAfterSelected(StorableObjectTreeNode node);
	void nodeBeforeExpanded(StorableObjectTreeNode node);
}
