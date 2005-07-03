/*-
 * $Id: CheckableNode.java,v 1.1 2005/05/25 07:55:08 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI.tree;

import java.util.Iterator;

import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.logic.*;

/**
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2005/05/25 07:55:08 $
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
	
	public CheckableNode(Namable object, boolean allowsChildren) {
		this(object, object.getName(), allowsChildren);
	}
	
	public CheckableNode(Object object, String name, boolean allowsChildren) {
		this(object, name, allowsChildren, false);
	}
	
	public CheckableNode(Namable object, boolean allowsChildren, boolean isChecked) {
		this(object, object.getName(), allowsChildren, isChecked);
	}
	
	public CheckableNode(Object object, String name, boolean allowsChildren, boolean isChecked) {
		this.object = object;
		this.name = name;
		this.allowsChildren = allowsChildren;
		setChecked(isChecked);
	}
	
	public boolean isChecked() {
		return this.isChecked;
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
		return this.allowsChildren;
	}

	public String getName() {
		if (this.object instanceof Namable)
			return ((Namable)this.object).getName();
		return this.name;
	}

	public Object getObject() {
		return this.object;
	}

	public boolean isService() {
		return false;
	}
}

