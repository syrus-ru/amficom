/*
 * $Id: StorableObjectTreeNode.java,v 1.3 2005/03/10 09:11:37 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree;

import java.awt.event.*;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/03/10 09:11:37 $
 * @module generalclient_v1
 */

public class StorableObjectTreeNode extends DefaultMutableTreeNode implements MouseListener {
	private static final long serialVersionUID = 3760850047289668145L;
	private String name;
	private ImageIcon icon;
	private boolean expanded = false;
	
	public StorableObjectTreeNode(Object obj, String name) {
		this(obj, name, false);
	}
	
	public StorableObjectTreeNode(Object obj, String name, boolean isFinal) {
		this(obj, name, null, isFinal);
	}
	
	public StorableObjectTreeNode(Object obj, String name, ImageIcon icon) {
		this(obj, name, icon, false);
	}
	
	public StorableObjectTreeNode(Object obj, String name, ImageIcon icon, boolean isFinal) {
		super(obj);
		this.name = name;
		this.icon = icon;
		this.setAllowsChildren(!isFinal);
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * @deprecated use getUserObject()
	 */
	public Object getObject() {
		return super.getUserObject();
	}
	
	public Icon getIcon() {
		return icon;
	}

	/**
	 * @param e
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		// empty
	}
	/**
	 * @param e
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		// empty
	}
	/**
	 * @param e
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		// empty	
	}
	/**
	 * @param e
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		// empty
	}
	/**
	 * @param e
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		// empty
	}
	
	public boolean isExpanded() {
		return expanded;
	}
	
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
}
