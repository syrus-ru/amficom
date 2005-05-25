/*-
 * $Id: IconedNode.java,v 1.1 2005/05/25 07:55:08 bob Exp $
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
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2005/05/25 07:55:08 $
 * @module generalclient_v1
 */

public class IconedNode extends AbstractItem {
	private Object object;
	private String name;
	private Icon icon;
	private boolean allowsChildren = true;

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
	}
	
	public IconedNode(Object object, String name) {
		this(object, name, null, true);
	}
	
	public IconedNode(Object object, String name, Icon icon) {
		this(object, name, icon, true);
	}
	
	public IconedNode(Object object, String name, Icon icon, boolean allowsChildren) {
		this.object = object;
		this.name = name;
		this.icon = icon;
		this.allowsChildren = allowsChildren;
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
	
	public String toString() {
		return getName();
	}

	public Object getObject() {
		return this.object;
	}

	public boolean isService() {
		return false;
	}
	
	public Icon getIcon() {
		return this.icon;
	}
}
