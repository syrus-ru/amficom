/*-
 * $Id: IconedNode.java,v 1.7 2005/09/06 14:30:18 bob Exp $
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
 * @version $Revision: 1.7 $, $Date: 2005/09/06 14:30:18 $
 * @module commonclient
 */

public class IconedNode extends AbstractItem implements Namable {
	private Object object;
	private String name;
	private Icon icon;
	private boolean allowsChildren = true;
	protected boolean named = false;

	public IconedNode() {
		// default empty constructor
	}
	
	public IconedNode(final Namable object, final boolean allowsChildren) {
		this(object, object.getName(), null, allowsChildren);
	}
	
	public IconedNode(final Namable object, final Icon icon) {
		this(object, object.getName(), icon, true);
	}
	
	public IconedNode(final Namable object, final Icon icon, final boolean allowsChildren) {
		this(object, object.getName(), icon, allowsChildren);
		this.named = false;
	}
	
	public IconedNode(final Object object, final String name, final  boolean allowsChildren) {
		this(object, name, null, allowsChildren);
	}
	
	public IconedNode(final Object object, final String name) {
		this(object, name, null, true);
	}
	
	public IconedNode(final Object object, final String name, final Icon icon) {
		this(object, name, icon, true);
	}
	
	public IconedNode(final Object object, 
	                  final String name, 
	                  final Icon icon, 
	                  final boolean allowsChildren) {
		this.object = object;
		this.name = name;
		this.icon = icon;
		this.allowsChildren = allowsChildren;
		this.named = true;
	}
	
	public void setCanHaveChildren(final boolean canHaveChildren) {
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
		this.named = true;
	}
	
	public String getName() {
		if (!this.named && this.object instanceof Namable) {
			return ((Namable)this.object).getName();
		}
		return this.name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public void setObject(final Object object) {
		this.object = object;
	}

	public Object getObject() {
		return this.object;
	}
	
	public boolean isService() {
		return false;
	}
	
	public void setIcon(final Icon icon) {
		this.icon = icon;
	}

	public Icon getIcon() {
		return this.icon;
	}
}
