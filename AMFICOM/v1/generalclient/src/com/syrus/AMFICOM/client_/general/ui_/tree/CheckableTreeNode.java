/*
 * $Id: CheckableTreeNode.java,v 1.1 2005/03/05 11:33:03 stas Exp $
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
import javax.swing.Icon;
import javax.swing.tree.MutableTreeNode;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/05 11:33:03 $
 * @module generalclient_v1
 */

public class CheckableTreeNode extends StorableObjectTreeNode {
	private static final long serialVersionUID = 3832617370274837815L;
	private boolean isChecked;
	private static Icon checkedIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/selectall.gif"));
	private static Icon uncheckedIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/unselect.gif"));

	public CheckableTreeNode(Object obj, String name) {
		this(obj, name, false);
	}

	public CheckableTreeNode(Object obj, String name, boolean isFinal) {
		this(obj, name, isFinal, false);
	}
	
	public CheckableTreeNode(Object obj, String name, boolean isFinal, boolean isChecked) {
		super(obj, name, isFinal);
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
			return CheckableTreeNode.checkedIcon;
		return CheckableTreeNode.uncheckedIcon;
	}
	
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e) || e.getClickCount() == 2) {
			this.isChecked = !this.isChecked;
			for (Enumeration en = children(); en.hasMoreElements();) {
				MutableTreeNode child = (MutableTreeNode) en.nextElement();
				if (child instanceof CheckableTreeNode)
					((CheckableTreeNode) child).setChecked(this.isChecked);
			}
			
		}	
	}
}
