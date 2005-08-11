/*-
 * $Id: IconedNode.java,v 1.5 2005/08/11 18:51:08 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI.tree;

import javax.swing.Icon;

import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.logic.AbstractItem;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.5 $, $Date: 2005/08/11 18:51:08 $
 * @module commonclient
 */

public class IconedNode extends AbstractItem implements Namable {
	private Object object;
	private String name;
	private Icon icon;
	private boolean allowsChildren = true;
	private boolean nameSet = false;

	public IconedNode() {
		// default empty constructor
	}
	
	public IconedNode(Namable object, boolean allowsChildren) {
		this(object, object.getName(), null, allowsChildren);
	}
	
	public IconedNode(Namable object, Icon icon) {
		this(object, object.getName(), icon, true);
	}
	
	public IconedNode(Namable object, Icon icon, boolean allowsChildren) {
		this(object, object.getName(), icon, allowsChildren);
	}
	
	public IconedNode(Object object, String name, boolean allowsChildren) {
		this(object, name, null, allowsChildren);
		this.nameSet = true;
	}
	
	public IconedNode(Object object, String name) {
		this(object, name, null, true);
		this.nameSet = true;
	}
	
	public IconedNode(Object object, String name, Icon icon) {
		this(object, name, icon, true);
		this.nameSet = true;
	}
	
	public IconedNode(Object object, String name, Icon icon, boolean allowsChildren) {
		this.object = object;
		this.name = name;
		this.icon = icon;
		this.allowsChildren = allowsChildren;
	}
	
	public void setCanHaveChildren(boolean canHaveChildren) {
		this.allowsChildren = canHaveChildren;
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

	public void setName(String name) {
		this.name = name;
		this.nameSet = true;
	}
	
	public String getName() {
		if (!this.nameSet && this.object instanceof Namable)
			return ((Namable)this.object).getName();
		return this.name;
	}
	
	public String toString() {
		return getName();
	}
	
	public void setObject(Object object) {
		this.object = object;
	}

	public Object getObject() {
		return this.object;
	}
	
	public boolean isService() {
		return false;
	}
	
	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public Icon getIcon() {
		return this.icon;
	}
}
