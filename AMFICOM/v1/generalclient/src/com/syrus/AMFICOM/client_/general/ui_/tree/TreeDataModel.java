/*
 * $Id: TreeDataModel.java,v 1.2 2005/03/10 07:54:59 stas Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/03/10 07:54:59 $
 * @module generalclient_v1
 */

public interface TreeDataModel {
	StorableObjectTreeNode getRoot();
	List getChildNodes(StorableObjectTreeNode node);
	Class getNodeChildClass(StorableObjectTreeNode node);
	ObjectResourceController getNodeChildController(StorableObjectTreeNode node);
	Color getNodeTextColor(StorableObjectTreeNode node);
}
