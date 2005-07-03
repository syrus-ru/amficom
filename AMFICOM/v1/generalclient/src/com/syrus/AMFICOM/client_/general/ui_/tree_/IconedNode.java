/*-
 * $Id: IconedNode.java,v 1.3 2005/04/18 08:54:35 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree_;

import javax.swing.Icon;

import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.logic.AbstractItem;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/04/18 08:54:35 $
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
		return allowsChildren;
	}

	public String getName() {
		if (object instanceof Namable)
			return ((Namable)object).getName();
		return name;
	}

	public Object getObject() {
		return object;
	}

	public boolean isService() {
		return false;
	}
	
	public Icon getIcon() {
		return icon;
	}
}
