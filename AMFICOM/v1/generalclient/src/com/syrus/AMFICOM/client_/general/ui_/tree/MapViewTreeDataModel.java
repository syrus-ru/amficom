/*
 * $Id: MapViewTreeDataModel.java,v 1.1 2005/03/17 14:44:00 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree;

import java.awt.Color;
import java.util.Enumeration;

import javax.swing.Icon;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/17 14:44:00 $
 * @module generalclient_v1
 */

class MapViewTreeDataModel implements SOTreeDataModel {
	private static MapViewTreeDataModel instance = new MapViewTreeDataModel();
	protected MapViewTreeDataModel() {
		//empty
	}
	public SOTreeDataModel getInstance() {
		return instance;
	}

	public void updateChildNodes(SONode node) {
		if(!node.isExpanded())
			return;
			
		Object userObject = node.getUserObject();
		if(userObject instanceof MapView) {
			boolean createmaps = true;
			boolean createschemess = true;
			for(Enumeration en = node.children(); en.hasMoreElements();)
			{
				SONode childNode = (SONode)en.nextElement();
				if(childNode.getUserObject().equals("maps"))
				{
					createmaps = false;
					childNode.getTreeDataModel().updateChildNodes(childNode);
				}
				if(childNode.getUserObject().equals("schemes"))
				{
					createschemess = false;
					childNode.getTreeDataModel().updateChildNodes(childNode);
				}
			}
			if(createmaps)
				node.add(new SOMutableNode(instance, "maps"));
			if(createschemess)
				node.add(new SOMutableNode(instance, "schemes"));
		}
		else if(userObject.equals("maps")) {
		}
		else if(userObject.equals("schemes")) {
		}
	}
	
	public String getNodeName(SONode node) {
		return "MapView";
	}

	public Icon getNodeIcon(SONode node) {
		return null;
	}
	
	public Color getNodeColor(SONode node) {
		return Color.BLACK;
	}
	
	public ObjectResourceController getNodeController(SONode node) {
		return null;
	}
}