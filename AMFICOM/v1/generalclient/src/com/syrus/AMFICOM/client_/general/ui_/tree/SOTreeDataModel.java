/*
 * $Id: SOTreeDataModel.java,v 1.2 2005/03/17 14:44:00 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree;

import java.awt.Color;

import javax.swing.Icon;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/03/17 14:44:00 $
 * @module generalclient_v1
 */

public interface SOTreeDataModel {
	void updateChildNodes(SONode node);
	
	String getNodeName(SONode node);
	Icon getNodeIcon(SONode node);
	Color getNodeColor(SONode node);
	ObjectResourceController getNodeController(SONode node);
}