/*-
 * $Id: CheckableNode.java,v 1.1 2005/03/30 13:27:20 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree_;

import java.util.Iterator;

import com.syrus.AMFICOM.logic.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/30 13:27:20 $
 * @module generalclient_v1
 */

public class CheckableNode extends AbstractItem {
	private Object object;
	private String name;
	private boolean allowsChildren = true;
	private boolean isChecked;

	public CheckableNode(Object object, String name) {
		this(object, name, true);
	}
	
	public CheckableNode(Object object, String name, boolean allowsChildren) {
		this(object, name, allowsChildren, false);
	}
	
	public CheckableNode(Object object, String name, boolean allowsChildren, boolean isChecked) {
		this.object = object;
		this.name = name;
		this.allowsChildren = allowsChildren;
		setChecked(isChecked);
	}
	
	public boolean isChecked() {
		return isChecked;
	}
	
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
		for (Iterator it = getChildren().iterator(); it.hasNext();) {
			Item node = (Item)it.next();
			if (node instanceof CheckableNode)
				((CheckableNode)node).setChecked(isChecked);
		}
	}
	
	public int getMaxChildrenCount() {
		return Integer.MAX_VALUE;
	}

	public boolean canHaveParent() {
		return true;
	}
	
	public boolean canHaveChildren() {
		return allowsChildren;
	}

	public String getName() {
		return name;
	}

	public Object getObject() {
		return object;
	}

	public boolean isService() {
		return false;
	}
}

