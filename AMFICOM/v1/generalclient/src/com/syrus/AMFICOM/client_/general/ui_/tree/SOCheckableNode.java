/*
 * $Id: SOCheckableNode.java,v 1.1 2005/03/14 13:30:48 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree;

import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/14 13:30:48 $
 * @module generalclient_v1
 */

public class SOCheckableNode extends SOMutableNode {
	private static final long serialVersionUID = 3832617370274837815L;
	private boolean isChecked;
	private static Icon checkedIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/selectall.gif"));
	private static Icon uncheckedIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/unselect.gif"));

	public SOCheckableNode(SOTreeDataModel treeDataModel, Object obj) {
		this(treeDataModel, obj, true);
	}

	public SOCheckableNode(SOTreeDataModel treeDataModel, Object obj, boolean allowsChildren) {
		this(treeDataModel, obj, allowsChildren, false);
	}
	
	public SOCheckableNode(SOTreeDataModel treeDataModel, Object obj, boolean allowsChildren, boolean isChecked) {
		super(treeDataModel, obj, allowsChildren);
		setChecked(isChecked);
	}
	
	public boolean isChecked() {
		return isChecked;
	}
	
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	
	public Icon getIcon() {
		if (this.isChecked)
			return SOCheckableNode.checkedIcon;
		return SOCheckableNode.uncheckedIcon;
	}
	
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			this.isChecked = !this.isChecked;
			for (Enumeration en = children(); en.hasMoreElements();) {
				SONode child = (SONode) en.nextElement();
				if (child instanceof SOCheckableNode)
					((SOCheckableNode)child).setChecked(this.isChecked);
			}
		}	
	}
}
